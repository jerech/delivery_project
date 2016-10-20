package com.paulpwo.delivery360.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Keep;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by paulpwo on 13/7/16.
 */
@Keep
public class HelpersDB {
    private static HelpersDB ourInstance = new HelpersDB();

    public static HelpersDB getInstance() {
        return ourInstance;
    }

    private HelpersDB() {
    }
    public void saveNoticeDeclineDeliveryForManager(final Context c,
                                                    final String title,
                                                    final String id_delivery,
                                                    final String id_restaurant,
                                                    final String body){
         SQLiteDatabase db = this.getDB(c);
        if(db != null) {
       /*     db.execSQL("INSERT INTO notice_manager (title,id_delivery,id_restaurant,body) VALUES " +
                    "('" + title  + "', '" +
                    Integer.parseInt(id_delivery) +
                    ",'" +  Integer.parseInt(id_restaurant)  +
                    "','"+  body + "') ");*/

            //Creamos el registro a insertar como objeto ContentValues
            ContentValues nuevoRegistro = new ContentValues();
            nuevoRegistro.put("title", title);
            nuevoRegistro.put("id_delivery",Integer.parseInt(id_delivery));
            nuevoRegistro.put("id_restaurant",Integer.parseInt(id_restaurant));
            nuevoRegistro.put("body", body);

            //Insertamos el registro en la base de datos
            db.insert("notice_manager", null, nuevoRegistro);


            //Cerramos la base de datos
            db.close();
        }
    }
    public void newDeliveryLocalDB(final Context c,
                                   final String id,
                                   final String address,
                                   final String note,
                                   final String time,
                                   final String time_driver,
                                   final String status,
                                   final String restaurant,
                                   final String phone_restaurant,
                                   final String restaurant_address){
        SQLiteDatabase db = this.getDB(c);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        if(db != null) {
            //Creamos el registro a insertar como objeto ContentValues
            ContentValues nuevoRegistro = new ContentValues();
            nuevoRegistro.put("id", id);
            nuevoRegistro.put("address",address);
            nuevoRegistro.put("note",note);
            nuevoRegistro.put("time", time);
            nuevoRegistro.put("time_driver", time_driver);
            nuevoRegistro.put("time", time);
            nuevoRegistro.put("status", status);
            nuevoRegistro.put("restaurant", restaurant);
            nuevoRegistro.put("phone_restaurant", phone_restaurant);
            nuevoRegistro.put("restaurant_address", restaurant_address);
            nuevoRegistro.put("time_init", getDateTime());

            //Insertamos el registro en la base de datos
            db.insert("deliveries", null, nuevoRegistro);
            //Cerramos la base de datos
            db.close();
        }
    }

    public void finishDelivery(final Context c,
                            final String id){
        SQLiteDatabase db = this.getDB(c);
        if(db != null) {
            //Creamos el registro a insertar como objeto ContentValues
            ContentValues nuevoRegistro = new ContentValues();
            nuevoRegistro.put("status", "0");
            //Actualizamos el registro en la base de datos
            db.update("deliveries", nuevoRegistro, "id=" + id, null);
            db.close();
        }
    }
    public void uptadeTimeDelivery(final Context c,
                                   final String id,
                                   final String time_driver){
        SQLiteDatabase db = this.getDB(c);
        if(db != null) {
            //Creamos el registro a insertar como objeto ContentValues
            ContentValues nuevoRegistro = new ContentValues();
            nuevoRegistro.put("time_driver", time_driver);
            nuevoRegistro.put("time_init", getDateTime());
            //Actualizamos el registro en la base de datos
            db.update("deliveries", nuevoRegistro, "id=" + id, null);
            db.close();
        }
    }
    public SQLiteDatabase getDB(Context c){
        QuotesReaderDbHelper dx = new QuotesReaderDbHelper(c);
        return  dx.getWritableDatabase();
    }
    public void delete(Context c ,String id){
        this.getDB(c).delete("notice_manager","_id=" + Integer.parseInt(id),null);
    }
    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
