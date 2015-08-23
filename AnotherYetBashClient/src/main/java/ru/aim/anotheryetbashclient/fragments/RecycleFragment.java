package ru.aim.anotheryetbashclient.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.RecycleQuotesAdapter;
import ru.aim.anotheryetbashclient.SimpleDividerItemDecoration;

public abstract class RecycleFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecycleQuotesAdapter adapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        setupRecyclerView(recyclerView);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public RecycleQuotesAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecycleQuotesAdapter adapter) {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
