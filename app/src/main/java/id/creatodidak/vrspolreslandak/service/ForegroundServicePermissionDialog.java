package id.creatodidak.vrspolreslandak.service;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ForegroundServicePermissionDialog extends DialogFragment {

    private PermissionListener permissionListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            permissionListener = (PermissionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement PermissionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("This app needs foreground service permission to run in the background.")
                .setTitle("Foreground Service Permission")
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Notify the activity that the permission is granted
                        permissionListener.onPermissionGranted();
                    }
                })
                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Notify the activity that the permission is denied
                        permissionListener.onPermissionDenied();
                    }
                });

        return builder.create();
    }

    public interface PermissionListener {
        void onPermissionGranted();

        void onPermissionDenied();
    }
}
