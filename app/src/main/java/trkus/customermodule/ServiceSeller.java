package trkus.customermodule;

public class ServiceSeller {
    String SellerUserId,Industry,CategoryOfBusiness,FirmName,MobileNumber,Address1,PinCode,Image1;

    public String getSellerUserId() {
        return SellerUserId;
    }

    public void setSellerUserId(String sellerUserId) {
        SellerUserId = sellerUserId;
    }

    public String getIndustry() {
        return Industry;
    }

    public void setIndustry(String industry) {
        Industry = industry;
    }

    public String getCategoryOfBusiness() {
        return CategoryOfBusiness;
    }

    public void setCategoryOfBusiness(String categoryOfBusiness) {
        CategoryOfBusiness = categoryOfBusiness;
    }

    public String getFirmName() {
        return FirmName;
    }

    public void setFirmName(String firmName) {
        FirmName = firmName;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public String getImage1() {
        return Image1;
    }

    public void setImage1(String image1) {
        Image1 = image1;
    }

    public ServiceSeller(String s, String s1, String categoryOfBusiness, String s2, String s3, String s4, String s5, String s6) {

        SellerUserId=s;
        Industry=s1;
        this.CategoryOfBusiness=categoryOfBusiness;
        FirmName=s2;
        MobileNumber=s3;
        PinCode=s5;
        Address1=s4;
        Image1=s6;
    }
}
