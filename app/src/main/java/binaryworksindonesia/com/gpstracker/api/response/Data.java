package binaryworksindonesia.com.gpstracker.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Meli Oktavia on 6/27/2016.
 */
public class Data {
    @SerializedName("id")
    String id;
    @SerializedName("devicename")
    String devicesname;
    @SerializedName("deviceid")
    String deviceid;
    @SerializedName("latitude")
    String latitude;
    @SerializedName("longitude")
    String longitude;
    @SerializedName("address")
    String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevicesname() {
        return devicesname;
    }

    public void setDevicesname(String devicesname) {
        this.devicesname = devicesname;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
