package com.example.tp2inf8405;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static android.provider.SettingsSlicesContract.KEY_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Context context = this;
    Button changeTheme;

    private final LatLng defaultLocation = new LatLng(45.5, -73);
    private static final int DEFAULT_ZOOM = 19;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSION_CODE = 1;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;
    private PlacesClient placesClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Activity activity = this;
    // bt device declaration
    private BtDevice bt;
    // array list of bt device
    ArrayList<BtDevice> arrayListDemo = new ArrayList<BtDevice>();
    private ListView listView;
    private BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    private Activity mActivity;
    private Button mCapteurs;

    private LinearLayout mLinearLayout;
    private Button mButton;

    private PopupWindow mPopupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_Light);
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_maps);
        changeTheme = (Button) findViewById(R.id.changeTheme);
        //arrayTest();
        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = MapsActivity.this;

        // Get the widgets reference from XML layout
        mLinearLayout = (LinearLayout) findViewById(R.id.rl);
        mButton = (Button) findViewById(R.id.checkInfos);
        mCapteurs = (Button) findViewById(R.id.capteurs);


        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        Places.initialize(getApplicationContext(), "AIzaSyBDoWjm9op94TYFclt-TIU6lMzjJmQDcJs");
        placesClient = Places.createClient(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ArrayAdapter adapter = new ArrayAdapter<BtDevice>(this,
                R.layout.list_view, arrayListDemo);

        listView = (ListView) findViewById(R.id.bluetooth_list);
        listView.setAdapter(adapter);


        //event listener for bt device list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                //get the info of the bt device clicked and pass them to popup to showed
                openDialog(arrayListDemo.get(position));
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToInfos();
            }
        });

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();
        mCapteurs.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, LightSensorActivity.class);
                startActivity(intent);
            }
        });
        changeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Utils.changeToTheme(activity, Utils.THEME_LIGHT);


                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Utils.changeToTheme(activity, Utils.THEME_BLACK);

                }

            }

        });

    }
    //popup function taking as a parameter a bt device
    private void openDialog(BtDevice btDevice) {
        ExampleDialog exampleDialog=new ExampleDialog(btDevice);
        exampleDialog.show(getSupportFragmentManager(),"Dialog");
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public int check (String btmac){
        int i,x; x=1;
        for(i=0; i<=arrayListDemo.size()-1; i++){
            if (arrayListDemo.get(i).mac.contentEquals(btmac))
                x = 0;
        } return x;

    }



    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (check (device.getAddress()) != 0 ){
                    if (device.getName() != null) {
                        //if new device is found we inctanciate a new btdevice and give it the adequate information
                        LatLng deviceLocation = defaultLocation;
                        if (lastKnownLocation != null) {
                            deviceLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        }
                        bt = new BtDevice();
                        bt.mac=device.getAddress();
                        bt.dName=device.getName();
                        bt.c=deviceLocation;
                        //adding the device to the list of bt device
                        arrayListDemo.add(bt);

                        //adding a marker to the map of the new bt device found
                        mMap.addMarker(new MarkerOptions()
                                .position(bt.c)
                                .title(bt.dName).icon(bitmapDescriptorFromVector(context, R.drawable.mbl)).snippet(bt.mac + "\n" + bt.c.latitude + "\n" +bt.c.longitude));

                    }
                    Log.i("BT", device.getName() + "\n" + device.getAddress());
                    listView.setAdapter(new ArrayAdapter<BtDevice>(context,
                            R.layout.list_view, arrayListDemo));

                    //sauvegarde de données des appareils dans un fichier
                    PrefConfig.writeListInPref(getApplicationContext(),arrayListDemo);
                    }
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setBuildingsEnabled(true);

        //mMap.setBuildingsEnabled(true);
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();

        //marker event listener, handles all the markers on map
        mMap.setOnMarkerClickListener(this);
    }
    //after a marker is clicked the we show info window containing the bt device info
    public boolean onMarkerClick(final Marker marker) {

        marker.showInfoWindow();

        return false;
    }
    private void getDeviceLocation() {

        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {

                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                LatLng currentLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                mMap.addMarker(new MarkerOptions()
                                        .position(currentLocation)
                                        .title("current position").icon(bitmapDescriptorFromVector(context, R.drawable.ic_baseline_emoji_people_24)));

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));



                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(currentLocation )      // Sets the center of the map to Mountain View
                                        .zoom(19.5f)                   // Sets the zoom
                                        .bearing(270)                // Sets the orientation of the camera to east
                                        .tilt(60)                   // Sets the tilt of the camera to 30 degrees
                                        .build();                   // Creates a CameraPosition from the builder
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            }
                        } else {
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getBluetoothPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        PERMISSION_CODE);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                //mMap.setMyLocationEnabled(true);
                //mMap.getUiSettings().setMyLocationButtonEnabled(true);
                getDeviceLocation();
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {


    }

    private void goToInfos(){
        Intent intent = new Intent(this, appInfos.class);
        startActivity(intent);
    }




}