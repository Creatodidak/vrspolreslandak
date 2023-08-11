package id.creatodidak.vrspolreslandak.dashboard.karhutla;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.adapter.ListKarhutlaAdapter;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.DataKarhutla;
import id.creatodidak.vrspolreslandak.api.models.FetchDataKarhutla;
import id.creatodidak.vrspolreslandak.api.models.ResponseChecker;
import id.creatodidak.vrspolreslandak.database.CreateDB;
import id.creatodidak.vrspolreslandak.database.HotspotTB;
import id.creatodidak.vrspolreslandak.helper.AuthHelper;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import id.creatodidak.vrspolreslandak.helper.PinVerifikasiHelper;
import id.creatodidak.vrspolreslandak.helper.Verifikasi;
import id.creatodidak.vrspolreslandak.service.FirebaseMsg;
import id.creatodidak.vrspolreslandak.service.PinInputDialog;
import id.creatodidak.vrspolreslandak.service.SoundService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Karhutla extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int AUTH_REQUEST_CODE = 1 ;
    TextView mkJudul, mkTanggal, mkcf7, mkcf8, mkcf9, nohot;
    SharedPreferences sharedPreferences;
    private Button btn, btnMaps;
    String wil, sat;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karhutla);

        sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        wil = sharedPreferences.getString("wilayah", null);
        sat = sharedPreferences.getString("satker", null);

        if(FirebaseMsg.BG != null && FirebaseMsg.BG.isPlaying()){
            FirebaseMsg.BG.stop();
        }

        mkJudul = findViewById(R.id.mkJudul);
        mkTanggal = findViewById(R.id.mkTanggal);
        mkcf7 = findViewById(R.id.mkcf7);
        mkcf8 = findViewById(R.id.mkcf8);
        mkcf9 = findViewById(R.id.mkcf9);
        nohot = findViewById(R.id.nohotspot);
        mkJudul.setText("DATA HOTSPOT DI WILKUM " + sat);

        mkTanggal.setText(DateUtils.getTodayFormatted());

        Intent stopSoundIntent = new Intent(this, SoundService.class);
        stopService(stopSoundIntent);

        btn = findViewById(R.id.btnLaporan);
        btnMaps = findViewById(R.id.btnMapsGlobal);
        loadfromserver();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Karhutla.this, LaporanKarhutla.class);
                startActivity(intent);
            }
        });

        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Karhutla.this, MapsGlobal.class);
                startActivity(intent);
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    private void loadfromserver() {
        String wilayah = sharedPreferences.getString("wilayah", null);

        Endpoint endpoint = Client.getClient().create(Endpoint.class);
        Call<ResponseChecker> call = endpoint.cekhotspot(wilayah);
        call.enqueue(new Callback<ResponseChecker>() {
            @Override
            public void onResponse(Call<ResponseChecker> call, Response<ResponseChecker> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isStatus()) {
                        CreateDB createDB = CreateDB.getInstance(Karhutla.this);

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
                                loadfromlocal(db);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseChecker> call, Throwable t) {
                Toast.makeText(Karhutla.this, "GAGAL MEMANGGIL SERVER!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadfromlocal(SQLiteDatabase db) {
        if (wil.equals("ALL")) {
            List<FetchDataKarhutla> fetchDataKarhutlas = new ArrayList<>();
            Cursor cursor1 = HotspotTB.getCountByConfidencea(db);
            if (cursor1 != null) {
                while (cursor1.moveToNext()) {
                    @SuppressLint("Range") int confidence = cursor1.getInt(cursor1.getColumnIndex(HotspotTB.COLUMN_CONFIDENCE));
                    @SuppressLint("Range") int count = cursor1.getInt(cursor1.getColumnIndex("count"));

                    // Update the TextViews to display the count based on confidence
                    if (confidence == 7) {
                        mkcf7.setText(String.valueOf(count));
                    } else if (confidence == 8) {
                        mkcf8.setText(String.valueOf(count));
                    } else if (confidence == 9) {
                        mkcf9.setText(String.valueOf(count));
                    }
                }

                cursor1.close();
            }

            Cursor cursor = HotspotTB.getHotspotsByTodaya(db);
            if (cursor != null) {
                nohot.setVisibility(View.GONE);
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") double latitude = cursor.getDouble(cursor.getColumnIndex(HotspotTB.COLUMN_LATITUDE));
                    @SuppressLint("Range") double longitude = cursor.getDouble(cursor.getColumnIndex(HotspotTB.COLUMN_LONGITUDE));
                    @SuppressLint("Range") int conf = cursor.getInt(cursor.getColumnIndex(HotspotTB.COLUMN_CONFIDENCE));
                    @SuppressLint("Range") String locationName = cursor.getString(cursor.getColumnIndex(HotspotTB.COLUMN_LOCATION));

                    FetchDataKarhutla fetchDataKarhutla = new FetchDataKarhutla(latitude, longitude, conf, locationName);
                    fetchDataKarhutlas.add(fetchDataKarhutla);

                }
                cursor.close();
            } else {
                nohot.setVisibility(View.VISIBLE);
            }

            RecyclerView recyclerView = findViewById(R.id.rvKarhutla);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            ListKarhutlaAdapter listKarhutlaAdapter = new ListKarhutlaAdapter(fetchDataKarhutlas);
            recyclerView.setAdapter(listKarhutlaAdapter);
        }
        else
        {
            List<FetchDataKarhutla> fetchDataKarhutlas = new ArrayList<>();
            Cursor cursor1 = HotspotTB.getCountByConfidence(db, wil.toLowerCase());
            if (cursor1 != null) {
                Log.i(wil, "DATA ADA!");
                while (cursor1.moveToNext()) {
                    @SuppressLint("Range") int confidence = cursor1.getInt(cursor1.getColumnIndex(HotspotTB.COLUMN_CONFIDENCE));
                    @SuppressLint("Range") int count = cursor1.getInt(cursor1.getColumnIndex("count"));

                    // Update the TextViews to display the count based on confidence
                    if (confidence == 7) {
                        mkcf7.setText(String.valueOf(count));
                    } else if (confidence == 8) {
                        mkcf8.setText(String.valueOf(count));
                    } else if (confidence == 9) {
                        mkcf9.setText(String.valueOf(count));
                    }
                }

                cursor1.close();
            }

            Cursor cursor = HotspotTB.getHotspotsByToday(db, wil.toLowerCase());
            if (cursor != null) {
                Log.i(wil, "DATA ADA XXX!");
                nohot.setVisibility(View.GONE);
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") double latitude = cursor.getDouble(cursor.getColumnIndex(HotspotTB.COLUMN_LATITUDE));
                    @SuppressLint("Range") double longitude = cursor.getDouble(cursor.getColumnIndex(HotspotTB.COLUMN_LONGITUDE));
                    @SuppressLint("Range") int conf = cursor.getInt(cursor.getColumnIndex(HotspotTB.COLUMN_CONFIDENCE));
                    @SuppressLint("Range") String locationName = cursor.getString(cursor.getColumnIndex(HotspotTB.COLUMN_LOCATION));

                    FetchDataKarhutla fetchDataKarhutla = new FetchDataKarhutla(latitude, longitude, conf, locationName);
                    fetchDataKarhutlas.add(fetchDataKarhutla);

                }
                cursor.close();
            } else {
                nohot.setVisibility(View.VISIBLE);
            }

            RecyclerView recyclerView = findViewById(R.id.rvKarhutla);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            ListKarhutlaAdapter listKarhutlaAdapter = new ListKarhutlaAdapter(fetchDataKarhutlas);
            recyclerView.setAdapter(listKarhutlaAdapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Periksa apakah izin lokasi diberikan
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {

            }
        }
    }

    private void enableMyLocation() {
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onPause() {
        super.onPause();
//        AuthHelper authHelper = new AuthHelper(this);
//
//        authHelper.performAuth(new AuthHelper.AuthCallback() {
//            @Override
//            public void onAuthSuccess() {
//                selubung.setVisibility(View.GONE);
//                loadfromserver();
//            }
//
//            @Override
//            public void onAuthFailed() {
//                // Otentikasi gagal, lakukan tindakan yang sesuai.
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from the AuthLogin activity
        if (requestCode == AUTH_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Authentication successful, continue loading Karhutla activity
                setContentView(R.layout.activity_karhutla);
            } else {
                // Authentication failed, close the app or show an error message
                finish();
            }
        }
    }
}
