package com.example.reader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class NewsItemAdapter extends ArrayAdapter<NewsItem> {

    private Context context;
    private DatabaseHelper databaseHelper; // DatabaseHelper instance

    public NewsItemAdapter(Context context, List<NewsItem> newsItemList, DatabaseHelper dbHelper) {
        super(context, 0, newsItemList);
        this.context = context;
        this.databaseHelper = dbHelper; // Initialize the database helper
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_news, parent, false);
        } else {

        }


        // Get the NewsItem object at the current position
        NewsItem newsItem = getItem(position);

        // Find and set the TextViews to display the news item details
        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
        TextView pubDateTextView = convertView.findViewById(R.id.pubDateTextView);

        if (newsItem != null) {
            titleTextView.setText(newsItem.getTitle());
            descriptionTextView.setText(newsItem.getDescription());
            pubDateTextView.setText(newsItem.getPubDate());
        }

        ImageButton favoriteImageButton = convertView.findViewById(R.id.favoriteImageButton);

        // Set the initial state of the favorite button
        if (newsItem != null && newsItem.isFavorite()) {
            favoriteImageButton.setImageResource(R.drawable.ic_like);
        } else {
            favoriteImageButton.setImageResource(R.drawable.ic_unlike);
        }

        favoriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle the isFavorite state
                boolean newFavoriteState = !newsItem.isFavorite();
                newsItem.setFavorite(newFavoriteState);

                // Update the image resource and database
                if (newFavoriteState) {
                    favoriteImageButton.setImageResource(R.drawable.ic_like);
                    long articleId = databaseHelper.insertFavoriteArticle(newsItem);
                    newsItem.setId(articleId);
                    showToast("Liked!");
                } else {
                    favoriteImageButton.setImageResource(R.drawable.ic_unlike);
                    long articleId = newsItem.getId();
                    if (articleId != -1) {
                        databaseHelper.deleteFavoriteArticle(articleId);
                    }
                    showToast("Article Unliked");
                }

                databaseHelper.updateWhenLiked(newsItem.getId(), newFavoriteState);
                // Notify the adapter that the data has changed
                notifyDataSetChanged();
            }
        });



        return convertView;
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}

