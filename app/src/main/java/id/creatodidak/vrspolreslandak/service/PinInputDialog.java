package id.creatodidak.vrspolreslandak.service;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import id.creatodidak.vrspolreslandak.R;

public class PinInputDialog {

    public interface PinInputCallback {
        void onPinEntered(String pin);
    }

    public static void showPinInputDialog(@NonNull Context context, boolean cancelable, @NonNull final PinInputCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pindialog, null);
        builder.setView(view);

        final EditText editPin = view.findViewById(R.id.editPin);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        TextView textTitle = view.findViewById(R.id.textTitle);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(cancelable);  // Set cancelable option

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = editPin.getText().toString();
                if (pin.length() == 4) {
                    callback.onPinEntered(pin);
                    dialog.dismiss();
                } else {
                    editPin.setError("PIN harus terdiri dari 4 angka");
                }
            }
        });

        dialog.show();
    }
}
