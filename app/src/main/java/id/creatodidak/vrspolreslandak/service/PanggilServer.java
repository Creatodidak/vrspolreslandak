package id.creatodidak.vrspolreslandak.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.DataKarhutla;
import id.creatodidak.vrspolreslandak.api.models.ResponseChecker;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.PeringatanKarhutla;
import id.creatodidak.vrspolreslandak.database.CreateDB;
import id.creatodidak.vrspolreslandak.database.HotspotTB;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PanggilServer extends Service {

    private static final long INTERVAL = 60 * 1000; // Interval dalam milidetik (1 menit)

    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            SharedPreferences sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
            String wilayah = sharedPreferences.getString("wilayah", null);

            Endpoint endpoint = Client.getClient().create(Endpoint.class);
            Call<ResponseChecker> call = endpoint.cekhotspot("menyuke");
            call.enqueue(new Callback<ResponseChecker>() {
                @Override
                public void onResponse(Call<ResponseChecker> call, Response<ResponseChecker> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().isStatus()) {
                            CreateDB createDB = CreateDB.getInstance(PanggilServer.this);

                            // Data yang diterima dari server
                            List<DataKarhutla> dataKarhutlaList = response.body().getData();

                            // Masukkan data ke dalam tabel Hotspot jika belum ada di database
                            SQLiteDatabase db = createDB.getWritableDatabase();
                            int dataLooped = 0;
                            int totalData = dataKarhutlaList.size();

                            for (DataKarhutla dataKarhutla : dataKarhutlaList) {
                                HotspotTB.insertHotspotIfNotExist(db,
                                        dataKarhutla.getData_id(),
                                        dataKarhutla.getLatitude(),
                                        dataKarhutla.getLongitude(),
                                        dataKarhutla.getConfidence(),
                                        dataKarhutla.getRadius(),
                                        dataKarhutla.getLocation(),
                                        dataKarhutla.getWilayah(),
                                        dataKarhutla.getResponse(),
                                        dataKarhutla.getStatus(),
                                        dataKarhutla.getNotif(),
                                        dataKarhutla.getCreated_at(),
                                        dataKarhutla.getUpdated_at());
                                dataLooped++;

                                if (dataLooped == totalData) {
                                    if (wilayah.equals("ALL")) {
                                        boolean ada = HotspotTB.hasHotspotsWithNullNotif(db);
                                        if (ada) {
                                            Intent intent = new Intent(PanggilServer.this, PeringatanKarhutla.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    } else {
                                        boolean ada = HotspotTB.hasHotspotsNotif(db, wilayah.toLowerCase());
                                        if (ada) {
                                            Intent intent = new Intent(PanggilServer.this, PeringatanKarhutla.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseChecker> call, Throwable t) {
                    Toast.makeText(PanggilServer.this, "GAGAL MEMANGGIL SERVER!", Toast.LENGTH_SHORT).show();
                }
            });

            handler.postDelayed(this, INTERVAL);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(); // Create the notification channel if needed
            Notification notification = createNotification();
            startForeground(1, notification); // Start the foreground service with the notification
        }

        handler.postDelayed(runnable, INTERVAL); // Start the periodic task

        Log.i("SERVICE", "SERVICE STARTED!");
    }

    private Notification createNotification() {
        // Konfigurasi notifikasi untuk Foreground Service

        Intent notificationIntent = new Intent(this, PeringatanKarhutla.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ForegroundServiceChannel")
                .setContentTitle("Foreground Service")
                .setContentText("Service sedang berjalan")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder.build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "ForegroundServiceChannel",
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Hentikan runnable saat Service dihancurkan
        handler.removeCallbacks(runnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
