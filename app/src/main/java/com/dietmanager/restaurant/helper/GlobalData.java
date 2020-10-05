package com.dietmanager.restaurant.helper;

import com.dietmanager.restaurant.model.Addon;
import com.dietmanager.restaurant.model.Order;
import com.dietmanager.restaurant.model.Profile;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    public static com.dietmanager.restaurant.model.ordernew.Order isselectedOrder;
    public static Addon selectedAddon;
    public static HashMap<String, RequestBody> registerMap = new HashMap<>();
    public static File REGISTER_AVATAR = null;
    public static File REGISTER_SHOP_BANNER = null;
    public static List<String> ORDER_STATUS_OLD = Arrays.asList("ORDERED", "RECEIVED", "ASSIGNED", "PROCESSING", "REACHED", "PICKEDUP", "ARRIVED", "COMPLETED");
    public static List<String> ORDER_STATUS = Arrays.asList("ORDERED", "RECEIVED", "ASSIGNED",
            "PROCESSING", "REACHED", "PICKEDUP", "ARRIVED","SCHEDULED","PICKUP_USER","READY",
            "COMPLETED","CANCELLED","'SEARCHING'");
    public static double roundoff(double data) {
        double value = Math.round(data);
        return value;
    }

}
