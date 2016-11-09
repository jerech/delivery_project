package com.paulpwo.delivery360.config;

/**
 * Created by pwxss on 2/7/16.
 */

public class Constants {

    //public final static String BASE_URL = "http://debug.marcanoyasociados.com/api/v1";
    //public final static String BASE_URL = "http://192.168.0.106/api/v1";
    //public final static String BASE_URL = "http://deliveryeveryday.com/api_develop/v1";
    public final static String BASE_URL = "http://deliveryeveryday.com/api/v1";

    public final static String BASE_URL_DRIVER_LOGOUT = BASE_URL + "/logoutDriver";
    public final static String BASE_URL_RESTAURANT_LOGOUT = BASE_URL + "/logoutRestaurant";
    public final static String BASE_URL_MANAGER_LOGOUT = BASE_URL + "/logoutManager";
    public final static String BASE_URL_DRIVER_SINGUP = BASE_URL + "/registerDriver";
    public final static String BASE_URL_DRIVER_UPDATE = "/driver/";
    public final static String BASE_URL_RESTAURANT_SINGUP = BASE_URL + "/registerRestaurant";
    public final static String BASE_URL_FIREBASE_SMS_DRIVE = BASE_URL + "/SetdeviceToken";
    public final static String BASE_URL_NEW_DELIVERY = BASE_URL + "/delivery";
    public final static String BASE_URL_BLOCK_USER = BASE_URL + "/BlockUser";
    public final static String BASE_URL_LIST_BLOCK = BASE_URL + "/driverListBlock";
    public final static String BASE_URL_SNIFF_NOTIFY = BASE_URL + "/sniffNotify";
    public final static String BASE_URL_DELETE_DELIVERY = BASE_URL + "/deliveryDelete";
    public final static String BASE_URL_PROFILE_MANAGER = BASE_URL + "/driverManagerProfile";
    public final static String BASE_URL_DETAIL_DELIVERY = BASE_URL + "/driverListDetail";
    public final static String BASE_URL_FINISH_DELIVERy = BASE_URL + "/FinishDeliveryPut";
    public final static String BASE_URL_COUNT_DELIVERIES = BASE_URL + "/deliveriesFreeCount";
    public final static String BASE_URL_SET_OFF_LINE = BASE_URL + "/offline"; /*POST*/
    public final static String BASE_URL_SET_ON_LINE = BASE_URL + "/online"; /*POST*/
    public final static String BASE_URL_RESTAURANTS_LIST = BASE_URL + "/restaurantList";
    public final static String BASE_URL_CHOOSE_DELIVERY = BASE_URL + "/driversChooseDelivery"; /*POST*/

    public final static String BASE_URL_RESET_PASS= BASE_URL + "/DriverRestorePassword/";
    public final static String BASE_URL_RESET_PASS_RESTAURANT= BASE_URL + "/RestaurantRestorePassword/";
    public final static String BASE_URL_STATUS_DRIVER_NEW_DELIVERY_ACCEPT = BASE_URL + "/pushResposeDriver";

    public final static String BASE_URL_IMAGE_BASE = "/images/base.jpg";
    public final static String BASE_URL_IMAGE_BASE_DRIVE_NO_PICTURE = "/images/noPicture.jpg";


    public final static String ID_PROFILE = "ID_PROFILE";
    public final static String TOKEN_PUSH= "TOKEN_PUSH";
    // TO STORE SHARED PREFERENCE
    public  static final String MY_SHARE_PREFERENCES = "MY_SHARE_PREFERENCES";
    // TO EMAIL IF USER REGISTERED IN APP
    public  static final String EMAIL_USER_LOGGED = "MY_SHARE_PREFERENCES";
    // TO Type USER (Restaurant = true )
    public  static final String TYPE_ACCOUNT_RESTAURAT = "TYPE_ACCOUNT_RESTAURAT";

    public static final String API_KEY = "API_KEY";
    // FOR USER PROFILE
    public static final String first_name = "first_name";
    public static final String last_name = "last_name";
    public static final String address = "address";
    public static final String phone = "phone";
    public static final String image_url = "image_url";
    public static final String created_At = "created_At";

    public static final String email_receive = "email_receive";
    public static final String keywords_subject = "keywords_subject";
    public static final String pass_email_receive = "pass_email_receive";
    public static final String emails_to_receive = "emails_to_receive";
    public static final String all_emails_to_receive = "all_emails_to_receive";

    public static final Integer NOTIFY_TIME_OUT = 10000;
    public static final Integer NOTIFY_ID_NEW_DELIVERY = 912934;
    public static final Integer TIME_SNIFF_NOTIFY_DB = 120000;
    public static final Integer ANIMATE_ITEM_RECYCLER_VIEW_TIME = 15;

    public static final String USER_STATUS = "USER_STATUS";
    //IMPORTANT...............
    // CONFIG TO TICK SERVICE
    public static final int UPDATE_INTERVAL_SERVICE = 15000;
    public static final int UPDATE_INTERVAL_SERVICE_2 = 1000;

    public final static String BASE_URL_UPDATE_DELIVERY = BASE_URL + "/updateTimeDeliverys";


    public static final String TAG_DRIVER = "TAG_DRIVER";
    public static final String TAG_RESTAURANT = "TAG_RESTAURANT";
    public static final String TAG_MANAGER = "TAG_MANAGER";

    public static final Integer JPG_QUALITY = 40;

    /*
    *               is manager profile
     */
    public final static String MANAGER_PRIFILE_INTEGER= "MANAGER_PRIFILE_BOOL";


    public interface ACTION {
       // public static String MAIN_ACTION = "com.marothiatechs.foregroundservice.action.main";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}
