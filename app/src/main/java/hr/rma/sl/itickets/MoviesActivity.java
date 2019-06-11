package hr.rma.sl.itickets;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.internal.ParcelableSparseArray;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MoviesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView listView;
    DrawerLayout mDrawerLayout;
    SwipeRefreshLayout mySwipeRefreshLayout;
    EditText inputSearch;
    Switch switcher;
    SharedPreferences prefs;

    MovieDetails movieDetails;
    ArrayList<MovieDetails> movieList;
    MovieArrayAdapter movieArrayAdapter;

    int currentPage = 2;

    public String language = Locale.getDefault().getLanguage(); //Ovdje se dobiva sistemski jezik, npr en, de, fr, hr itd.
    public String country = Locale.getDefault().getCountry();  //Ovdje se dobiva država za taj jezik, npr US, UK, DE, AT itd.

    private FirebaseAuth firebaseAuth;
    private Firebase mRootRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(prefs.getBoolean("night",true)){
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        listView = findViewById(R.id.list);
        inputSearch = findViewById(R.id.inputSearch);
        switcher = findViewById(R.id.switcher);


        progressDialog = new ProgressDialog(this);

        //mRootRef = new Firebase("https://itickets-6ee08.firebaseio.com/");

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //getting current user
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        Intent endReservationIntent = getIntent();
        final String movieTime = endReservationIntent.getStringExtra("movie_time");

        movieDetails = new MovieDetails();
        movieList = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(MoviesActivity.this,R.layout.movie_list,movieList);
        listView.setAdapter(movieArrayAdapter);

        // Create Navigation drawer and inlfate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.userEmail);
        navUsername.setText(user.getEmail());

        Menu menu =  navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.dayNight);

        View actionToggleView = MenuItemCompat.getActionView(menuItem);
        switcher = actionToggleView.findViewById(R.id.switcher);

        if(prefs.getBoolean("night",true))
            switcher.setChecked(true);

        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                prefs.getBoolean("night", isChecked);
                if(isChecked){
                    prefs.edit().putBoolean("night", true).apply();
                    getDelegate().setLocalNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES);
                    //recreate();
                }
                else{
                    prefs.edit().putBoolean("night", false).apply();
                    getDelegate().setLocalNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO);
                    //recreate();
                }
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator =
                    VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        navigationView.setCheckedItem(R.id.home);
        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch(menuItem.getItemId()) {
                            case R.id.menuLogout:

                                progressDialog.setMessage(getString(R.string.logout_navbar));
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

                                Intent reservationsIntent = new Intent(getApplicationContext(), ViewReservations.class);
                                startActivity(reservationsIntent);

                                // Closing drawer on item click
                                mDrawerLayout.closeDrawers();

                                // Set item in checked state
                                menuItem.setChecked(true);
                                break;
                            case R.id.home:
                                // Closing drawer on item click
                                mDrawerLayout.closeDrawers();

                                break;
                            case R.id.dayNight:
                                switcher.setChecked(!switcher.isChecked());
                                recreate();
                                // Closing drawer on item click
                                //mDrawerLayout.closeDrawers();

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

        listView.setOnItemClickListener(this);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            private int visibleThreshold = 5;
            private int previousTotal = 0;
            private boolean loading = true;

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    Log.i("a", "listview ended");
                    fetchMovies();
                    Log.i("a", "fetching new movies");
                    loading = true;
                }
            }
        });

        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
                if (count < before) {
                    // We're deleting char so we need to reset the adapter data
                    movieArrayAdapter.resetData();
                }
                movieArrayAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        new CheckConnectionStatus().execute("https://api.themoviedb.org/3/movie/now_playing?api_key=ca154282d20a40b5458c55ae5dbf8bf2&language=en-GB&page=1");
        //Executing AsyncTask, passing api as parameter
        Log.i("ulaz", "prvi ulaz");

    }



    private void updateGPS(){
        Uri gmmIntentUri = Uri.parse("google.navigation:q=CineStar+Rijeka,+Rijeka+Croatia&avoid=tf");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void fetchMovies(){
        Log.i("a", "page: " + currentPage);
        if(currentPage < 4){
            new CheckConnectionStatus().execute("https://api.themoviedb.org/3/movie/now_playing?api_key=ca154282d20a40b5458c55ae5dbf8bf2&language="+language+"-"+country+"&page=" + currentPage);
            currentPage++;
        }
    }

    private void refreshPage(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        /*currentPage = 2;
        movieList = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(MoviesActivity.this,R.layout.movie_list,movieList);
        listView.setAdapter(movieArrayAdapter);
        new CheckConnectionStatus().execute("https://api.themoviedb.org/3/movie/upcoming?api_key=ca154282d20a40b5458c55ae5dbf8bf2&language="+language+"-"+country+"&page=1");
        Log.i("refresh", "refresh");*/
        mySwipeRefreshLayout.setRefreshing(false);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        dialog.hide();
                    }
                }, 500);
        Intent intent = new Intent(this, MoviesActivity.class);
        startActivity(intent);
        finish();
    }

    //This method is invoked whenever we click over any item of list
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Moving to MovieDetailsActivity from MoviesActivity. Sending the MovieDetails object from one activity to another activity
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("MOVIE_DETAILS", (MovieDetails)parent.getItemAtPosition(position));
        ArrayList<String> movieTitles = new ArrayList<>();
        for(int i=0; i < movieList.size(); i++){
            movieTitles.add(movieList.get(i).getOriginal_title());
        }
        intent.putExtra("movie_list", movieTitles);
        startActivity(intent);
    }

    //AsyncTask to process network request
    class CheckConnectionStatus extends AsyncTask<String, Void, String> {
        //This method will run on UIThread and it will execute before doInBackground
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        //This method will run on background thread and after completion it will return result to onPostExecute
        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
//As we are passing just one parameter to AsyncTask, so used param[0] to get value at 0th position that is URL
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//Getting inputstream from connection, that is response which we got from server
                InputStream inputStream = urlConnection.getInputStream();
//Reading the response
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s = bufferedReader.readLine();
                bufferedReader.close();
//Returning the response message to onPostExecute method
                return s;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage(), e);
            }
            return null;
        }
        //This method runs on UIThread and it will execute when doInBackground is completed
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject = null;
            try {

//Parent JSON Object. Json object start at { and end at }
                jsonObject = new JSONObject(s);

//JSON Array of parent JSON object. Json array starts from [ and end at ]
                JSONArray jsonArray = jsonObject.getJSONArray("results");

//Reading JSON object inside Json array
                for (int i =0; i<jsonArray.length();i++)
                {
//Reading JSON object at 'i'th position of JSON Array
                    JSONObject object = jsonArray.getJSONObject(i);
                    MovieDetails movieDetails = new MovieDetails();
                    movieDetails.setOriginal_title(object.getString("title"));
                    movieDetails.setVote_average(object.getDouble("vote_average"));
                    movieDetails.setOverview(object.getString("overview"));
                    movieDetails.setRelease_date(object.getString("release_date"));
                    movieDetails.setPoster_path(object.getString("poster_path"));
                    movieList.add(movieDetails);
                }

                //Creating custom array adapter instance and setting context of MoviesActivity, List item layout file and movie list.
                //Setting adapter to listview
                movieArrayAdapter.notifyDataSetChanged();
                //listView.setAdapter(movieArrayAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

