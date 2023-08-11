package id.creatodidak.vrspolreslandak.auth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.dashboard.Dashboard;

public class Login2 extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    EditText pin;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.AuthenticationCallback authenticationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        pin = findViewById(R.id.loginpin);

        String datapin = sharedPreferences.getString("PIN", null);
        boolean sidik = sharedPreferences.getBoolean("SIDIKJARI", false);

        if (sidik) {
            BiometricManager biometricManager = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                biometricManager = (BiometricManager) getSystemService(BIOMETRIC_SERVICE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (biometricManager.canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS) {

                    return;
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                biometricPrompt = new BiometricPrompt.Builder(this)
                        .setTitle("Verifikasi Sidik Jari")
                        .setNegativeButton("Batal", getMainExecutor(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                pin.setVisibility(View.VISIBLE);
                            }
                        }).build();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        Intent intent = new Intent(Login2.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        // Handle fingerprint authentication failure
                        Toast.makeText(Login2.this, "Verifikasi sidik jari gagal. Coba lagi.", Toast.LENGTH_SHORT).show();
                    }
                };
            }

            CancellationSignal cancellationSignal = new CancellationSignal();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                biometricPrompt.authenticate(cancellationSignal, getMainExecutor(), authenticationCallback);
            }
        } else {
            pin.setVisibility(View.VISIBLE);
        }


        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    if (pin.getText().toString().equals(datapin)) {
                        Intent intent = new Intent(Login2.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                        hideSoftKeyboard();
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login2.this);
                        alertDialogBuilder.setTitle("PIN SALAH");
                        alertDialogBuilder.setPositiveButton("OK", null);
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        pin.setText("");
                        showSoftKeyboard();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}