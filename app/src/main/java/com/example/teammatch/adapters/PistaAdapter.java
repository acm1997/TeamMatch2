package com.example.teammatch.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.teammatch.R;
import com.example.teammatch.objects.Binding;

import java.util.ArrayList;
import java.util.List;

public class PistaAdapter extends RecyclerView.Adapter<PistaAdapter.MyViewHolder> {
    private List<Binding> mDataset;
    private static final String TAG = "Event Pistas: ";
    private List<Binding> mDatasetAux;


    public interface OnListInteractionListener{
        public void onListInteraction(Binding b);
    }

    public OnListInteractionListener mListener;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mNombrePista;
        public View mView;

        public Binding mItem;

        public MyViewHolder(View v) {
            super(v);
            mView=v;
            mNombrePista = v.findViewById(R.id.idNombrePista);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PistaAdapter(List<Binding> myDataset, OnListInteractionListener listener) {
        mDataset = myDataset;
        mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        // Create new views (invoked by the layout manager)
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pista, parent, false);

        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mItem = mDataset.get(position);
        holder.mNombrePista.setText(mDataset.get(position).getFoafName().getValue());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListInteraction(holder.mItem);

                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void swap(List<Binding> dataset){
        mDataset = dataset;
        mDatasetAux = mDataset;
        notifyDataSetChanged();
    }

    //BUSCADOR PISTAS
    public List<Binding> filtrado(String nombre){
        log("Pista de nombre: "+nombre);
        List<Binding> listaPistasFiltradas = new ArrayList<>();
        log("Pista lista tamaño: " + listaPistasFiltradas.size());
        if(nombre.isEmpty()){
            listaPistasFiltradas.addAll(mDatasetAux);
        }else{
            listaPistasFiltradas.clear();
            log("Pista lista tamaño: " + listaPistasFiltradas.size());
            for(Binding binding : mDatasetAux){
                if(binding.getFoafName().getValue().toLowerCase().contains(nombre.toLowerCase())){
                    listaPistasFiltradas.add(binding);
                    log("Pista lista tamaño: " + listaPistasFiltradas.size());
                }
            }
        }
        log("Pista lista tamaño: " + listaPistasFiltradas.size());
        return listaPistasFiltradas;
    }

    //Carga la nueva lista
    public void loadBuscador(List<Binding> items){
        mDataset.clear();
        mDataset = items;
        notifyDataSetChanged();
    }

    private void log(String msg) {
        try {
            Thread.sleep(500);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }
}
