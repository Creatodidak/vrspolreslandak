package id.creatodidak.vrspolreslandak.dashboard.pedulistunting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.ClientStunting;
import id.creatodidak.vrspolreslandak.api.EndpointStunting;
import id.creatodidak.vrspolreslandak.api.models.stunting.AmbilToken;
import id.creatodidak.vrspolreslandak.api.models.stunting.Dokumentasi;
import id.creatodidak.vrspolreslandak.api.models.stunting.DokumentasiAdapter;
import id.creatodidak.vrspolreslandak.api.models.stunting.DokumentasiItem;
import id.creatodidak.vrspolreslandak.api.models.stunting.HapusItem;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import id.creatodidak.vrspolreslandak.helper.ShareWaUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = 30) // Requires Android 11 or later
public class Dokstunting extends AppCompatActivity {
    private Button btnAmbilGambar;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    Button btnKirim, btnTambah;
    ConstraintLayout foto;
    ConstraintSet constraintSet;
    ImageView fotonya, wm1, btnCancel;
    TextView wm2, wm3, wm4, wm5, wm6, koordinat;
    Spinner spjenis;
    CardView wmbox, formTambah, belumadadata;
    EditText target, lokasi;
    LocationManager locationManager;
    EndpointStunting endpoint;
    String LOKASI_FILE = "VRS/STUNTING";
    String LABEL_FILE = "KAMPANYEKAARHUTLA_";
    Uri contentUri;
    SharedPreferences sharedPreferences;
    String wilayah, nrp;
    String nama;
    private String currentPhotoPath;
    private ActivityResultLauncher<Intent> galleryLauncher;
    RecyclerView rv;

