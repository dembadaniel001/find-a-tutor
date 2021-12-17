package com.demba.findatutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.demba.findatutor.teachers.RequestTeacher;
import com.demba.findatutor.teachers.Terms;
import com.demba.findatutor.teachers.ViewRequests;
import com.demba.findatutor.users.users;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    private int RC_SIGN_IN = 50;
    private FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String role1,role2;
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                userId = user.getUid();

                reference = FirebaseDatabase.getInstance().getReference().child("users")
                        .child(userId);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("userId").exists()){
                    Toast.makeText(MainActivity.this,"Welcome back",Toast.LENGTH_LONG).show();
                        }   else {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            userId = user.getUid();
                            name = user.getDisplayName();
                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("users");
                            users usercreate = new users(userId,name,age,gender,subject1,subject2,
                                    location,role,profile,phone);
                            reference.child(userId).setValue(usercreate);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } else {
                // Sign in failed. If response is null the user canceled the
               Toast.makeText(this,"The sign in failed",Toast.LENGTH_LONG).show();
            }
        }
    }

    //    Button logout = findViewById(R.id.logout);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null){
            auth();
        }

        //       update user details

                 /*----------------Navigation drawer------------------*/

                drawer = findViewById(R.id.drawerLayout);
                navigationView = findViewById(R.id.nav_view);
                toolbar = findViewById(R.id.toolbar);

                setSupportActionBar(toolbar);
                navigationView.bringToFront();

                navigationView.setNavigationItemSelectedListener(this);

                navigationView.setCheckedItem(R.id.nav_home);
                /* Hide or show items in navigation drawer*/
            role1 = "normal";
            role2 = "teacher";
            userId = currentUser.getUid();
             reference = FirebaseDatabase.getInstance().getReference().child("users")
                     .child(userId);
             reference.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String role3 = snapshot.child("role").getValue().toString();
                    if (role3.equals(role1)){
                    Menu menu = navigationView.getMenu();
                    menu.findItem(R.id.nav_view_request).setVisible(false);

                    }else if (role3.equals(role2)){
                        Menu menu = navigationView.getMenu();
                        menu.findItem(R.id.nav_view_request).setVisible(false);
                        menu.findItem(R.id.nav_request).setVisible(false);
                    }else {
                        Menu menu = navigationView.getMenu();
                        menu.findItem(R.id.nav_request).setVisible(false);
                    }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             });


        /*----------------Navigation drawer------------------*/

//        Bottom Nav
            bottomNavigationView = findViewById(R.id.bottomNav);
            bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new home()).commit();
    }
    public void ClickMenu(View view){
        //open drawer
        openDrawer(drawer);
    }

    private static void openDrawer(DrawerLayout drawer) {
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);

        }else{
            super.onBackPressed();
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_profile:
                Toast.makeText( this,"Profile",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_request:
                Intent intent = new Intent(MainActivity.this, RequestTeacher.class);
                startActivity(intent);
                break;
            case R.id.nav_view_request:
                Intent intent1 = new Intent(MainActivity.this, ViewRequests.class);
                startActivity(intent1);
                break;
            case R.id.nav_logout:
                Toast.makeText( this,"Logout",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_terms:
                Intent intent2 = new Intent(MainActivity.this, Terms.class);
                startActivity(intent2);
                break;
            case R.id.nav_rate:
                Toast.makeText( this,"Rate us",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_share:
                Toast.makeText( this,"Share",Toast.LENGTH_LONG).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}