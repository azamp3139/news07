package com.example.azampasha.news07;

/**
 * Created by AZAM PASHA on 26-03-2018.
 */





        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.support.annotation.NonNull;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.squareup.picasso.Picasso;

        import java.io.IOException;
        import java.io.InputStream;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.util.List;

/**
 * Created by AZAM PASHA on 25-03-2018.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, List<News> mynews) {
        super(context, 0,mynews);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Find the earthquake at the given position in the list of earthquakes
        News currentNews = getItem(position);

        TextView titleview=listItemView.findViewById(R.id.title);
        String mtitle=currentNews.gettitle();
        titleview.setText(mtitle);

        TextView authorview=listItemView.findViewById(R.id.author);
        String mauthor=currentNews.getauthor();
        authorview.setText(mauthor);

        TextView timeview=listItemView.findViewById(R.id.time);
        String mtime=currentNews.gettime();
        timeview.setText(mtime);

        ImageView img = (ImageView) listItemView.findViewById(R.id.image);
        String imgurl=currentNews.getimg();
        try{
            Picasso.get().load(imgurl).into(img);
        }
        catch (Exception e) {
            Log.e("Newsadapter","unable to load image");
        }




        return listItemView;
    }
}
