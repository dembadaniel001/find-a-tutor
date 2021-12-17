package com.demba.findatutor.normal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demba.findatutor.R;
import com.demba.findatutor.users.users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfileNormal extends AppCompatActivity {
    private DatabaseReference reference;
    FirebaseDatabase rootNode;
    FirebaseAuth auth;
    EditText nameG,genderG,subject1G,subject2G,locationG,phoneG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_normal);

        nameG = findViewById(R.id.name);
        genderG= findViewById(R.id.gender);
//        subject1G = findViewById(R.id.subject1);
//        subject2G = findViewById(R.id.subject2);
        locationG = findViewById(R.id.location);
        phoneG = findViewById(R.id.phone);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameS = snapshot.child("name").getValue().toString();
                String genderS = snapshot.child("gender").getValue().toString();
                String subject1S = snapshot.child("subject1").getValue().toString();
                String subject2S = snapshot.child("subject2").getValue().toString();
                String locationS = snapshot.child("location").getValue().toString();
                String phoneS = snapshot.child("phone").getValue().toString();

                nameG.setText(""+nameS);
                genderG.setText(""+genderS);
                subject1G.setText(""+subject1S);
                subject2G.setText(""+subject2S);
                locationG.setText(""+locationS);
                phoneG.setText(""+phoneS);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Button normalProfile= findViewById(R.id.normalProfile);
//        Button teacherProfile= findViewById(R.id.teacherProfile);

        normalProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String userId,name,age,gender,subject1,subject2,location,role,profile,phone;
            userId = auth.getCurrentUser().getUid();
            name=nameG.getText().toString();
            age="";
            gender=genderG.getText().toString();
            subject1="";
            subject2="";
            location=locationG.getText().toString();
            role="normal";
            profile="";
            phone=phoneG.getText().toString();
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                users usercreate = new users(userId,name,age,gender,subject1,subject2,location,role,profile,phone);
                reference.child(userId).setValue(usercreate);
                Toast.makeText(UpdateProfileNormal.this,"Profile updated successfully",Toast.LENGTH_LONG).show();
            }
        });
//        teacherProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String userId,name,age,gender,subject1,subject2,location,role,profile,phone;
//                userId = auth.getCurrentUser().getUid();
//                name=nameG.getText().toString();
//                age="";
//                gender=genderG.getText().toString();
//                subject1=subject1G.getText().toString();
//                subject2=subject2G.getText().toString();
//                location=locationG.getText().toString();
//                role="teacher";
//                profile="";
//                phone=phoneG.getText().toString();
//                rootNode = FirebaseDatabase.getInstance();
//                reference = rootNode.getReference("users");
//                users usercreate = new users(userId,name,age,gender,subject1,subject2,location,role,profile,phone);
//                reference.child(userId).setValue(usercreate);
//
//                Toast.makeText(UpdateProfileNormal.this,"Profile updated successfully",Toast.LENGTH_LONG).show();
//
//            }
//        });



    }
}