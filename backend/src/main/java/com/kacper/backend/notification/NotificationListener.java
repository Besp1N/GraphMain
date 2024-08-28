package com.kacper.backend.notification;

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
public class NotificationListener extends Thread implements DisposableBean
{
    private Connection connection;
    private BufferedWriter writer;
    private final Object lock = new Object();
    private final NotificationService notificationService;

    public NotificationListener(
            DataSource dataSource,
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
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
            try {
                writer.write("cos sie zepsulo");
                writer.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
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

                        notificationService.sendNotification(payload);

                        writer.write("\n");
                        writer.write("Received notification: " + payload + "\n");
                        writer.write("\n");
                        writer.newLine();
                        writer.flush();
                    }
                }
            }
        } catch (SQLException | IOException e) {
            try {
                writer.write("cos sie zepsulo");
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
            this.interrupt(); // Interrupt the thread
        } catch (SQLException | IOException e) {
            try {
                writer.write("cos sie zepsulo");
                writer.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}

