package com.kacper.backend.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kacper.backend.user_notification.UserNotification;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class NotificationTestListener extends Thread implements DisposableBean {
    private Connection connection;
    private BufferedWriter writer;
    private final Object lock = new Object();
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public NotificationTestListener(
            DataSource dataSource,
            NotificationService notificationService
    ) throws IOException {
        this.notificationService = notificationService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(true);
            Statement statement = connection.createStatement();
            statement.execute("LISTEN notifications_channel;");
            statement.close();
            writer = new BufferedWriter(new FileWriter("notifications.log", true));
            this.start();
            Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
        } catch (SQLException | IOException e) {
            writer.write("\n");
            writer.write("Cos sie stanelo: " + e);
            writer.write("\n");
            writer.flush();
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (lock) {
                    lock.wait(500);
                }
                PGNotification[] notifications = connection.unwrap(PGConnection.class).getNotifications();
                if (notifications != null) {
                    for (PGNotification notification : notifications) {
                        String payload = notification.getParameter();
                        NotificationDTO notificationDto = objectMapper.readValue(payload, NotificationDTO.class);
                        notificationService.addUserNotification(notificationDto);

                        writer.write("\n");
                        writer.write("Received notification: " + payload + "\n");
                        writer.write("\n");
                        writer.flush();
                    }
                }
            }
        } catch (SQLException | IOException e) {
            try {
                writer.write("\n");
                writer.write("Cos sie stanelo: " + e);
                writer.write("\n");
                writer.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
            if (writer != null) {
                writer.close();
            }
            this.interrupt();
        } catch (SQLException | IOException e) {
            try {
                writer.write("\n");
                writer.write("Cos sie stanelo: " + e);
                writer.write("\n");
                writer.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}