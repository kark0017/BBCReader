package com.example.reader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FavoriteArticlesActivity extends AppCompatActivity {

    private ListView favoriteListView;
    private DatabaseHelper databaseHelper;
    private List<NewsItem> favoriteNewsItems;
    private FavoriteNewsItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_articles_activity);

        favoriteListView = findViewById(R.id.favoriteListView);
        databaseHelper = new DatabaseHelper(this);

        // Retrieve favorite articles from the database
        favoriteNewsItems = databaseHelper.getFavoriteArticlesList();

        // Create an adapter for displaying favorite articles
        adapter = new FavoriteNewsItemAdapter(this, favoriteNewsItems);

        // Set the adapter for the ListView
        favoriteListView.setAdapter(adapter);

        favoriteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem clickedNewsItem = favoriteNewsItems.get(position);

                // Create an intent to open the NewsDetailsActivity
                Intent intent = new Intent(FavoriteArticlesActivity.this, NewsDetailsActivity.class);

                // Pass the necessary data to the NewsDetailsActivity
                intent.putExtra("title", clickedNewsItem.getTitle());
                intent.putExtra("description", clickedNewsItem.getDescription());
                intent.putExtra("link", clickedNewsItem.getLink());

                // Start the NewsDetailsActivity
                startActivity(intent);
            }
        });
    }
}
