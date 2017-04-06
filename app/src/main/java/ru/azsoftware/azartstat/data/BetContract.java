package ru.azsoftware.azartstat.data;

/**
 * Created by Андрей on 12.03.2017.
 */

import android.provider.BaseColumns;

public final class BetContract {


    public static final class BetEntry implements BaseColumns {
        public final static String TABLE_NAME = "bet";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_BANK = "bank";
        public final static String COLUMN_PROFIT = "profit";
        public final static String COLUMN_REVERT_DATE = "revertdate";
        ;

    }
}
