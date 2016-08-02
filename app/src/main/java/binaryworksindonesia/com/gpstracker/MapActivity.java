package binaryworksindonesia.com.gpstracker;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import binaryworksindonesia.com.gpstracker.api.request.HajjTrackerRequest;
import binaryworksindonesia.com.gpstracker.api.response.HajjPerson;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, HajjTrackerRequest.OnHajjPersonRequestListener {
    String longitude, latitude, address, id, deviceName;
    private GoogleMap mMap;
    Handler UI_HANDLER = new Handler();
    int counter = 0;

    ArrayList<Double> listDoubleLatDeviceIntent = new ArrayList<Double>();
    ArrayList<Double> listDoubleLongDeviceIntent = new ArrayList<Double>();
    ArrayList<String> listLongitudeIntent, listLatitudeIntent, listAddressIntent, listIdIntent, listDeviceNameIntent;

    // BottomSheetBehavior variable
    private BottomSheetBehavior bottomSheetBehavior;

    // TextView variable
    private TextView bottomSheetHeading, tvLocation;

    Button btnHistoryLocation;
    ImageButton ibCall, ibMessage;
    String phone = "081234567876";
    Double latKetua, longKetua;
    String leaderAddress;
    int jabatanID;
    HashMap<Marker, Integer> markerHashMap = new HashMap<Marker, Integer>();
    int markerCounter = 0;
    Marker markerAnggota, markerLeader;
    List<Marker> markerList = new ArrayList<Marker>();
    Location loc1 = new Location("");
    Location loc2 = new Location("");

    TextView tvJarak, tvJarakTitikDua, getTvJarakVal;
    float distanceInMeters;

    HajjTrackerRequest hajjTrackerRequest;
    String longitudeString, latitudeString, addressString, deviceNameString, idString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        InitBottomSheetView();
        hajjTrackerRequest = new HajjTrackerRequest(this);
        initBottomSheetListeners();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        //getSupportActionBar().setTitle("TEST");
        listLongitudeIntent = (ArrayList<String>) getIntent().getSerializableExtra("intentLongitude");
        listLatitudeIntent = (ArrayList<String>) getIntent().getSerializableExtra("intentLatitude");
        listAddressIntent = (ArrayList<String>) getIntent().getSerializableExtra("intentAddress");
        listIdIntent = (ArrayList<String>) getIntent().getSerializableExtra("intentId");
        listDeviceNameIntent = (ArrayList<String>) getIntent().getSerializableExtra("intentDeviceName");

        // <CONVERT TO DOUBLE AND ADD THEM TO THE LIST ++++++++++++++++++++++++++++++++++++++++++++>
        LatLangConvertToDouble(listLatitudeIntent, listLongitudeIntent);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void OnHajjTrackerRequestSuccess(HajjPerson hajjPersonResponse) {
        System.out.println("OnHajjTrackerRequestSuccess ok " + hajjPersonResponse.getMessage());
        for (int i=0; i<hajjPersonResponse.getListData().size(); i++){
            longitudeString = hajjPersonResponse.getListData().get(i).getLongitude();
            latitudeString = hajjPersonResponse.getListData().get(i).getLatitude();
            addressString = hajjPersonResponse.getListData().get(i).getAddress();
            deviceNameString = hajjPersonResponse.getListData().get(i).getDevicesname();
            idString = hajjPersonResponse.getListData().get(i).getId();
            System.out.println( "hajjPersonResponse " + hajjPersonResponse.getListData().get(i).getId());
            System.out.println( "hajjPersonResponse " + hajjPersonResponse.getListData().get(i).getLatitude());
            System.out.println( "hajjPersonResponse " + hajjPersonResponse.getListData().get(i).getLongitude());


        }

        listLongitudeIntent.add(longitudeString);
        listLatitudeIntent.add(latitudeString);
        listAddressIntent.add(addressString);
        listIdIntent.add(idString);
        listDeviceNameIntent.add(deviceNameString);

        LatLangConvertToDouble(listLatitudeIntent, listLongitudeIntent);
        for (int i=0; i<listLatitudeIntent.size(); i++){
            System.out.println("listLatitudeIntent " + listLatitudeIntent.get(i));
            System.out.println("listLongitudeIntent  " + listLongitudeIntent.get(i));

        }

        RefreshMap();
    }

    @Override
    public void OnHajjTrackerRequestFailed(String message) {
        System.out.println("OnHajjTrackerRequestFailed");
    }

    private void InitBottomSheetView() {
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        bottomSheetHeading = (TextView) findViewById(R.id.bottomSheetHeading);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        ibCall = (ImageButton) findViewById(R.id.ibCall);
        ibMessage = (ImageButton) findViewById(R.id.ibMessage);
        btnHistoryLocation = (Button) findViewById(R.id.btnHistoryLocation);

        btnHistoryLocation.setOnClickListener(this);
        ibCall.setOnClickListener(this);
        ibMessage.setOnClickListener(this);

        tvJarak = (TextView) findViewById(R.id.tvJarak);
        tvJarakTitikDua = (TextView) findViewById(R.id.tvJarakTitikDua);
        getTvJarakVal = (TextView) findViewById(R.id.tvJarakVal);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHistoryLocation:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                Intent intent = new Intent(MapActivity.this, HistoryLocationActivity.class);
                String hajjName = bottomSheetHeading.getText().toString();
                System.out.println("btnHistoryLocation +++++++++++++++++++++>> " + hajjName );
                intent.putExtra("hajjNameIntent" , hajjName);
                startActivity(intent);
                //finish();
                break;
            case R.id.ibCall:
                System.out.println("ibCall ++++++++++++++++++ >> ");
                call();
                break;
            case R.id.ibMessage:
                System.out.println("ibMessage +++++++++++++++++ >> ");
                sendSMS();
                break;
        }
    }

    public void call() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }

    public void sendSMS() {
        String number = "12346556";  // The number on which you want to send SMS
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
    }

    private void initBottomSheetListeners() {
        // register the listener for button click

        // Capturing the callbacks for bottom sheet
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

               /* if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetHeading.setText(getString(R.string.text_collapse_me));
                } else {
                    bottomSheetHeading.setText(getString(R.string.text_expand_me));
                }*/

                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                }
            }


            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });


    }

    @Override
    public void onMapReady(final GoogleMap map) {

        System.out.println("++++++++++++++++++++++++ ON MAP READY +++++++++++++++++++++++++=");
        mMap = map;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                int pos = markerHashMap.get(marker);
                System.out.println("pos +++++++++++++++++++>> " + pos);
                HitungJarak();
                LatLng latLng1 = new LatLng(listDoubleLatDeviceIntent.get(0), listDoubleLongDeviceIntent.get(0));
                LatLng latLng2 = new LatLng(latKetua, longKetua);
                HitungJarak2(latLng1, latLng2);
                HitungJarak3(latKetua, longKetua, listDoubleLatDeviceIntent.get(0), listDoubleLongDeviceIntent.get(0));

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                if (pos == 1) {
                    bottomSheetHeading.setText("Muhammad Nur Jamil");
                    leaderAddress = "+++++++";
                    tvLocation.setText(leaderAddress);

                    //HIDE JARAK
                    tvJarak.setVisibility(View.GONE);
                    tvJarakTitikDua.setVisibility(View.GONE);
                    getTvJarakVal.setVisibility(View.GONE);
                } else if (pos == 2) {
                    bottomSheetHeading.setText(listDeviceNameIntent.get(0));
                    tvLocation.setText(listAddressIntent.get(0));
                    //HitungJarak();

                    //SHOW JARAK
                    tvJarak.setVisibility(View.VISIBLE);
                    tvJarakTitikDua.setVisibility(View.VISIBLE);
                    getTvJarakVal.setVisibility(View.VISIBLE);
                    getTvJarakVal.setText(String.valueOf(distanceInMeters) + " M");
                }


                return false;
            }
        });
        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(listDoubleLatDeviceIntent.get(0), listDoubleLongDeviceIntent.get(0)))
                .zoom(14)
                .bearing(0)
                .tilt(45)
                .build();

        for (int i = 0; i < listDoubleLatDeviceIntent.size(); i++) {
            markerCounter++;
            markerAnggota = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(listDoubleLatDeviceIntent.get(i), listDoubleLongDeviceIntent.get(i)))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_people_64))
                    .title("..."));
            markerList.add(markerAnggota);
            markerHashMap.put(markerAnggota, 2);
        }


        //Leader
        GetLeaderPosition(mMap);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 3000, null);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MapActivity.this, "onInfoWindowClick", Toast.LENGTH_LONG).show();

            }
        });


        UI_HANDLER.postDelayed(UI_UPDTAE_RUNNABLE, 30000);

    }

    private void CallApigetLastPosition(){
        String app = "traccar";
        String module = "traccar";
        String action = "getLastPosition";
        String reqId = "58C21DDE123E63F40BB596A39FC31945";
        hajjTrackerRequest.callApiHajjPerson(app, module, action, reqId);
    }

    Runnable UI_UPDTAE_RUNNABLE = new Runnable() {

        @Override
        public void run() {
            UI_HANDLER.postDelayed(UI_UPDTAE_RUNNABLE, 30000);
            counter++;
            ClearTheFirstData();
            markerList.clear();
            markerHashMap.clear();
            CallApigetLastPosition();
        }
    };

    private void RefreshMap() {
        System.out.println(" ++++++++++++++++++++++++++++++++++++ REFRESH MAP ++++++++++++++++++++++++++++++++++++ ");
        System.out.println(" ++++++++++++++++++++++++++++++++++++ listDoubleLatDeviceIntent ++++++++++++++++++++>> " + listDoubleLatDeviceIntent.size());
        System.out.println(" ++++++++++++++++++++++++++++++++++++ REFRESH MAP ++++++++++++++++++++++++++++++++++++ ");

        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(listDoubleLatDeviceIntent.get(0), listDoubleLongDeviceIntent.get(0)));

        this.mMap.moveCamera(center);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        this.mMap.animateCamera(zoom);

        for (int i = 0; i < listDoubleLatDeviceIntent.size(); i++) {
            markerAnggota = this.mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(listDoubleLatDeviceIntent.get(i), listDoubleLongDeviceIntent.get(i)))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_people_64))
                    .title("..."));
            markerList.add(markerAnggota);
            markerHashMap.put(markerAnggota, 2);
        }

        GetLeaderPosition(mMap);
    }

    @Override
    public void onBackPressed() {
        UI_HANDLER.removeCallbacks(UI_UPDTAE_RUNNABLE);
        super.onBackPressed();
    }

    private void ClearTheFirstData() {
        mMap.clear();
        listDoubleLatDeviceIntent.clear();
        listDoubleLongDeviceIntent.clear();
        listAddressIntent.clear();
        listLatitudeIntent.clear();
        listLongitudeIntent.clear();
        listIdIntent.clear();
        listDeviceNameIntent.clear();
        System.out.println("+++++++++++++++++++++++++++++++ ClearTheFirstData +++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++++++++ >> " + listDoubleLongDeviceIntent.size());
        System.out.println("+++++++++++++++++++++++++++++++ >> " + listDoubleLatDeviceIntent.size());
        System.out.println("+++++++++++++++++++++++++++++++ ClearTheFirstData +++++++++++++++++++++++++++++++");

    }

    private void LatLangConvertToDouble(ArrayList<String> latitudeString, ArrayList<String> longitudeString) {
        try {
            System.out.println("ONCREATE ==========================================================");
            for (int i = 0; i < latitudeString.size(); i++) {
                listDoubleLatDeviceIntent.add(Double.parseDouble(latitudeString.get(i)));
                listDoubleLongDeviceIntent.add(Double.parseDouble(longitudeString.get(i)));
                System.out.println("Double.parseDouble(listLatitudeIntent.get(i)) ======================= >> " + Double.parseDouble(latitudeString.get(i)));
                System.out.println("Double.parseDouble(listLongitudeIntent.get(i)) ======================= >> " + Double.parseDouble(longitudeString.get(i)));
            }
            System.out.println("ONCREATE ==========================================================");
        } catch (NumberFormatException nfe) {
            System.out.println("NumberFormatException ====================================>> " + nfe);
            Toast.makeText(getApplicationContext(), "NFE ===========>> " + nfe, Toast.LENGTH_LONG).show();
        }

    }

    private void GetLeaderPosition(GoogleMap mMapLeader) {
        System.out.println("GetLeaderPosition +++++++++++++++++++++++++++++++++");

        markerCounter++;
        jabatanID = 1;
        latKetua = -6.193160;
        longKetua = 106.854508;


        //+++++++++++++++++++++++++++++
        System.out.println("++++++++++++++++++++++++ ON MAP READY +++++++++++++++++++++++++=");
        //Toast.makeText(getApplicationContext(), "onMapReady", Toast.LENGTH_SHORT).show();

        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(latKetua, longKetua))
                .zoom(14)
                .bearing(0)
                .tilt(45)
                .build();


        markerLeader = mMapLeader.addMarker(new MarkerOptions()
                .position(new LatLng(latKetua, longKetua))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.black_people_64))
                .title("..."));
        markerHashMap.put(markerLeader, 1);
        markerList.add(markerLeader);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        GetLeaderAttribute();

    }

    private void GetLeaderAttribute() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        System.out.println("GetLeaderAttribute +++++++++++++++++++++++++++++++++");

        try {
            addresses = geocoder.getFromLocation(-6.193160, 106.854508, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            leaderAddress = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            System.out.println("leaderAddress =>" + leaderAddress + " city =>" + city + " state=> " + state + " country=> " + country + " postalCode => " + postalCode + " knownName=> " + knownName);
        } catch (IOException e) {
            System.out.println("GetLeaderAttribute IOException ++++++++++++++++++++++++");
            e.printStackTrace();
        }
    }

    //Menghitung jarak
    private void HitungJarak() {
        loc1.setLatitude(listDoubleLatDeviceIntent.get(0));
        loc1.setLongitude(listDoubleLongDeviceIntent.get(0));


        loc2.setLatitude(latKetua);
        loc2.setLongitude(longKetua);

        distanceInMeters = loc1.distanceTo(loc2);

        System.out.println("HitungJarak distanceInMeters +++++++++++++++++++++++ >> " + distanceInMeters);
        System.out.println("distanceInMeters loc1 lat +++++++++++++++++++++++ >> " + loc1.getLatitude());
        System.out.println("distanceInMeters loc1 long +++++++++++++++++++++++ >> " + loc1.getLongitude());
        System.out.println("distanceInMeters loc2 lat +++++++++++++++++++++++ >> " + loc2.getLatitude());
        System.out.println("distanceInMeters loc2 long +++++++++++++++++++++++ >> " + loc2.getLongitude());
    }

    public double HitungJarak2(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        System.out.println("HitungJarak 2 Radius * c ===>> " + Radius * c + " Radius Value" + valueResult + "   KM  " + kmInDec + " Meter   " + meterInDec);
        return Radius * c;
    }

    public static float HitungJarak3(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);
        System.out.println("HitungJarak ====> " + dist);

        return dist;
    }

}
