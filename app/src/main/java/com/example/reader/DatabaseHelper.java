package com.example.reader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database constants
    private static final String DATABASE_NAME = "FavoriteArticles.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    private static final String TABLE_NAME = "favorite_articles";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PUB_DATE = "pub_date";
    private static final String COLUMN_LINK = "link";

    public static final String COLUMN_LIKED = "liked";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create your existing tables
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PUB_DATE + " TEXT, " +
                COLUMN_LINK + " TEXT, " +
                COLUMN_LIKED + " INTEGER DEFAULT 0" + // Add the new column
                ")");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void updateWhenLiked(long articleId, boolean isFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_LIKED, isFavorite ? 1 : 0); // 1 for true, 0 for false

        // Update the record with the given articleId
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[] { String.valueOf(articleId) });

        // Close the database connection
        db.close();
    }

    public long insertFavoriteArticle(NewsItem newsItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, newsItem.getTitle());
        values.put(COLUMN_DESCRIPTION, newsItem.getDescription());
        values.put(COLUMN_PUB_DATE, newsItem.getPubDate());
        values.put(COLUMN_LINK, newsItem.getLink());
        values.put(COLUMN_LIKED, newsItem.isFavorite() ? 1 : 0); // Store 1 for liked, 0 for unliked
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }
    public int deleteFavoriteArticle(long articleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(articleId)});
    }

    public List<NewsItem> getFavoriteArticlesList() {
        List<NewsItem> favoriteNewsItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_PUB_DATE, COLUMN_LINK};
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            NewsItem newsItem = new NewsItem();
            newsItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            newsItem.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
            newsItem.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
            newsItem.setPubDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PUB_DATE)));
            newsItem.setLink(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LINK)));
            favoriteNewsItems.add(newsItem);
        }

        cursor.close();
        return favoriteNewsItems;
    }


}
