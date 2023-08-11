package id.creatodidak.vrspolreslandak.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import id.creatodidak.vrspolreslandak.R;

public class SoundService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // ...

        // Buat notifikasi untuk layanan latar belakang
        Notification notification = new NotificationCompat.Builder(this, "notifikasiVRSPolresLandak")
                .setContentTitle("Background Sound Service")
                .setContentText("Service is running in the background")
                .setSmallIcon(R.drawable.icon) // Ganti dengan ikon yang sesuai
                .build();

        // Jalankan layanan sebagai layanan latar belakang dengan notifikasi
        startForeground(1, notification);

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
