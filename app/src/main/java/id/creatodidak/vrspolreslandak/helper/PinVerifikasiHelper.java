package id.creatodidak.vrspolreslandak.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PinVerifikasiHelper {
    private Context context;
    private SharedPreferences sharedPreferences;

    public PinVerifikasiHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("SESSION_DATA", Context.MODE_PRIVATE);
    }

    public interface PinVerifikasiCallback {
        void onPinVerifikasiBerhasil();

        void onPinVerifikasiGagal();
    }

    public void cekPinVerifikasi(final String inputPin, final PinVerifikasiCallback callback) {
        String datapin = sharedPreferences.getString("PIN", null);

        if (datapin != null && datapin.equals(inputPin)) {
            callback.onPinVerifikasiBerhasil();
        } else {
            callback.onPinVerifikasiGagal();
        }
    }
}
