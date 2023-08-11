package id.creatodidak.vrspolreslandak.dashboard.karhutla;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.database.CreateDB;
import id.creatodidak.vrspolreslandak.database.HotspotTB;
import id.creatodidak.vrspolreslandak.service.PanggilServer;

public class PeringatanKarhutla extends AppCompatActivity {
    private final boolean backPressedOnce = false; // Flag untuk menandakan apakah tombol back telah ditekan
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peringatan_karhutla);

        // Panggil method untuk mengubah mode tampilan menjadi "Immersive Sticky"
        hideSystemUI();

        // Mulai layanan PanggilServer
        Intent serviceIntent = new Intent(this, PanggilServer.class);
        stopService(serviceIntent);

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setLooping(true); // Mengatur agar MP3 diputar berulang-ulang

        mediaPlayer.start();
        // Tombol untuk membuka Activity Karhutla
        Button btn = findViewById(R.id.btnBukavrs);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDB createDB = CreateDB.getInstance(PeringatanKarhutla.this);
                SQLiteDatabase db = createDB.getWritableDatabase();

                HotspotTB.updateAllNotifToYes(db);


                // Hentikan layanan PanggilServer
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                Intent serviceIntent = new Intent(PeringatanKarhutla.this, PanggilServer.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                } else {
                    startService(serviceIntent);
                }

                // Buka Activity Karhutla
                Intent intent = new Intent(PeringatanKarhutla.this, Karhutla.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void hideSystemUI() {
        // Cek apakah perangkat menggunakan versi Android Lollipop (API level 21) atau di atasnya
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    // Override method onBackPressed()
    @Override
    public void onBackPressed() {
        // Tampilkan pesan bahwa tombol kembali tidak dapat digunakan
        Toast.makeText(this, "Tekan Tombol Buka VRS", Toast.LENGTH_SHORT).show();
    }

}
