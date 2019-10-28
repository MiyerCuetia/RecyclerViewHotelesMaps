package com.example.recyclerviewhoteles;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyclerviewhoteles.models.Lugar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import io.realm.Realm;
import io.realm.RealmResults;

public class GoogleMaps extends FragmentActivity implements
        OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {
    private Integer idLugar;
    private String accionUsuario, lat, lng, nombreLugar, tipoLugar;

    //Variables para la gestion del mapa y marker
    private GoogleMap mMap;
    private Marker miMarker, ubicacionMarker;
    private static final int LOCATION_REQUEST_CODE = 1;

    //Variables para obtener la ubicacion
    LocationManager locationManager;
    LocationListener locationListener;
    AlertDialog alert = null;

    //Hacemos referencia a los componentes graficos del layout activity_google_maps.xml
    TextView tvCoordenada;
    ImageButton ibUbicacion;
    EditText etLatitudActual, etLongitudActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        //Inicializamos los componentes graficos del layout activity_google_maps.xml
        tvCoordenada = findViewById(R.id.tv_coordenada);
        ibUbicacion = findViewById(R.id.ib_ubicacion);
        etLatitudActual = findViewById(R.id.et_latitudActual);
        etLongitudActual = findViewById(R.id.et_longitudActual);

        //Validacion para saber si el dispositivo tine instalado los googleplay services si es asi
        //Vamos a indicar a dicho objeto que controlará el mapa de google
        //si no mostramos un error
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (status == ConnectionResult.SUCCESS) {
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
            mapFragment.getMapAsync(this);
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getApplicationContext(), 10);
            dialog.show();
        }
    }

    //Dentro del método onMapReady vamos a utilizar el objeto googleMap de acuerdo
    // a lo que requiramos, en nuestro caso  queremos agregar un marker de nuestra ubicacion actual, tambien marcadores
    // dentro del mapa segun coordenadas digitadas o de la lista.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Controles UI

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        //Al ImageButton le asignamos el evento OnClickListener, para gestionar la ubicacion actual
        ibUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Inicializacion la variable locationManager y le asignamos el servicio de localizacion
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                //Validacion de permisos en tiempo de ejecucion
                if (ContextCompat.checkSelfPermission(GoogleMaps.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(GoogleMaps.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            AlertNoGps();
                        }

                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(GoogleMaps.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_REQUEST_CODE);

                        // LOCATION_REQUEST_CODE is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        AlertNoGps();
                    }
                }
                /*****/


                //Creamos un listener de la clase LocationListener, el LocationListener es el escuchador de las actualizaciones
                locationListener = new LocationListener() {
                    //Siempre que reciba una actualizacion dibujaremos un marker
                    @Override
                    public void onLocationChanged(Location location) {

                        if (location != null) {
                            LatLng coordenadaUbicacion = new LatLng(location.getLatitude(), location.getLongitude());
                            //Validacion para eliminar duplicacion de marker
                            if (ubicacionMarker != null) {
                                ubicacionMarker.remove();
                            }

                            nombreLugar = "Estas aqui";
                            etLatitudActual.setText(String.valueOf(location.getLatitude()));
                            etLongitudActual.setText(String.valueOf(location.getLongitude()));

                            ubicacionMarker = mMap.addMarker(new MarkerOptions().title(nombreLugar).snippet(location.getLatitude()
                                    + " , " + location.getLongitude()).position(coordenadaUbicacion).visible(true));
                            ubicacionMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadaUbicacion, 17));
                            //Evento
                            mMap.setOnMarkerClickListener(GoogleMaps.this);
                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    //Cuando cambia el proveedor de estado, de activo a inactivo
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    //Cuando cambia el proveedor se activa
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    //Cuando cambia el proveedor se desactiva
                    }
                };


                if (ContextCompat.checkSelfPermission(GoogleMaps.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    //Capturamos la posicion de la ubicacion  segun el proveedor en mi caso GPS

                    //EL primer parametro me indica el proveedor que voy a utilizar
                    //EL segundo parametro me indica el tiempo de actualizacion del gps
                    //El tercer parametro me indica la variacion  de espacio
                    //El cuarto parametro, es el locationlistener, quien controlara estas actualizaciones
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        });

        /*****/

        //Validadciones para dibujar el marker, dependiendo de la accion
        accionUsuario = getIntent().getExtras().getString("accionUser");
        Log.e("miAccionUser", accionUsuario);
        if (accionUsuario.equals("clickBtnBuscar")) {
            tvCoordenada.setText(R.string.coordenada_busqueda);

            nombreLugar = "Hubicacion de tu busqueda";
            //Obtenemos los valores de la cajas de texto, que se enviaron por medio de un intent de la actividad MainActivity
            //para despues dibujar el marker de la ubicacion de la coordenada
            lat = getIntent().getExtras().getString("latitudDes");
            lng = getIntent().getExtras().getString("longitudDes");

            etLatitudActual.setText(lat);
            etLongitudActual.setText(lng);

            LatLng coordenadas = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            // Marcadores
            miMarker = mMap.addMarker(new MarkerOptions().draggable(true).title(nombreLugar).position(coordenadas));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 17));
            //Asignamos Eventos para hacer Clickeable y dragable el marker
            mMap.setOnMarkerDragListener(this);
            mMap.setOnMarkerClickListener(this);

        } else if (accionUsuario.equals("clickItemRecyclerViewHotel")) {
            //Obtenemos el id, que se enviaron por medio de un intent desde la actividad MainActivity
            idLugar = getIntent().getExtras().getInt("idLugar");

            //Validamos si ese id existe dentro un objeto realm, y obtnemos las coordenadas de es id
            //luego dibujamos el marker
            Realm realm = Realm.getDefaultInstance();
            RealmResults<Lugar> results = realm.where(Lugar.class).findAll();
            for (Lugar lugar : results) {

                if (idLugar == lugar.getLugId()) {
                    tvCoordenada.setText(R.string.coordenada_actual);

                    nombreLugar = lugar.getLugNombre();
                    tipoLugar = lugar.getTipoLugar();
                    lat = lugar.getLugLatitud();
                    lng = lugar.getLugLongitud();

                    etLatitudActual.setText(lat);
                    etLongitudActual.setText(lng);

                    LatLng coordenadas = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                    // Marcadores
                    miMarker = mMap.addMarker(new MarkerOptions().title(nombreLugar).snippet(tipoLugar).position(coordenadas));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 17));
                    // Asignamos Evento para que el marker sea clickeable
                    mMap.setOnMarkerClickListener(this);
                }
            }
        }
    }

    //Metodo para activar gps por medio de un dialog
    private void AlertNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El sistema GPS esta desactivado, ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }
    /*****/
    //Metodos del Marker Dragable Cuando inicia, cuando es dragable y cuando termina
    @Override
    public void onMarkerDragStart(Marker marker) {
        if (marker.getPosition().latitude == miMarker.getPosition().latitude & marker.getPosition().longitude == miMarker.getPosition().longitude) {
            Toast.makeText(this, "INICIO", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        if (marker.getPosition().latitude == miMarker.getPosition().latitude & marker.getPosition().longitude == miMarker.getPosition().longitude) {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            etLatitudActual.setText(Double.toString(marker.getPosition().latitude));
            etLongitudActual.setText(Double.toString(marker.getPosition().longitude));
        }
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (marker.getPosition().latitude == miMarker.getPosition().latitude & marker.getPosition().longitude == miMarker.getPosition().longitude) {
            Toast.makeText(this, "FIN", Toast.LENGTH_SHORT).show();
        }
    }

    /****/
    //Metodo para asignarle el evento click al marker
    @Override
    public boolean onMarkerClick(Marker marker) {
        etLatitudActual.setText(Double.toString(marker.getPosition().latitude));
        etLongitudActual.setText(Double.toString(marker.getPosition().longitude));
        return false;
    }

    //Metodo para la validacion de permisos de localizacion
    //recibe los resultados de las solicitudes de permisos.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            // ¿Permisos asignados?
            if (permissions.length > 0 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            } else {
                Toast.makeText(this, "Error de permisos", Toast.LENGTH_LONG).show();
            }
        }
    }
}
