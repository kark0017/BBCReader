package com.example.reader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NewsDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_NEWS_ITEM = "extra_news_item";

    private NewsItem newsItem;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.news_details);

            // Retrieve the link and other data from the intent
            Intent intent = getIntent();
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            final String link = intent.getStringExtra("link");

            // Initialize views
            TextView titleTextView = findViewById(R.id.titleTextView);
            TextView descriptionTextView = findViewById(R.id.descriptionTextView);
            Button openLinkButton = findViewById(R.id.linkButton);

            // Set the text for title and description
            titleTextView.setText(title);
            descriptionTextView.setText(description);

            // Set an OnClickListener for the button to open the link
            openLinkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openLinkInBrowser(link);
                }
            });
        }

        private void openLinkInBrowser(String link) {
            // Open the link in a web browser
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(browserIntent);
        }
    }


