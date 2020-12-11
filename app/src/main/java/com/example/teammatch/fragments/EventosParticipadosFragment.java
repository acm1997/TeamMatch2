package com.example.teammatch.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teammatch.AppContainer;
import com.example.teammatch.AppExecutors;
import com.example.teammatch.MyApplication;
import com.example.teammatch.R;
import com.example.teammatch.activities.EventDetailsActivity;
import com.example.teammatch.adapters.EventAdapter;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.room_db.TeamMatchDataBase;
import com.example.teammatch.ui.EventosCreadosFragmentViewModel;
import com.example.teammatch.ui.EventosParticipacionFragmentViewModel;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class EventosParticipadosFragment extends Fragment {

    private EventAdapter mAdapter;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_eventos, container, false);

        //loadMisParticipaciones();

        //Mis eventos participados
        mProgressBar = v.findViewById(R.id.progressBar2);
        mRecyclerView = v.findViewById(R.id.my_recycler_view_EventosParticipacion);

        RecyclerView mRecyclerView = v.findViewById(R.id.my_recycler_view_EventosParticipacion);

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mlayoutManager);

        AppContainer appContainer = ((MyApplication) this.requireActivity().getApplication()).appContainer;
        EventosParticipacionFragmentViewModel mViewModel = new ViewModelProvider(this.requireActivity(), appContainer.factoryEventosParticipacion).get(EventosParticipacionFragmentViewModel.class);
        mViewModel.getEventosParticipacion().observe(this.requireActivity(), eventos -> {
            mAdapter.load(eventos);
            if (eventos != null ) showReposDataView();
            else showLoading();
        });

        mAdapter = new EventAdapter(item -> {
            Intent eventoIntent = new Intent(getActivity(), EventDetailsActivity.class);
            Evento.packageIntent(eventoIntent, item.getNombre(), item.getFecha().toString(), item.getParticipantes(), item.getDescripcion(), item.getDeporte(), item.getPista(), item.getUserCreatorId(), item.getLatitud(), item.getLongitud(), item.getEventoPhotoPath());
            startActivity(eventoIntent);
        });

        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    private void showReposDataView(){
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    private void showLoading(){
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
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
