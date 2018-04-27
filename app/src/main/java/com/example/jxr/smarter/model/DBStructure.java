package com.example.jxr.smarter.model;

import android.provider.BaseColumns;

/**
 * Created by jxr on 25/4/18.
 */

public class DBStructure {
    public static abstract class tableEntry implements BaseColumns {
        public static final String TABLE_NAME = "usage";
        public static final String COLUMN_ID = "userid";
        public static final String COLUMN_AIR = "air";
        public static final String COLUMN_FRIDGE = "fridge";
        public static final String COLUMN_WASH = "wash";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_HOUR = "hour";
        public static final String COLUMN_TEMP = "temp";
    }
}
