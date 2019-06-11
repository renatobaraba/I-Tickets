package hr.rma.sl.itickets;

/**
 * Created by Darko on 30-May-18.
 */

public class MovieReservations {
    private String Hall;
    private String Movie;
    private String StartDate;
    private String StartHour;

    public MovieReservations(){

    }

    public MovieReservations(String hall, String movie, String startDate, String startHour){
        this.Hall = hall;
        this.Movie = movie;
        this.StartDate = startDate;
        this.StartHour = startHour;
    }

    public void setHall(String hall) {
        this.Hall = hall;
    }

    public String getHall() {
        return Hall;
    }

    public void setMovie(String movie){
        this.Movie = movie;
    }

    public String getMovie(){
        return Movie;
    }

    public void setStartDate(String startDate) {
        this.StartDate = startDate;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartHour(String startHour) {
        this.StartHour = startHour;
    }

    public String getStartHour() {
        return StartHour;
    }
}
