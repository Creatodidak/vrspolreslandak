package id.creatodidak.vrspolreslandak.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import id.creatodidak.vrspolreslandak.auth.Blokir;
import id.creatodidak.vrspolreslandak.service.PinInputDialog;

@RequiresApi(api = Build.VERSION_CODES.P)
public class AuthHelper {

    private static final String PIN_ATTEMPT_KEY = "pin_attempt";
    private static final int MAX_PIN_ATTEMPTS = 3;

    private Context context;
    private SharedPreferences sharedPreferences;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.AuthenticationCallback authenticationCallback;

    public AuthHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("SESSION_DATA", Context.MODE_PRIVATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void performAuth(final AuthCallback callback) {
        boolean sidik = sharedPreferences.getBoolean("SIDIKJARI", false);

        if (sidik) {
            BiometricManager biometricManager = (BiometricManager) context.getSystemService(Context.BIOMETRIC_SERVICE);
            if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
                biometricPrompt = new BiometricPrompt.Builder(context)
                        .setTitle("Verifikasi Sidik Jari")
                        .setNegativeButton("Batal", context.getMainExecutor(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                callback.onAuthFailed();
                            }
                        }).build();

                authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        callback.onAuthSuccess();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        Toast.makeText(context, "Verifikasi sidik jari gagal. Coba lagi.", Toast.LENGTH_SHORT).show();
                        callback.onAuthFailed();
                    }
                };

                CancellationSignal cancellationSignal = new CancellationSignal();
                biometricPrompt.authenticate(cancellationSignal, context.getMainExecutor(), authenticationCallback);
            } else {
                showPinDialog(callback);
            }
        } else {
            showPinDialog(callback);
        }
    }

    private void showPinDialog(final AuthCallback callback) {
        int pinAttempts = sharedPreferences.getInt(PIN_ATTEMPT_KEY, 0);
        if (pinAttempts < MAX_PIN_ATTEMPTS) {
            PinInputDialog.showPinInputDialog((Activity) context, false, new PinInputDialog.PinInputCallback() {
                @Override
                public void onPinEntered(String pin) {
                    String datapin = sharedPreferences.getString("PIN", null);
                    if (pin.equals(datapin)) {
                        sharedPreferences.edit().putInt(PIN_ATTEMPT_KEY, 0).apply(); // Reset attempts
                        callback.onAuthSuccess();
                    } else {
                        showPinErrorDialog(callback);
                    }
                }
            });
        } else {
            // Panggil Blokir.java karena sudah 3 kali percobaan PIN yang salah
            Intent intent = new Intent(context, Blokir.class);
            context.startActivity(intent);

            // Finish activity sebelumnya
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        }
    }

    private void showPinErrorDialog(final AuthCallback callback) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("PIN SALAH");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int pinAttempts = sharedPreferences.getInt(PIN_ATTEMPT_KEY, 0);
                sharedPreferences.edit().putInt(PIN_ATTEMPT_KEY, pinAttempts + 1).apply();
                showPinDialog(callback);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public interface AuthCallback {
        void onAuthSuccess();
        void onAuthFailed();
    }
}
