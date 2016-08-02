package binaryworksindonesia.com.gpstracker;

/**
 * Created by Meli Oktavia on 4/18/2016.
 */


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.widget.Toast;

/**
 * Created by binaryvi on 07/04/2016.
 */
public class CekKoneksi {

    public static final int CEK_WIFI = 1;
    public static final int CEK_KONEKSI_DATA = 2;
    public static final String CONNECT_WIFI = "Connect WiFi";
    public static final String CONNECT_DATA = "Connect Mobile Data";
    public static final String CONNECT_NONE = "No Internet Connection. Please your setting";
    public boolean connection = false;


    Context mContext;
    public CekKoneksi(Context mContext) {
        this.mContext = mContext;
    }

    public boolean cekKoneksi(int koneksi) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        switch (koneksi){
            case CEK_WIFI:
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                }
                else{
                    connected = false;
                }
                break;
            case CEK_KONEKSI_DATA:
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                }
                else{
                    connected = false;
                }
                break;
            default:
                break;
        }

        return connected;
    }

    public String cekKoneksi(){

        String result = "";

        if(cekKoneksi(CEK_WIFI)) {
            result = CONNECT_WIFI;
            connection = true;
        }else if(cekKoneksi(CEK_KONEKSI_DATA)){
            result = CONNECT_DATA;
            connection = true;
        }else {
            result = CONNECT_NONE;
            //buka form setting
            cekSettingKoneksi();
        }
        return result;
    }

    private  void cekSettingKoneksi(){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        //(WifiManager)this.context.getSystemService(Context.WIFI_SERVICE);
        //wifiManager.setWifiEnabled(status);
        Toast.makeText(mContext.getApplicationContext(),"status :: " + wifiManager.isWifiEnabled(), Toast.LENGTH_SHORT).show();

        if(!wifiManager.isWifiEnabled()){
            //startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            //wifi setting
            mContext.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
        }else{
            //nyalakan paket data setting
            mContext.startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
        }
    }

}
