package id.creatodidak.vrspolreslandak.dashboard.karhutla.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import id.creatodidak.vrspolreslandak.R;

public class PetaHotspot extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Konfigurasi OSM
        Configuration.getInstance().load(getActivity(),
                getActivity().getSharedPreferences("OpenStreetMap", 0));

        // Inflate layout untuk fragment
        View rootView = inflater.inflate(R.layout.fragment_peta_hotspot, container, false);

        // Inisialisasi MapView
        MapView mapView = rootView.findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        // Jika ingin mengakses lokasi pengguna pada peta:
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(true);

        GeoPoint kalimantanBarat = new GeoPoint(0.0221, 109.3304); // Latitude, Longitude (Koordinat Kalimantan Barat)
        mapView.getController().setCenter(kalimantanBarat);
        mapView.getController().setZoom(7); // Anda dapat menyesuaikan level zoom sesuai kebutuhan
        return rootView;
    }
}
