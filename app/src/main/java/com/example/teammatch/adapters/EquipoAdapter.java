package com.example.teammatch.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.teammatch.R;
import com.example.teammatch.objects.Equipo;

import java.util.ArrayList;
import java.util.List;

public class EquipoAdapter extends RecyclerView.Adapter<EquipoAdapter.ViewHolder> {

    private List<Equipo> mItems = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(Equipo item);
    }

    private final OnItemClickListener listener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public EquipoAdapter(EquipoAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public EquipoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.equipo_detalle,parent,false);

        return new EquipoAdapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EquipoAdapter.ViewHolder holder, int position) {
        holder.bind(mItems.get(position),listener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void add(Equipo item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    public void load(List<Equipo> items){
        mItems.clear();
        mItems = items;
        notifyDataSetChanged();
    }

    public void clear(){
        mItems.clear();
        notifyDataSetChanged();
    }

    public Object getItem(int pos) {
        return mItems.get(pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreView;
        private TextView miembrosView;
        private TextView descripcionView;

        public ViewHolder(View itemView) {
            super(itemView);
            nombreView = itemView.findViewById(R.id.nombreEquipo);
            miembrosView = itemView.findViewById(R.id.numMiembros);
            descripcionView = itemView.findViewById(R.id.descEquipo);
        }

        public void bind(final Equipo equipo, final EquipoAdapter.OnItemClickListener listener) {

            nombreView.setText(equipo.getNombre());

            miembrosView.setText(equipo.getMiembros().toString());

            descripcionView.setText(equipo.getDescripcion());

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onItemClick(equipo);
                }
            });
        }
    }
}
