package id.creatodidak.vrspolreslandak.dashboard.karhutla;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.database.CreateDB;
import id.creatodidak.vrspolreslandak.database.HotspotTB;
import id.creatodidak.vrspolreslandak.helper.BitmapUtils;

public class MapsGlobal extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_global);

        mapView = findViewById(R.id.mapglobal);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    @SuppressLint("Range")
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 7));

                            CreateDB createDB = CreateDB.getInstance(this);
                            SQLiteDatabase db = createDB.getReadableDatabase();
                            Cursor cursor = HotspotTB.getHotspotsByTodaya(db);
                            Cursor cursor1 = HotspotTB.getCountByConfidencea(db);
                            int total = 0;

                            while (cursor1.moveToNext()) {
                                int confidence = cursor1.getInt(cursor1.getColumnIndex(HotspotTB.COLUMN_CONFIDENCE));
                                int count = cursor1.getInt(cursor1.getColumnIndex("count"));
                                // Add logging to check count values
                                Log.d("PetaHotspotGmaps", "Confidence: " + confidence + ", Count: " + count);
                                total = total + count;
                                if (confidence == 7) {
                                    TextView cf7TextView = findViewById(R.id.cf7m);
                                    cf7TextView.setText(String.valueOf(count));
                                } else if (confidence == 8) {
                                    TextView cf8TextView = findViewById(R.id.cf8m);
                                    cf8TextView.setText(String.valueOf(count));
                                } else if (confidence == 9) {
                                    TextView cf9TextView = findViewById(R.id.cf9m);
                                    cf9TextView.setText(String.valueOf(count));
                                }
                            }

                            TextView totals = findViewById(R.id.cftotal);
                            totals.setText(String.valueOf(total));
                            cursor1.close();

                            // Iterate through the cursor and add markers for each hotspot
                            if (cursor != null) {
                                while (cursor.moveToNext()) {
                                    double latitude = cursor.getDouble(cursor.getColumnIndex(HotspotTB.COLUMN_LATITUDE));
                                    double longitude = cursor.getDouble(cursor.getColumnIndex(HotspotTB.COLUMN_LONGITUDE));
                                    int conf = cursor.getInt(cursor.getColumnIndex(HotspotTB.COLUMN_CONFIDENCE));
                                    String locationName = cursor.getString(cursor.getColumnIndex(HotspotTB.COLUMN_LOCATION));

                                    LatLng hotspotLocation = new LatLng(latitude, longitude);

                                    int customMarkerIcon;

                                    if (conf == 7) {
                                        customMarkerIcon = R.drawable.markergreen;
                                    } else if (conf == 8) {
                                        customMarkerIcon = R.drawable.markeryellow;
                                    } else if (conf == 9) {
                                        customMarkerIcon = R.drawable.markerred;
                                    } else {
                                        customMarkerIcon = R.drawable.fireblack;
                                    }

                                    Bitmap customMarkerBitmap = BitmapUtils.getBitmapFromVectorDrawable(this, customMarkerIcon);
                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .position(hotspotLocation)
                                            .title(locationName)
                                            .icon(BitmapDescriptorFactory.fromBitmap(customMarkerBitmap));


                                    googleMap.addMarker(markerOptions);
                                }
                                cursor.close();
                            }
                        }
                    });
        } else {
            // Jika izin lokasi tidak diberikan, minta izin lokasi
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
