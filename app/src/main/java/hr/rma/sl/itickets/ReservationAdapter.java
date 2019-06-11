package hr.rma.sl.itickets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darko on 25-May-18.
 */

public class ReservationAdapter extends ArrayAdapter<UserInformation> {

    private List<UserInformation> userInformationList = new ArrayList<>();

    private Context context;

    public ReservationAdapter(@NonNull Context context, ArrayList<UserInformation> list) {
        super(context, 0, list);
        this.context = context;
        this.userInformationList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.reservations,parent,false);

        UserInformation currentUser = userInformationList.get(position);

        TextView movieName = listItem.findViewById(R.id.movieName);
        movieName.setText(currentUser.getMovieName());
        TextView day = listItem.findViewById(R.id.day);
        day.setText(currentUser.getDay());
        TextView hour = listItem.findViewById(R.id.hour);
        hour.setText(currentUser.getHour());
        TextView hall = listItem.findViewById(R.id.hall);
        hall.setText(currentUser.getHall());
        TextView numberOfTickets = listItem.findViewById(R.id.numOfTickets);
        numberOfTickets.setText(String.valueOf(currentUser.getNumberOfTickets()));
        TextView seats = listItem.findViewById(R.id.seats);
        seats.setText((CharSequence) currentUser.getSeats());

        return listItem;
    }
}
