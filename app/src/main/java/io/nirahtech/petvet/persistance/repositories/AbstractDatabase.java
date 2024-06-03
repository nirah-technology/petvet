package io.nirahtech.petvet.persistance.repositories;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Objects;

public abstract class AbstractDatabase implements WriteableDatabase {

    private final AbstractSQLiteOpenHelper sqliteHelper;
    private final SQLiteDatabase writeableDatabase;
    private final SQLiteDatabase readableDatabase;


    protected AbstractDatabase(final AbstractSQLiteOpenHelper sqliteHelper) {
        this.sqliteHelper = sqliteHelper;
        this.writeableDatabase = this.sqliteHelper.getWritableDatabase();
        this.readableDatabase = this.sqliteHelper.getReadableDatabase();
    }

    @Override
    public long insert(ContentValues values) {
        Objects.requireNonNull(values, "Values is required for insert into AbstractDatabase.");
        return this.writeableDatabase.insert(this.sqliteHelper.getTableName(), null, values);
    }

    @Override
    public int delete(String selection, String[] selectionArguments) {
        return this.writeableDatabase.delete(this.sqliteHelper.getTableName(), selection, selectionArguments);
    }
}
