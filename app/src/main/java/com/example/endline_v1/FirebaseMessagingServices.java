package com.example.endline_v1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;

public class FirebaseMessagingServices extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("FCM", remoteMessage.getNotification().toString());

        String title = remoteMessage.getNotification().getTitle();
        String msg = remoteMessage.getNotification().getBody();
        Uri imgUri = remoteMessage.getNotification().getImageUrl();
        Bitmap imgBitmap = null;
        try {
            imgBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        createNotificationChannel("FCM", "FCM channel", NotificationManager.IMPORTANCE_HIGH);
        createNotification("FCM", 1, title, msg, intent, imgBitmap);
    }

    void createNotificationChannel(String channelId, String channelName, int importance){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, importance));
        }
    }

    void createNotification(String channelId, int id, String title, String text, Intent intent, Bitmap imgBitmap){
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.logo_v1)
                .setContentTitle(title)
                .setContentText("유통기한 알림!!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(
                        new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(text).setSummaryText("유통기한 알림!!")
//                        new NotificationCompat.BigPictureStyle()
//                                .bigPicture(imgBitmap)
//                                .setBigContentTitle(title)
//                                .setSummaryText("유통기한 임박 알림!!")
                )
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

    void destroyNotification(int id){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}
