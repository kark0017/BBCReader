package com.example.reader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reader.NewsItem;
import com.example.reader.R;

import java.util.List;

public class FavoriteNewsItemAdapter extends ArrayAdapter<NewsItem> {

    private Context context;
    private List<NewsItem> favoriteNewsItems;

    public FavoriteNewsItemAdapter(Context context, List<NewsItem> favoriteNewsItems) {
        super(context, 0, favoriteNewsItems);
        this.context = context;
        this.favoriteNewsItems = favoriteNewsItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_favorite_news, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = convertView.findViewById(R.id.favoriteTitleTextView);
            viewHolder.descriptionTextView = convertView.findViewById(R.id.favoriteDescriptionTextView);
            // Add other views if needed

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NewsItem favoriteNewsItem = favoriteNewsItems.get(position);

        if (favoriteNewsItem != null) {
            viewHolder.titleTextView.setText(favoriteNewsItem.getTitle());
            viewHolder.descriptionTextView.setText(favoriteNewsItem.getDescription());
            // Set other data as needed
        }

        return convertView;
    }

    static class ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        // Add other views as needed
    }
}
