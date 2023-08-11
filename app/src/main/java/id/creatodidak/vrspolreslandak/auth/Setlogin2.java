package id.creatodidak.vrspolreslandak.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.dashboard.Dashboard;

@RequiresApi(api = Build.VERSION_CODES.P)
public class Setlogin2 extends AppCompatActivity {
    EditText etPin;
    TextView selanjutnya;
    SharedPreferences sharedPreferences;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.AuthenticationCallback authenticationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setlogin2);

        etPin = findViewById(R.id.etsetPin);
        selanjutnya = findViewById(R.id.selanjutnya);
        sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);

        selanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etPin.getText().toString().isEmpty()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("PIN", etPin.getText().toString());
                    editor.apply();
                    BiometricManager biometricManager = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        biometricManager = (BiometricManager) getSystemService(BIOMETRIC_SERVICE);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (biometricManager.canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS) {
                            editor.putBoolean("SIDIKJARI", false);
                            editor.apply();
                            Intent intent = new Intent(Setlogin2.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }

                    biometricPrompt = new BiometricPrompt.Builder(Setlogin2.this)
                            .setTitle("Verifikasi Sidik Jari")
                            .setNegativeButton("Batal", getMainExecutor(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).build();

                    authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                            editor.putBoolean("SIDIKJARI", true);
                            editor.apply();
                            Intent intent = new Intent(Setlogin2.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            Toast.makeText(Setlogin2.this, "Verifikasi sidik jari gagal. Coba lagi.", Toast.LENGTH_SHORT).show();
                        }
                    };

                    CancellationSignal cancellationSignal = new CancellationSignal();
                    biometricPrompt.authenticate(cancellationSignal, getMainExecutor(), authenticationCallback);
                }
            }
        });

        sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
    }
}