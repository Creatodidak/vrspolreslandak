package id.creatodidak.vrspolreslandak.api.models.stunting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.models.stunting.ModelItem;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ViewHolder> {

    private Context context;
    private List<ModelItem.Item> itemList;
    private OnItemClickListener onItemClickListener;

    public AdapterItem(Context context, List<ModelItem.Item> itemList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.itemList = itemList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelItem.Item item = itemList.get(position);

        holder.namaItemTextView.setText(item.getNama());

        holder.hapusItemImageView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onDeleteClick(item.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView namaItemTextView;
        ImageView hapusItemImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namaItemTextView = itemView.findViewById(R.id.namaitem);
            hapusItemImageView = itemView.findViewById(R.id.hapusitem);
        }
    }

    public interface OnItemClickListener {
        void onDeleteClick(int itemId);
    }
}
