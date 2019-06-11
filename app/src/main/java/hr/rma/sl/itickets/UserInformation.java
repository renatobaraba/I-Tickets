package hr.rma.sl.itickets;

import java.util.ArrayList;

/**
 * Created by Darko on 23-May-18.
 */

public class UserInformation {
    private String Day;
    private String Hour;
    private String MovieName;
    private int NumberOfTickets;
    private String Hall;
    private String Seats;

    public UserInformation(){

    }

    public UserInformation(String day, String hour, String movieName, int numberOfTickets, String hall, String seats){
        this.Day = day;
        this.Hour = hour;
        this.MovieName = movieName;
        this.NumberOfTickets = numberOfTickets;
        this.Hall = hall;
        this.Seats = seats;
    }

    public void setDay(String day) {
        this.Day = day;
    }

    public String getDay() {
        return Day;
    }

    public void setHour(String hour) {
        this.Hour = hour;
    }

    public String getHour() {
        return Hour;
    }

    public void setMovieName(String movieName){
        this.MovieName = movieName;
    }

    public String getMovieName(){
        return MovieName;
    }

    public void setHall(String hall) {
        this.Hall = hall;
    }

    public String getHall() {
        return Hall;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.NumberOfTickets = numberOfTickets;
    }

    public int getNumberOfTickets() {
        return NumberOfTickets;
    }

    public void setSeats(String seats) {
        this.Seats = seats;
    }

    public String getSeats() {
        return Seats;
    }
}
