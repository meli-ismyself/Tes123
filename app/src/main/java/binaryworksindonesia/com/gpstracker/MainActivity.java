package binaryworksindonesia.com.gpstracker;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import binaryworksindonesia.com.gpstracker.api.request.HajjTrackerRequest;
import binaryworksindonesia.com.gpstracker.api.response.HajjPerson;


public class MainActivity extends Activity implements HajjTrackerRequest.OnHajjPersonRequestListener {
    private TextView lblLocation;
    EditText etUsername, etPassword;
    String username, password;
    private Button btnMap;
    ArrayList<String> listLatitude = new ArrayList<String>();
    ArrayList<String> listLongitude = new ArrayList<String>();
    ArrayList<String> listAddress = new ArrayList<String>();
    ArrayList<String> listId = new ArrayList<String>();
    ArrayList<String> listDeviceName = new ArrayList<String>();

    String latitude;
    String longitude;
    String address, id, deviceName;
    CekKoneksi cekKoneksi;

    HajjTrackerRequest hajjTrackerRequest;
    String longitudeString, latitudeString, addressString, deviceNameString, idString;
    boolean isSuccess;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hajjTrackerRequest = new HajjTrackerRequest(this);

        cekKoneksi = new CekKoneksi(this);
        Toast.makeText(MainActivity.this, cekKoneksi.cekKoneksi(), Toast.LENGTH_SHORT).show();
        //lblLocation = (TextView) findViewById(R.id.lblLocation);
        InitializeWidget();
        if (cekKoneksi.connection) {
            System.out.println("Koneksi Tersambung ===========================================" + cekKoneksi.connection);
        } else {
            System.out.println("Koneksi Gagal ===========================================" + cekKoneksi.connection);
        }

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = etUsername.getText().toString().trim();
                password = etPassword.getText().toString().trim();

                System.out.println("username ====> " +username);
                System.out.println("password ====> " +password);

                if (etUsername.getText().toString().isEmpty() || etUsername.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Password atau username masih kosong", Toast.LENGTH_SHORT).show();
                }else{
                    CallApigetLastPosition();
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void InitializeWidget(){
        btnMap = (Button) findViewById(R.id.buttonMap);
        etUsername = (EditText)findViewById(R.id.et_user_name);
        etPassword = (EditText)findViewById(R.id.et_password);

    }

    private void CallApigetLastPosition(){
        String app = "traccar";
        String module = "traccar";
        String action = "getLastPosition";
        String reqId = "58C21DDE123E63F40BB596A39FC31945";
        hajjTrackerRequest.callApiHajjPerson(app, module, action, reqId);
    }
    public void OpenMap() {
        System.out.println(" ++++++++++++++++++++++++++ OpenMap ++++++++++++++++++++++++++");
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        intent.putExtra("intentLongitude", listLongitude);
        intent.putExtra("intentLatitude", listLatitude);
        intent.putExtra("intentAddress", listAddress);
        intent.putExtra("intentId", listId);
        intent.putExtra("intentDeviceName", listDeviceName);
        startActivity(intent);
    }


    @Override
    public void OnHajjTrackerRequestSuccess(HajjPerson hajjPersonResponse) {
        System.out.println("OnHajjTrackerRequestSuccess ok " + hajjPersonResponse.getMessage());
        isSuccess = hajjPersonResponse.isSuccess();
        if (isSuccess){
            System.out.println("isSuccess " + isSuccess);
            for (int i=0; i<hajjPersonResponse.getListData().size(); i++){
                longitudeString = hajjPersonResponse.getListData().get(i).getLongitude();
                latitudeString = hajjPersonResponse.getListData().get(i).getLatitude();
                addressString = hajjPersonResponse.getListData().get(i).getAddress();
                deviceNameString = hajjPersonResponse.getListData().get(i).getDevicesname();
                idString = hajjPersonResponse.getListData().get(i).getId();
                System.out.println( "hajjPersonResponse" + hajjPersonResponse.getListData().get(i).getId());
            }
            listLongitude.add(longitudeString);
            listLatitude.add(latitudeString);
            listAddress.add(addressString);
            listId.add(idString);
            listDeviceName.add(deviceNameString);
            OpenMap();
        }else {
            System.out.println("isSuccess " + isSuccess);
        }

    }

    @Override
    public void OnHajjTrackerRequestFailed(String message) {
        System.out.println("OnHajjTrackerRequestFailed");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://binaryworksindonesia.com.gpstracker/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://binaryworksindonesia.com.gpstracker/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}


