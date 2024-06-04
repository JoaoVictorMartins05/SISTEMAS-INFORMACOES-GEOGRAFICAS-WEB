package com.example.posicionamentoglobalgps;

import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void mostrarGooleMaps(double latitude, double longitude) {
        WebView wv = findViewById(R.id.webv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude);
    }


    public void buscarInformacoesGPS(View v) {
        LocationManager mLocManager = null;
        LocationListener mLocListener;
        mLocManager = (LocationManager) getSystemService(MainActivity.this.LOCATION_SERVICE);
        mLocListener = new MinhaLocalizacao();

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                    }, 1);
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            android.Manifest.permission.ACCESS_NETWORK_STATE
                    }, 1);
            return;
        }


        mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocListener);
        if(mLocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            String texto = "Latitude: " + MinhaLocalizacao.latitude + "\n" +
                            "Longitude: " + MinhaLocalizacao.longitude + "\n";
            Toast.makeText(MainActivity.this, texto, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(MainActivity.this, "GPS DESABILITADO", Toast.LENGTH_LONG).show();
        }
        this.mostrarGooleMaps( MinhaLocalizacao.latitude,  MinhaLocalizacao.longitude);
    }
}