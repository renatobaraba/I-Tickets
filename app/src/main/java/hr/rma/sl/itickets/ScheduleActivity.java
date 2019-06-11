package hr.rma.sl.itickets;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private Firebase mRootRef;
    private String userID;

    private FirebaseUser user;

    String title;
    String hall;
    Spinner spinDay;
    Spinner spinHour;
    Spinner spinHalls;
    ArrayList<String> daysList;
    ArrayList<String> hoursList;
    ArrayList<String> hallsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent movieIntent = getIntent();
        title = movieIntent.getStringExtra("movie_name");

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("itickets-6ee08");
        //mRootRef = new Firebase("https://itickets-6ee08.firebaseio.com/");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        //this line shows back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        daysList = new ArrayList<String>();
        hoursList = new ArrayList<String>();
        hallsList = new ArrayList<String>();
        spinDay = findViewById(R.id.daySpinner);
        spinHour = findViewById(R.id.hourSpinner);
        spinHalls = findViewById(R.id.hallSpinner);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                /*if(user != null){
                    toastMessage("Already signed in with: " + user.getEmail());
                }else{
                    toastMessage("Signed out: ");
                }*/
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        spinHalls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinHalls.getSelectedItemPosition() == 0){
                    if(spinDay != null){
                        spinDay.setSelection(0);
                    }
                    if(spinHour != null){
                        spinHour.setSelection(0);
                    }
                }
                if(spinHalls.getSelectedItemPosition() == 1){
                    if(spinDay != null){
                        spinDay.setSelection(1);
                    }
                    if(spinHour != null){
                        spinHour.setSelection(1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinDay.getSelectedItemPosition() == 0){
                    if(spinHalls != null){
                        spinHalls.setSelection(0);
                    }
                    if(spinHour != null){
                        spinHour.setSelection(0);
                    }
                }
                if(spinDay.getSelectedItemPosition() == 1){
                    if(spinHalls != null){
                        spinHalls.setSelection(1);
                    }
                    if(spinHour != null){
                        spinHour.setSelection(1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinHour.getSelectedItemPosition() == 0){
                    if(spinDay != null){
                        spinDay.setSelection(0);
                    }
                    if(spinHalls != null){
                        spinHalls.setSelection(0);
                    }
                }
                if(spinHour.getSelectedItemPosition() == 1){
                    if(spinDay != null){
                        spinDay.setSelection(1);
                    }
                    if(spinHalls != null){
                        spinHalls.setSelection(1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds: dataSnapshot.child("Schedule").getChildren()){
            for(DataSnapshot data: ds.getChildren()){
                if(data.getKey().toString().equals(title)){
                    final MovieReservations movieReservations = new MovieReservations();
                    movieReservations.setStartDate(data.getValue(MovieReservations.class).getStartDate());
                    movieReservations.setStartHour(data.getValue(MovieReservations.class).getStartHour());
                    movieReservations.setHall(data.getValue(MovieReservations.class).getHall());

                    hall = movieReservations.getHall();

                    daysList.add(movieReservations.getStartDate());
                    ArrayAdapter<String> adapterDays = new ArrayAdapter<String>(this, R.layout.spinner_item, daysList);
                    spinDay.setAdapter(adapterDays);

                    hoursList.add(movieReservations.getStartHour());
                    ArrayAdapter<String> adapterHours = new ArrayAdapter<String>(this, R.layout.spinner_item, hoursList);
                    spinHour.setAdapter(adapterHours);

                    hallsList.add(movieReservations.getHall());
                    ArrayAdapter<String> adapterHalls = new ArrayAdapter<String>(this, R.layout.spinner_item, hallsList);
                    spinHalls.setAdapter(adapterHalls);
                }
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener != null){
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void chooseSeats(View view) {
        String movieHall = spinHalls.getItemAtPosition(spinHalls.getSelectedItemPosition()).toString();
        if (movieHall.equals("Hall 1")) {
            Intent goToSeatsIntent = new Intent(this, Hall_1_Activity.class);
            String movieDay = spinDay.getItemAtPosition(spinDay.getSelectedItemPosition()).toString();
            String movieHour = spinHour.getItemAtPosition(spinHour.getSelectedItemPosition()).toString();
            goToSeatsIntent.putExtra("movie_name", title);
            goToSeatsIntent.putExtra("movie_day", movieDay);
            goToSeatsIntent.putExtra("movie_hour", movieHour);
            goToSeatsIntent.putExtra("movie_hall", movieHall);
            startActivity(goToSeatsIntent);
            finish();
        } else {
            Intent goToSeatsIntent = new Intent(this, Hall_2_Activity.class);
            String movieDay = spinDay.getItemAtPosition(spinDay.getSelectedItemPosition()).toString();
            String movieHour = spinHour.getItemAtPosition(spinHour.getSelectedItemPosition()).toString();
            goToSeatsIntent.putExtra("movie_name", title);
            goToSeatsIntent.putExtra("movie_day", movieDay);
            goToSeatsIntent.putExtra("movie_hour", movieHour);
            goToSeatsIntent.putExtra("movie_hall", movieHall);
            startActivity(goToSeatsIntent);
            finish();
        }
    }
}
