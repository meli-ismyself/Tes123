package binaryworksindonesia.com.gpstracker.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Meli Oktavia on 6/27/2016.
 */
public class HajjPerson {
    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private ArrayList<Data> listData = new ArrayList<>();
    @SerializedName("message")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Data> getListData() {
        return listData;
    }

    public void setListData(ArrayList<Data> listData) {
        this.listData = listData;
    }
}
