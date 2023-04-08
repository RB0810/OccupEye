package com.example.occupeye.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.occupeye.EditPage;
import com.example.occupeye.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private StorageReference storageReference;
    private StorageReference profileStorage;
    private FirebaseFirestore fStore;
    private Button editprofilebtn;
    private TextView username;
    private TextView term;
    private TextView pillar;
    private TextView hostelBlock;
    private TextView hostelResident;

    private View rootView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView pfp;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         rootView = inflater.inflate(R.layout.fragment_user, container, false);
         fStore = FirebaseFirestore.getInstance();
         storageReference = FirebaseStorage.getInstance().getReference();
         profileStorage = storageReference.child("Users/"+"admin"+"/profile.jpg");
         editprofilebtn = rootView.findViewById(R.id.editprofilebtn);
         pfp = rootView.findViewById(R.id.profile_image);
         username = rootView.findViewById(R.id.nametxt);
         pillar = rootView.findViewById(R.id.pillartxt);
         term = rootView.findViewById(R.id.termtxt);
         hostelBlock = rootView.findViewById(R.id.blocktxt);
         hostelResident = rootView.findViewById(R.id.residenttxt);

        profileStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(pfp);
            }
        });
        DocumentReference documentReference = fStore.collection("Users").document("admin");
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot.exists()){
                    username.setText(documentSnapshot.getString("Name"));
                    pillar.setText(documentSnapshot.getString("Pillar"));
                    term.setText(documentSnapshot.getString("Term"));
                    hostelBlock.setText(documentSnapshot.getString("Hostel Block"));
                    hostelResident.setText(documentSnapshot.getString("Hostel resident"));
                }
            }
        });
         editprofilebtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i = new Intent(getActivity(), EditPage.class);
                 i.putExtra("username", username.getText().toString());
                 i.putExtra("pillar", pillar.getText().toString());
                 i.putExtra("term", term.getText().toString());
                 i.putExtra("hostel block", hostelBlock.getText().toString());
                 i.putExtra("hostel resident", hostelResident.getText().toString());
                 startActivity(i);
             }
         });
         return rootView;
    }
}