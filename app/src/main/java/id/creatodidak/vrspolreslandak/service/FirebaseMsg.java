package id.creatodidak.vrspolreslandak.service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.Karhutla;

public class FirebaseMsg extends FirebaseMessagingService {

    public static MediaPlayer BG;
    Intent intent;
    private static final String TAG = "MyFirebaseMsgService";
    private static final String SHARED_PREF_NAME = "MySharedPref";
    private static final String FCM_TOKEN_KEY = "FCM_TOKEN";
    public static final String BROADCAST_ACTION = "id.creatodidak.vrspolreslandak";
    RemoteMessage remoteMessage2;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        //broadcast to intent

    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);

        // Save the FCM token to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FCM_TOKEN_KEY, token);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void handleIntent(Intent intent) { //handle on background

        try {
            if (intent.getExtras() != null) {
                /**
                 *
                 * @Ambil parameter jika perlu
                 */
                String title = intent.getExtras().getString("gcm.notification.title");
                String body = intent.getExtras().getString("gcm.notification.body");
                String notification_id = intent.getExtras().getString("gcm.notification.notification_id");
                String from = intent.getExtras().getString("gcm.notification.from");
                String channel_id = intent.getExtras().getString("gcm.notification.channel_id"); //notifikasiVRSPolresLandak
                String topic = intent.getExtras().getString("topic"); //NotifikasiKarhutlaVRS

                //if(channel_id.equals("notifikasiVRSPolresLandak")) { //pakai ini jika ingin deteksi "notifikasiVRSPolresLandak"

                /**
                 *
                 * Build ulang dan kirim ke fungsi onMessageReceived
                 */

                RemoteMessage.Builder builder = new RemoteMessage.Builder("FirebaseMsg");
                for (String key : intent.getExtras().keySet()) {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }
                onMessageReceived(builder.build());
                //}

            } else {
                super.handleIntent(intent);
            }
        } catch (Exception e) {
            super.handleIntent(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        remoteMessage2 = remoteMessage;
        if (remoteMessage.getNotification() != null && !remoteMessage.getData().isEmpty()) {
            if (remoteMessage.getData().get("topic").equals("NotifikasiKarhutlaVRS")) {
                int notificationId = 98070129;
                String channelId = "notifikasiVRSPolresLandak";
                CharSequence channelName = "notifikasiVRSPolresLandak";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);

                Intent intent = new Intent(this, Karhutla.class); // Ganti dengan class activity yang sesuai
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(2000);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.icon)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setContentIntent(pendingIntent)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                notificationManagerCompat.notify(notificationId, builder.build());
                playSound();
            } else if (remoteMessage.getData().get("topic").equals("UpdateVRS")) {
                int notificationId = 90;
                String channelId = "updateVRSPolresLandak";
                CharSequence channelName = "updateVRSPolresLandak";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=id.creatodidak.vrspolreslandak"));
                intent.setPackage("com.android.vending");
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Create a rhythmic pattern with on-off intervals
                    long[] pattern = {0, 200, 100, 200, 100, 200}; // Vibrate for 200ms, pause for 100ms, vibrate for 200ms, and so on

                    // Vibrate with the specified pattern
                    VibrationEffect vibrationEffect = VibrationEffect.createWaveform(pattern, -1); // -1 means don't repeat
                    vibrator.vibrate(vibrationEffect);
                } else {
                    // For devices below Android O, vibrate for 2 seconds
                    vibrator.vibrate(1000);
                }


                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.icon)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icps))
                        .setBadgeIconType(R.mipmap.icon)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setContentIntent(pendingIntent)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);


                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                notificationManagerCompat.notify(notificationId, builder.build());
            }
        }
    }

    private void playSound() {
        BG = MediaPlayer.create(getBaseContext(), R.raw.alarm);
        BG.setLooping(false);
        BG.setVolume(100, 100);
        BG.start();

        Vibrator v = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        Objects.requireNonNull(v).vibrate(2000);
    }
}

