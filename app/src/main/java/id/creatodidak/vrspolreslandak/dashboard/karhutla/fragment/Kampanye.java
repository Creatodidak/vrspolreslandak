package id.creatodidak.vrspolreslandak.dashboard.karhutla.fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.ServerResponse;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Kampanye extends Fragment implements View.OnClickListener {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    Button btnKirim, btnFoto, btnRefresh;
    ConstraintLayout foto;
    ConstraintSet constraintSet;
    ImageView fotonya, wm1;
    TextView wm2, wm3, wm4, wm5, wm6, koordinat;
    CardView wmbox;
    EditText target, lokasi;
    LocationManager locationManager;
    Endpoint endpoint;
    String LOKASI_FILE = "VRS/KARHUTLA/KAMPANYE";
    String LABEL_FILE = "KAMPANYEKAARHUTLA_";
    Uri contentUri;
    SharedPreferences sharedPreferences;
    String wilayah;
    private String currentPhotoPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kampanye, container, false);

        btnKirim = view.findViewById(R.id.btnKirimKampanye);
        btnFoto = view.findViewById(R.id.btnFoto);
        foto = view.findViewById(R.id.fotoPenanganan);
        fotonya = view.findViewById(R.id.fotonya);
        wm1 = view.findViewById(R.id.wm1);
        wm2 = view.findViewById(R.id.wm2);
        wm3 = view.findViewById(R.id.wm3);
        wm4 = view.findViewById(R.id.wm4);
        wm5 = view.findViewById(R.id.wm5);
        wm6 = view.findViewById(R.id.wm6);
        wmbox = view.findViewById(R.id.wmbox);
        constraintSet = new ConstraintSet();
        target = view.findViewById(R.id.etTargetKampanye);
        lokasi = view.findViewById(R.id.etLokasiKampanye);
        endpoint = Client.getClient().create(Endpoint.class);
        koordinat = view.findViewById(R.id.tvKoordinatKampanye);
        btnRefresh = view.findViewById(R.id.btRefKoordinat);

        getLocation(koordinat);


        sharedPreferences = getActivity().getSharedPreferences("SESSION_DATA", Context.MODE_PRIVATE);
        wilayah = sharedPreferences.getString("satker", null);

        btnFoto.setOnClickListener(this);
        btnKirim.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnFoto) {
            dispatchCameraIntent();
        } else if (v.getId() == R.id.btnKirimKampanye) {
            kirimdata();
        } else if (v.getId() == R.id.btRefKoordinat) {
            koordinat.setText("Merefresh lokasi...");
            getLocation(koordinat);
        }
    }

    private void kirimdata() {
        String namatarget = target.getText().toString();
        String lokasikampanye = lokasi.getText().toString();
        String koord = koordinat.getText().toString();
        String nrp = sharedPreferences.getString("nrp", null);
        Bitmap bitmap = getBitmapFromView(foto);
        saveBitmapToGallery(bitmap);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", "image.jpg", imageRequestBody);

        Call<ServerResponse> call = endpoint.updKampanyeKarhutla(nrp, koord, namatarget, lokasikampanye, image);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }

    private void dispatchCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                File photoFile = createImageFile(getActivity());
                if (photoFile != null) {
                    Uri photoUri = FileProvider.getUriForFile(getActivity(),
                            getActivity().getPackageName() + ".provider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    private File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = LABEL_FILE + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), LOKASI_FILE);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Tampilkan foto di ImageView
            Glide.with(this)
                    .load(currentPhotoPath)
                    .into(fotonya);

            wmbox.setVisibility(View.VISIBLE);
            wm1.setVisibility(View.VISIBLE);
            wm2.setVisibility(View.VISIBLE);

            // Dapatkan rasio foto dari currentPhotoPath
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(currentPhotoPath, options);
            int width = options.outWidth;
            int height = options.outHeight;

            // Menyederhanakan rasio dengan FPB
            int fpb = findGreatestCommonDivisor(width, height);
            int simplifiedWidth = width / fpb;
            int simplifiedHeight = height / fpb;

            // Hasil rasio yang disederhanakan
            String ratio = String.format(Locale.getDefault(), "%d:%d", simplifiedWidth, simplifiedHeight);

            // Ubah rasio ConstraintLayout dengan id R.id.foto
            constraintSet.clone(foto);
            constraintSet.setDimensionRatio(R.id.fotonya, ratio);
            constraintSet.applyTo(foto);

            btnFoto.setVisibility(View.GONE);
            btnKirim.setEnabled(true);
            wm2.setText("98070129");
            wm3.setText("LAPORAN KAMPANYE MENGAJAK MASYARAKAT BERSAMA - SAMA UNTUK MENCEGAH KARHUTLA DI WILAYAH HUKUM " + wilayah);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wm4.setText(DateUtils.getTodayFormatted().toUpperCase() + " " + DateUtils.getCurrentTimeWithFormat());
            }

            // Dapatkan lokasi dan tampilkan di wm5
            getLocation(wm5);
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

    @SuppressLint("MissingPermission")
    private void getLocation(TextView tv) {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String locationText = latitude + "," + longitude;
                tv.setText(locationText);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }
        };

        // Set minimum waktu dan jarak untuk mendapatkan pembaruan lokasi
        long minTime = 10000; // 10 detik
        float minDistance = 10; // 10 meter

        // Meminta pembaruan lokasi dari provider
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener);
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void saveBitmapToGallery(Bitmap bitmap) {
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), LOKASI_FILE);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = LABEL_FILE + timeStamp + ".jpg";
        File imageFile = new File(storageDir, imageFileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            contentUri = Uri.fromFile(imageFile);
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
