package id.creatodidak.vrspolreslandak.dashboard.pedulistunting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.ClientStunting;
import id.creatodidak.vrspolreslandak.api.EndpointStunting;
import id.creatodidak.vrspolreslandak.api.models.stunting.AdapterItem;
import id.creatodidak.vrspolreslandak.api.models.stunting.DataPosyanduAdapter;
import id.creatodidak.vrspolreslandak.api.models.stunting.HapusItem;
import id.creatodidak.vrspolreslandak.api.models.stunting.ModelItem;
import id.creatodidak.vrspolreslandak.api.models.stunting.RingkasanStunting;
import id.creatodidak.vrspolreslandak.helper.CustomDialogForm;
import id.creatodidak.vrspolreslandak.helper.NonScrollableLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBingkisan extends AppCompatActivity implements AdapterItem.OnItemClickListener {

    Button btnAdditem;
    RecyclerView item;
    CustomDialogForm customDialog;
    EndpointStunting endpointStunting;
    SharedPreferences sh;
    String type;
    String satker;
    CardView belum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bingkisan);

        btnAdditem = findViewById(R.id.btnTambahItemBingkisan);
        item = findViewById(R.id.rvItemBingkisan);
        endpointStunting = ClientStunting.getClient().create(EndpointStunting.class);

        sh = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        satker = sh.getString("satker", "POLRES LANDAK");
        type = "bingkisan";
        belum = findViewById(R.id.belumadadata);

        btnAdditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

        loadData();
    }

    private void loadData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddBingkisan.this);
        builder.setMessage("Memuat data...")
                .setIcon(R.drawable.icon)
                .setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();

        Call<ModelItem> call = endpointStunting.getItem(satker, type);
        call.enqueue(new Callback<ModelItem>() {
            @Override
            public void onResponse(Call<ModelItem> call, Response<ModelItem> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().isAda()){
                        alert.dismiss();
                        belum.setVisibility(View.GONE);

                        List<ModelItem.Item> itemList = response.body().getItemList();

                        AdapterItem adapter = new AdapterItem(AddBingkisan.this, itemList, AddBingkisan.this);

                        RecyclerView.LayoutManager lm = new LinearLayoutManager(AddBingkisan.this);
                        item.setLayoutManager(lm);
                        item.setAdapter(adapter);
                    }else{
                        alert.dismiss();
                        belum.setVisibility(View.VISIBLE);
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddBingkisan.this);
                    builder.setTitle("DATA GAGAL DIMUAT")
                            .setMessage("Silahkan periksa jaringan anda atau hubungi TIK Polres Landak")
                            .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    alert.dismiss();
                                    finish();
                                }
                            })
                            .setIcon(R.drawable.icon)
                            .setCancelable(false);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<ModelItem> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBingkisan.this);
                builder.setTitle("DATA GAGAL DIMUAT")
                        .setMessage("Silahkan periksa jaringan anda atau hubungi TIK Polres Landak")
                        .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                alert.dismiss();
                                finish();
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void deleteData(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddBingkisan.this);
        builder.setMessage("Menghapus data...")
                .setIcon(R.drawable.icon)
                .setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();

        String ids = String.valueOf(id);
        Call<HapusItem> call = endpointStunting.delItem(satker, type, id);
        call.enqueue(new Callback<HapusItem>() {
            @Override
            public void onResponse(Call<HapusItem> call, Response<HapusItem> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().isSuccess()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddBingkisan.this);
                        builder.setTitle("RESPONSE")
                                .setMessage(response.body().getMessage())
                                .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        alert.dismiss();
                                        loadData();
                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddBingkisan.this);
                        builder.setTitle("RESPONSE")
                                .setMessage(response.body().getMessage())
                                .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        alert.dismiss();
                                        loadData();
                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddBingkisan.this);
                    builder.setTitle("DATA GAGAL DIHAPUS")
                            .setMessage("Silahkan periksa jaringan anda atau hubungi TIK Polres Landak")
                            .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    alert.dismiss();
                                    loadData();
                                }
                            })
                            .setIcon(R.drawable.icon)
                            .setCancelable(false);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<HapusItem> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBingkisan.this);
                builder.setTitle("DATA GAGAL DIHAPUS")
                        .setMessage("Silahkan periksa jaringan anda atau hubungi TIK Polres Landak")
                        .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                alert.dismiss();
                                loadData();
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void showCustomDialog() {
        customDialog = new CustomDialogForm(this, new CustomDialogForm.OnSubmitClickListener() {
            @Override
            public void onSubmitClicked(String data) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBingkisan.this);
                builder.setTitle("⚠ Peringatan")
                        .setMessage("Yakin tambah data?")
                        .setPositiveButton("KIRIM DATA", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                kirimdata(data);
                            }
                        })
                        .setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();

                customDialog.dismiss();
            }
        });

        customDialog.show();
    }

    private void kirimdata(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddBingkisan.this);
        builder.setMessage("Mengirim data...")
                .setIcon(R.drawable.icon)
                .setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();

        Call<HapusItem> call = endpointStunting.sendItem(data, satker, type);
        call.enqueue(new Callback<HapusItem>() {
            @Override
            public void onResponse(Call<HapusItem> call, Response<HapusItem> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().isSuccess()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddBingkisan.this);
                        builder.setTitle("RESPONSE")
                                .setMessage(response.body().getMessage())
                                .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        alert.dismiss();
                                        loadData();
                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddBingkisan.this);
                        builder.setTitle("RESPONSE")
                                .setMessage(response.body().getMessage())
                                .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        alert.dismiss();
                                        loadData();
                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddBingkisan.this);
                    builder.setTitle("DATA GAGAL DIKIRIM")
                            .setMessage("Silahkan periksa jaringan anda atau hubungi TIK Polres Landak")
                            .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    alert.dismiss();
                                    loadData();
                                }
                            })
                            .setIcon(R.drawable.icon)
                            .setCancelable(false);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<HapusItem> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBingkisan.this);
                builder.setTitle("DATA GAGAL DIKIRIM")
                        .setMessage("Silahkan periksa jaringan anda atau hubungi TIK Polres Landak")
                        .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                alert.dismiss();
                                loadData();
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public void onDeleteClick(int itemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Gunakan 'this' atau getActivity() jika Anda berada dalam fragment
        builder.setTitle("YAKIN HAPUS DATA?")
                .setMessage("Data yang sudah dihapus tidak dapat dikembalikan...")
                .setPositiveButton("YA, HAPUS DATA!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        deleteData(itemId);
                    }
                })
                .setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.icon)
                .setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }

}