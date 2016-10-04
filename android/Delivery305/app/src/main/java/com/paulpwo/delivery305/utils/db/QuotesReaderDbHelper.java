package com.paulpwo.delivery305.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Keep;

/**
 * Created by paulpwo on 11/7/16.
 */
@Keep
public class QuotesReaderDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Delivery305.db";
    public static final int DATABASE_VERSION = 1;

    public QuotesReaderDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Crear la tabla Quotes
        db.execSQL(QuotesDataSource.CREATE_QUOTES_SCRIPT);
        db.execSQL(QuotesDataSource.CREATE_QUOTES_SCRIPT_2);
        //Insertar registros iniciales
       // db.execSQL(QuotesDataSource.INSERT_QUOTES_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Añade los cambios que se realizarán en el esquema
    }
}
