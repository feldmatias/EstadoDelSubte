package com.subte.mati.estadodelsubte;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.content.res.AppCompatResources;

import com.subte.mati.estadodelsubte.view.MainActivity;

public class SubwayNotification {

    private NotificationCompat.Builder notif;
    private Context context;
    private int id;

    public SubwayNotification(Context context, String line, String content){

        this.initChannels(context);
        this.id = line.charAt(0);
        this.context = context;

        this.notif = new NotificationCompat.Builder(context, "Subte");

        notif.setSmallIcon(R.drawable.ic_notification);
        notif.setLargeIcon(this.getLargeIcon(line));

        notif.setContentTitle("Subte " + line);
        notif.setContentText(content);
        notif.setAutoCancel(true);
        notif.setStyle(new NotificationCompat.BigTextStyle().bigText(content));
        notif.setDefaults(Notification.DEFAULT_ALL);
        setOpenActivity();
    }

    private Bitmap getLargeIcon(String line) {
        String name = "ic_notification_" + line.toLowerCase();
        int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        Drawable icon = AppCompatResources.getDrawable(context, id);
        return ((BitmapDrawable)icon).getBitmap();
    }

    private void setOpenActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent contIntent = PendingIntent.getActivity(context, 0, intent, 0);
        notif.setContentIntent(contIntent);
    }

    public void send(){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(this.id, notif.build());
    }


    public void initChannels(Context context) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            return;
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel("Subte", "Subte", NotificationManager.IMPORTANCE_HIGH);

        channel.setDescription("Notificaciones del subte");
        channel.enableVibration(true);
        channel.enableLights(true);

        notificationManager.createNotificationChannel(channel);
    }
}
