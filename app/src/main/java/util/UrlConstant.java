package util;

/**
 * Created by riya on 13/2/18.
 */

public class UrlConstant {
//    http://webservicestrkus.tarule.com/
    public static String base_url = "http://webapi.trkus.com/";
    public static String LOGIN_URL = base_url + "api/Account/UserRegistration";
    public static String RESEND_OTP = base_url + "api/Account/ResendOTPRegistration";
    public static String VERIFY_URL = base_url + "api/Account/VerifyOTP";
    public static String AddUserInformation_URL = base_url + "api/Account/AddUserInformation";
    public static String GETSTATE_URL = base_url + "api/Account/GetStateName";
    public static String GETIndustry_URL = base_url + "api/Account/GetIndustry";
    public static String GETBusiness_Category_URL = base_url + "api/Account/GetCategory";
    public static String GETADD_SEELER_PROFILE = base_url + "api/Account/AddSellerProfile";
    public static String GET_STORE_byID = base_url + "api/Customer/GetStore?CategoryId=";
    public static String AddFavourite_Seller = base_url + "api/Order/FavoriteSeller";
    public static String Post_Order_Url = base_url + "api/Order/OrderAddDailyNeedsItem";
    public static String GET_Customer_Order_Url = base_url + "api/Order/GetOrder?CustomerUserId=";
    public static String GET_Favourite_Seller = base_url + "api/Order/GetFavoriteSeller?CustomerUserId=";
    public static String GET_Favourite_Contact = base_url + "api/Order/GetFavoriteContact?CustomerId=";
    public static String POST_Favourite_Contact = base_url + "api/Order/FavoriteContact";
    public static String GETCountry_URL = base_url + "api/Account/GetCountryName";
    public static String GETCity_URL = base_url + "api/Account/GetCityName";
    public static String POST_Customer_Profile_Update = base_url + "api/Customer/AddCustomerProfile";
    public static String GET_Customer_Profile_Update = base_url + "api/Customer/GetCustomerProfileDetails?UserId=";
    public static String POST_Daily_Need_Item = base_url + "api/Order/AddDailyNeedsItem";
    public static String GET_Daily_Need_Item = base_url + "api/Order/GetDailyNeedsItem?CustomerUserId=";
    public static String POST_Important_Document = base_url + "api/Order/ImportantDocuments";
    public static String GET_Important_Document = base_url + "api/Order/GetImportantDocuments?CustomerId=";
    public static String GET_Seller_Favourite_Contact = base_url + "api/Seller/GetFavoriteContact?SellerId=";
    public static String POST_Seller_Favourite_Contact = base_url + "api/Seller/FavoriteContact";
    public static String GET_Seller_Order_Url = base_url + "api/Seller/GetCustomerOrder?SellerId=";
    public static String GET_Seller_RemoveFavouriteContact_Url = base_url + "api/Seller/RemoveFavoriteContact?Id=";
    public static String GET_Customer_RemoveFavouriteContact_Url = base_url + "api/Order/RemoveFavoriteContact?Id=";
    public static String POST_Reorder_Item = base_url + "api/Order/ReOrderDailyNeedsItem";
    public static String POST_Seller_Schedule = base_url + "api/Seller/CreateShedule";
    public static String GET_Seller_Profile_Update = base_url + "api/Account/GetProfileDetails?UserId=";
    public static String GET_Custor_Order_Detail = base_url + "api/Order/GetOrderDetails?CustomerUserId=";
    public static String GET_Seller_Availability=base_url+"api/Seller/GetShedule?SellerId=";
    public static String POST_Seller_Appointment=base_url+"api/Order/AddAppointment";
    public static String GET_Customer_Appointment=base_url+"api/Order/GetAppointmentHistory?CustomerId=";
    public static String GET_SELLER_DASHBOARD=base_url+"api/Seller/GetSellerDashBoard?SellerId=";
    public static String GET_Notification=base_url+"api/Account/GetNotification";
    public static String GET_Seller_Order_Detail=base_url+"api/Seller/GetCustomerOrderDetails?SellerId=";
    public static String GET_Seller_Appointment_History=base_url+"/api/Seller/GetCustomerAppoinment?SellerId=";
    public static String POST_SellerToCustomerChat = base_url + "api/Seller/SellerToCustomerChat";
    public static String POST_CustometosellerChat = base_url + "api/Seller/CustometosellerChat";
    public static String GET_SellerChatHistory = base_url + "api/Seller/GetSellerChatHistory?SellerId=";
    public static String GET_CustomerChatHistory = base_url + "api/Seller/GetCustomerChatHistory?CustomerId=";
}
