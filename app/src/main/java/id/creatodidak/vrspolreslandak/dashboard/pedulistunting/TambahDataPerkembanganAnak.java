package id.creatodidak.vrspolreslandak.dashboard.pedulistunting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.ClientStunting;
import id.creatodidak.vrspolreslandak.api.EndpointStunting;
import id.creatodidak.vrspolreslandak.api.models.stunting.Respstunting;
import id.creatodidak.vrspolreslandak.api.models.stunting.SearchByNIK;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahDataPerkembanganAnak extends AppCompatActivity implements View.OnClickListener {

    EditText etnik, etbb, ettb, etlk;
    TextView tvLoadRegData, tvSStatus, tvSNama;
    CardView search, btsearch;
    ProgressBar pbSearch, pbLoad;
    ImageView icSearch;
    LinearLayout formdatakembang, loading;
    ScrollView svForm;
    Button btnaddkembang;
    EndpointStunting endpointStunting;
    private boolean backButtonDisabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data_perkembangan_anak);

        svForm = findViewById(R.id.formReg);
        etnik = findViewById(R.id.etSearchNik);
        etbb = findViewById(R.id.etBBKembang);
        ettb = findViewById(R.id.etTBKembang);
        etlk = findViewById(R.id.etLKKembang);
        tvLoadRegData = findViewById(R.id.tvLoadRegData);
        tvSStatus = findViewById(R.id.searchStatus);
        tvSNama = findViewById(R.id.searchNama);
        search = findViewById(R.id.search);
        btsearch = findViewById(R.id.btSearch);
        pbSearch = findViewById(R.id.pbSearch);
        pbLoad = findViewById(R.id.pbLoadRegData);
        icSearch = findViewById(R.id.icSearch);
        formdatakembang = findViewById(R.id.formDataKembang);
        loading = findViewById(R.id.loadingRegData);
        btnaddkembang = findViewById(R.id.btnAddKembang);

        endpointStunting = ClientStunting.getClient().create(EndpointStunting.class);
        btsearch.setOnClickListener(this);
        btnaddkembang.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btSearch){
            if(etnik.getText().toString().length() == 16){
                icSearch.setVisibility(View.GONE);
                pbSearch.setVisibility(View.VISIBLE);
                search.setVisibility(View.GONE);
                formdatakembang.setVisibility(View.GONE);

                searchbynik(etnik.getText().toString());
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataPerkembanganAnak.this);
                builder.setTitle("Periksa kembali data anda!")
                        .setMessage("NIK HARUS 16 DIGIT!")
                        .setPositiveButton("PERBAIKI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();
            }
        }else if(v.getId() == R.id.btnAddKembang){
            if(isDataValid()){
                AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataPerkembanganAnak.this);
                builder.setTitle("⚠ Peringatan")
                        .setMessage("Data yang sudah diinput tidak dapat dihapus...")
                        .setPositiveButton("KIRIM DATA", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                loading.setVisibility(View.VISIBLE);
                                svForm.setVisibility(View.INVISIBLE);
                                dialog.dismiss();
                                kirimdata();
                                backButtonDisabled = true;
                            }
                        })
                        .setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataPerkembanganAnak.this);
                builder.setTitle("Periksa kembali data anda!")
                        .setMessage("MASIH ADA DATA YANG BELUM DIISI!")
                        .setPositiveButton("PERBAIKI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private void kirimdata() {
        SharedPreferences sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        String satker = sharedPreferences.getString("satker", "POLRES LANDAK");
        Call<Respstunting> call = endpointStunting.sendKembangAnak(etnik.getText().toString(), etbb.getText().toString(), ettb.getText().toString(), etlk.getText().toString(),satker);
        call.enqueue(new Callback<Respstunting>() {
            @Override
            public void onResponse(Call<Respstunting> call, Response<Respstunting> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().isOps()){
                        loading.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataPerkembanganAnak.this);
                        builder.setTitle("DATA BERHASIL DIKIRIM")
                                .setMessage(response.body().getMessage())
                                .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        TambahDataPerkembanganAnak.super.onBackPressed();
                                        finish();
                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else{
                        loading.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataPerkembanganAnak.this);
                        builder.setTitle("DATA GAGAL DIKIRIM")
                                .setMessage(response.body().getMessage())
                                .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        svForm.setVisibility(View.VISIBLE);
                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }else{
                    loading.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataPerkembanganAnak.this);
                    builder.setTitle("DATA GAGAL DIKIRIM")
                            .setMessage("Silahkan periksa jaringan anda atau hubungi TIK Polres Landak")
                            .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    svForm.setVisibility(View.VISIBLE);
                                }
                            })
                            .setIcon(R.drawable.icon)
                            .setCancelable(false);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<Respstunting> call, Throwable t) {
                loading.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataPerkembanganAnak.this);
                builder.setTitle("DATA GAGAL DIKIRIM")
                        .setMessage("Silahkan periksa jaringan anda atau hubungi TIK Polres Landak")
                        .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                svForm.setVisibility(View.VISIBLE);
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void searchbynik(String nik) {
        Call<SearchByNIK> call = endpointStunting.searchAnak(nik);
        call.enqueue(new Callback<SearchByNIK>() {
            @Override
            public void onResponse(Call<SearchByNIK> call, Response<SearchByNIK> response) {
                if(response.isSuccessful() && response.body() != null){
                    icSearch.setVisibility(View.VISIBLE);
                    pbSearch.setVisibility(View.GONE);
                    search.setVisibility(View.VISIBLE);

                    if(response.body().isFound()){
                        tvSStatus.setText("DATA ANAK DITEMUKAN!");
                        tvSStatus.setTextColor(Color.parseColor("#00ff00"));
                        tvSNama.setVisibility(View.VISIBLE);
                        tvSNama.setText(response.body().getData().getNama());
                        formdatakembang.setVisibility(View.VISIBLE);
                    }else{
                        tvSStatus.setText("DATA ANAK TIDAK DITEMUKAN! (DB NULL)");
                        tvSStatus.setTextColor(Color.parseColor("#ff0000"));
                        tvSNama.setVisibility(View.GONE);
                        formdatakembang.setVisibility(View.GONE);
                    }
                }else{
                    icSearch.setVisibility(View.VISIBLE);
                    pbSearch.setVisibility(View.GONE);
                    search.setVisibility(View.VISIBLE);
                    tvSStatus.setText("DATA ANAK TIDAK DITEMUKAN! (Server Error!)");
                    tvSStatus.setTextColor(Color.parseColor("#ff0000"));
                    tvSNama.setVisibility(View.GONE);
                    formdatakembang.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SearchByNIK> call, Throwable t) {
                icSearch.setVisibility(View.VISIBLE);
                pbSearch.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                tvSStatus.setText("DATA ANAK TIDAK DITEMUKAN! (Network Error!)");
                tvSStatus.setTextColor(Color.parseColor("#ff0000"));
                tvSNama.setVisibility(View.GONE);
                formdatakembang.setVisibility(View.GONE);
            }
        });
    }

    private boolean isDataValid() {
        return !TextUtils.isEmpty(etnik.getText()) &&
               !TextUtils.isEmpty(etnik.getText()) &&
               !TextUtils.isEmpty(ettb.getText()) &&
               !TextUtils.isEmpty(etlk.getText());
    }

    @Override
    public void onBackPressed() {
        if (!backButtonDisabled) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Yakin ingin keluar?")
                    .setMessage("Data yang sudah diisi mungkin akan hilang.")
                    .setPositiveButton("KELUAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            TambahDataPerkembanganAnak.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder.setIcon(R.drawable.icon);
            builder.setCancelable(false);
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}