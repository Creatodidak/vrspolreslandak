package id.creatodidak.vrspolreslandak.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HotspotTB {

    public static final String TABLE_NAME = "hotspot";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_DATA_ID = "data_id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_CONFIDENCE = "confidence";
    public static final String COLUMN_RADIUS = "radius";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_WILAYAH = "wilayah";
    public static final String COLUMN_RESPONSE = "response";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_NOTIF = "notif";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATA_ID + " TEXT, " +
                    COLUMN_LATITUDE + " TEXT, " +
                    COLUMN_LONGITUDE + " TEXT, " +
                    COLUMN_CONFIDENCE + " TEXT, " +
                    COLUMN_RADIUS + " TEXT, " +
                    COLUMN_LOCATION + " TEXT, " +
                    COLUMN_WILAYAH + " TEXT, " +
                    COLUMN_RESPONSE + " TEXT, " +
                    COLUMN_STATUS + " TEXT, " +
                    COLUMN_NOTIF + " TEXT, " +
                    COLUMN_CREATED_AT + " TEXT, " +
                    COLUMN_UPDATED_AT + " TEXT)";

    public static void insertHotspotIfNotExist(SQLiteDatabase db, String dataId, double latitude,
                                               double longitude, int confidence, int radius,
                                               String location, String wilayah, String response,
                                               String status, String notif, String createdAt, String updatedAt) {
        // Cek apakah data dengan dataId sudah ada di database
        String[] projection = {COLUMN_DATA_ID, COLUMN_RESPONSE, COLUMN_STATUS};
        String selection = COLUMN_DATA_ID + "=?";
        String[] selectionArgs = {dataId};
        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        // Jika dataId tidak ditemukan di database, masukkan data tersebut ke database
        if (cursor == null || cursor.getCount() == 0) {
            cursor.close();
            insertHotspot(db, dataId, latitude, longitude, confidence, radius, location,
                    wilayah, response, status, notif, createdAt, updatedAt);
        } else {
            // Jika dataId sudah ada di database, cek nilai response dan status
            cursor.moveToFirst();
            String existingResponse = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESPONSE));
            String existingStatus = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));

            // Bandingkan nilai response dan status yang ada di database dengan data dari server
            boolean isResponseSame = (response == null && existingResponse == null) || (response != null && response.equals(existingResponse));
            boolean isStatusSame = (status == null && existingStatus == null) || (status != null && status.equals(existingStatus));

            // Jika nilai response atau status berbeda, lakukan pembaruan data di database
            if (!isResponseSame || !isStatusSame) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_RESPONSE, response);
                values.put(COLUMN_STATUS, status);

                db.update(TABLE_NAME, values, selection, selectionArgs);
            }

            cursor.close();
        }
    }


    public static long insertHotspot(SQLiteDatabase db, String dataId, double latitude,
                                     double longitude, int confidence, int radius,
                                     String location, String wilayah, String response,
                                     String status, String notif, String createdAt, String updatedAt) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATA_ID, dataId);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_CONFIDENCE, confidence);
        values.put(COLUMN_RADIUS, radius);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_WILAYAH, wilayah);
        values.put(COLUMN_RESPONSE, response);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_NOTIF, notif);
        values.put(COLUMN_CREATED_AT, createdAt);
        values.put(COLUMN_UPDATED_AT, updatedAt);
        return db.insert(TABLE_NAME, null, values);
    }

    public static Cursor getAllHotspots(SQLiteDatabase db) {
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public static Cursor getHotspotsByToday(SQLiteDatabase db, String wilayah) {
        // Get today's date in the format "YYYY-MM-DD"
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Build the SQL query with the WHERE clause to filter by today's date and wilayah
        String selection = "SUBSTR(" + COLUMN_CREATED_AT + ", 1, 10) = ? AND " + COLUMN_WILAYAH + " = ?";
        String[] selectionArgs = {todayDate, wilayah};

        // Execute the query with the WHERE clause to filter by today's date and wilayah
        return db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }

    public static Cursor getCountByConfidence(SQLiteDatabase db, String wilayah) {
        // Get today's date in "YYYY-MM-DD" format
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Build the SQL query with the WHERE clause to filter by today's date and wilayah
        String query = "SELECT " + COLUMN_CONFIDENCE + ", COUNT(*) AS count FROM " + TABLE_NAME +
                " WHERE SUBSTR(" + COLUMN_CREATED_AT + ", 1, 10) = ? AND " + COLUMN_WILAYAH + " = ?" +
                " GROUP BY " + COLUMN_CONFIDENCE;

        // Execute the query with the todayDate and wilayah as the selection arguments
        return db.rawQuery(query, new String[]{todayDate, wilayah});
    }


    public static Cursor getHotspotsByTodaya(SQLiteDatabase db) {
        // Get today's date in the format "YYYY-MM-DD"
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Build the SQL query with the WHERE clause to filter by today's date
        String selection = "SUBSTR(" + COLUMN_CREATED_AT + ", 1, 10) = ?";
        String[] selectionArgs = {todayDate};

        // Execute the query with the WHERE clause to filter by today's date
        return db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }


    public static Cursor getCountByConfidencea(SQLiteDatabase db) {
        // Get today's date in "YYYY-MM-DD" format
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Build the SQL query with the WHERE clause to filter by today's date
        String query = "SELECT " + COLUMN_CONFIDENCE + ", COUNT(*) AS count FROM " + TABLE_NAME +
                " WHERE SUBSTR(" + COLUMN_CREATED_AT + ", 1, 10) = ?" +
                " GROUP BY " + COLUMN_CONFIDENCE;

        // Execute the query with the todayDate as the selection argument
        return db.rawQuery(query, new String[]{todayDate});
    }


    public static void updateAllNotifToYes(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIF, "YES");

        db.update(TABLE_NAME, values, null, null);
    }

    public static Cursor getHotspotsNotif(SQLiteDatabase db, String wilayah) {
        // Get today's date in the format "YYYY-MM-DD"
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Build the SQL query with the WHERE clause to filter by today's date, notif = null, and wilayah
        String selection = "SUBSTR(" + COLUMN_CREATED_AT + ", 1, 10) = ? AND " + COLUMN_NOTIF + " IS NULL AND " + COLUMN_WILAYAH + " = ?";
        String[] selectionArgs = {todayDate, wilayah};

        // Execute the query with the WHERE clause to filter by today's date, notif = null, and wilayah
        return db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }

    public static Cursor getHotspotsNotifa(SQLiteDatabase db) {
        // Get today's date in the format "YYYY-MM-DD"
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Build the SQL query with the WHERE clause to filter by today's date and notif = null
        String selection = "SUBSTR(" + COLUMN_CREATED_AT + ", 1, 10) = ? AND " + COLUMN_NOTIF + " IS NULL";
        String[] selectionArgs = {todayDate};

        // Execute the query with the WHERE clause to filter by today's date and notif = null
        return db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }

    public static boolean hasHotspotsWithNullNotif(SQLiteDatabase db) {
        // Get today's date in the format "YYYY-MM-DD"
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Build the SQL query with the WHERE clause to filter by today's date and notif = null
        String selection = "SUBSTR(" + COLUMN_CREATED_AT + ", 1, 10) = ? AND " + COLUMN_NOTIF + " IS NULL";
        String[] selectionArgs = {todayDate};

        // Execute the query with the WHERE clause to filter by today's date and notif = null
        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        // Check if there are any rows in the cursor (i.e., data with null value in COLUMN_NOTIF)
        boolean hasDataWithNullNotif = cursor != null && cursor.moveToFirst();

        // Close the cursor to release resources
        if (cursor != null) {
            cursor.close();
        }

        return hasDataWithNullNotif;
    }

    public static boolean hasHotspotsNotif(SQLiteDatabase db, String wilayah) {
        // Get today's date in the format "YYYY-MM-DD"
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Build the SQL query with the WHERE clause to filter by today's date, notif = null, and wilayah
        String selection = "SUBSTR(" + COLUMN_CREATED_AT + ", 1, 10) = ? AND " + COLUMN_NOTIF + " IS NULL AND " + COLUMN_WILAYAH + " = ?";
        String[] selectionArgs = {todayDate, wilayah};

        // Execute the query with the WHERE clause to filter by today's date, notif = null, and wilayah
        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        // Check if the cursor is not empty (has data)
        boolean hasData = cursor != null && cursor.moveToFirst();

        // Close the cursor to release resources
        if (cursor != null) {
            cursor.close();
        }

        return hasData;
    }


    // Buat metode lain untuk update, delete, dll. sesuai kebutuhan
}
