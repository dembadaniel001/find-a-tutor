package com.demba.findatutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.demba.findatutor.users.users;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private int RC_SIGN_IN = 50;
    private FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String userId = "";String name="";
    String age="";String gender="";String subject1="";String subject2="";String location="";
    String role="normal";String profile="";String phone="";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:{
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                task.isSuccessful();{
                                    Toast.makeText(MainActivity.this,"Logout success",
                                            Toast.LENGTH_LONG).show();
                                    auth();
                                }
                            }
                        });
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //    Button logout = findViewById(R.id.logout);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
           Query q = FirebaseDatabase.getInstance().getReference().child("users").child("userId")
                    .equalTo(currentUser.getUid());
               userId = mAuth.getCurrentUser().getUid();
               name = mAuth.getCurrentUser().getDisplayName();

               rootNode = FirebaseDatabase.getInstance();
               reference = rootNode.getReference("users");
               users usercreate = new users(userId,name,age,gender,subject1,subject2,location,role,profile,phone);
               reference.child(userId).setValue(usercreate);



        }else{

            auth();
        }

        //       update user details



//        Bottom Nav
            bottomNavigationView = findViewById(R.id.bottomNav);
            bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new home()).commit();

//          userId= mAuth.getCurrentUser().getUid();
//          name = mAuth.getCurrentUser().getDisplayName();







    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment=null;
            switch (item.getItemId()){
                case R.id.profile:
                    fragment=new profile();
                    break;
                case R.id.home:
                    fragment=new home();
                    break;
                case R.id.messages:
                    fragment=new messages();
                    break;
                case R.id.search:
                    fragment=new search();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            return true;
        }
    };
    public void auth(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);



    }
}