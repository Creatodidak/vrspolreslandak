package id.creatodidak.vrspolreslandak.helper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static String getTodayFormatted() {
        // Get current date and time
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        // Format the date as "SENIN, 31 JULI 2023"
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM yyyy", new Locale("id"));
        return sdf.format(today);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getCurrentTimeWithFormat() {
        // Get the current time
        Date currentTime = new Date();

        // Create a SimpleDateFormat object with the desired format
        SimpleDateFormat sdf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("HH:mm:ss z");
        }
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7")); // Set the desired time zone

        // Format the current time using the SimpleDateFormat object
        String formattedTime = sdf.format(currentTime);

        return formattedTime;
    }
}
