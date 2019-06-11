package hr.rma.sl.itickets;

import android.content.Context;
import android.graphics.Movie;
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

public class MovieArrayAdapter extends ArrayAdapter {

    private List<MovieDetails> movieDetailsList;

    private int resource;

    private Filter movieFilter;

    private List<MovieDetails> origDetailsList;

    private Context context;


    public MovieArrayAdapter(Context context, int resource, List<MovieDetails> movieDetails) {
        super(context, resource, movieDetails);
        this.context = context;
        this.movieDetailsList = movieDetails;
        this.resource = resource;
        this.origDetailsList = movieDetailsList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieDetails details = movieDetailsList.get(position);

        View view = LayoutInflater.from(context).inflate(resource,parent,false);

        TextView movieName = (TextView) view.findViewById(R.id.textView);
        TextView rating = view.findViewById(R.id.rating);
        ImageView image = (ImageView) view.findViewById(R.id.imageView);

        movieName.setText(details.getOriginal_title());
        rating.setText(String.valueOf(details.getVote_average()));

        Glide.with(context).load("https://image.tmdb.org/t/p/w500/"+ details.getPoster_path()).into(image);

        return  view;
    }

    public void resetData(){
        movieDetailsList  = origDetailsList;
    }
    @Override
    public Filter getFilter() {
        if (movieFilter == null)
            movieFilter = new MovieFilter();

        return movieFilter;
    }

    @Nullable
    @Override
    public MovieDetails getItem(int position) {
        return movieDetailsList.get(position);
    }

    public int getCount() {
        return  movieDetailsList.size();
    }

    private class MovieFilter extends Filter {



        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origDetailsList;
                results.count = origDetailsList.size();
            }
            else {
                // We perform filtering operation
                List<MovieDetails> nMovieList = new ArrayList<MovieDetails>();

                for (MovieDetails m : movieDetailsList) {
                    if (m.getOriginal_title().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        nMovieList.add(m);
                }

                results.values = nMovieList;
                results.count = nMovieList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                movieDetailsList = (List<MovieDetails>) results.values;
                notifyDataSetChanged();
            }

        }

    }
}
