package com.tomoeats.restaurant.helper;

import com.tomoeats.restaurant.model.Addon;
import com.tomoeats.restaurant.model.Order;
import com.tomoeats.restaurant.model.Profile;

import java.io.File;
import java.util.HashMap;

import okhttp3.RequestBody;

/**
 * Created by Tamil on 12/27/2017.
 */

public class GlobalData {

    public static String accessToken = "";
    public static String email = "";
    public static String password = "";
    public static Profile profile;
    public static Order selectedOrder;
    public static Addon selectedAddon;
    public static HashMap<String, RequestBody> registerMap = new HashMap<>();
    public static File REGISTER_AVATAR = null;


}
