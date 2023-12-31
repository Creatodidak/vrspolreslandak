package id.creatodidak.vrspolreslandak.dashboard.pedulistunting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.ClientStunting;
import id.creatodidak.vrspolreslandak.api.EndpointStunting;
import id.creatodidak.vrspolreslandak.api.models.stunting.AmbilToken;
import id.creatodidak.vrspolreslandak.api.models.stunting.Dokumentasi;
import id.creatodidak.vrspolreslandak.api.models.stunting.DokumentasiAdapter;
import id.creatodidak.vrspolreslandak.api.models.stunting.DokumentasiItem;
import id.creatodidak.vrspolreslandak.helper.NonScrollableLayoutManager;
import id.creatodidak.vrspolreslandak.helper.ShareWaUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahLaporanKegiatan extends AppCompatActivity {

    EndpointStunting endpointStunting;
    SharedPreferences sharedPreferences;
    TextView tvLaporan;
    Button btnLaporan;
    String nama, satker;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_laporan_kegiatan);

        endpointStunting = ClientStunting.getClient().create(EndpointStunting.class);
        tvLaporan = findViewById(R.id.tvIsiLaporan);
        btnLaporan = findViewById(R.id.btKirimLaporan);
        sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        nama = sharedPreferences.getString("pangkat", null)+" "+sharedPreferences.getString("nama", null);
        satker = sharedPreferences.getString("satker", "");
        btnLaporan.setEnabled(false);
        getToken(nama);
        rv = findViewById(R.id.rvDokumentasi);
    }

    private void getToken(String nama) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TambahLaporanKegiatan.this);
        builder.setMessage("Mengambil token...")
                .setIcon(R.drawable.icon)
                .setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();

        Call<AmbilToken> call = endpointStunting.getToken(nama);
        call.enqueue(new Callback<AmbilToken>() {
            @Override
            public void onResponse(Call<AmbilToken> call, Response<AmbilToken> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().isStatus()){
                        getLaporan(response.body().getToken());
                        alert.dismiss();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(TambahLaporanKegiatan.this);
                        builder.setTitle("TOKEN")
                                .setMessage("TOKEN TIDAK TERSEDIA, COBA LAGI NANTI ATAU HUBUNGI TIK PORLES LANDAK!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        alert.dismiss();
                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(TambahLaporanKegiatan.this);
                    builder.setTitle("TOKEN")
                            .setMessage("TOKEN TIDAK TERSEDIA, COBA LAGI NANTI ATAU HUBUNGI TIK PORLES LANDAK!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    alert.dismiss();
                                }
                            })
                            .setIcon(R.drawable.icon)
                            .setCancelable(false);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<AmbilToken> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TambahLaporanKegiatan.this);
                builder.setTitle("TOKEN")
                        .setMessage("TOKEN TIDAK TERSEDIA, COBA LAGI NANTI ATAU HUBUNGI TIK PORLES LANDAK!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                alert.dismiss();
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void getLaporan(String token) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TambahLaporanKegiatan.this);
        builder.setMessage("Mengambil Laporan...")
                .setIcon(R.drawable.icon)
                .setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
        Call<ResponseBody> call = endpointStunting.getLaporanWa(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null){
                    try {
                        String laporanText = response.body().string();
                        loadDokumentasi(token, laporanText);
                        alert.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    alert.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(TambahLaporanKegiatan.this);
                    builder.setTitle("NIHIL")
                            .setMessage("LAPORAN TIDAK TERSEDIA, COBA LAGI NANTI ATAU HUBUNGI TIK PORLES LANDAK!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(R.drawable.icon)
                            .setCancelable(false);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                alert.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(TambahLaporanKegiatan.this);
                builder.setTitle("NIHIL")
                        .setMessage("LAPORAN TIDAK TERSEDIA, COBA LAGI NANTI ATAU HUBUNGI TIK PORLES LANDAK!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void loadDokumentasi(String token, String laporanText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TambahLaporanKegiatan.this);
        builder.setMessage("Mengambil Laporan...")
                .setIcon(R.drawable.icon)
                .setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();

        Call<Dokumentasi> call = endpointStunting.getDokumentasi(token, satker);
        call.enqueue(new Callback<Dokumentasi>() {
            @Override
            public void onResponse(Call<Dokumentasi> call, Response<Dokumentasi> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().isAda()){
                        alert.dismiss();

                        List<DokumentasiItem> dokumentasiList = response.body().getDokumentasi();

                        DokumentasiAdapter adapter = new DokumentasiAdapter(TambahLaporanKegiatan.this, dokumentasiList);
//                        LinearLayoutManager layoutManager = new LinearLayoutManager(TambahLaporanKegiatan.this);
                        NonScrollableLayoutManager layoutManager = new NonScrollableLayoutManager(TambahLaporanKegiatan.this);
                        rv.setLayoutManager(layoutManager);
                        rv.setAdapter(adapter);


                        List<Drawable> imageDrawables = new ArrayList<>();

                        for (int i = 0; i < rv.getChildCount(); i++) {
                            View itemView = rv.getChildAt(i);
                            ImageView imageView = itemView.findViewById(R.id.dokGambar); // Replace with your ImageView ID

                            Drawable drawable = imageView.getDrawable();
                            imageDrawables.add(drawable);
                        }

                        tvLaporan.setText(laporanText);
                        btnLaporan.setEnabled(true);
                        if(btnLaporan.isEnabled()){
                            btnLaporan.setVisibility(View.VISIBLE);

                            btnLaporan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ShareWaUtils.shareTextToWhatsApp(TambahLaporanKegiatan.this, laporanText);
                                }
                            });
                        }else{
                            btnLaporan.setVisibility(View.GONE);
                        }
                    }else{
                        alert.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(TambahLaporanKegiatan.this);
                        builder.setTitle("NIHIL")
                                .setMessage("LAPORAN TIDAK TERSEDIA, COBA LAGI NANTI ATAU HUBUNGI TIK PORLES LANDAK!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }else{
                    alert.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(TambahLaporanKegiatan.this);
                    builder.setTitle("NIHIL")
                            .setMessage("LAPORAN TIDAK TERSEDIA, COBA LAGI NANTI ATAU HUBUNGI TIK PORLES LANDAK!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(R.drawable.icon)
                            .setCancelable(false);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<Dokumentasi> call, Throwable t) {
                alert.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(TambahLaporanKegiatan.this);
                builder.setTitle("NIHIL")
                        .setMessage("LAPORAN TIDAK TERSEDIA, COBA LAGI NANTI ATAU HUBUNGI TIK PORLES LANDAK!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}