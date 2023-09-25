package com.example.reader;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle toggle;
    private NewsItemAdapter adapter;
    private DatabaseHelper databaseHelper; // Add this line

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        listView = findViewById(R.id.listView);
        databaseHelper = new DatabaseHelper(this); // Initialize DatabaseHelper

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("MainActivity", "Item clicked at position " + position);
            }
        });

        // Handle NavigationView item clicks
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {

                } else if (id == R.id.nav_favorite_articles) {
                    // Start the Favorite Articles activity
                    startActivity(new Intent(MainActivity.this, FavoriteArticlesActivity.class));
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Execute the AsyncTask to fetch RSS feed
        FetchRssFeedTask fetchRssFeedTask = new FetchRssFeedTask();
        fetchRssFeedTask.execute();



    }

    private class FetchRssFeedTask extends AsyncTask<Void, Void, List<NewsItem>> {

        @Override
        protected List<NewsItem> doInBackground(Void... voids) {
            try {
                URL url = new URL("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(inputStream, null);

                    List<NewsItem> newsItemList = parseXml(parser);

                    inputStream.close();
                    connection.disconnect();

                    return newsItemList;
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }


        private List<NewsItem> parseXml(XmlPullParser parser) throws XmlPullParserException, IOException {
            List<NewsItem> newsItemList = new ArrayList<>();
            NewsItem currentNewsItem = null;

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("item".equals(tagName)) {
                            currentNewsItem = new NewsItem();
                        } else if (currentNewsItem != null) {
                            if ("title".equals(tagName)) {
                                currentNewsItem.setTitle(parser.nextText());
                            } else if ("description".equals(tagName)) {
                                currentNewsItem.setDescription(parser.nextText());
                            } else if ("pubDate".equals(tagName)) {
                                currentNewsItem.setPubDate(parser.nextText());
                            } else if ("link".equals(tagName)) {
                                currentNewsItem.setLink(parser.nextText());
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if ("item".equals(tagName) && currentNewsItem != null) {
                            newsItemList.add(currentNewsItem);
                            currentNewsItem = null;
                        }
                        break;
                }

                eventType = parser.next();
            }

            return newsItemList;
        }

        @Override
        protected void onPostExecute(List<NewsItem> newsItems) {
            if (newsItems != null) {
                Log.d("MainActivity", "Received " + newsItems.size() + " news items.");
                // Create and set the adapter here
                NewsItemAdapter adapter = new NewsItemAdapter(MainActivity.this, newsItems, databaseHelper);
                listView.setAdapter(adapter);
            } else {
                Log.e("MainActivity", "News items are null."); // Log an error message
            }
        }

    }
}
