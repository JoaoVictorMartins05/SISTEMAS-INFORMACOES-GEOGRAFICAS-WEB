package com.example.arcgisnavigation;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;

public class MainActivity extends AppCompatActivity {

    private MapView mMapView;
    private LocationDisplay mLocationDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        mMapView = findViewById(R.id.mapView);
        setupMap();
        setupLocationDisplay();
        setupGPS();
    }

    private void setupMap() {
        if (mMapView != null) {
            Basemap.Type basemapType = Basemap.Type.STREETS_VECTOR;
            double latitude = -13.0121;
            double longitude = -39.9098;
            int levelOfDetail = 20;
            ArcGISMap map = new ArcGISMap(basemapType,latitude,longitude,levelOfDetail);
            mMapView.setMap(map);
        }
    }

    private void setupLocationDisplay() {
        mLocationDisplay = mMapView.getLocationDisplay();
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
        mLocationDisplay.startAsync();
    }

    private void setupGPS(){
        // Listen to changes in the status of the location data source.
        mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent-> {
            if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError()== null) {
                return;
            }
            int requestPermissionsCode = 2;
            String[] requestPermissions = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION};
            if (!(ContextCompat.checkSelfPermission(MainActivity.this, requestPermissions[0]) ==
                    PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this,
                    requestPermissions[1]) == PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(MainActivity.this, requestPermissions,
                        requestPermissionsCode);
            } else {
                Toast.makeText(MainActivity.this, "Erro.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationDisplay.startAsync();
        } else {
            Toast.makeText(MainActivity.this, "permissão recusada", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        if (mMapView != null) {
            mMapView.pause();
        }
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.resume();
        }
    }
    @Override
    protected void onDestroy() {
        if (mMapView != null) {
            mMapView.dispose();
        }
        super.onDestroy();
    }
}
