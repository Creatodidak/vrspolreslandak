package id.creatodidak.vrspolreslandak.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.P)
public class Verifikasi {

    private Context context;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.AuthenticationCallback authenticationCallback;

    public Verifikasi(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void cekVerifikasiSidikJari(final VerifikasiCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            BiometricManager biometricManager = (BiometricManager) context.getSystemService(Context.BIOMETRIC_SERVICE);
            if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
                biometricPrompt = new BiometricPrompt.Builder(context)
                        .setTitle("Verifikasi Sidik Jari")
                        .setNegativeButton("Batal", context.getMainExecutor(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                callback.onVerifikasiFailed();
                            }
                        }).build();

                authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        callback.onVerifikasiBerhasil();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        Toast.makeText(context, "Verifikasi sidik jari gagal. Coba lagi.", Toast.LENGTH_SHORT).show();
                        callback.onVerifikasiFailed();
                    }
                };

                CancellationSignal cancellationSignal = new CancellationSignal();
                biometricPrompt.authenticate(cancellationSignal, context.getMainExecutor(), authenticationCallback);
            } else {
                callback.onVerifikasiTidakDidukung();
            }
        } else {
            // Versi Android tidak mendukung verifikasi sidik jari
            callback.onVerifikasiTidakDidukung();
        }
    }

    public interface VerifikasiCallback {
        void onVerifikasiBerhasil();
        void onVerifikasiFailed();
        void onVerifikasiTidakDidukung();
    }
}
