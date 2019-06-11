package hr.rma.sl.itickets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;

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
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.options:
                Log.i("Menu Item selected", "aaaaaaa");
                Toast.makeText(this, "Stisnuto options", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.about:
                Log.i("Menu Item selected", "ddddddd");
                Toast.makeText(this, "Stisnuto about", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toMovies(View view){
        if(isNetworkAvailable() == true){
            Intent moviesIntent = new Intent(this, MoviesActivity.class);
            startActivity(moviesIntent);
        } else{
            alertDialogMessage();
        }

    }

    public void onRegister(View view){
        if(isNetworkAvailable() == true){
            Intent register = new Intent(this, RegisterActivity.class);
            startActivity(register);
        } else{
            alertDialogMessage();
        }
    }

    public void onLogin(View view){
        if(isNetworkAvailable() == true){
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        } else{
            alertDialogMessage();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void alertDialogMessage(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        builder.setMessage(R.string.need_internet)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }, 700);
    }
}
