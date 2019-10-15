package com.example.recyclerviewhoteles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recyclerviewhoteles.models.Lugar;
import java.util.ArrayList;

public class AdapterHoteles extends RecyclerView.Adapter<AdapterHoteles.ViewHolderHoteles> implements View.OnClickListener {

    ArrayList<Lugar> listLugares;
    private View.OnClickListener listener;

    //Generamos un constructor
    public AdapterHoteles(ArrayList<Lugar> listLugares) {
        this.listLugares = listLugares;
    }

    //El metodo onCreateViewHolder enlaza el adaptador con el archivo xml item_hotel
    //Inflamos mediante un View el archivo xml item_hotel
    //Tambien le asiganamos el evento  OnClickListener;
    @Override
    public ViewHolderHoteles onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel, null, false);
        view.setOnClickListener(this);
        return new ViewHolderHoteles(view);
    }

    //Se encarga de hacer la comunicacion entre nuetro adpatador y la clase ViewHolderHoteles
    @Override
    public void onBindViewHolder(ViewHolderHoteles holder, int position) {
        holder.tvTitulo.setText(listLugares.get(position).getLugNombre());
        holder.tvUbicacion.setText(listLugares.get(position).getLugDireccion());
        holder.imageHotel.setImageResource(listLugares.get(position).getImagenHotel());
    }

    // retorna el tamaño de la lista
    @Override
    public int getItemCount() {
        return listLugares.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;

    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    //Hacemos referencia a los componentes graficos del archivo xml item_hotel
    public class ViewHolderHoteles extends RecyclerView.ViewHolder {
        TextView tvTitulo;
        TextView tvUbicacion;
        ImageView imageHotel;

        public ViewHolderHoteles(@NonNull View itemView) {
            super(itemView);

            tvTitulo = itemView.findViewById(R.id.tv_titulo);
            tvUbicacion = itemView.findViewById(R.id.tv_ubicacion);
            imageHotel = itemView.findViewById(R.id.image_hotel);
        }
    }
}
