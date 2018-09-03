package kobdratenkov.ncedu.ru.lifetracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.location.*;
import android.view.View;
import android.widget.TextView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;



public class MainActivity extends AppCompatActivity {
    private TextView statusNet;
    private TextView statusGPS;
    private TextView coordGPS;
    private TextView coordNet;
    private LocationManager locationManager;
    private StringBuilder GPS;
    private StringBuffer Net;
    private Queue<UserCoord> coordsQueue;
    int step = 0;
    final int LOCKATION_PERMISSION = 0;
    final int SAVE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusGPS = findViewById(R.id.enabledGPS);
        statusNet = findViewById(R.id.enabledNet);
        coordGPS = findViewById(R.id.coordGPS);
        coordNet = findViewById(R.id.coordNet);
        coordsQueue = new ArrayBlockingQueue<UserCoord>(50);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SAVE_PERMISSION);
            }
            return;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        onClickStartButton(null);
        checkEnabled();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permitions, int[] results){
        switch(requestCode){
            case LOCKATION_PERMISSION:{
                if(results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED){
                    checkEnabled();
                }
                else{
                    checkEnabled();
                }return;
            }
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
            coordsQueue.add(new UserCoord(location.getLatitude(), location.getLongitude(), location.getTime()));
            ++step;
            if (step >= 5) {
                new Writer(getApplicationContext(), coordsQueue).run();
                step = 0;
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle bundle) {
            /*if (provider.equals(LocationManager.GPS_PROVIDER)) {
                statusGPS.setText("GPS status " + String.valueOf(status));
            }
            if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                statusNet.setText("network status " + String.valueOf(status));
            }*/
            checkEnabled();
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onProviderDisabled(String s) {
            checkEnabled();
        }
    };

    private void showLocation(Location location) {
        if (location != null) {
            if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                coordGPS.setText(formatLocation(location));
            }
            if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
                coordNet.setText(formatLocation(location));
            }
        }
    }

    private String formatLocation(Location location) {
        if (location != null) {
            return String.format("coords: lat = %1$4f, lon = %2$4f, time = %3$tT", location.getLatitude(), location.getLongitude(), new Date(location.getTime()));
        }
        return "";
    }

    private void checkEnabled() {
        statusGPS.setText("GPS is " + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        statusNet.setText("Net is " + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    private void onClickStartButton(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCKATION_PERMISSION);
            }
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        showLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
        checkEnabled();
    }

    private void onClickStopButton(){
        locationManager.removeUpdates(locationListener);
        new Writer(getApplicationContext(), coordsQueue).run();
    }

    private void onClickSendButton() {
        new HttpRequestTask(coordsQueue).execute();
        //HttpEntity<UserCoord> requestBody = new HttpEntity<>(coordToSend, headers);
        //restTemplate.put("http://localhost:7474/upload", requestBody, new Object[]{});
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.startButton:
                onClickStartButton(view);
                break;
            case R.id.stopButton:
                onClickStopButton();
            case R.id.sendButton:
                onClickSendButton();
        }
    }
}
