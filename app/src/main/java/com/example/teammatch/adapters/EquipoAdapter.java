package com.example.teammatch.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teammatch.R;
import com.example.teammatch.objects.Equipo;
import com.example.teammatch.room_db.TeamMatchDAO;
import com.example.teammatch.room_db.TeamMatchDataBase;

import java.util.ArrayList;
import java.util.List;

public class EquipoAdapter extends RecyclerView.Adapter<EquipoAdapter.ViewHolder> {

    private List<Equipo> mItems = new ArrayList<>();


    public interface OnItemClickListener {
        void onItemClick(Equipo item);
    }

    private final OnItemClickListener listener;


    // Provide a suitable constructor (depends on the kind of dataset)
    public EquipoAdapter(EquipoAdapter.OnItemClickListener listener) { this.listener = listener; }

    @Override
    public EquipoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.equipo,parent,false);

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
        private ImageView imageView;
        private ImageButton imageButton;
        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            nombreView = itemView.findViewById(R.id.nomEquipo);
            imageView = itemView.findViewById(R.id.item_imageTeam);
            imageButton = itemView.findViewById(R.id.imageDeleteTeam);

        }

        public void bind(final Equipo equipo, final EquipoAdapter.OnItemClickListener listener) {

            nombreView.setText(equipo.getNombre());

            Bitmap myBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Imagenes/"+equipo.getEquipoPhotoPath());

            //Tamano imagen
            int alto = 128;
            int ancho = 128;
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ancho, alto);
            imageView.setLayoutParams(params);
            imageView.setImageBitmap(myBitmap);

            imageButton.setOnClickListener(v -> {
                TeamMatchDataBase equipodatabase = TeamMatchDataBase.getInstance(context);
                TeamMatchDAO equipodao = equipodatabase.getDao();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        equipodao.deleteEquipo(equipo);
                    }
                }).start();
            });

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onItemClick(equipo);
                }
            });
        }
    }
}
