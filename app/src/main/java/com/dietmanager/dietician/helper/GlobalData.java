package com.dietmanager.dietician.helper;

import com.dietmanager.dietician.model.Addon;
import com.dietmanager.dietician.model.Order;
import com.dietmanager.dietician.model.Profile;
import com.dietmanager.dietician.model.ingredients.IngredientsItem;
import com.dietmanager.dietician.model.subscribe.SubscribedUser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;

/**
 * Created by Tamil on 12/27/2017.
 */

public class GlobalData {

    public static List<IngredientsItem> ingredientsItemList=new ArrayList<>();
    public static List<IngredientsItem> selectedIngredientsList=new ArrayList<>();

    public static String accessToken = "";
    public static String email = "";
    public static String password = "";
    public static Profile profile;
    public static Order selectedOrder;
    public static com.dietmanager.dietician.model.ordernew.Order isselectedOrder;
    public static String name, access_token, mobileNumber, imageUrl;
    public static String loginBy = "manual";
    public static String mobile = "";
    public static String hashcode = "";
    public static int otpValue = 0;
    public static SubscribedUser profileModel = null;
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
