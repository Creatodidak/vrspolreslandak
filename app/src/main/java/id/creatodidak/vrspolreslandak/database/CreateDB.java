package id.creatodidak.vrspolreslandak.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class CreateDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "vrsdata.db";
    private static final int DATABASE_VERSION = 1;
    private static CreateDB instance;

    public CreateDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized CreateDB getInstance(Context context) {
        if (instance == null) {
            instance = new CreateDB(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Membuat tabel-tabel di sini
        db.execSQL(HotspotTB.CREATE_TABLE);
//        db.execSQL(PenangananKarhutlaTB.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Meng-upgrade skema database di sini jika diperlukan
        db.execSQL("DROP TABLE IF EXISTS " + HotspotTB.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + PenangananKarhutlaTB.TABLE_NAME);
        onCreate(db);
    }

    public boolean isDatabaseExists() {
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();
        } catch (SQLiteException e) {
            // Database belum ada atau tidak dapat diakses
        }
        if (db != null) {
            db.close();
            return true;
        } else {
            return false;
        }
    }

    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }
}
