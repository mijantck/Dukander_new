package com.mrsoftit.dukander;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class recharech_offer_list extends Fragment {

    private View view;
    OfferAdapter adapter;

public recharech_offer_list(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view= inflater.inflate(R.layout.recharech_offer_list_fragment, container, false);

        CollectionReference offer = FirebaseFirestore.getInstance()
                .collection("offer");


        Query query = offer;

        FirestoreRecyclerOptions<OfferNote> options = new FirestoreRecyclerOptions.Builder<OfferNote>()
                .setQuery(query, OfferNote.class)
                .build();

        adapter = new OfferAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.offerRecyclearview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
