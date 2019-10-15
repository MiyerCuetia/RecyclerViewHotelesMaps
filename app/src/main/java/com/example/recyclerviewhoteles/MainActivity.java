package com.example.recyclerviewhoteles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recyclerviewhoteles.models.Lugar;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    EditText etLatitudDes, etLongitudDes;
    Button btnOtenerCoordenadas;

    ArrayList<Lugar> listLugares;
    RecyclerView recyclerViewHotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLatitudDes = findViewById(R.id.et_latitudDes);
        etLongitudDes = findViewById(R.id.et_longitudDes);
        btnOtenerCoordenadas = findViewById(R.id.btn_obtenerCoordenadas);

        btnOtenerCoordenadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String latituDes = etLatitudDes.getText().toString();
                String longitudDes = etLongitudDes.getText().toString();

                if(!latituDes.isEmpty() & !longitudDes.isEmpty()){

                    Intent intent = new Intent(MainActivity.this, GoogleMaps.class);
                    Bundle b = new Bundle();
                    b.putString("accionUser", "clickBtnBuscar");
                    b.putString("latitudDes", latituDes);
                    b.putString("longitudDes", longitudDes);
                    intent.putExtras(b);
                    startActivity(intent);

                }else{
                    Toast.makeText(MainActivity.this, "Error - campos vacios.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        listLugares = new ArrayList<>();
        recyclerViewHotel = findViewById(R.id.amRvHoteles);
        recyclerViewHotel.setLayoutManager(new LinearLayoutManager(this));

        llenarLugares();

        //Creamos una intacia de la clase AdapterHoteles y le pasamos la lista
        AdapterHoteles adapter = new AdapterHoteles(listLugares);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GoogleMaps.class);
                Bundle b = new Bundle();
                b.putString("accionUser", "clickItemRecyclerViewHotel");
                b.putInt("idLugar", listLugares.get(recyclerViewHotel.getChildAdapterPosition(view)).getLugId());
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        recyclerViewHotel.setAdapter(adapter);
    }
    //Creamos los objeto de tipo Lugar
    public void objetoRealm() {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Lugar> results = realm.where(Lugar.class).findAll();

        if(results.size()==0){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Lugar lugar = realm.createObject(Lugar.class);
                    lugar.setLugId(1);
                    lugar.setLugNombre("Camino Real");
                    lugar.setLugDireccion("Popayan");
                    lugar.setLugTelefono("018000555");
                    lugar.setLugCorreo("caminoreal@gmail.com");
                    lugar.setLugLatitud("2.451906");
                    lugar.setLugLongitud("-76.607501");
                    lugar.setLugDescripcion("Hotel Camino Real Centro Popayan");
                    lugar.setTipoLugar("Hotel");
                    lugar.setImagenHotel(R.drawable.hotel_camino_real);

                    Lugar lugar2 = realm.createObject(Lugar.class);
                    lugar2.setLugId(2);
                    lugar2.setLugNombre("San Martin");
                    lugar2.setLugDireccion("Popayan");
                    lugar2.setLugTelefono("018000555");
                    lugar2.setLugCorreo("sm@gmail.com");
                    lugar2.setLugLatitud("2.455283");
                    lugar2.setLugLongitud("-76.597127");
                    lugar2.setLugDescripcion("Hotel San Martin");
                    lugar2.setTipoLugar("Hotel");
                    lugar2.setImagenHotel(R.drawable.hotel_san_martin);

                    Lugar lugar3 = realm.createObject(Lugar.class);
                    lugar3.setLugId(3);
                    lugar3.setLugNombre("Monasterio");
                    lugar3.setLugDireccion("Popayan");
                    lugar3.setLugTelefono("018000555");
                    lugar3.setLugCorreo("m@gmail.com");
                    lugar3.setLugLatitud("2.443449");
                    lugar3.setLugLongitud("-76.609363");
                    lugar3.setLugDescripcion("Hotel Monasterio");
                    lugar3.setTipoLugar("Hotel");
                    lugar3.setImagenHotel(R.drawable.hotel_monasterio);

                    Lugar lugar4 = realm.createObject(Lugar.class);
                    lugar4.setLugId(4);
                    lugar4.setLugNombre("La Plazuela");
                    lugar4.setLugDireccion("Popayan");
                    lugar4.setLugTelefono("018000555");
                    lugar4.setLugCorreo("lp@gmail.com");
                    lugar4.setLugLatitud("2.442077");
                    lugar4.setLugLongitud("-76.608129");
                    lugar4.setLugDescripcion("Hotel la Plazuela");
                    lugar4.setTipoLugar("Hotel");
                    lugar4.setImagenHotel(R.drawable.hotel_plazuela);
                }
            });
        }
    }

    //llenarLugares Es el encargado de alimentar nuestra lista listLugares
    public void llenarLugares() {
        //Ubicacion Cali
        etLatitudDes.setText("3.449555");
        etLongitudDes.setText("-76.532526");

        objetoRealm();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Lugar> results = realm.where(Lugar.class).findAll();
        for (Lugar lugar : results) {
            listLugares.add(new Lugar(lugar.getLugId(), lugar.getLugNombre(), lugar.getLugDireccion(), lugar.getLugTelefono(),
                    lugar.getLugCorreo(), lugar.getLugLatitud(), lugar.getLugLongitud(), lugar.getLugDescripcion(),
                    lugar.getTipoLugar(), lugar.getImagenHotel()));
        }
    }
}

