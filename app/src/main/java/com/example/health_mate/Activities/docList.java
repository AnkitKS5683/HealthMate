package com.example.health_mate.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.health_mate.ViewModels.DocInfo;
import com.example.health_mate.Adapters.DoctorAdapter;
import com.example.health_mate.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class docList extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    LocationManager locationManager;
    Location location;
    FusedLocationProviderClient fusedLocationProviderClient;

    double lat;
    double lon;

    private String type;

    private DoctorAdapter da;

    RecyclerView rv;
    LinearLayoutManager llm;
    private TextView lati;
    private TextView longi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_list);

        Intent i = getIntent();
        type = i.getStringExtra("type");

        lati = findViewById(R.id.tvlat);
        longi = findViewById(R.id.tvlong);
        if (ActivityCompat.checkSelfPermission(docList.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(docList.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

         getLocation();
       /*  if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            lati.setText((int) lat);
            longi.setText((int) lon);
        } */

        rv = (RecyclerView) findViewById(R.id.rv);
        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setAdapter(da);
      //  fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Doctors").child(type);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    DocInfo di = d.getValue(DocInfo.class);
                    if (distance(di.getLat(), di.getLon(), lat, lon) < 1)
                        da.addDoc(di);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5, (LocationListener) docList.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void onLocationChanged(Location location) {
      //  Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(docList.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            //String address = addresses.get(0).getAddressLine(0);
            lat=addresses.get(0).getLatitude();
            lon=addresses.get(0).getLongitude();
            //textView_location.setText(address);
            lati.setText((int) lat);
            longi.setText((int) lon);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

   // @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

  //  @Override
    public void onProviderEnabled(String provider) {

    }

  //  @Override
    public void onProviderDisabled(String provider) {

    }


  //  private Location getLastKnownLocation() {
      /*  locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation; */
     /*   if (ActivityCompat.checkSelfPermission(docList.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //When Permission Given
            getLocation();
        } else {
            ActivityCompat.requestPermissions(docList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        return location; */
    //}

   /* private void getLocation() {
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
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Location> task) {
                //Initialize location
                location = task.getResult();
                if (location != null) {
                    //initialize geo Coder

                    //Initialize address
                    try {
                        Geocoder geocoder = new Geocoder(docList.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        lat=addresses.get(0).getLatitude();
                        lon=addresses.get(0).getLongitude();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    } */

    /** calculates the distance between two locations in MILES */
    private double distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75; // in miles, change to 6371 for kilometers
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;
        return dist;
    }
}
