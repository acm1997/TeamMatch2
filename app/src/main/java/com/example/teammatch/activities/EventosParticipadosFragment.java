package com.example.teammatch.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teammatch.AppExecutors;
import com.example.teammatch.R;
import com.example.teammatch.adapters.EventAdapter;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.room_db.TeamMatchDataBase;

import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class EventosParticipadosFragment extends Fragment {

    private EventAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_eventosparticipados, container, false);

        loadMisParticipaciones();

        //Mis eventos
        RecyclerView mRecyclerView = v.findViewById(R.id.my_recycler_view_EventosParticipacion);

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mlayoutManager);

        mAdapter = new EventAdapter(item -> {
            Intent eventoIntent = new Intent(getActivity(), EventDetailsActivity.class);
            Evento.packageIntent(eventoIntent, item.getNombre(), item.getFecha().toString(), item.getParticipantes(), item.getDescripcion(), item.getDeporte(), item.getPista(), item.getUserCreatorId(), item.getLatitud(), item.getLongitud());
            startActivity(eventoIntent);
        });

        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    private void loadMisParticipaciones() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("Preferences", MODE_PRIVATE);
        Long usuario_id = preferences.getLong("usuario_id", 0);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Evento> eventosParticipacion= TeamMatchDataBase.getInstance(getActivity()).getDao().getAllParticipacionesByUser(usuario_id);
                requireActivity().runOnUiThread(() -> mAdapter.load(eventosParticipacion));
            }
        });
    }
}
