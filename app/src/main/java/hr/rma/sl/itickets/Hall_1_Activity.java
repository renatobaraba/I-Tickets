package hr.rma.sl.itickets;

import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.Arrays;
import java.util.List;

public class Hall_1_Activity extends AppCompatActivity {

    List<View> allElements;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private Firebase mRootRef;
    private String userID;

    private FirebaseUser user;
    private String title;
    private String movieDay;
    private String movieHour;
    private String hall;
    private int numberOfTickets;
    private int maxNumberTickets;
    private ArrayList<String> seats;
    private ArrayList<String> seatsList;
    private ArrayList<String> newList;
    private String seatsToBeReserved;

    ConstraintLayout room;

    Button confirmPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hall_1);

        Intent movieIntent = getIntent();
        title = movieIntent.getStringExtra("movie_name");
        movieDay = movieIntent.getStringExtra("movie_day");
        movieHour = movieIntent.getStringExtra("movie_hour");
        hall = movieIntent.getStringExtra("movie_hall");

        seats = new ArrayList<String>();
        seatsList = new ArrayList<String>();
        newList = new ArrayList<>();
        numberOfTickets = 0;
        maxNumberTickets = 7;
        seatsToBeReserved = "";

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //this line shows back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);



        confirmPurchase = findViewById(R.id.confirmation_btn);

        mRootRef = new Firebase("https://itickets-6ee08.firebaseio.com/");

        //initializing firebase authentication object
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("itickets-6ee08");
        user = mAuth.getCurrentUser();
        userID = user.getUid();

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
                seatsToBeReserved = showData(dataSnapshot);

                room = (ConstraintLayout) findViewById(R.id.basicLL);
                allElements = getAllChildrenBFS(room);
                for (int i = 0; i < allElements.size(); i++) {
                    final View iElement = allElements.get(i);

                    if(iElement instanceof ImageButton){
                        final int reservedSeats = iElement.getId();
                        switch (reservedSeats){
                            case R.id.rowA1_1:
                                if(seatsToBeReserved.contains("A1_1")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_two_seats_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                            }
                            break;
                            case R.id.rowA1_2:
                                if(seatsToBeReserved.contains("A1_2")){
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowA1_3:
                                if(seatsToBeReserved.contains("A1_3")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowA1_4:
                                if(seatsToBeReserved.contains("A1_4")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_two_seats_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowB1_1:
                                if(seatsToBeReserved.contains("B1_1")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowB1_2:
                                if(seatsToBeReserved.contains("B1_2")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowB1_3:
                                if(seatsToBeReserved.contains("B1_3")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowB1_4:
                                if(seatsToBeReserved.contains("B1_4")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowB1_5:
                                if(seatsToBeReserved.contains("B1_5")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowC1_1:
                                if(seatsToBeReserved.contains("C1_1")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowC1_2:
                                if(seatsToBeReserved.contains("C1_2")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowC1_3:
                                if(seatsToBeReserved.contains("C1_3")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowC1_4:
                                if(seatsToBeReserved.contains("C1_4")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowC1_5:
                                if(seatsToBeReserved.contains("C1_5")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowD1_1:
                                if(seatsToBeReserved.contains("D1_1")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowD1_2:
                                if(seatsToBeReserved.contains("D1_2")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowD1_3:
                                if(seatsToBeReserved.contains("D1_3")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowD1_4:
                                if(seatsToBeReserved.contains("D1_4")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowD1_5:
                                if(seatsToBeReserved.contains("D1_5")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowE1_1:
                                if(seatsToBeReserved.contains("E1_1")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowE1_2:
                                if(seatsToBeReserved.contains("E1_2")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowE1_3:
                                if(seatsToBeReserved.contains("E1_3")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowE1_4:
                                if(seatsToBeReserved.contains("E1_4")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowE1_5:
                                if(seatsToBeReserved.contains("E1_5")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowF1_1:
                                if(seatsToBeReserved.contains("F1_1")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowF1_2:
                                if(seatsToBeReserved.contains("F1_2")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowF1_3:
                                if(seatsToBeReserved.contains("F1_3")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowF1_4:
                                if(seatsToBeReserved.contains("F1_4")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                            case R.id.rowF1_5:
                                if(seatsToBeReserved.contains("F1_5")) {
                                    iElement.setBackgroundResource(R.drawable.image_button_chair_reserved);
                                    iElement.setFocusable(false);
                                    iElement.setEnabled(false);
                                    iElement.setClickable(false);
                                }
                                break;
                        }
                        iElement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final int currentSeat = iElement.getId();
                                switch (currentSeat){
                                    case R.id.rowA1_1:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("A1_1");
                                                }
                                                iElement.setSelected(false);
                                            }
                                            else{
                                                iElement.setSelected(!iElement.isSelected());
                                                if(iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("A1_1");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("A1_1");
                                                }
                                            }
                                        break;
                                    case R.id.rowA1_2:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("A1_2");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("A1_2");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("A1_2");
                                                }
                                            }

                                        break;
                                    case R.id.rowA1_3:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("A1_3");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("A1_3");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("A1_3");
                                                }
                                            }
                                        break;
                                    case R.id.rowA1_4:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("A1_4");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("A1_4");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("A1_4");
                                                }
                                            }
                                        break;
                                    case R.id.rowB1_1:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("B1_1");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("B1_1");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("B1_1");
                                                }
                                            }
                                        break;
                                    case R.id.rowB1_2:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("B1_2");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("B1_2");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("B1_2");
                                                }
                                            }
                                        break;
                                    case R.id.rowB1_3:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("B1_3");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("B1_3");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("B1_3");
                                                }
                                            }
                                        break;
                                    case R.id.rowB1_4:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("B1_4");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("B1_4");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("B1_4");
                                                }
                                            }
                                        break;
                                    case R.id.rowB1_5:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("B1_5");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("B1_5");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("B1_5");
                                                }
                                            }
                                        break;
                                    case R.id.rowC1_1:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("C1_1");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("C1_1");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("C1_1");
                                                }
                                            }
                                        break;
                                    case R.id.rowC1_2:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("C1_2");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("C1_2");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("C1_2");
                                                }
                                            }
                                        break;
                                    case R.id.rowC1_3:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("C1_3");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("C1_3");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("C1_3");
                                                }
                                            }
                                        break;
                                    case R.id.rowC1_4:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("C1_4");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("C1_4");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("C1_4");
                                                }
                                            }
                                        break;
                                    case R.id.rowC1_5:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("C1_5");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("C1_5");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("C1_5");
                                                }
                                            }
                                        break;
                                    case R.id.rowD1_1:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("D1_1");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("D1_1");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("D1_1");
                                                }
                                            }
                                        break;
                                    case R.id.rowD1_2:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("D1_2");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("D1_2");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("D1_2");
                                                }
                                            }
                                        break;
                                    case R.id.rowD1_3:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("D1_3");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("D1_3");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("D1_3");
                                                }
                                            }
                                        break;
                                    case R.id.rowD1_4:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("D1_4");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("D1_4");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("D1_4");
                                                }
                                            }
                                        break;
                                    case R.id.rowD1_5:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("D1_5");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("D1_5");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("D1_5");
                                                }
                                            }
                                        break;
                                    case R.id.rowE1_1:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("E1_1");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("E1_1");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("E1_1");
                                                }
                                            }
                                        break;
                                    case R.id.rowE1_2:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("E1_2");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("E1_2");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("E1_2");
                                                }
                                            }
                                        break;
                                    case R.id.rowE1_3:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("E1_3");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("E1_3");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("E1_3");
                                                }
                                            }
                                        break;
                                    case R.id.rowE1_4:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("E1_4");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("E1_4");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("E1_4");
                                                }
                                            }
                                        break;
                                    case R.id.rowE1_5:
                                            if(numberOfTickets > maxNumberTickets){
                                                if(iElement.isSelected()){
                                                    numberOfTickets--;
                                                    seats.remove("E1_5");
                                                }
                                                iElement.setSelected(false);
                                            }else {
                                                iElement.setSelected(!iElement.isSelected());
                                                if (iElement.isSelected()) {
                                                    numberOfTickets++;
                                                    seats.add("E1_5");
                                                }
                                                else{
                                                    numberOfTickets--;
                                                    seats.remove("E1_5");
                                                }
                                            }
                                        break;
                                    case R.id.rowF1_1:
                                        if(numberOfTickets > maxNumberTickets){
                                            if(iElement.isSelected()){
                                                numberOfTickets--;
                                                seats.remove("F1_1");
                                            }
                                            iElement.setSelected(false);
                                        }else {
                                            iElement.setSelected(!iElement.isSelected());
                                            if (iElement.isSelected()) {
                                                numberOfTickets++;
                                                seats.add("F1_1");
                                            }
                                            else{
                                                numberOfTickets--;
                                                seats.remove("F1_1");
                                            }
                                        }
                                        break;
                                    case R.id.rowF1_2:
                                        if(numberOfTickets > maxNumberTickets){
                                            if(iElement.isSelected()){
                                                numberOfTickets--;
                                                seats.remove("F1_2");
                                            }
                                            iElement.setSelected(false);
                                        }else {
                                            iElement.setSelected(!iElement.isSelected());
                                            if (iElement.isSelected()) {
                                                numberOfTickets++;
                                                seats.add("F1_2");
                                            }
                                            else{
                                                numberOfTickets--;
                                                seats.remove("F1_2");
                                            }
                                        }
                                        break;
                                    case R.id.rowF1_3:
                                        if(numberOfTickets > maxNumberTickets){
                                            if(iElement.isSelected()){
                                                numberOfTickets--;
                                                seats.remove("F1_3");
                                            }
                                            iElement.setSelected(false);
                                        }else {
                                            iElement.setSelected(!iElement.isSelected());
                                            if (iElement.isSelected()) {
                                                numberOfTickets++;
                                                seats.add("F1_3");
                                            }
                                            else{
                                                numberOfTickets--;
                                                seats.remove("F1_3");
                                            }
                                        }
                                        break;
                                    case R.id.rowF1_4:
                                        if(numberOfTickets > maxNumberTickets){
                                            if(iElement.isSelected()){
                                                numberOfTickets--;
                                                seats.remove("F1_4");
                                            }
                                            iElement.setSelected(false);
                                        }else {
                                            iElement.setSelected(!iElement.isSelected());
                                            if (iElement.isSelected()) {
                                                numberOfTickets++;
                                                seats.add("F1_4");
                                            }
                                            else{
                                                numberOfTickets--;
                                                seats.remove("F1_4");
                                            }
                                        }
                                        break;
                                    case R.id.rowF1_5:
                                        if(numberOfTickets > maxNumberTickets){
                                            if(iElement.isSelected()){
                                                numberOfTickets--;
                                                seats.remove("F1_5");
                                            }
                                            iElement.setSelected(false);
                                        }else {
                                            iElement.setSelected(!iElement.isSelected());
                                            if (iElement.isSelected()) {
                                                numberOfTickets++;
                                                seats.add("F1_5");
                                            }
                                            else{
                                                numberOfTickets--;
                                                seats.remove("F1_5");
                                            }
                                        }
                                        break;
                                }
                            }
                        });
                    }
                    if(iElement instanceof Button){
                        iElement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(numberOfTickets == 0){
                                    Toast.makeText(getApplicationContext(), R.string.reserve_failed, Toast.LENGTH_SHORT).show();
                                }else{
                                    reserveTickets(view);
                                }
                            }
                        });
                    }
                    if(iElement instanceof Toolbar){
                        iElement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private List<View> getAllChildrenBFS(View v) {
        List<View> visited = new ArrayList<View>();
        List<View> unvisited = new ArrayList<View>();
        unvisited.add(v);

        while (!unvisited.isEmpty()) {
            View child = unvisited.remove(0);
            visited.add(child);
            if (!(child instanceof ViewGroup)) continue;
            ViewGroup group = (ViewGroup) child;
            final int childCount = group.getChildCount();
            for (int i=0; i<childCount; i++) unvisited.add(group.getChildAt(i));
        }
        return visited;
    }

    // writing data
    public void reserveTickets(View view){
        Intent endReservationIntent = new Intent(this, MoviesActivity.class);
        Firebase childRef = mRootRef.child("itickets-6ee08").child("Users").child(hall).child(user.getUid()).child(title);

        childRef.child("Day").setValue(movieDay);
        childRef.child("Hour").setValue(movieHour);
        childRef.child("NumberOfTickets").setValue(numberOfTickets);
        childRef.child("Hall").setValue(hall);
        for(int i = 0; i < seats.size(); i++) {
            if(seats.get(i) != null) {
                String[] strSplit = seats.get(i).split("\\s+");
                seatsList.addAll(Arrays.asList(strSplit));
            }
            //Log.i("newlist", seatsList.toString());
        }

        String seatsString = "";
        for (String s: seatsList){
            seatsString += s + " ";
        }
        childRef.child("Seats").setValue(seatsString);
        Toast.makeText(Hall_1_Activity.this, R.string.reserve_success,Toast.LENGTH_SHORT).show();

        Intent gotoIntent = new Intent(this, ViewReservations.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                (int) (Math.random() * 100), gotoIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder
                (getApplicationContext())
                .setContentTitle(title)
                .setContentText(movieDay + ": " + movieHour)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_movie_ticket)
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
        //startActivity(endReservationIntent);
        finish();
    }

    // reading data
    private String showData(DataSnapshot dataSnapshot) {
        ArrayList<String> lista = new ArrayList<>();

        for (DataSnapshot ds: dataSnapshot.child("Users").child(hall).getChildren()) {
            for(DataSnapshot data: ds.getChildren()){
                if(data.getKey().toString().equals(title)){
                    UserInformation uInfo = new UserInformation();
                    uInfo.setSeats(data.getValue(UserInformation.class).getSeats());
                    uInfo.setHall(data.getValue(UserInformation.class).getHall());
                    if(uInfo.getHall() != null){
                        if(uInfo.getHall().equals(hall)){
                            lista.add(uInfo.getSeats());
                        }
                    }
                }
            }
        }

        for(int i=0; i<lista.size(); i++){
            if(lista.get(i) != null){
                String[] strSplit = lista.get(i).split("\\s+");
                for(String str: strSplit){
                    newList.add(str);
                }
            }
        }

        String seatsToCheck = "";
        for (String s: newList){
            seatsToCheck += s + " ";
        }

        return seatsToCheck;
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
