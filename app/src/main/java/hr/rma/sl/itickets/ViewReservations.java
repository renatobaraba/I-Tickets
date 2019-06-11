package hr.rma.sl.itickets;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Darko on 23-May-18.
 */

public class ViewReservations extends AppCompatActivity {
    private static final String TAG = "ViewReservations";

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private Firebase mRootRef;
    private String userID;

    Switch switcher;

    SharedPreferences prefs;

    private ReservationAdapter reservationAdapter;
    private ListView mListView;
    private ProgressDialog progressDialog;
    private DrawerLayout mDrawerLayout;
    private ArrayList<UserInformation> array;
    private Boolean night;

    protected void onCreate(@Nullable Bundle savedInstanceState){
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_reservations_layout);

        mListView = findViewById(R.id.listview);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("itickets-6ee08");
        mRootRef = new Firebase("https://itickets-6ee08.firebaseio.com/");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        switcher = findViewById(R.id.switcher);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        array = new ArrayList<>();

        NavigationView navigationView = findViewById(R.id.nav_drawer);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.userEmail);
        navUsername.setText(user.getEmail());
        progressDialog = new ProgressDialog(this);
        mDrawerLayout = findViewById(R.id.reservation_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.reservations);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator =
                    VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        navigationView.setCheckedItem(R.id.reservations);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch(menuItem.getItemId()) {
                            case R.id.menuLogout:

                                progressDialog.setMessage("Logging out. Please Wait...");
                                progressDialog.show();

                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                progressDialog.hide();
                                                FirebaseAuth.getInstance().signOut();
                                                finish();

                                                Intent logout = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(logout);
                                            }
                                        }, 1000);

                                // Closing drawer on item click
                                mDrawerLayout.closeDrawers();

                                // Set item in checked state
                                menuItem.setChecked(true);
                                break;
                            case R.id.reservations:

                                // Closing drawer on item click
                                mDrawerLayout.closeDrawers();

                                break;
                            case R.id.home:
                                Intent homeIntent = new Intent(getApplicationContext(), MoviesActivity.class);
                                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(homeIntent);

                                // Closing drawer on item click
                                mDrawerLayout.closeDrawers();

                                // Set item in checked state
                                menuItem.setChecked(true);
                                finish();
                                break;
                            case R.id.map:
                                updateGPS();
                                // Closing drawer on item click
                                mDrawerLayout.closeDrawers();
                                break;
                        }

                        return true;
                    }
                });

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

        /*mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                array.remove(i);
                reservationAdapter.notifyDataSetChanged();

                return true;
            }
        });*/

    }

    private void updateGPS(){
        Uri gmmIntentUri = Uri.parse("google.navigation:q=CineStar+Rijeka,+Rijeka+Croatia&avoid=tf");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void showData(DataSnapshot dataSnapshot) {
        final ArrayList<UserInformation> array = new ArrayList<>();

        for(DataSnapshot ds: dataSnapshot.child("Users").getChildren()){
            for(DataSnapshot data: ds.getChildren()){
                for(DataSnapshot innerdata: data.getChildren()){
                    Log.i("ds", ds.toString());
                    Log.i("ds", ds.getKey().toString());
                    Log.i("data", data.toString());
                    Log.i("data", data.getKey().toString());
                    Log.i("innerdata", innerdata.toString());
                    Log.i("innerdata", innerdata.getKey().toString());
                    if(data.getKey().toString().equals(userID)){
                        final UserInformation uInfo = new UserInformation();
                        uInfo.setDay(innerdata.getValue(UserInformation.class).getDay());
                        uInfo.setHour(innerdata.getValue(UserInformation.class).getHour());
                        uInfo.setMovieName(innerdata.getKey().toString());
                        uInfo.setNumberOfTickets(innerdata.getValue(UserInformation.class).getNumberOfTickets());
                        uInfo.setHall(innerdata.getValue(UserInformation.class).getHall());
                        uInfo.setSeats(innerdata.getValue(UserInformation.class).getSeats());
                        array.add(new UserInformation(uInfo.getDay(), uInfo.getHour(), uInfo.getMovieName(), uInfo.getNumberOfTickets(),
                                uInfo.getHall(), uInfo.getSeats()));
                    }
                }
            }
        }

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                AlertDialog.Builder adb=new AlertDialog.Builder(ViewReservations.this);
                adb.setTitle(R.string.delete);
                adb.setMessage(getString(R.string.delete_information) + array.get(i).getMovieName() + "?");
                adb.setPositiveButton(R.string.delete_yes, new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        myRef.child("Users").child(array.get(i).getHall()).child(userID).child(array.get(i).getMovieName()).removeValue();
                        array.remove(i);
                        reservationAdapter.notifyDataSetChanged();
                    }});
                adb.setNegativeButton(R.string.delete_cancel, null);
                adb.show();

                return true;
            }
        });

        reservationAdapter = new ReservationAdapter(this,array);
        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
        mListView.setAdapter(reservationAdapter);
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

}
