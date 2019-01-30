package com.mani.movies.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.mani.movies.datastruct.MovieDetails;

@Database(entities = MovieDetails.class, version = 1, exportSchema = false)
public abstract class AppDb extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String TAG = AppDb.class.getSimpleName();
    private static final String dbName = MovieDetails.class.getSimpleName();
    private static AppDb instance;

    public static AppDb getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDb.class,
                        AppDb.dbName)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return instance;
    }

    public abstract MovieDao movieDao();
}
