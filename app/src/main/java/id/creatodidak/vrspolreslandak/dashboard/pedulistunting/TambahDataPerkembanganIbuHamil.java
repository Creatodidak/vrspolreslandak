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
import android.widget.CheckBox;
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

public class TambahDataPerkembanganIbuHamil extends AppCompatActivity implements View.OnClickListener {

    EditText etnik, etbb, etlp, etusia, etdenyut, etsistolik, etdiastolik;
    TextView tvLoadRegData, tvSStatus, tvSNama;
    CardView search, btsearch;
    ProgressBar pbSearch, pbLoad;
    ImageView icSearch;
    LinearLayout formdatakembang, loading;
    ScrollView svForm;
    CheckBox cb1, cb2, cb3, cb4;
    Button btnaddkembang;
    EndpointStunting endpointStunting;
    private boolean backButtonDisabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data_perkembangan_ibu_hamil);
        svForm = findViewById(R.id.formReg);
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
        etnik = findViewById(R.id.etSearchNik);
        etbb = findViewById(R.id.etBBIbu2);
        etlp = findViewById(R.id.etLPIbu2);
        etusia = findViewById(R.id.etUsiaKandungan2);
        etdenyut = findViewById(R.id.etDenyutJantungBayi2);
        etsistolik = findViewById(R.id.etSistolik);
        etdiastolik = findViewById(R.id.etDiastolik);
        cb1 = findViewById(R.id.cbIbu);
        cb2 = findViewById(R.id.cbIbu5);
        cb3 = findViewById(R.id.cbIbu6);
        cb4 = findViewById(R.id.cbIbu7);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataPerkembanganIbuHamil.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataPerkembanganIbuHamil.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataPerkembanganIbuHamil.this);
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
        String nik = etnik.getText().toString();
        String bb = etbb.getText().toString();
        String lp = etlp.getText().toString();
        String usia = etusia.getText().toString();
        String denyut = etdenyut.getText().toString();
        String sistolik = etsistolik.getText().toString();
        String diastolik = etdiastolik.getText().toString();
        String valcb1 = getcbval(cb1);
        String valcb2 = getcbval(cb2);
        String valcb3 = getcbval(cb3);
        String valcb4 = getcbval(cb4);

        Call<Respstunting> call = endpointStunting.sendDataKembangIbu(nik, bb, lp, usia, denyut, sistolik, diastolik, valcb1, valcb2, valcb3, valcb4, satker);
        call.enqueue(new Callback<Respstunting>() {
            @Override
            public void onResponse(Call<Respstunting> call, Response<Respstunting> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().isOps()){
                        loading.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataPerkembanganIbuHamil.this);
                        builder.setTitle("DATA BERHASIL DIKIRIM")
                                .setMessage(response.body().getMessage())
                                .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        TambahDataPerkembanganIbuHamil.super.onBackPressed();
                                        finish();
                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else{
                        loading.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataPerkembanganIbuHamil.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataPerkembanganIbuHamil.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(TambahDataPerkembanganIbuHamil.this);
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
        Call<SearchByNIK> call = endpointStunting.searchIbu(nik);
        call.enqueue(new Callback<SearchByNIK>() {
            @Override
            public void onResponse(Call<SearchByNIK> call, Response<SearchByNIK> response) {
                if(response.isSuccessful() && response.body() != null){
                    icSearch.setVisibility(View.VISIBLE);
                    pbSearch.setVisibility(View.GONE);
                    search.setVisibility(View.VISIBLE);

                    if(response.body().isFound()){
                        tvSStatus.setText("DATA IBU DITEMUKAN!");
                        tvSStatus.setTextColor(Color.parseColor("#00ff00"));
                        tvSNama.setVisibility(View.VISIBLE);
                        tvSNama.setText(response.body().getData().getNama());
                        formdatakembang.setVisibility(View.VISIBLE);
                    }else{
                        tvSStatus.setText("DATA IBU TIDAK DITEMUKAN! (DB NULL)");
                        tvSStatus.setTextColor(Color.parseColor("#ff0000"));
                        tvSNama.setVisibility(View.GONE);
                        formdatakembang.setVisibility(View.GONE);
                    }
                }else{
                    icSearch.setVisibility(View.VISIBLE);
                    pbSearch.setVisibility(View.GONE);
                    search.setVisibility(View.VISIBLE);
                    tvSStatus.setText("DATA IBU TIDAK DITEMUKAN! (Server Error!)");
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
                tvSStatus.setText("DATA IBU TIDAK DITEMUKAN! (Network Error!)");
                tvSStatus.setTextColor(Color.parseColor("#ff0000"));
                tvSNama.setVisibility(View.GONE);
                formdatakembang.setVisibility(View.GONE);
            }
        });
    }

    private String getcbval(CheckBox cb) {
        if (cb.isChecked()) {
            return cb.getText().toString();
        } else {
            return "-";
        }
    }

    @Override
    public void onBackPressed() {
        if (!backButtonDisabled) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Yakin ingin keluar?")
                    .setMessage("Data yang sudah diisi mungkin akan hilang.")
                    .setPositiveButton("KELUAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            TambahDataPerkembanganIbuHamil.super.onBackPressed();
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

    private boolean isDataValid() {
        return !TextUtils.isEmpty(etnik.getText()) &&
                !TextUtils.isEmpty(etbb.getText()) &&
                !TextUtils.isEmpty(etlp.getText()) &&
                !TextUtils.isEmpty(etusia.getText()) &&
                !TextUtils.isEmpty(etdenyut.getText()) &&
                !TextUtils.isEmpty(etsistolik.getText()) &&
                !TextUtils.isEmpty(etdiastolik.getText());
    }
}