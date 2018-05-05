package util;

/**
 * Created by riya on 13/2/18.
 */

public class UrlConstant {

    public static String base_url="http://webservicestrkus.tarule.com/";
    public static  String LOGIN_URL=base_url+"api/Account/UserRegistration";
    public static String RESEND_OTP=base_url+"api/Account/ResendOTPRegistration";
    public static String VERIFY_URL=base_url+"api/Account/VerifyOTP";
    public static String AddUserInformation_URL=base_url+"api/Account/AddUserInformation";
    public static String GETSTATE_URL=base_url+"api/Account/GetStateName";
    public static String GETIndustry_URL=base_url+"api/Account/GetIndustry";
    public static String GETBusiness_Category_URL=base_url+"api/Account/GetCategory";
    public static String GETADD_SEELER_PROFILE=base_url+"api/Account/AddSellerProfile";
    public static String GET_STORE_byID=base_url+"api/Customer/GetStore?CategoryId=";
    public static String AddFavourite_Seller=base_url+"api/Order/FavoriteSeller";
    public static String Post_Order_Url=base_url+"api/Order/OrderAddDailyNeedsItem";
    public static String GET_Customer_Order_Url=base_url+"api/Order/GetOrder?CustomerUserId=";
    public static String GET_Favourite_Seller=base_url+"api/Order/GetFavoriteSeller?CustomerUserId=";


}
