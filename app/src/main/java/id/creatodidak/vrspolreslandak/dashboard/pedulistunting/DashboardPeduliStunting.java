package id.creatodidak.vrspolreslandak.dashboard.pedulistunting;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.ClientStunting;
import id.creatodidak.vrspolreslandak.api.EndpointStunting;
import id.creatodidak.vrspolreslandak.api.models.stunting.RingkasanStunting;
import id.creatodidak.vrspolreslandak.helper.CustomNestedScrollView;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardPeduliStunting extends AppCompatActivity implements View.OnClickListener {
    ImageView burger;
    LinearLayout llmenu, menu1, menu2, menu3, menu4, menu5, menu6, menu7, loadingData;
    TextView jG1, tG1, jG2, tG2, jG3, tG3, jG4, tG4, jG5, tG5, jG6, tG6, waktudata, tvLoadData, tt1, tt2, tt3, tt4, tt5, tt6;
    ProgressBar pbLoadData;
    View viewToAnimate;
    EndpointStunting endpointStunting;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_peduli_stunting);

        burger = findViewById(R.id.burger);
        llmenu = findViewById(R.id.llmenu);
        menu1 = findViewById(R.id.menu1);
        menu2 = findViewById(R.id.menu2);
        menu3 = findViewById(R.id.menu3);
        menu4 = findViewById(R.id.menu4);
        menu5 = findViewById(R.id.menu5);
        menu6 = findViewById(R.id.menu6);
        menu7 = findViewById(R.id.menu7);
        jG1 = findViewById(R.id.jGburuk);
        tG1 = findViewById(R.id.tGburuk);
        jG2 = findViewById(R.id.jGkurang);
        tG2 = findViewById(R.id.tGkurang);
        jG3 = findViewById(R.id.jGbaik);
        tG3 = findViewById(R.id.tGbaik);
        jG4 = findViewById(R.id.jRGlebih);
        tG4 = findViewById(R.id.tRGlebih);
        jG5 = findViewById(R.id.jGlebih);
        tG5 = findViewById(R.id.tGlebih);
        jG6 = findViewById(R.id.jObesitas);
        tG6 = findViewById(R.id.tObesitas);
        tt1 = findViewById(R.id.tt1);
        tt2 = findViewById(R.id.tt2);
        tt3 = findViewById(R.id.tt3);
        tt4 = findViewById(R.id.tt4);
        tt5 = findViewById(R.id.tt5);
        tt6 = findViewById(R.id.tt6);

        waktudata = findViewById(R.id.waktudata);
        waktudata.setText(DateUtils.getTodayFormatted());
        loadingData = findViewById(R.id.loadingData);
        tvLoadData = findViewById(R.id.tvLoadData);
        pbLoadData = findViewById(R.id.pbLoadData);


        viewToAnimate = llmenu;

        endpointStunting = ClientStunting.getClient().create(EndpointStunting.class);

        burger.setOnClickListener(this);
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);
        menu4.setOnClickListener(this);
        menu5.setOnClickListener(this);
        menu6.setOnClickListener(this);
        menu7.setOnClickListener(this);

        loadRingkasan();
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void loadRingkasan() {
        Call<RingkasanStunting> call = endpointStunting.ringkasanStunting();
        call.enqueue(new Callback<RingkasanStunting>() {
            @Override
            public void onResponse(Call<RingkasanStunting> call, Response<RingkasanStunting> response) {
                loadingData.setVisibility(View.GONE);
                assert response.body() != null;
                jG1.setText(String.valueOf(response.body().getJumGiziBuruk()));
                jG2.setText(String.valueOf(response.body().getJumGiziKurang()));
                jG3.setText(String.valueOf(response.body().getJumGiziBaik()));
                jG4.setText(String.valueOf(response.body().getJumGiziLebih()));
                jG5.setText(String.valueOf(response.body().getJumGiziLebihLebih()));
                jG6.setText(String.valueOf(response.body().getJumObesitas()));

                if (response.body().getJumGiziBuruk() - response.body().getTotalGiziBuruk() < 0) {
                    tG1.setText("🔽 " + String.valueOf(Math.abs(response.body().getJumGiziBuruk() - response.body().getTotalGiziBuruk())).replace("-", ""));
                    tG1.setTextColor(Color.parseColor("#7CB342"));
                } else if (response.body().getJumGiziBuruk() - response.body().getTotalGiziBuruk() > 0) {
                    tG1.setText("🔼 " + String.valueOf((response.body().getJumGiziBuruk() - response.body().getTotalGiziBuruk())));
                    tG1.setTextColor(Color.parseColor("#BC0000"));
                } else {
                    tG1.setText("~");
                }

                if (response.body().getJumGiziKurang() - response.body().getTotalGiziKurang() < 0) {
                    tG2.setText("🔽 " + String.valueOf(Math.abs(response.body().getJumGiziKurang() - response.body().getTotalGiziKurang())).replace("-", ""));
                    tG2.setTextColor(Color.parseColor("#7CB342")); } else if (response.body().getJumGiziKurang() -
                        response.body().getTotalGiziKurang()> 0) {
                    tG2.setText("🔼 " + String.valueOf((response.body().getJumGiziKurang() - response.body().getTotalGiziKurang())));
                    tG2.setTextColor(Color.parseColor("#BC0000"));
                } else {
                    tG2.setText("~");
                }

                if (response.body().getJumGiziBaik() - response.body().getTotalGiziBaik() < 0) {
                    tG3.setText("🔽 " + String.valueOf(Math.abs(response.body().getJumGiziBaik() - response.body().getTotalGiziBaik())).replace("-", ""));
                    tG3.setTextColor(Color.parseColor("#BC0000")); } else if (response.body().getJumGiziBaik() -
                        response.body().getTotalGiziBaik()> 0) {
                    tG3.setText("🔼 " + String.valueOf((response.body().getJumGiziBaik() - response.body().getTotalGiziBaik())));
                    tG3.setTextColor(Color.parseColor("#7CB342"));
                } else {
                    tG3.setText("~");
                }

                if (response.body().getJumGiziLebih() - response.body().getTotalGiziLebih() < 0) {
                    tG4.setText("🔽 " + String.valueOf(Math.abs(response.body().getJumGiziLebih() - response.body().getTotalGiziLebih())).replace("-", ""));
                    tG4.setTextColor(Color.parseColor("#7CB342")); } else if (response.body().getJumGiziLebih() -
                        response.body().getTotalGiziLebih()> 0) {
                    tG4.setText("🔼 " + String.valueOf((response.body().getJumGiziLebih() - response.body().getTotalGiziLebih())));
                    tG4.setTextColor(Color.parseColor("#BC0000"));
                } else {
                    tG4.setText("~");
                }

                if (response.body().getJumGiziLebihLebih() - response.body().getTotalGiziLebihLebih() < 0) {
                    tG5.setText("🔽 " + String.valueOf(Math.abs(response.body().getJumGiziLebihLebih() - response.body().getTotalGiziLebihLebih())).replace("-", ""));
                    tG5.setTextColor(Color.parseColor("#7CB342")); } else if (response.body().getJumGiziLebihLebih() -
                        response.body().getTotalGiziLebihLebih()> 0) {
                    tG5.setText("🔼 " + String.valueOf((response.body().getJumGiziLebihLebih() - response.body().getTotalGiziLebihLebih())));
                    tG5.setTextColor(Color.parseColor("#BC0000"));
                } else {
                    tG5.setText("~");
                }

                if (response.body().getJumObesitas() - response.body().getTotalObesitas() < 0) {
                    tG6.setText("🔽 " + String.valueOf(Math.abs(response.body().getJumObesitas() - response.body().getTotalObesitas())).replace("-", ""));
                    tG6.setTextColor(Color.parseColor("#7CB342")); } else if (response.body().getJumObesitas() -
                        response.body().getTotalObesitas()> 0) {
                    tG6.setText("🔼 " + String.valueOf((response.body().getJumObesitas() - response.body().getTotalObesitas())));
                    tG6.setTextColor(Color.parseColor("#BC0000"));
                } else {
                    tG6.setText("~");
                }

                tt1.setText("Total: " + String.valueOf(response.body().getTotalGizi().getGiziBuruk()));
                tt2.setText("Total: " + String.valueOf(response.body().getTotalGizi().getGiziKurang()));
                tt3.setText("Total: " + String.valueOf(response.body().getTotalGizi().getGiziBaik()));
                tt4.setText("Total: " + String.valueOf(response.body().getTotalGizi().getResikoGiziLebih()));
                tt5.setText("Total: " + String.valueOf(response.body().getTotalGizi().getGiziLebih()));
                tt6.setText("Total: " + String.valueOf(response.body().getTotalGizi().getObesitas()));
            }

            @Override
            public void onFailure(Call<RingkasanStunting> call, Throwable t) {
                pbLoadData.setVisibility(View.GONE);
                tvLoadData.setText("GAGAL MEMUAT DATA DARI SERVER!\nPERIKSA JARINGAN INTERNET ANDA,\nATAU HUBUNGI TIK POLRES LANDAK!\n\n" + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.burger) {
            if (llmenu.getVisibility() == View.GONE) {
                viewToAnimate.setVisibility(View.VISIBLE);
                Animator slideIn = AnimatorInflater.loadAnimator(this, R.animator.slideinleft);
                slideIn.setTarget(viewToAnimate);
                slideIn.start();
            } else {
                viewToAnimate.setVisibility(View.GONE);
            }
        } else if (v.getId() == R.id.menu1) {
            Intent intent = new Intent(DashboardPeduliStunting.this, TambahDataAnak.class);
            startActivity(intent);
            viewToAnimate.setVisibility(View.GONE);
        } else if (v.getId() == R.id.menu2) {
            Intent intent = new Intent(DashboardPeduliStunting.this, TambahDataPerkembanganAnak.class);
            startActivity(intent);
            viewToAnimate.setVisibility(View.GONE);
        } else if (v.getId() == R.id.menu3) {
            Intent intent = new Intent(DashboardPeduliStunting.this, TambahDataIbuHamil.class);
            startActivity(intent);
            viewToAnimate.setVisibility(View.GONE);
        } else if (v.getId() == R.id.menu4) {
            Intent intent = new Intent(DashboardPeduliStunting.this, TambahDataPerkembanganIbuHamil.class);
            startActivity(intent);
            viewToAnimate.setVisibility(View.GONE);
        } else if (v.getId() == R.id.menu5) {
            Intent intent = new Intent(DashboardPeduliStunting.this, TambahDataIbuMenyusui.class);
            startActivity(intent);
            viewToAnimate.setVisibility(View.GONE);
        } else if (v.getId() == R.id.menu6) {
            Intent intent = new Intent(DashboardPeduliStunting.this, TambahDataPerkembanganIbuMenyusui.class);
            startActivity(intent);
            viewToAnimate.setVisibility(View.GONE);
        } else if (v.getId() == R.id.menu7) {
            Intent intent = new Intent(DashboardPeduliStunting.this, TambahLaporanKegiatan.class);
            startActivity(intent);
            viewToAnimate.setVisibility(View.GONE);
        }
    }

    @Override
    protected void  onResume(){
        super.onResume();
        loadingData.setVisibility(View.VISIBLE);
        loadRingkasan();
    }
}