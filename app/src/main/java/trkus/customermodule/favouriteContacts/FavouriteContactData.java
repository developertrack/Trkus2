package trkus.customermodule.favouriteContacts;

public class FavouriteContactData {

    String Name, Address, FirmName, MobileNumber;

    public FavouriteContactData(String s, String address, String s1, String s2) {

        this.Name = s;
        this.Address = address;
        this.FirmName = s1;
        this.MobileNumber = s2;

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
}
