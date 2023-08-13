package id.creatodidak.vrspolreslandak.api.models.stunting;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;

public class DokumentasiAdapter extends RecyclerView.Adapter<DokumentasiAdapter.DokumentasiViewHolder> {

    private Context context;
    private List<DokumentasiItem> dokumentasiList;

    public DokumentasiAdapter(Context context, List<DokumentasiItem> dokumentasiList) {
        this.context = context;
        this.dokumentasiList = dokumentasiList;
    }

    @NonNull
    @Override
    public DokumentasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemdokumentasi, parent, false);
        return new DokumentasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DokumentasiViewHolder holder, int position) {
        DokumentasiItem dokumentasiItem = dokumentasiList.get(position);
        boolean dok = holder.sharedPreferences.getBoolean(dokumentasiItem.getLink(), false);

        Glide.with(context)
                .load(dokumentasiItem.getLink())
                .placeholder(R.drawable.bg)
                .error(R.drawable.bg)
                .timeout(15000)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.dokGambar);


        String dokJenisText = "DOKUMENTASI " + dokumentasiItem.getJenis() +" DI POSYANDU PRESISI " + dokumentasiItem.getSatker();
        holder.dokJenis.setText(dokJenisText);
        holder.dokSatker.setText(dokumentasiItem.getSatker());
        if(dok){
            holder.dokDownload.setVisibility(View.GONE);
        }

        holder.dokDownload.setOnClickListener(v -> {
            String imageUrl = dokumentasiItem.getLink();
            String fileName = "image_" + System.currentTimeMillis() + ".jpg";
            String downloadDir = Environment.getExternalStorageDirectory() + File.separator + "VRS/STUNTING";

            File directory = new File(downloadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl))
                    .setTitle("Downloading Image")
                    .setDescription("Image Download")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "VRS/STUNTING/" + fileName);

            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            long downloadId = downloadManager.enqueue(request);

            BroadcastReceiver onCompleteReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long receivedDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (downloadId == receivedDownloadId) {

                        SharedPreferences.Editor editor = holder.sharedPreferences.edit();
                        editor.putBoolean(dokumentasiItem.getLink(), true);
                        editor.apply();

                        holder.dokDownload.setVisibility(View.GONE);

                        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            long[] pattern = {0, 200, 100, 200, 100, 200}; // Vibrate for 200ms, pause for 100ms, vibrate for 200ms, and so on
                            VibrationEffect vibrationEffect = VibrationEffect.createWaveform(pattern, -1); // -1 means don't repeat
                            vibrator.vibrate(vibrationEffect);
                        } else {
                            vibrator.vibrate(1000);
                        }


                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "download")
                                .setContentTitle("DOWNLOAD SELESAI")
                                .setContentText("Dokumentasi Berhasil Di Download, gunakan dengan bijak...")
                                .setSmallIcon(R.mipmap.icon)
                                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon))
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        notificationManager.notify(1, builder.build());

                        context.unregisterReceiver(this);
                    }
                }
            };
            context.registerReceiver(onCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        });



    }

    @Override
    public int getItemCount() {
        return dokumentasiList.size();
    }

    public class DokumentasiViewHolder extends RecyclerView.ViewHolder {

        ImageView dokGambar;
        TextView dokJenis, dokSatker;
        Button dokDownload;
        SharedPreferences sharedPreferences;

        public DokumentasiViewHolder(@NonNull View itemView) {
            super(itemView);
            dokGambar = itemView.findViewById(R.id.dokGambar);
            dokJenis = itemView.findViewById(R.id.dokJenis);
            dokSatker = itemView.findViewById(R.id.dokSatker);
            dokDownload = itemView.findViewById(R.id.dokDownload);
            sharedPreferences = context.getSharedPreferences("DOKUMENTASI", Context.MODE_PRIVATE);
        }
    }
}
