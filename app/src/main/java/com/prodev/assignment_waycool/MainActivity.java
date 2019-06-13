package com.prodev.assignment_waycool;

import android.app.AlertDialog;

import android.location.Address;
import android.location.Geocoder;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.prodev.assignment_waycool.Fragments.Farg_Cart;
import com.prodev.assignment_waycool.Fragments.Farg_Profile;
import com.prodev.assignment_waycool.Fragments.Farg_home;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private Location location;
    private TextView locationTv;
    TextView tx;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds
    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;
ProgressBar prog;
Toolbar toolbarTop;
Spinner sp;
    ArrayList<String> arraySpinner ;
    HashSet<String> hash  =new HashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prog=(ProgressBar)findViewById(R.id.prog);
       // locationTv = findViewById(R.id.tx1);
        // we add permissions we need to request location of the users
        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();


         toolbarTop = (Toolbar) findViewById(R.id.toolbar);
         sp=(Spinner)toolbarTop.findViewById(R.id.Spinner01);

        //mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);
        prog.setVisibility(View.VISIBLE);
           // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //------------------------------------------------------------------------------------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment frag = null;
                switch (menuItem.getItemId()){
                    case R.id.home_tab:
                        frag = new Farg_home();
                        break;
                    case R.id.cart_tab:
                        frag = new Farg_Cart();
                        break;
                    case R.id.profile_tab:
                        frag = new Farg_Profile();
                        break;
                }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Farg_home()).commit();

        //--------------------------------------------------------------------------------------------------------




    }


    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPlayServices()) {
            locationTv.setText("You need to install Google Play Services to use the App properly");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // stop location updates
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Permissions ok, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            //locationTv.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
try{
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            prog.setVisibility(View.INVISIBLE);

            hash.add(address);
            hash.add("Use My Current Location!");
           arraySpinner = new ArrayList<>(hash);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item,arraySpinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp.setAdapter(adapter);


    sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
            // TODO Auto-generated method stub
            if(arraySpinner.get(position).equals("Use My Current Location!")){
                onLocationChanged(location);
            }
           // Toast.makeText(getBaseContext(), arraySpinner.get(position), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    });


            //locationTv.setText(address+"\n"+city+"\n"+state+", "+postalCode+"\n"+country);
           // mTitle.setText(address);
                }catch (Exception e){}
        }
        else{
            prog.setVisibility(View.VISIBLE);
        }

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            try{
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                prog.setVisibility(View.INVISIBLE);
               // mTitle.setText(address);
                hash = new HashSet<>();

                hash.add(address);

               // arraySpinner.add(1,address);
                //arraySpinner.remove(2);
               // locationTv.setText(address+"\n"+city+"\n"+state+", "+postalCode+"\n"+country);
            }catch (Exception e){}
        }
        else{prog.setVisibility(View.VISIBLE);}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(MainActivity.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.
                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                } else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }

                break;
        }
    }
}