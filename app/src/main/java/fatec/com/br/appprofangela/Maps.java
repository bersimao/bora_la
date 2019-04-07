package fatec.com.br.appprofangela;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import com.google.maps.errors.ApiException;

import org.joda.time.DateTime;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import java.util.Arrays;


public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final int overview = 0;

    LocationManager locationManager;

    LocationListener locationListener;

    String cidade = "";

    int cameraZoom = 19;

    int destCount = 0;

    Button salvarButton;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    Location lastKnowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    updateMap(lastKnowLocation);
                }
            }
        }
    }

    public void updateMap(Location location) {

        LatLng localPesquisado = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localPesquisado, cameraZoom));
        mMap.addMarker(new MarkerOptions().position(localPesquisado).title(cidade));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();

        destCount = intent.getIntExtra("destCount",0);

        salvarButton = findViewById(R.id.button_salvar_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        String mapsApiKey = getString(R.string.google_maps_api_key);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), mapsApiKey);
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.getView().setBackgroundColor(Color.WHITE);

        autocompleteFragment.setLocationRestriction(RectangularBounds.newInstance(new LatLng(-23.5489, -46.6388), new LatLng(14.235, -51.9253)));

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                // TODO: Get info about the selected place.
                mMap.clear();

                Log.i("Maps", "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());

                final String nome = place.getName();
                LatLng latLng = place.getLatLng();

                final MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(nome);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mMap.addMarker(markerOptions);

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, cameraZoom));

                salvarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DirectionsResult results = getDirectionsDetails("São José dos Campos, São Paulo, Brasil",place.getAddress(),TravelMode.DRIVING);

                        getEndLocationTitle(results);

                        if(results != null){

                            Log.i("DirectionsAPI-YES", getEndLocationTitle(results));

                        } else {

                            Log.i("DirectionsAPI-NO", "null");
                        }


                        Log.i("MapsLOG2", markerOptions.title(nome).toString());


                        Log.i("MapsLOG", place.getAddress());

                        Intent intent = new Intent().setClass(Maps.this, Trajetos.class);

                        Log.i("MapsDestCount", "An error occurred: " + Integer.toString(destCount));

                        switch (destCount){

                            case 1:
                                BaseActivity.localInicial = place.getAddress();
                                BaseActivity.opcaoShowDialog = 0;
                                break;
                            case 2:
                                BaseActivity.enderecoDestinoTemp = place.getAddress();
                                BaseActivity.opcaoShowDialog = 1;
                                break;
                            default:
                                BaseActivity.localInicial = " ";
                        }
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("Maps", "An error occurred: " + status);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateMap(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location lastKnowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            updateMap(lastKnowLocation);

            /*
            if (lastKnowLocation != null) {

                updateMap(lastKnowLocation);
            }
            */

        }
    }

    private DirectionsResult getDirectionsDetails(String orign, String destination, TravelMode mode){

        //Instant now = Instant.now(); Otra forma de pegar a data atual, porém mostra o dia e hora UTC.

        LocalDate date = LocalDate.parse("9999-12-31");
        Instant instant = date.atStartOfDay(ZoneId.of("America/Sao_Paulo")).toInstant();



        Log.i("DirectionsAPI5",instant.toString());

        try{
            return DirectionsApi.newRequest(getGeoContext())
                    .mode(mode)
                    .origin(orign)
                    .destination(destination)
                    .departureTimeNow()
                    .await();

        } catch (ApiException e){
            e.printStackTrace();
            Log.i("DirectionsAPI1",e.toString());
            return null;
        } catch (InterruptedException e){
            Log.i("DirectionsAPI2",e.toString());
            e.printStackTrace();
            Log.i("DirectionsAPI3",e.toString());
            return null;

        } catch (IOException e){
            Log.i("DirectionsAPI4",e.toString());
            e.printStackTrace();
            return null;

        }
    }

    private String getEndLocationTitle(DirectionsResult results){
        return  "Time :"+ results.routes[overview].legs[overview].duration.humanReadable + " Distance :" + results.routes[overview].legs[overview].distance.humanReadable;
    }


    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .queryRateLimit(3)
                .apiKey(getText(R.string.google_maps_api_key).toString())
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build();
        return geoApiContext;
    }

}

/**
 * Manipulates the map once available.
 * This callback is triggered when the map is ready to be used.
 * This is where we can add markers or lines, add listeners or move the camera. In this case,
 * we just add a marker near Sydney, Australia.
 * If Google Play services is not installed on the device, the user will be prompted to install
 * it inside the SupportMapFragment. This method will only be triggered once the user has
 * installed Google Play services and returned to the app.
 */

