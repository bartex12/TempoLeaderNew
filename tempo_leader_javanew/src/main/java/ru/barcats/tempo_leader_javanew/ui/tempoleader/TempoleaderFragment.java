package ru.barcats.tempo_leader_javanew.ui.tempoleader;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.model.P;

public class TempoleaderFragment extends Fragment {

    public static final String TAG ="33333";
    private TempoleaderViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tempoleader, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String dataFile;
        // когда грузим с главного экрана или со шторки, аргументы = null
        if (getArguments() != null) {
            Log.d(TAG, "** TempoleaderFragment onViewCreated " +
                    " NAME_OF_FILE = " + getArguments().getString(P.NAME_OF_FILE) +
                    " FROM_ACTIVITY = " +  getArguments().getInt(P.FROM_ACTIVITY) +
                    " FINISH_FILE_ID = " + getArguments().getInt(P.FINISH_FILE_ID));
            dataFile = getArguments().getString(P.NAME_OF_FILE);
        }else {
            dataFile = P.FILENAME_OTSECHKI_SEC;
        }

        slideshowViewModel =
                ViewModelProviders.of(this).get(TempoleaderViewModel.class);

        slideshowViewModel.loadDataSet(dataFile)
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<DataSet>>() {
            @Override
            public void onChanged(ArrayList<DataSet> dataSets) {
                //TODO
                Log.d(TAG, " /*/ dataSets size =  " + dataSets.size());
                //показываем список на экране
                showSetList(view, dataSets);
            }
        });

       }

       private  void showSetList(View view, ArrayList<DataSet> dataSets){

           RecyclerView recyclerView = view.findViewById(R.id.recycler_tempoleader);
           LinearLayoutManager manager = new LinearLayoutManager(getActivity());
           recyclerView.setLayoutManager(manager);
           RecyclerViewTempoleaderAdapter adapter = new RecyclerViewTempoleaderAdapter( dataSets);
           recyclerView.setAdapter(adapter);
       }

    }