    private List<String> jenis = Arrays.asList("PILIH JENIS DOKUMENTASI", "KEGIATAN", "AKTIVITAS PIMPINAN", "PENGUKURAN ANTROPOMETRI DAN PEMERIKSAAN KESEHATAN ANAK", "PEMERIKSAAN KESEHATAN IBU", "EDUKASI KEPADA PESERTA", "PEMBERIAN PAKET BINGKISAN", "PEMBERIAN MAKANAN ASUPAN GIZI TAMBAHAN");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dokstunting);

        btnAmbilGambar = findViewById(R.id.btnAmbilGambar);
        foto = findViewById(R.id.fotoPenanganan);
        fotonya = findViewById(R.id.fotonya);
        wm1 = findViewById(R.id.wm1);
        wm2 = findViewById(R.id.wm2);
        wm3 = findViewById(R.id.wm3);
        wm4 = findViewById(R.id.wm4);
        wm6 = findViewById(R.id.wm6);
        wmbox = findViewById(R.id.wmbox);
        constraintSet = new ConstraintSet();
        endpoint = ClientStunting.getClient().create(EndpointStunting.class);
        btnKirim = findViewById(R.id.btnKirimGambar);
        btnCancel = findViewById(R.id.btnCancels);
        formTambah = findViewById(R.id.formDokumentasi);
        spjenis = findViewById(R.id.spJenisDok);
        btnTambah = findViewById(R.id.btnTambahDokumentasi);
        rv = findViewById(R.id.rvDokumentasi);
        sharedPreferences = getSharedPreferences("SESSION_DATA", Context.MODE_PRIVATE);
        wilayah = sharedPreferences.getString("satker", null);
        nrp = sharedPreferences.getString("nrp", null);
        nama = sharedPreferences.getString("pangkat", null)+" "+sharedPreferences.getString("nama", null);
        belumadadata = findViewById(R.id.belumadadata);

        ArrayAdapter<String> jkAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenis);
        jkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spjenis.setAdapter(jkAdapter);

        spjenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = spjenis.getSelectedItem().toString();
                if(!selected.equals("PILIH JENIS DOKUMENTASI")) {
                    wm3.setText("DOKUMENTASI " + selected + "\nPOSYANDU PRESISI " + sharedPreferences.getString("satker", null));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                handleGalleryResult(data);
            }
        });

        btnAmbilGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!spjenis.getSelectedItem().toString().equals("PILIH JENIS DOKUMENTASI")) {
                    opengaleri();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Dokstunting.this);
                    builder.setTitle("Periksa kembali data anda!")
                            .setMessage("PILIH JENIS DOKUMENTASI TERLEBIH DAHULU!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(R.drawable.icon)
                            .setCancelable(false);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formTambah.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
                btnTambah.setVisibility(View.GONE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formTambah.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                btnTambah.setVisibility(View.VISIBLE);
            }
        });

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Dokstunting.this);
                builder.setTitle("KONFIRMASI")
                        .setMessage("Dokumentasi yang sudah diupload tidak dapat dihapus kembali, lanjutkan?")
                        .setPositiveButton("LANJUTKAN", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                kirimdata();
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
        });

        getToken(nama);
    }

    private void kirimdata() {
        Bitmap bitmap = getBitmapFromView(foto);
        uploadImage(bitmap);
    }
    private void uploadImage(Bitmap bitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Dokstunting.this);
        builder.setMessage("Mengupload dokumentasi...")
                .setIcon(R.drawable.icon)
                .setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", "image.jpg", imageRequestBody);
        String satker = sharedPreferences.getString("satker", "");

        Call<HapusItem> call = endpoint.uploadDokumentasi(satker, spjenis.getSelectedItem().toString(), image);
        call.enqueue(new Callback<HapusItem>() {
            @Override
            public void onResponse(Call<HapusItem> call, Response<HapusItem> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().isSuccess()){
                        alert.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Dokstunting.this);
                        builder.setTitle("BERHASIL")
                                .setMessage(response.body().getMessage().toUpperCase())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        formTambah.setVisibility(View.GONE);
                                        rv.setVisibility(View.VISIBLE);
                                        btnTambah.setVisibility(View.VISIBLE);
                                        getToken(nama);
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else{
                        alert.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(Dokstunting.this);
                        builder.setTitle("GAGAL")
                                .setMessage(response.body().getMessage().toUpperCase())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }else{
                    alert.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(Dokstunting.this);
                    builder.setTitle("GAGAL")
                            .setMessage("DOKUMENTASI GAGAL DIKIRIM!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
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
                alert.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(Dokstunting.this);
                builder.setTitle("GAGAL")
                        .setMessage("DOKUMENTASI GAGAL DIKIRIM!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


    }
    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    private void opengaleri() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }
    private void handleGalleryResult(Intent data) {
        if (data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    foto.setVisibility(View.VISIBLE);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    String imagePath = cursor.getString(column_index);

                    // Handle the image file obtained from the gallery
                    if (imagePath != null) { // Check if imagePath is not null
                        currentPhotoPath = imagePath; // Update currentPhotoPath with the selected image path

                        File imageFile = new File(imagePath);
                        Uri imageUri = Uri.fromFile(imageFile);
                        Glide.with(this)
                                .load(imageUri)
                                .into(fotonya);

                        wm1.setVisibility(View.VISIBLE);
                        wm2.setVisibility(View.VISIBLE);
                        wmbox.setVisibility(View.VISIBLE);

                        btnAmbilGambar.setText("GANTI");
                        btnKirim.setVisibility(View.VISIBLE);

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(currentPhotoPath, options);
                        int width = options.outWidth;
                        int height = options.outHeight;
                        int fpb = findGreatestCommonDivisor(width, height);
                        int simplifiedWidth = width / fpb;
                        int simplifiedHeight = height / fpb;
                        String ratio = String.format(Locale.getDefault(), "%d:%d", simplifiedWidth, simplifiedHeight);
                        constraintSet.clone(foto);
                        constraintSet.setDimensionRatio(R.id.fotonya, ratio);
                        constraintSet.applyTo(foto);

//                        kirim.setVisibility(View.VISIBLE);
                        wm2.setText(nrp);

                        wm4.setText(DateUtils.getTodayFormatted());
                    } else {
                        Toast.makeText(this, "Image path is null", Toast.LENGTH_SHORT).show();
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }
    private int findGreatestCommonDivisor(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    private void getToken(String nama) {
        
        AlertDialog.Builder builder = new AlertDialog.Builder(Dokstunting.this);
        builder.setMessage("Mengambil token...")
                .setIcon(R.drawable.icon)
                .setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();

        Call<AmbilToken> call = endpoint.getToken(nama);
        call.enqueue(new Callback<AmbilToken>() {
            @Override
            public void onResponse(Call<AmbilToken> call, Response<AmbilToken> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().isStatus()){
                        getLaporan(response.body().getToken());
                        alert.dismiss();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(Dokstunting.this);
                        builder.setTitle("TOKEN")
                                .setMessage("TOKEN TIDAK TERSEDIA, COBA LAGI NANTI ATAU HUBUNGI TIK PORLES LANDAK!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        alert.dismiss();
                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .setCancelable(false);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Dokstunting.this);
                    builder.setTitle("TOKEN")
                            .setMessage("TOKEN TIDAK TERSEDIA, COBA LAGI NANTI ATAU HUBUNGI TIK PORLES LANDAK!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    alert.dismiss();
                                }
                            })
                            .setIcon(R.drawable.icon)
                            .setCancelable(false);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<AmbilToken> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Dokstunting.this);
                builder.setTitle("TOKEN")
                        .setMessage("TOKEN TIDAK TERSEDIA, COBA LAGI NANTI ATAU HUBUNGI TIK PORLES LANDAK!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                alert.dismiss();
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    private void getLaporan(String token) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Dokstunting.this);
        builder.setMessage("Mengambil Laporan...")
                .setIcon(R.drawable.icon)
                .setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
        Call<Dokumentasi> call = endpoint.getDokumentasi(token, sharedPreferences.getString("satker", ""));
        call.enqueue(new Callback<Dokumentasi>() {
            @Override
            public void onResponse(Call<Dokumentasi> call, Response<Dokumentasi> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().isAda()){
                        alert.dismiss();
                        List<DokumentasiItem> dokumentasiList = response.body().getDokumentasi();

                        DokumentasiAdapter adapter = new DokumentasiAdapter(Dokstunting.this, dokumentasiList);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(Dokstunting.this);
                        rv.setLayoutManager(layoutManager);
                        rv.setAdapter(adapter);

                        belumadadata.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                    }else{
                        alert.dismiss();
                        belumadadata.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                    }
                }else{
                    alert.dismiss();
                    belumadadata.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Dokumentasi> call, Throwable t) {
                alert.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(Dokstunting.this);
                builder.setTitle("NIHIL")
                        .setMessage("DOKUMENTASI TIDAK TERSEDIA, COBA LAGI NANTI ATAU HUBUNGI TIK PORLES LANDAK!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                belumadadata.setVisibility(View.VISIBLE);
                                rv.setVisibility(View.GONE);
                            }
                        })
                        .setIcon(R.drawable.icon)
                        .setCancelable(false);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}
