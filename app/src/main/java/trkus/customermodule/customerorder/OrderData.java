package trkus.customermodule.customerorder;

public class OrderData {

    String SellerUserId,OrderId,Itemn,OrderDate,FirmName,FirmImage,OrderImage1;

    public String getSellerUserId() {
        return SellerUserId;
    }

    public void setSellerUserId(String sellerUserId) {
        SellerUserId = sellerUserId;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getItemn() {
        return Itemn;
    }

    public void setItemn(String itemn) {
        Itemn = itemn;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getFirmName() {
        return FirmName;
    }

    public void setFirmName(String firmName) {
        FirmName = firmName;
    }

    public String getFirmImage() {
        return FirmImage;
    }

    public void setFirmImage(String firmImage) {
        FirmImage = firmImage;
    }

    public String getOrderImage1() {
        return OrderImage1;
    }

    public void setOrderImage1(String orderImage1) {
        OrderImage1 = orderImage1;
    }

    public OrderData(String s, String s1, String s2, String s3, String s4, String s5, String s6) {

        this.SellerUserId=s;
        this.OrderId=s1;
        this.Itemn=s2;
        this.OrderDate=s3;
        this.FirmName=s4;
        this.FirmImage=s5;
        this.OrderImage1=s6;
    }
}
