package com.example.cs310project;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class NotificationActivity extends FirebaseMessagingService {
    Notification notification;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            String sender = remoteMessage.getData().get("sender");

            createAndShowNotification(title, sender);
        }
    }

    private void createAndShowNotification(String title, String sender) {
        // Notification channel ID (required for Android Oreo and later)
        String channelId = "message_notification";

        // Create an intent to launch the app when the notification is tapped
        Intent intent = new Intent(this, FriendActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Build the notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText("from: " + sender)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true); // Dismiss the notification when tapped

        // Create the notification
        notification = builder.build();

        // Generate a unique notification ID using Random
        Random random = new Random();
        int notificationId = random.nextInt(Integer.MAX_VALUE);

        // Display the notification using NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);
    }
}
