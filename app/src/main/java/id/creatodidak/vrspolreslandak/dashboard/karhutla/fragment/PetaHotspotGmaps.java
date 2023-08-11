package id.creatodidak.vrspolreslandak.dashboard.karhutla.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.database.CreateDB;
import id.creatodidak.vrspolreslandak.database.HotspotTB;
import id.creatodidak.vrspolreslandak.helper.BitmapUtils;
import id.creatodidak.vrspolreslandak.helper.DateUtils;

public class PetaHotspotGmaps extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private final String wilayah = "menyuke";
    private final String satker = "POLSEK MENYUKE";

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            PetaHotspotGmaps.this.googleMap = googleMap;

            // Tampilkan lokasi pengguna pada peta (jika izin lokasi telah diberikan)
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            if (location != null) {
                                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 7));

                                CreateDB createDB = CreateDB.getInstance(requireContext());
                                TextView mjudul = getView().findViewById(R.id.mapsjudul);
                                TextView mtgl = getView().findViewById(R.id.mapstanggal);
//                                mjudul.setText("DATA HOTSPOT DI WILKUM "+satker);
                                mjudul.setText("DATA HOTSPOT DI WILKUM POLRES LANDAK");
                                mtgl.setText(DateUtils.getTodayFormatted());

                                SQLiteDatabase db = createDB.getReadableDatabase();
//                                Cursor cursor = HotspotTB.getHotspotsByToday(db, wilayah);
//                                Cursor cursor1 = HotspotTB.getCountByConfidence(db, wilayah);
                                Cursor cursor = HotspotTB.getHotspotsByTodaya(db);
                                Cursor cursor1 = HotspotTB.getCountByConfidencea(db);
                                if (cursor1 != null) {
                                    while (cursor1.moveToNext()) {
                                        @SuppressLint("Range") int confidence = cursor1.getInt(cursor1.getColumnIndex(HotspotTB.COLUMN_CONFIDENCE));
                                        @SuppressLint("Range") int count = cursor1.getInt(cursor1.getColumnIndex("count"));

                                        // Add logging to check count values
                                        Log.d("PetaHotspotGmaps", "Confidence: " + confidence + ", Count: " + count);

                                        // Update the TextViews to display the count based on confidence
                                        if (confidence == 7) {
                                            TextView cf7TextView = getView().findViewById(R.id.cf7m);
                                            cf7TextView.setText(String.valueOf(count));
                                        } else if (confidence == 8) {
                                            TextView cf8TextView = getView().findViewById(R.id.cf8m);
                                            cf8TextView.setText(String.valueOf(count));
                                        } else if (confidence == 9) {
                                            TextView cf9TextView = getView().findViewById(R.id.cf9m);
                                            cf9TextView.setText(String.valueOf(count));
                                        }
                                    }

                                    cursor1.close();
                                }

                                // Iterate through the cursor and add markers for each hotspot
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        @SuppressLint("Range") double latitude = cursor.getDouble(cursor.getColumnIndex(HotspotTB.COLUMN_LATITUDE));
                                        @SuppressLint("Range") double longitude = cursor.getDouble(cursor.getColumnIndex(HotspotTB.COLUMN_LONGITUDE));
                                        @SuppressLint("Range") int conf = cursor.getInt(cursor.getColumnIndex(HotspotTB.COLUMN_CONFIDENCE));
                                        @SuppressLint("Range") String locationName = cursor.getString(cursor.getColumnIndex(HotspotTB.COLUMN_LOCATION));

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

                                        Bitmap customMarkerBitmap = BitmapUtils.getBitmapFromVectorDrawable(requireContext(), customMarkerIcon);

                                        // Set the custom marker icon as a bitmap for each hotspot
                                        MarkerOptions markerOptions = new MarkerOptions()
                                                .position(hotspotLocation)
                                                .title(locationName)
                                                .icon(BitmapDescriptorFactory.fromBitmap(customMarkerBitmap));

                                        googleMap.addMarker(markerOptions);
                                    }
                                    cursor.close();
                                }

//                                // Menambahkan marker tambahan
//                                LatLng markerLocation = new LatLng(0.0123, 109.1234); // Latitude, Longitude (Koordinat marker tambahan)
//                                googleMap.addMarker(new MarkerOptions().position(markerLocation).title("Marker Tambahan"));
                            }
                        });
            } else {
                // Jika izin lokasi tidak diberikan, minta izin lokasi
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_peta_hotspot_gmaps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        // Inisialisasi FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }
}
