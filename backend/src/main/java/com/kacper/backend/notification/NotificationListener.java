package com.kacper.backend.notification;

import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

@Component
public class NotificationListener extends Thread implements DisposableBean
{
    private Connection connection;
    private final Object lock = new Object();
    private final NotificationService notificationService;

    Logger logger = Logger.getLogger(NotificationListener.class.getName());

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
            this.start();
            Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
        } catch (SQLException e) {
           throw new RuntimeException(e);
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

                        logger.info("Received notification: " + payload);

                        notificationService.sendNotification(payload);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            this.interrupt(); // Interrupt the thread
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

