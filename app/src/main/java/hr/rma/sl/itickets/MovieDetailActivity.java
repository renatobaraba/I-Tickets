package hr.rma.sl.itickets;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView image;
    private TextView title, date, rating, overview;
    MovieDetails details;
    ArrayList<String> movieList;
    ArrayList<String> daysList;
    ArrayList<String> hoursList;
    String[] days;
    String[] hours;

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private Firebase mRootRef;
    private DatabaseReference myRef;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent movieIntent = getIntent();
        movieList = movieIntent.getStringArrayListExtra("movie_list");

        daysList = new ArrayList<String>();
        days = new String[] { "Monday", "Tuesday", "Wednesday", "Friday", "Saturday", "Sunday"};

        hoursList = new ArrayList<String>();
        hours = new String[] { "16:00", "17:00", "18:00", "19:00", "19:30", "20:00", "20:30", "21:00", "22:00", "23:00"};

        for (int j = 0; j < days.length; j++) {
            daysList.add(days[j]);
        }

        for (int k = 0; k < hours.length; k++) {
            hoursList.add(hours[k]);
        }

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //this line shows back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        image = (ImageView) findViewById(R.id.poster);
        title = (TextView) findViewById(R.id.title);
        date = (TextView)findViewById(R.id.date);
        rating = (TextView)findViewById(R.id.rating);
        overview = (TextView) findViewById(R.id.overview);

        mRootRef = new Firebase("https://itickets-6ee08.firebaseio.com/");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("itickets-6ee08");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Getting the value from bundle, means the value which we had during switching to this activity from main activity
        details = (MovieDetails) getIntent().getExtras().getSerializable("MOVIE_DETAILS");

        if(details !=null)
        {
            //Showing image from the movie db api into imageview using glide library
            Glide.with(this).load("https://image.tmdb.org/t/p/w500/"+ details.getPoster_path()).into(image);
            title.setText(details.getOriginal_title());
            date.setText(details.getRelease_date());
            rating.setText(Double.toString(details.getVote_average()));
            overview.setText(details.getOverview());
            collapsingToolbar.setTitle(details.getOriginal_title());
        }
    }

    public void onBuyClick(View view){
        Intent intent = new Intent(this, ScheduleActivity.class);
        intent.putExtra("movie_name", details.getOriginal_title());
        startActivity(intent);
        finish();
    }

    private void showData(DataSnapshot dataSnapshot) {
        if(movieList.size() == 20) {
            for (int i = 0; i < movieList.size(); i++) {
                if (!movieList.get(i).contains(".") && !movieList.get(i).contains("#") && !movieList.get(i).contains("$") &&
                        !movieList.get(i).contains("[") && !movieList.get(i).contains("]")) {
                    if (!dataSnapshot.child("Schedule").child("Hall 1").hasChild(movieList.get(i))) {
                        Firebase hallOneRef = mRootRef.child("itickets-6ee08").child("Schedule").child("Hall 1").child(movieList.get(i));
                        Firebase hall2Ref = mRootRef.child("itickets-6ee08").child("Schedule").child("Hall 2").child(movieList.get(i));

                        hallOneRef.child("Movie").setValue(movieList.get(i));
                        hallOneRef.child("Hall").setValue("Hall 1");
                        hall2Ref.child("Movie").setValue(movieList.get(i));
                        hall2Ref.child("Hall").setValue("Hall 2");

                        // database initialization with movie time
                        if (i < 10) {
                            hallOneRef.child("StartDate").setValue(daysList.get(0));
                            hallOneRef.child("StartHour").setValue(hoursList.get(i));
                            hall2Ref.child("StartDate").setValue(daysList.get(5));
                            hall2Ref.child("StartHour").setValue(hoursList.get(9 - i));
                        } else {
                            hallOneRef.child("StartDate").setValue(daysList.get(1));
                            hallOneRef.child("StartHour").setValue(hoursList.get(i-10));
                            hall2Ref.child("StartDate").setValue(daysList.get(4));
                            hall2Ref.child("StartHour").setValue(hoursList.get(19 - i));
                        }
                    }
                }
            }
        }

        if(movieList.size() == 40) {
            for (int i = 0; i < movieList.size(); i++) {
                if(!movieList.get(i).contains(".") && !movieList.get(i).contains("#") && !movieList.get(i).contains("$") &&
                        !movieList.get(i).contains("[") && !movieList.get(i).contains("]")){
                    if (!dataSnapshot.child("Schedule").child("Hall 1").hasChild(movieList.get(i))) {
                        Firebase hallOneRef = mRootRef.child("itickets-6ee08").child("Schedule").child("Hall 1").child(movieList.get(i));
                        Firebase hall2Ref = mRootRef.child("itickets-6ee08").child("Schedule").child("Hall 2").child(movieList.get(i));

                        hallOneRef.child("Movie").setValue(movieList.get(i));
                        hallOneRef.child("Hall").setValue("Hall 1");
                        hall2Ref.child("Movie").setValue(movieList.get(i));
                        hall2Ref.child("Hall").setValue("Hall 2");

                        // database initialization with movie time
                        if (i < 10) {
                            hallOneRef.child("StartDate").setValue(daysList.get(0));
                            hallOneRef.child("StartHour").setValue(hoursList.get(i));
                            hall2Ref.child("StartDate").setValue(daysList.get(5));
                            hall2Ref.child("StartHour").setValue(hoursList.get(9 - i));
                        } else if (i >= 10 && i < 20) {
                            hallOneRef.child("StartDate").setValue(daysList.get(1));
                            hallOneRef.child("StartHour").setValue(hoursList.get(i-10));
                            hall2Ref.child("StartDate").setValue(daysList.get(4));
                            hall2Ref.child("StartHour").setValue(hoursList.get(19 - i));
                        } else if(i >= 20 && i < 30){
                            hallOneRef.child("StartDate").setValue(daysList.get(2));
                            hallOneRef.child("StartHour").setValue(hoursList.get(i-20));
                            hall2Ref.child("StartDate").setValue(daysList.get(3));
                            hall2Ref.child("StartHour").setValue(hoursList.get(29-i));
                        } else if(i >= 30 && i < 40){
                            hallOneRef.child("StartDate").setValue(daysList.get(3));
                            hallOneRef.child("StartHour").setValue(hoursList.get(i-30));
                            hall2Ref.child("StartDate").setValue(daysList.get(2));
                            hall2Ref.child("StartHour").setValue(hoursList.get(39-i));
                        }
                    }
                }
            }
        }

        if(movieList.size() == 60) {
            for (int i = 0; i < movieList.size(); i++) {
                if (!movieList.get(i).contains(".") && !movieList.get(i).contains("#") && !movieList.get(i).contains("$") &&
                        !movieList.get(i).contains("[") && !movieList.get(i).contains("]")) {
                    if (!dataSnapshot.child("Schedule").child("Hall 1").hasChild(movieList.get(i))) {
                        Firebase hallOneRef = mRootRef.child("itickets-6ee08").child("Schedule").child("Hall 1").child(movieList.get(i));
                        Firebase hall2Ref = mRootRef.child("itickets-6ee08").child("Schedule").child("Hall 2").child(movieList.get(i));

                        hallOneRef.child("Movie").setValue(movieList.get(i));
                        hallOneRef.child("Hall").setValue("Hall 1");
                        hall2Ref.child("Movie").setValue(movieList.get(i));
                        hall2Ref.child("Hall").setValue("Hall 2");

                        // database initialization with movie time
                        if (i < 10) {
                            hallOneRef.child("StartDate").setValue(daysList.get(0));
                            hallOneRef.child("StartHour").setValue(hoursList.get(i));
                            hall2Ref.child("StartDate").setValue(daysList.get(5));
                            hall2Ref.child("StartHour").setValue(hoursList.get(9 - i));
                        } else if (i >= 10 && i < 20) {
                            hallOneRef.child("StartDate").setValue(daysList.get(1));
                            hallOneRef.child("StartHour").setValue(hoursList.get(i-10));
                            hall2Ref.child("StartDate").setValue(daysList.get(4));
                            hall2Ref.child("StartHour").setValue(hoursList.get(19 - i));
                        } else if(i >= 20 && i < 30){
                            hallOneRef.child("StartDate").setValue(daysList.get(2));
                            hallOneRef.child("StartHour").setValue(hoursList.get(i-20));
                            hall2Ref.child("StartDate").setValue(daysList.get(3));
                            hall2Ref.child("StartHour").setValue(hoursList.get(29-i));
                        } else if(i >= 30 && i < 40){
                            hallOneRef.child("StartDate").setValue(daysList.get(3));
                            hallOneRef.child("StartHour").setValue(hoursList.get(i-30));
                            hall2Ref.child("StartDate").setValue(daysList.get(2));
                            hall2Ref.child("StartHour").setValue(hoursList.get(39-i));
                        } else if(i >= 40 && i < 50){
                            hallOneRef.child("StartDate").setValue(daysList.get(4));
                            hallOneRef.child("StartHour").setValue(hoursList.get(i-40));
                            hall2Ref.child("StartDate").setValue(daysList.get(1));
                            hall2Ref.child("StartHour").setValue(hoursList.get(49-i));
                        } else if(i >= 50 && i < 60){
                            hallOneRef.child("StartDate").setValue(daysList.get(5));
                            hallOneRef.child("StartHour").setValue(hoursList.get(i-50));
                            hall2Ref.child("StartDate").setValue(daysList.get(0));
                            hall2Ref.child("StartHour").setValue(hoursList.get(59-i));
                        }
                    }
                }
            }
        }
    }
}
