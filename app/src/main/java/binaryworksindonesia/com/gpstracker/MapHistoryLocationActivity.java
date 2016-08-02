package binaryworksindonesia.com.gpstracker;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapHistoryLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    ArrayList<Double> listDoubleLatDeviceIntent = new ArrayList<Double>();
    ArrayList<Double> listDoubleLongDeviceIntent = new ArrayList<Double>();
    ArrayList<String> listLongitudeIntent, listLatitudeIntent, listAddressIntent, listIdIntent, listDeviceNameIntent;

    Double testLat, testLong;
    // BottomSheetBehavior variable
    private BottomSheetBehavior bottomSheetBehavior;

    // TextView variable
    private TextView bottomSheetHeading, tvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_history_location);
        InitBottomSheetView();
        initBottomSheetListeners();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        TestingMap();
        // <CONVERT TO DOUBLE AND ADD THEM TO THE LIST ++++++++++++++++++++++++++++++++++++++++++++>
        // LatLangConvertToDouble(listLatitudeIntent, listLongitudeIntent);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void InitBottomSheetView() {
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        bottomSheetHeading = (TextView) findViewById(R.id.bottomSheetHeading);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
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
        //Toast.makeText(getApplicationContext(), "onMapReady", Toast.LENGTH_SHORT).show();
        mMap = map;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                System.out.println("onMarkerClick +++++++++++++++++ >> ");
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomSheetHeading.setText("Tanggal...");
                tvLocation.setText("Address...");
                System.out.println("marker id ==> " + marker.getId());
                return false;
            }
        });
        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(testLat, testLong))
                .zoom(14)
                .bearing(0)
                .tilt(45)
                .build();


        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(testLat, testLong))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_people_64))
                .title("...")
                .snippet("..."));

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
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MapHistoryLocationActivity.this, "onInfoWindowClick", Toast.LENGTH_LONG).show();

            }
        });

        //TITLE AND SNIPPET MULTILINES +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);

                // Getting the position from the marker
                LatLng latLng = marker.getPosition();

                // Getting reference to the TextView to set latitude
                TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);

                // Getting reference to the TextView to set longitude
                TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);

                // Setting the title
                tvLat.setText(String.valueOf(marker.getTitle()));

                if (marker.getSnippet().contains("null")) {
                    // Setting the snippet
                    tvLng.setText("");
                } else {
                    // Setting the snippet
                    tvLng.setText(String.valueOf(marker.getSnippet()));
                }


                // Returning the view containing InfoWindow contents
                return v;
            }
        });

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                    /*
                    // Creating an instance of MarkerOptions to set position
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting position on the MarkerOptions
                    markerOptions.position(latLng);

                    // Animating to the currently touched position
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    // Adding marker on the GoogleMap
                    Marker marker = mMap.addMarker(markerOptions);

                    // Showing InfoWindow on the GoogleMap
                    marker.showInfoWindow();
                    */
                //
                System.out.println("setOnMapClickListener ++++++++++++++++++++++++++++ >> ");
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void TestingMap() {
        testLat = -6.174880;
        testLong = 106.844197;

    }

}
