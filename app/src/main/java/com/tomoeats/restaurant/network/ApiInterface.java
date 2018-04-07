package com.tomoeats.restaurant.network;

/**
 * Created by tamil@appoets.com on 30-08-2017.
 */


import com.tomoeats.restaurant.model.Addon;
import com.tomoeats.restaurant.model.AuthToken;
import com.tomoeats.restaurant.model.Category;
import com.tomoeats.restaurant.model.Cuisine;
import com.tomoeats.restaurant.model.HistoryModel;
import com.tomoeats.restaurant.model.IncomingOrders;
import com.tomoeats.restaurant.model.Order;
import com.tomoeats.restaurant.model.Profile;
import com.tomoeats.restaurant.model.Transporter;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {


    /*-------------USER--------------------*/

    @GET("api/shop/profile")
    Call<Profile> getProfile(@QueryMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/shop/profile/{id}")
    Call<Profile> updateProfile(@Path("id") int id,@QueryMap HashMap<String, String> params);
//
//    @Multipart
//    @POST("api/user/profile")
//    Call<User> updateProfileWithImage(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part filename);
//
//    @FormUrlEncoded
//    @POST("api/user/otp")
//    Call<Otp> postOtp(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("oauth/token")
    Call<AuthToken> login(@FieldMap HashMap<String, String> params);

    @Multipart
    @POST("api/shop/register")
    Call<Profile> signUp(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part filename);

    @FormUrlEncoded
    @POST("oauth/token")
    Call<AuthToken> postLogin(@FieldMap HashMap<String, String> params);


    @GET("api/shop/cuisines")
    Call<List<Cuisine>> getCuisines();

    @FormUrlEncoded
    @PATCH("api/shop/order/{id}")
    Call<Order> updateOrderStatus(@Path("id") int id, @FieldMap HashMap<String, String> params);


    /*-------------ADD-ONS--------------------*/
    @GET("api/shop/addons")
    Call<List<Addon>> getAddons();

    @FormUrlEncoded
    @POST("api/shop/addons")
    Call<Addon> addAddon(@Field("name") String name);

    @FormUrlEncoded
    @PATCH("api/shop/addons/{id}")
    Call<Addon> updateAddon(@Path("id") int id,@Field("name") String name);

    @DELETE("api/shop/addons/{id}")
    Call<List<Addon>> deleteAddon(@Path("id") int id);


    /*------------- CATEGORY --------------------*/
    @GET("api/shop/categories")
    Call<List<Category>> getCategory();

/*
    @FormUrlEncoded
    @POST("api/shop/addons")
    Call<Addon> addAddon(@Field("name") String name);

    @FormUrlEncoded
    @PATCH("api/shop/addons/{id}")
    Call<Addon> updateAddon(@Path("id") int id,@Field("name") String name);

    @DELETE("api/shop/addons/{id}")
    Call<List<Addon>> deleteAddon(@Path("id") int id);
*/



//    @FormUrlEncoded
//    @POST("api/user/social/login")
//    Call<LoginModel> postSocialLogin(@FieldMap HashMap<String, String> params);
//
//    @FormUrlEncoded
//    @POST("api/user/forgot/password")
//    Call<ForgotPassword> forgotPassword(@Field("phone") String mobile);
//
//    @FormUrlEncoded
//    @POST("api/user/reset/password")
//    Call<ResetPassword> resetPassword(@FieldMap HashMap<String, String> params);
//
//    @FormUrlEncoded
//    @POST("api/user/profile/password")
//    Call<ChangePassword> changePassword(@FieldMap HashMap<String, String> params);
//
//
//    @GET("api/user/notification")
//    Call<FavoriteList> getNotification();
//
//    /*-------------SHOP--------------------*/
//
//    @GET("api/user/shops")
//    Call<RestaurantsData> getshops(@QueryMap HashMap<String, String> params);
//
//    @GET("api/user/categories")
//    Call<ShopDetail> getCategories(@QueryMap HashMap<String, String> params);
//
//
//    /*-------------CUISINE--------------------*/
//    @GET("api/user/cuisines")
//    Call<List<Cuisine>> getcuCuisineCall();
//
//    /*-------------CART--------------------*/
//
//    @FormUrlEncoded
//    @POST("api/user/cart")
//    Call<AddCart> postAddCart(@FieldMap HashMap<String, String> params);
//
//    @GET("api/user/cart")
//    Call<AddCart> getViewCart();
//
//    @GET("api/user/clear/cart")
//    Call<ClearCart> clearCart();
//
//    @FormUrlEncoded
//    @POST("api/user/order")
//    Call<Order> postCheckout(@FieldMap HashMap<String, String> params);
//
//    /*-------------ADDRESS--------------------*/
//
//    @GET("api/user/address")
//    Call<List<Address>> getAddresses();
//
//    @POST("api/user/address")
//    Call<Address> saveAddress(@Body Address address);
//
//    @PATCH("api/user/address/{id}")
//    Call<Address> updateAddress(@Path("id") int id, @Body Address address);
//
//    @DELETE("api/user/address/{id}")
//    Call<Message> deleteAddress(@Path("id") int id);
//
//    /*-------------FAVORITE--------------------*/
//
//    @FormUrlEncoded
//    @POST("api/user/favorite")
//    Call<Favorite> doFavorite(@Field("shop_id") int shop_id);
//
//    @DELETE("api/user/favorite/{id}")
//    Call<Favorite> deleteFavorite(@Path("id") int id);
//
//    @GET("api/user/favorite")
//    Call<FavoriteList> getFavoriteList();
//
    /*-------------ORDER--------------------*/

    @GET("api/shop/order")
    Call<IncomingOrders> getIncomingOrders(@Query("t") String type);

    @GET("api/shop/history")
    Call<HistoryModel> getHistory();

    @GET("api/shop/transporterlist")
    Call<List<Transporter>> getTransporter();

//    @GET("api/user/ongoing/order")
//    Call<List<Order>> getOngoingOrders();
//
//    @GET("api/user/order/{id}")
//    Call<Order> getParticularOrders(@Path("id") int id);
//
//    @GET("api/user/order")
//    Call<List<Order>> getPastOders();
//
//    @DELETE("api/user/order/{id}")
//    Call<Order> cancelOrder(@Path("id") int id, @Query("reason") String reason);
//
//    @FormUrlEncoded
//    @POST("api/user/rating")
//    Call<Message> rate(@FieldMap HashMap<String, String> params);
//
//    @FormUrlEncoded
//    @POST("api/user/reorder")
//    Call <AddCart> reOrder(@FieldMap HashMap<String, String> params);
//
//     /*-------------DISPUTE--------------------*/
//
//    @GET("api/user/disputehelp")
//    Call<List<DisputeMessage>> getDisputeList();
//
//    @FormUrlEncoded
//    @POST("api/user/dispute")
//    Call<Order> postDispute(@FieldMap HashMap<String, String> params);
//
//
//    /*-------------SEARCH--------------------*/
//    @GET("api/user/search")
//    Call<Search> getSearch(@QueryMap HashMap<String, String> params);
//
//    /*-----------------------WALLET-----------------------*/
//    @GET("api/user/wallet")
//    Call<List<WalletHistory>> getWalletHistory();
//
//    @GET("api/user/wallet/promocode")
//    Call<List<Promotions>> getWalletPromoCode();
//
//    @FormUrlEncoded
//    @POST("api/user/wallet/promocode")
//    Call<PromotionResponse> applyWalletPromoCode(@Field("promocode_id") String id);
//
//
//    @GET("json?")
//    Call<ResponseBody> getResponse(@Query("latlng") String param1, @Query("key") String param2);
//
//    /*-------------PAYMENT--------------------*/
//    @GET("api/user/card")
//    Call<List<Card>> getCardList();
//
//    @FormUrlEncoded
//    @POST("api/user/card")
//    Call<Message> addCard(@Field("stripe_token") String stripeToken);
//
//    @FormUrlEncoded
//    @POST("api/user/add/money")
//    Call<AddMoney> addMoney(@FieldMap HashMap<String, String> params);
//
//    @DELETE("api/user/card/{id}")
//    Call<Message> deleteCard(@Path("id") int id);


}
