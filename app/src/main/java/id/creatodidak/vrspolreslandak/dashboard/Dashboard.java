package id.creatodidak.vrspolreslandak.dashboard;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.admin.NotifikasiUpdate;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.DataKarhutla;
import id.creatodidak.vrspolreslandak.api.models.ResponseChecker;
import id.creatodidak.vrspolreslandak.dashboard.harkamtibmas.Harkamtibmas;
import id.creatodidak.vrspolreslandak.dashboard.humas.Humas;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.Karhutla;
import id.creatodidak.vrspolreslandak.dashboard.lantas.Lantas;
import id.creatodidak.vrspolreslandak.dashboard.pedulistunting.DashboardPeduliStunting;
import id.creatodidak.vrspolreslandak.dashboard.pengamanan.Pengamanan;
import id.creatodidak.vrspolreslandak.dashboard.pimpinan.Pimpinan;
import id.creatodidak.vrspolreslandak.dashboard.situasi.Situasi;
import id.creatodidak.vrspolreslandak.dashboard.tahanan.Tahanan;
import id.creatodidak.vrspolreslandak.database.CreateDB;
import id.creatodidak.vrspolreslandak.database.HotspotTB;
import id.creatodidak.vrspolreslandak.service.PanggilServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    ImageView ivprofile, iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9, iv11;
    TextView tvnotif, tvmsg, tvatensi;
    Button btReport;
    private androidx.appcompat.app.AlertDialog dialog;

    SharedPreferences sharedPreferences;
    String nrp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        nrp = sharedPreferences.getString("nrp", null);

        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);
        iv4 = findViewById(R.id.iv4);
        iv5 = findViewById(R.id.iv5);
        iv6 = findViewById(R.id.iv6);
        iv7 = findViewById(R.id.iv7);
        iv8 = findViewById(R.id.iv8);
        iv9 = findViewById(R.id.iv9);
        iv11 = findViewById(R.id.iv11);

        if(!nrp.equals("98070129")){
            iv11.setVisibility(View.GONE);
        }else{
            iv11.setOnClickListener(this);
        }

        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        iv4.setOnClickListener(this);
        iv5.setOnClickListener(this);
        iv6.setOnClickListener(this);
        iv7.setOnClickListener(this);
        iv8.setOnClickListener(this);
        iv9.setOnClickListener(this);

        shownotif("Wait a moment...", "Sinkronisasi data dengan server...");

        Endpoint endpoint = Client.getClient().create(Endpoint.class);
        Call<ResponseChecker> call = endpoint.cekhotspot("menyuke");
        call.enqueue(new Callback<ResponseChecker>() {
            @Override
            public void onResponse(Call<ResponseChecker> call, Response<ResponseChecker> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isStatus()) {
                        Log.i("KARHUTLA", "KARHUTLA FOUND!");
                        CreateDB createDB = CreateDB.getInstance(Dashboard.this);
                        List<DataKarhutla> dataKarhutlaList = response.body().getData();

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
                                dialog.dismiss();
                            }
                        }
                    } else {
                        dialog.dismiss();
                        Log.i("KARHUTLA", "KARHUTLA NOT FOUND!");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseChecker> call, Throwable t) {
                Toast.makeText(Dashboard.this, "GAGAL MEMANGGIL SERVER!", Toast.LENGTH_SHORT).show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                proceedAfterCameraPermission();
            }
        } else {
            proceedAfterCameraPermission();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P &&
                !Settings.canDrawOverlays(Dashboard.this)) {
            showPermissionDialog();
        } else {
            onPermissionGranted();
        }

        if(!areNotificationsEnabled()){
            showNotificationPermissionDialog();
        }
    }

    private void showNotificationPermissionDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Izin Notifikasi Diperlukan")
                .setMessage("Untuk menggunakan fitur ini, Anda perlu mengaktifkan izin notifikasi. Tekan OK untuk membuka pengaturan notifikasi.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_ALL_APPS_NOTIFICATION_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
        alertDialog.show();
    }

    public boolean areNotificationsEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            return notificationManager.areNotificationsEnabled();
        }
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        return notificationManagerCompat.areNotificationsEnabled();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv1) {
            Intent intent = new Intent(Dashboard.this, Pimpinan.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv2) {
            Intent intent = new Intent(Dashboard.this, Karhutla.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv3) {
            Intent intent = new Intent(Dashboard.this, Situasi.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv4) {
            Intent intent = new Intent(Dashboard.this, Harkamtibmas.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv5) {
            Intent intent = new Intent(Dashboard.this, Humas.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv6) {
            Intent intent = new Intent(Dashboard.this, Lantas.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv7) {
            Intent intent = new Intent(Dashboard.this, Pengamanan.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv8) {
            Intent intent = new Intent(Dashboard.this, Tahanan.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv9) {
            Intent intent = new Intent(Dashboard.this, DashboardPeduliStunting.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv11) {
            Intent intent = new Intent(Dashboard.this, NotifikasiUpdate.class);
            startActivity(intent);
        }
    }

    private void shownotif(String judul, String pesan) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(judul);
        builder.setMessage(pesan);
        builder.setIcon(R.drawable.icon); // Ganti R.drawable.icon dengan resource ID icon yang diinginkan
        builder.setCancelable(false);
        dialog = builder.create(); // Gunakan variabel dialog yang telah dideklarasikan sebagai field
        dialog.show();
    }


    private void showpilihan(String judul, String pesan, String positif) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(judul);
        builder.setMessage(pesan);
        builder.setIcon(R.drawable.icon); // Ganti R.drawable.icon dengan resource ID icon yang diinginkan

        builder.setPositiveButton(positif, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

//        builder.setNegativeButton(positif, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });

        dialog = builder.create(); // Gunakan variabel dialog yang telah dideklarasikan sebagai field
        dialog.show();
    }

    private void showPermissionDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("IZIN DIPERLUKAN")
                .setMessage("Agar anda dapat mendapatkan data terbaru dari server secara realtime, silahkan berikan izin dengan menekan tombol dibawah")
                .setPositiveButton("BERI IZIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("BATALKAN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onPermissionDenied();
                    }
                })
                .setCancelable(false)
                .create();
        alertDialog.show();
    }


    public void onPermissionGranted() {
        Intent serviceIntent = new Intent(Dashboard.this, PanggilServer.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }

    }

    public void onPermissionDenied() {
        Toast.makeText(this, "Foreground service permission denied. The app may not function properly.", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                proceedAfterCameraPermission();
            } else {
                Toast.makeText(this, "Camera permission denied. The app may not function properly.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }


    private void proceedAfterCameraPermission() {
        onPermissionGranted();
    }
}