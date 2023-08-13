package id.creatodidak.vrspolreslandak.helper;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ShareWaUtils {

    public static void shareTextToWhatsApp(Context context, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp"); // Package name of WhatsApp

        try {
            context.startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            // WhatsApp not installed or no suitable app to handle the intent
            Toast.makeText(context, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
