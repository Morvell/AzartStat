package ru.azsoftware.azartstat.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.azsoftware.azartstat.data.BetContract.BetEntry;

public class BetDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = BetDBHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "hotel.db";

    private static final int DATABASE_VERSION = 1;

    public BetDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + BetEntry.TABLE_NAME + " ("
                + BetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BetEntry.COLUMN_DATE + " TEXT NOT NULL, "
                + BetEntry.COLUMN_BANK + " INTEGER NOT NULL DEFAULT 0, "
                + BetEntry.COLUMN_PROFIT + " INTEGER NOT NULL DEFAULT 0);";

        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_GUESTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
