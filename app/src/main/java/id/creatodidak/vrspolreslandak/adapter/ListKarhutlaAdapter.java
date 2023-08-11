package id.creatodidak.vrspolreslandak.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.models.FetchDataKarhutla;

public class ListKarhutlaAdapter extends RecyclerView.Adapter<ListKarhutlaAdapter.FetchDataKarhutlaViewHolder> {

    private final List<FetchDataKarhutla> fdKarhutla;

    public ListKarhutlaAdapter(List<FetchDataKarhutla> fdKarhutla) {
        this.fdKarhutla = fdKarhutla;
    }

    @NonNull
    @Override
    public FetchDataKarhutlaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listkarhutla, parent, false);
        return new FetchDataKarhutlaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FetchDataKarhutlaViewHolder holder, int position) {
        final FetchDataKarhutla FetchDataKarhutla = fdKarhutla.get(position);
        holder.lokasi.setText(FetchDataKarhutla.getLocationName());
        holder.koordinat.setText("(" + FetchDataKarhutla.getLatitude() + " , " + FetchDataKarhutla.getLongitude() + ")");

        if (FetchDataKarhutla.getConfidence() == 7) {
            holder.icon.setImageResource(R.drawable.firegreen);
            holder.textViewFetchDataKarhutlaName.setText("LOW");
        } else if (FetchDataKarhutla.getConfidence() == 8) {
            holder.icon.setImageResource(R.drawable.fireyellow);
            holder.textViewFetchDataKarhutlaName.setText("MEDIUM");
        } else if (FetchDataKarhutla.getConfidence() == 9) {
            holder.icon.setImageResource(R.drawable.firered);
            holder.textViewFetchDataKarhutlaName.setText("HIGH");
        } else {
            holder.icon.setImageResource(R.drawable.fireblack);
            holder.textViewFetchDataKarhutlaName.setText("UNKNOWN");
        }

        holder.maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMaps(v.getContext(), FetchDataKarhutla.getLatitude(), FetchDataKarhutla.getLongitude());
            }
        });

        holder.btResp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "FITUR BELUM TERSEDIA!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGoogleMaps(Context context, double latitude, double longitude) {
        String uri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        // Memeriksa apakah Google Maps terpasang pada perangkat
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            // Jika Google Maps tidak terpasang, Anda bisa menambahkan logika untuk menampilkan pesan atau tindakan alternatif.
            // Contohnya, membuka web browser dengan URL Google Maps.
            // Anda juga dapat menunjukkan pesan bahwa pengguna perlu menginstal Google Maps untuk menggunakan fitur ini.
            Toast.makeText(context, "Google Maps tidak terpasang", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return fdKarhutla.size();
    }

    static class FetchDataKarhutlaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFetchDataKarhutlaName, lokasi, koordinat;
        ImageView icon;
        Button maps, btResp;

        FetchDataKarhutlaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFetchDataKarhutlaName = itemView.findViewById(R.id.status);
            lokasi = itemView.findViewById(R.id.lkLokasi);
            koordinat = itemView.findViewById(R.id.lkKoordinat);
            icon = itemView.findViewById(R.id.lkIcon);
            maps = itemView.findViewById(R.id.btnMapslk);
            btResp = itemView.findViewById(R.id.btnResponselk);
        }
    }
}
