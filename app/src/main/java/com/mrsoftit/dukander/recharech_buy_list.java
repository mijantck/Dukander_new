package com.mrsoftit.dukander;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class recharech_buy_list extends Fragment {





    private View view;
    BuyCoinAdapter adapter;
    ImageButton sendbutton;
    EditText editText;


    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    String user_id = Objects.requireNonNull(currentUser).getUid();


    public recharech_buy_list(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=  inflater.inflate(R.layout.recharch_buy_list_fragment, container, false);

        final CollectionReference Buy = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("Buy");


        Query query = Buy.orderBy("time", Query.Direction.ASCENDING);


        FirestoreRecyclerOptions<BuyNote> options = new FirestoreRecyclerOptions.Builder<BuyNote>()
                .setQuery(query, BuyNote.class)
                .build();

        adapter = new BuyCoinAdapter(options);

        final RecyclerView recyclerView = view.findViewById(R.id.buyCoinreciclearview);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
        manager.setStackFromEnd(true);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        sendbutton = view.findViewById(R.id.sendbutton);
        editText = view.findViewById(R.id.editText);

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Long time = System.currentTimeMillis()/1000;
                final String gmail = currentUser.getEmail().toString();
                final String sms  = editText.getText().toString();
                Buy.add(new BuyNote(time,null,user_id,gmail,sms)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                   if (task.isSuccessful()){
                       editText.setText(null);

                    final String id =task.getResult().getId();
                    Buy.document(id).update("id",id).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                   }
                    }
                });

                CollectionReference usersSMS = FirebaseFirestore.getInstance().collection("usersSMS");
                usersSMS.add(new BuyNote(time,null,user_id,gmail,sms)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                    }
                });
            }
        });




















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
