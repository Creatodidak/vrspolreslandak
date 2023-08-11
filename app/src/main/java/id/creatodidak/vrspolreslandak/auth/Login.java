package id.creatodidak.vrspolreslandak.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.LoginResponse;
import id.creatodidak.vrspolreslandak.api.models.ServerResponse;
import id.creatodidak.vrspolreslandak.dashboard.pedulistunting.TambahDataAnak;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    EditText nrp, pass;

    Button btnlogin;
    SharedPreferences sharedPreferences;
    private AlertDialog dialog;
    private AlertDialog dialog2;
    Endpoint endpoint;
    @Override
    @RequiresApi(api = Build.VERSION_CODES.P)
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        nrp = findViewById(R.id.etNrp);
        pass = findViewById(R.id.etPass);
        btnlogin = findViewById(R.id.btLogin);

        endpoint = Client.getClient().create(Endpoint.class);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shownotif("LOGIN", "MENCOBA LOGIN...");

                Call<LoginResponse> call = endpoint.login(nrp.getText().toString(), pass.getText().toString());
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        if (response.isSuccessful()) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.isStatus()) {
                                List<LoginResponse.LoginDetail> loginDetails = loginResponse.getLoginDetails();
                                if (loginDetails != null && !loginDetails.isEmpty()) {
                                    LoginResponse.LoginDetail data = loginDetails.get(0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("nrp", data.getNrp());
                                    editor.putString("nama", data.getNama());
                                    editor.putString("pangkat", data.getPangkat());
                                    editor.putString("satker", data.getSatker());
                                    editor.putString("satfung", data.getSatfung());
                                    editor.putString("jabatan", data.getJabatan());
                                    editor.putString("tanggal_lahir", data.getTanggalLahir());
                                    editor.putString("foto", data.getFoto());
                                    editor.putString("wa", data.getWa());
                                    editor.putString("wilayah", data.getWilayah());
                                    editor.apply();

                                    getAndSaveFCMToken(data.getNrp(), data.getWilayah());
                                } else {
                                    // Show an error message for unexpected response format
                                    showpilihan("GAGAL LOGIN", "Server response format error", "ULANGI");
                                }
                            } else {
                                // Show an error message with the server's message
                                showpilihan("GAGAL LOGIN", loginResponse.getMsg(), "ULANGI");
                            }
                        } else {
                            // Show an error message for unsuccessful API call
                            showpilihan("GAGAL LOGIN", "COBA LAGI SAAT JARINGAN INTERNET ANDA BAGUS", "ULANGI");
                        }
                    }


                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        showpilihan("GAGAL LOGIN", "COBA LAGI SAAT JARINGAN INTERNET ANDA BAGUS", "ULANGI");
                    }
                });

            }
        });

        if (sharedPreferences.getBoolean("login", false)) {
            if (sharedPreferences.getString("PIN", null) != null) {
                Intent intent = new Intent(Login.this, Login2.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(Login.this, Setlogin2.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void shownotif(String judul, String pesan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(judul);
        builder.setMessage(pesan);
        builder.setIcon(R.drawable.icon); // Ganti R.drawable.icon dengan resource ID icon yang diinginkan
        builder.setCancelable(false);
        dialog2 = builder.create(); // Gunakan variabel dialog yang telah dideklarasikan sebagai field
        dialog2.show();
    }


    private void showpilihan(String judul, String pesan, String positif) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(judul);
        builder.setMessage(pesan);
        builder.setIcon(R.drawable.icon); // Ganti R.drawable.icon dengan resource ID icon yang diinginkan

        builder.setPositiveButton(positif, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.create(); // Gunakan variabel dialog yang telah dideklarasikan sebagai field
        dialog.show();
    }

    private void getAndSaveFCMToken(String nrp, String wilayah) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM TOKEN", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    String token = task.getResult();

                    if(!token.isEmpty()) {
                        Call<ServerResponse> call = endpoint.savetoken(nrp, wilayah, token);

                        call.enqueue(new Callback<ServerResponse>() {
                            @Override
                            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                dialog2.dismiss();

                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().isStatus()) {
                                        SharedPreferences sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("FCM_TOKEN", token);
                                        editor.putBoolean("login", true);
                                        editor.apply();

                                        Intent intent = null;
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                                            intent = new Intent(Login.this, Setlogin2.class);
                                        }

                                        startActivity(intent);
                                        finish();
                                    } else {
                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Login.this);
                                        builder.setTitle("Ooopsss!")
                                                .setMessage("Error: SEND FCM TOKEN (0x69)")
                                                .setPositiveButton("KELUAR", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        finish();
                                                    }
                                                })
                                                .setIcon(R.drawable.icon)
                                                .setCancelable(false);
                                        android.app.AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                } else {
                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Login.this);
                                    builder.setTitle("Ooopsss!")
                                            .setMessage("Error: SERVER ERROR (0x17)")
                                            .setPositiveButton("KELUAR", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    finish();
                                                }
                                            })
                                            .setIcon(R.drawable.icon)
                                            .setCancelable(false);
                                    android.app.AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ServerResponse> call, Throwable t) {
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Login.this);
                                builder.setTitle("Ooopsss!")
                                        .setMessage("Error: NETWORK ERROR (0x96)")
                                        .setPositiveButton("KELUAR", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                finish();
                                            }
                                        })
                                        .setIcon(R.drawable.icon)
                                        .setCancelable(false);
                                android.app.AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });
                    }else{
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Login.this);
                        builder.setTitle("Ooopsss!")
                                .setMessage("Error: NO FCM TOKEN! (0x07)")
                                .setPositiveButton("KELUAR", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .setCancelable(false);
                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
    }
}