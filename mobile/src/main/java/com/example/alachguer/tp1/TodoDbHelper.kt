package com.example.alachguer.tp1

import android.content.Context
import android.database.sqlite.*

class TodoDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "Todo.db"

        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DbContract.TodoEntry.TABLE_NAME + " (" +
                        DbContract.TodoEntry.COLUMN_NAME_ID + " TEXT PRIMARY KEY," +
                        DbContract.TodoEntry.COLUMN_NAME_TITLE + " TEXT," +
                        DbContract.TodoEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                        DbContract.TodoEntry.COLUMN_NAME_DATE + " TEXT," +
                        DbContract.TodoEntry.COLUMN_NAME_TIMEHOUR + " TEXT," +
                        DbContract.TodoEntry.COLUMN_NAME_TIMEMINUTE + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DbContract.TodoEntry.TABLE_NAME
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    


}