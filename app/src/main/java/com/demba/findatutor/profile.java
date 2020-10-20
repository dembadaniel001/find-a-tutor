package com.demba.findatutor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {

    FirebaseDatabase rootNode;
    private DatabaseReference reference;
    FirebaseAuth auth;
    ImageView profilepic;
    Button updateProfile;
    TextView username,gender,subject1,subject2,location,phone;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
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
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        username = v.findViewById(R.id.username);
        username = v.findViewById(R.id.username);
        gender= v.findViewById(R.id.gender);
        subject1 = v.findViewById(R.id.subject1);
        subject2 = v.findViewById(R.id.subject2);
        location = v.findViewById(R.id.location);
        phone = v.findViewById(R.id.phone);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            String name = snapshot.child("name").getValue().toString();
            String genderS = snapshot.child("gender").getValue().toString();
            String subject1S = snapshot.child("subject1").getValue().toString();
            String subject2S = snapshot.child("subject2").getValue().toString();
            String locationS = snapshot.child("location").getValue().toString();
            String phoneS = snapshot.child("phone").getValue().toString();

            username.setText(""+name);
            gender.setText(""+genderS);
            subject1.setText(""+subject1S);
            subject2.setText(""+subject2S);
            location.setText(""+locationS);
            phone.setText(""+phoneS);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        updateProfile= v.findViewById(R.id.updateProfile);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),UpdateProfile.class));
            }
        });
        return v;

    }


}