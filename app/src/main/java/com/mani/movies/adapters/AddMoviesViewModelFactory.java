package com.mani.movies.adapters;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.mani.movies.db.AppDb;

import java.util.List;

public class AddMoviesViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDb appDb;
    private final List<String> ids;

    public AddMoviesViewModelFactory(AppDb appDb, List<String> ids) {
        this.appDb = appDb;
        this.ids = ids;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AddMoviesViewModel(appDb, ids);
    }

}
