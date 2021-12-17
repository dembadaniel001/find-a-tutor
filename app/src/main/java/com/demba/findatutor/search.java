package com.demba.findatutor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.demba.findatutor.users.users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class search extends Fragment {
    private RecyclerView searchList;
    SearchView find;
    EditText edtsearch;
    ImageButton btnSearch;
    private DatabaseReference userreference;
    private FirebaseAuth mAuth;
    private static final int REQUEST_CALL = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment search.
     */
    // TODO: Rename and change types and number of parameters
    public static search newInstance(String param1, String param2) {
        search fragment = new search();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        edtsearch = (EditText) view.findViewById(R.id.edtsearch);
        btnSearch = (ImageButton) view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchView = edtsearch.getText().toString();
                fUserSearch(searchView);
            }
        });
        searchList = (RecyclerView) view.findViewById(R.id.searchList);
        searchList.setHasFixedSize(true);
        searchList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        userreference = FirebaseDatabase.getInstance().getReference().child("users");

        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
            }
        }
    }
    public void fUserSearch(String searchView) {
        Query userSearchQuery = userreference
                .orderByChild("name")
                .startAt(searchView).endAt(searchView + "\uf8ff");
                FirebaseRecyclerOptions<users> options =new FirebaseRecyclerOptions.Builder<users>()
                .setQuery(userSearchQuery,users.class)
                .build();
        FirebaseRecyclerAdapter<users, home.usersViewHolder> adapter =
                new FirebaseRecyclerAdapter<users, home.usersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final home.usersViewHolder usersViewHolder,
                                                    int i, @NonNull final users users) {
//                        usersViewHolder.profileimage.setImageResource(users.getProfile());

                        usersViewHolder.name.setText(""+users.getName());
                        usersViewHolder.subject1.setText(""+users.getSubject1());
                        usersViewHolder.subject2.setText(""+users.getSubject2());
                        usersViewHolder.location.setText(""+users.getLocation());
                        usersViewHolder.call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String number = users.getPhone();
                                if (number.trim().length()>0){
                                    if (ContextCompat.checkSelfPermission(getActivity(),
                                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                                        ActivityCompat.requestPermissions(getActivity(),
                                                new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
                                    }else {
                                        String dial = "tel:" +number;
                                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                                    }

                                }else{
                                    Toast.makeText(getActivity(),"User number not available",
                                            Toast.LENGTH_LONG).show();
                                }

                            }


                        });
                    }



                    @NonNull
                    @Override
                    public home.usersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.search_list, parent, false);
                        home.usersViewHolder viewHolder = new home.usersViewHolder(v);
                        return viewHolder;
                    }
                };
        adapter.startListening();
        searchList.setAdapter(adapter);
    }



    public static class usersViewHolder extends RecyclerView.ViewHolder{
        TextView name,subject1,subject2,location;
        ImageView profileimage;
        ImageButton call,message;
        View view;
        public usersViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.name);
            subject1 = itemView.findViewById(R.id.subject1);
            subject2 = itemView.findViewById(R.id.subject2);
            location = itemView.findViewById(R.id.location);
            profileimage = itemView.findViewById(R.id.profileimage);

        }
    }
}