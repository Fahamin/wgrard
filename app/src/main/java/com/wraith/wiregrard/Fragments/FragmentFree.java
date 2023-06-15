package com.wraith.wiregrard.Fragments;

import static com.wraith.wiregrard.utils.Fun.checkInternet;
import static com.wraith.wiregrard.utils.Fun.foregroundServiceRunning;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wireguard.android.backend.GoBackend;
import com.wraith.wiregrard.Adapter.FreeServerAdapter;
import com.wraith.wiregrard.MainActivity;
import com.wraith.wiregrard.Model.VpnModel;
import com.wraith.wiregrard.R;
import com.wraith.wiregrard.Service.MyForegroundService;
import com.wraith.wiregrard.utils.Fun;
import com.wraith.wiregrard.utils.TinyDB;

import java.util.ArrayList;
import java.util.List;

public class FragmentFree extends Fragment implements FreeServerAdapter.OnSelectListener {

    RecyclerView recyclerView;
    List<VpnModel> list = new ArrayList<>();

    TinyDB tinyDB; // = new TinyDB(getActivity());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_free, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Fun(getActivity());
        tinyDB = new TinyDB(getActivity());
        list = Fun.FreeServerList;
        recyclerView = view.findViewById(R.id.rv_Free);
        FreeServerAdapter adapter = new FreeServerAdapter(list, getActivity(),this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onSelected(int server) {
        tinyDB.putInt("serverPos", server);
        Log.e("onstar", "" + server);

        Intent mIntent = new Intent();
        mIntent.putExtra("server", server);
        getActivity().setResult(getActivity().RESULT_OK, mIntent);
        getActivity().finish();
    }
}