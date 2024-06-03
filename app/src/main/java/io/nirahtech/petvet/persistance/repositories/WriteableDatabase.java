package io.nirahtech.petvet.persistance.repositories;

import android.content.ContentValues;

public interface WriteableDatabase {
    long insert(ContentValues values);
    int update(ContentValues values, String selection, String[] selectionArguments);
    int delete(String selection, String[] selectionArguments);
}
