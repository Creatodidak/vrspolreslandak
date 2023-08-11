package id.creatodidak.vrspolreslandak.dashboard.karhutla;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.adapter.ListKarhutlaAdapter;
import id.creatodidak.vrspolreslandak.api.models.FetchDataKarhutla;
import id.creatodidak.vrspolreslandak.database.CreateDB;
import id.creatodidak.vrspolreslandak.database.HotspotTB;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import id.creatodidak.vrspolreslandak.service.SoundService;

//public class Karhutla extends AppCompatActivity implements OnMapReadyCallback {
public class Karhutla extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    TextView mkJudul, mkTanggal, mkcf7, mkcf8, mkcf9, nohot;
    SharedPreferences sharedPreferences;
    private Button btn, btnMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karhutla);
        sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        String wil = sharedPreferences.getString("wilayah", null);
        String sat = sharedPreferences.getString("satker", null);

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

        CreateDB createDB = CreateDB.getInstance(Karhutla.this);
        SQLiteDatabase db = createDB.getReadableDatabase();

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
        } else {
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


        btn = findViewById(R.id.btnLaporan);
        btnMaps = findViewById(R.id.btnMapsGlobal);


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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Periksa apakah izin lokasi diberikan
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, lakukan operasi yang membutuhkan izin lokasi
                enableMyLocation();
            } else {
                // Izin ditolak, Anda bisa memberikan pesan bahwa izin diperlukan untuk
                // menggunakan fitur peta atau melakukan tindakan lain
            }
        }
    }

    // Aktifkan fitur "My Location" pada peta
    private void enableMyLocation() {
//        if (mMap != null) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                mMap.setMyLocationEnabled(true);
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        mapView.onLowMemory();
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Menampilkan peta Kalimantan Barat
//        LatLng kalimantanBarat = new LatLng(-0.278780, 111.475494);
//        mMap.addMarker(new MarkerOptions().position(kalimantanBarat).title("Kalimantan Barat"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kalimantanBarat, 8));
//    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
