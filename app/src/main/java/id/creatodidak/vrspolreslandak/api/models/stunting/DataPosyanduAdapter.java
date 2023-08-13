package id.creatodidak.vrspolreslandak.api.models.stunting;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.creatodidak.vrspolreslandak.R;

public class DataPosyanduAdapter extends RecyclerView.Adapter<DataPosyanduAdapter.ViewHolder> {

    private List<RingkasanStunting.Dataposyandu> dataList;

    public DataPosyanduAdapter(List<RingkasanStunting.Dataposyandu> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posyandupresisi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RingkasanStunting.Dataposyandu data = dataList.get(position);

        if (data.isAda()) {
            holder.itemView.setVisibility(View.VISIBLE);

            holder.tvJudul.setText("POSYANDU PRESISI "+data.getSatker());
            holder.tvPolsek.setText("Ringkasan:"); // Ganti ke String.valueOf(data.getJumanak())
            holder.tvAnak.setText(String.valueOf(data.getJumanak()) + " Orang"); // Konversi ke string
            holder.tvIbuHamil.setText(String.valueOf(data.getJumibuhamil()) + " Orang"); // Konversi ke string
            holder.tvIbuMenyusui.setText(String.valueOf(data.getJumibumenyusui()) + " Orang"); // Konversi ke string
            holder.tvVitamin.setText(String.valueOf(data.getJumvitamin())); // Konversi ke string
//            holder.tvBingkisan.setText(String.valueOf(data.getJumbingkisan()) + " Paket"); // Konversi ke string
//            holder.tvEPudding.setText(String.valueOf(data.getJummakanan()) + " Paket");

            List<RingkasanStunting.Daftarbingkisan> daftarbingkisanList = data.getDaftarbingkisanList();
            List<RingkasanStunting.Daftarmakanan> daftarmakananList = data.getDaftarmakananList();

            if (daftarbingkisanList != null && !daftarbingkisanList.isEmpty()) {
                StringBuilder bingkisanText = new StringBuilder();
                for (RingkasanStunting.Daftarbingkisan bingkisan : daftarbingkisanList) {
                    bingkisanText.append("- "+ bingkisan.getNama()).append("\n");
                }
                bingkisanText.delete(bingkisanText.length() - 1, bingkisanText.length());
                holder.tvBingkisan.setText(String.valueOf(data.getJumbingkisan()) + " Paket @ berisikan:\n"+bingkisanText.toString());
            } else {
                holder.tvBingkisan.setText("Tidak ada bingkisan");
            }

            if (daftarmakananList != null && !daftarmakananList.isEmpty()) {
                StringBuilder makananText = new StringBuilder();
                for (RingkasanStunting.Daftarmakanan makanan : daftarmakananList) {
                    makananText.append("- "+ makanan.getNama()).append("\n");
                }
                makananText.delete(makananText.length() - 1, makananText.length());
                holder.tvEPudding.setText(String.valueOf(data.getJummakanan()) + " Paket @ berisikan:\n"+makananText.toString());
            } else {
                holder.tvEPudding.setText("Tidak ada makanan");
            }

        } else {
            holder.tvJudul.setText("POSYANDU PRESISI "+data.getSatker());
            holder.tvPolsek.setText("TIDAK ADA KEGIATAN POSYANDU PRESISI");
            holder.tvPolsek.setTextColor(Color.parseColor("#ff0000"));
            holder.tabel.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvPolsek, tvAnak, tvIbuHamil, tvIbuMenyusui, tvVitamin, tvBingkisan, tvEPudding;
        TableLayout tabel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tabel = itemView.findViewById(R.id.tabel);
            tvJudul = itemView.findViewById(R.id.rvJudul);
            tvPolsek = itemView.findViewById(R.id.rvPolsek);
            tvAnak = itemView.findViewById(R.id.rvAnak);
            tvIbuHamil = itemView.findViewById(R.id.rvIbuHamil);
            tvIbuMenyusui = itemView.findViewById(R.id.rvIbuMenyusui);
            tvVitamin = itemView.findViewById(R.id.rvVitamin);
            tvBingkisan = itemView.findViewById(R.id.rvBingkisan);
            tvEPudding = itemView.findViewById(R.id.rvEkstraPudding);
        }
    }
}
