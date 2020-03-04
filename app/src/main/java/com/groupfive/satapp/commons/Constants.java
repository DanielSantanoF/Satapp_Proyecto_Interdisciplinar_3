package com.groupfive.satapp.commons;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Constants {
    public static final String MASTER_ACCES_TOKEN = "groupfivemasterkey";
    public static final String SATAPP_API_URL = "https://satappgroupfive.herokuapp.com/";
    public static final String APP_SETTINGS_FILE = "APP_SETTINGS";
    public static final String SHARED_PREFERENCES_AUTH_TOKEN = "authToken";
    public static final int IMAGE_READ_REQUEST_CODE = 42;
    public static final String FOTOS_NEW_TICKET_IMAGE_PART = "fotos";
    public static final String LOADING_GIF = "https://digitalsynopsis.com/wp-content/uploads/2016/06/loading-animations-preloader-gifs-ui-ux-effects-10.gif";
    public static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("dd/MM/yyyy");

}
