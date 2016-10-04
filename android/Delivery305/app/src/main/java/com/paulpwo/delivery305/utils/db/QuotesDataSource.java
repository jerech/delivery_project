package com.paulpwo.delivery305.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Keep;

/**
 * Created by paulpwo on 11/7/16.
 */
@Keep
public class QuotesDataSource {

    //Metainformación de la base de datos
    public static final String QUOTES_TABLE_NAME = "delivery305";


       //Script de Creación de la tabla Quotes
 /*   public static final String CREATE_QUOTES_SCRIPT ="CREATE TABLE IF NOT EXISTS delivery305 " +
            "( _id integer PRIMARY KEY AUTOINCREMENT,  " +
            "id_restaurant integer, " +
            "address text, " +
            "note text, " +
            "time text, " +
            "timer text, " +
            "id_driver integer,  " +
            "time_driver text, " +
            " status integer(1) )";*/

    //Script de Creación de la tabla Quotes
    public static final String CREATE_QUOTES_SCRIPT ="CREATE TABLE IF NOT EXISTS notice_manager " +
            "( _id integer PRIMARY KEY AUTOINCREMENT,  " +
            "title text, " +
            "id_delivery integer UNIQUE, " +
            "id_restaurant integer, " +
            "body text   )";
    //Script de Creación de la tabla Quotes
    public static final String CREATE_QUOTES_SCRIPT_2 ="CREATE TABLE IF NOT EXISTS deliveries " +
            "( _id integer PRIMARY KEY AUTOINCREMENT,  " +
            "id text , " +
            "address text , " +
            "note text , " +
            "time text , " +
            "time_driver text , " +
            "status text , " +
            "id_restaurant text , " +
            "restaurant text , " +
            "phone_restaurant text , " +
            "restaurant_address text,  " +
            "time_init DEFAULT CURRENT_TIMESTAMP  " +
            "  )";

    private QuotesReaderDbHelper openHelper;
    private SQLiteDatabase database;

    public QuotesDataSource(Context context) {
        //Creando una instancia hacia la base de datos
        openHelper = new QuotesReaderDbHelper(context);
        database = openHelper.getWritableDatabase();
    }
}