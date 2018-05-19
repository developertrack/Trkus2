package trkus.sellermodule.sellerordermodule;

public class SellerOrderData {

    String SellerUserId, OrderId, ItemName, OrderDate, Name, Address, OrderImage1;

    public SellerOrderData(String s, String s1, String s2, String s3, String s4, String address, String s5) {

        SellerUserId = s;
        OrderId = s1;
        ItemName = s2;
        OrderDate = s3;
        Name = s4;
        Address = address;
        OrderImage1 = s5;


    }

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

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getOrderImage1() {
        return OrderImage1;
    }

    public void setOrderImage1(String orderImage1) {
        OrderImage1 = orderImage1;
    }
}
