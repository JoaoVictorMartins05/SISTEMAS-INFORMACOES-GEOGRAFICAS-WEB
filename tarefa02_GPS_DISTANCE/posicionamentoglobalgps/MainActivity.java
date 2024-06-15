package com.example.posicionamentoglobalgps;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Ponto p1, p2;
    String PROVIDER;


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
        PROVIDER = LocationManager.NETWORK_PROVIDER;
    }


    public void mostrarGooleMaps(double latitude, double longitude) {
        WebView wv = findViewById(R.id.webv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude);
    }


    public Ponto getPonto() {
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
            return null;
        }


        mLocManager.requestLocationUpdates(PROVIDER, 0, 0, mLocListener);

        Location localAtual = mLocManager.getLastKnownLocation(PROVIDER);

        if(!mLocManager.isProviderEnabled(PROVIDER)){
            Toast.makeText(MainActivity.this, "GPS DESABILITADO", Toast.LENGTH_LONG).show();
        }

        return new Ponto(localAtual.getLatitude(), localAtual.getLongitude(),
                localAtual.getAltitude());
    }

    public void reset(View v){
        p1 = new Ponto();
        p2 = new Ponto();
        EditText edtPto = findViewById(R.id.editPtoA);
        edtPto.setText("");
        edtPto = findViewById(R.id.editPtoB);
        edtPto.setText("");
    }

    public void lerPontoA(View v){
        p1 = this.getPonto();
        EditText edtPto = findViewById(R.id.editPtoA);
        edtPto.setText(p1.imprimir2());
    }

    public void verPontoA(View v){
        this.mostrarGooleMaps( p1.getLatitude(),  p1.getLongitude());
    }

    public void lerPontoB(View v){
        p2 = this.getPonto();
        EditText edtPto = findViewById(R.id.editPtoB);
        edtPto.setText(p2.imprimir2());
    }

    public void verPontoB(View v){
        this.mostrarGooleMaps( p2.getLatitude(),  p2.getLongitude());
    }

    public void calcularDistancia(View v){
        LocationManager mLocManager = (LocationManager) getSystemService(MainActivity.this.LOCATION_SERVICE);
        float[] resultado = new float[1];
        Location.distanceBetween(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude(), resultado);

        if(mLocManager.isProviderEnabled(PROVIDER)){
            String texto = "*** Distância: " + resultado[0] + "/n";
            Toast.makeText(MainActivity.this, texto, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(MainActivity.this, "GPS DESABILITADO", Toast.LENGTH_LONG).show();
        }

    }



















}