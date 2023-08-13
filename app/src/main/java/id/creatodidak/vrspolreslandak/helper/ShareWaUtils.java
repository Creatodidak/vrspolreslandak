package id.creatodidak.vrspolreslandak.helper;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

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


    public static void shareTextAndImagesToWhatsApp(Context context, String text, List<String> imageUrls) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);

        ArrayList<Uri> imageUris = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            // Convert image URLs to Uri and add them to the list
            Uri imageUri = Uri.parse(imageUrl);
            imageUris.add(imageUri);
        }

        sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        sendIntent.setType("image/*");

        sendIntent.setPackage("com.whatsapp"); // Package name of WhatsApp

        try {
            context.startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            // WhatsApp not installed or no suitable app to handle the intent
            Toast.makeText(context, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void shareTextAndDrawablesToWhatsApp(Context context, String text, List<Drawable> imageDrawables) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);

        // Set the text as an ArrayList<CharSequence> (list with a single element)
        ArrayList<CharSequence> textList = new ArrayList<>();
        textList.add(text);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textList);

        ArrayList<Uri> imageUris = new ArrayList<>();
        for (Drawable drawable : imageDrawables) {
            Bitmap bitmap = drawableToBitmap(drawable);
            Uri imageUri = getImageUri(context, bitmap);
            imageUris.add(imageUri);
        }

        sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        sendIntent.setType("image/*");

        sendIntent.setPackage("com.whatsapp");

        try {
            context.startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show();
        }
    }


    // Helper methods for converting Drawable to Bitmap and Bitmap to Uri
    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private static Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Image", null);
        return Uri.parse(path);
    }



}
