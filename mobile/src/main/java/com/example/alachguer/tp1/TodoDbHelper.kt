package com.example.alachguer.tp1

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.*

class TodoDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 2
        val DATABASE_NAME = "Todo.db"

        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DbContract.TodoEntry.TABLE_NAME + " (" +
                        DbContract.TodoEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DbContract.TodoEntry.COLUMN_NAME_TITLE + " TEXT, " +
                        DbContract.TodoEntry.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                        DbContract.TodoEntry.COLUMN_NAME_DATE + " TEXT, " +
                        DbContract.TodoEntry.COLUMN_NAME_TYPE + " TEXT, " +
                        DbContract.TodoEntry.COLUMN_NAME_TIMEHOUR + " TEXT, " +
                        DbContract.TodoEntry.COLUMN_NAME_TIMEMINUTE + " TEXT, " +
                        DbContract.TodoEntry.COLUMN_NAME_NOTIFICATION + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DbContract.TodoEntry.TABLE_NAME
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_DELETE_ENTRIES)
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertTodo(todo: TodoModel): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DbContract.TodoEntry.COLUMN_NAME_TITLE, todo.title)
        values.put(DbContract.TodoEntry.COLUMN_NAME_DESCRIPTION, todo.description)
        values.put(DbContract.TodoEntry.COLUMN_NAME_DATE, todo.date.toString())
        values.put(DbContract.TodoEntry.COLUMN_NAME_TYPE, todo.type)
        values.put(DbContract.TodoEntry.COLUMN_NAME_TIMEHOUR, todo.timeHour)
        values.put(DbContract.TodoEntry.COLUMN_NAME_TIMEMINUTE, todo.timeMinute)
        values.put(DbContract.TodoEntry.COLUMN_NAME_NOTIFICATION, todo.notification)

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DbContract.TodoEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteTodo(todoId: String): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = DbContract.TodoEntry.COLUMN_NAME_ID + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(todoId)
        // Issue SQL statement.
        db.delete(DbContract.TodoEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun readTodo(todoId: Int): ArrayList<TodoModel> {
        val todos = ArrayList<TodoModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DbContract.TodoEntry.TABLE_NAME + " WHERE "
                    + DbContract.TodoEntry.COLUMN_NAME_ID + "='" + todoId + "'", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var title: String
        var description: String
        var date: String
        var type: String
        var timeHour: Int
        var timeMinute: Int
        var notif: Int

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                title = cursor.getString(cursor.getColumnIndex(DbContract.TodoEntry.COLUMN_NAME_TITLE))
                description = cursor.getString(cursor.getColumnIndex(DbContract.TodoEntry.COLUMN_NAME_DESCRIPTION))
                date = cursor.getString(cursor.getColumnIndex(DbContract.TodoEntry.COLUMN_NAME_DATE))
                type = cursor.getString(cursor.getColumnIndex(DbContract.TodoEntry.COLUMN_NAME_TYPE))
                timeHour = cursor.getInt(cursor.getColumnIndex(DbContract.TodoEntry.COLUMN_NAME_TIMEHOUR))
                timeMinute = cursor.getInt(cursor.getColumnIndex(DbContract.TodoEntry.COLUMN_NAME_TIMEMINUTE))
                notif = cursor.getInt(cursor.getColumnIndex(DbContract.TodoEntry.COLUMN_NAME_NOTIFICATION))

                todos.add(TodoModel(title, description, date, type, timeHour, timeMinute, notif))
                cursor.moveToNext()
            }
        }
        return todos
    }

    fun readAllTodos(): ArrayList<TodoModel> {
        val todos = ArrayList<TodoModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DbContract.TodoEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var todoId: Int
        var title: String
        var description: String
        var date: String
        var type: String
        var timeHour: Int
        var timeMinute: Int
        var notif: Int

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                todoId = cursor.getInt(cursor.getColumnIndex(DbContract.TodoEntry.COLUMN_NAME_ID))
                title = cursor.getString(cursor.getColumnIndex(DbContract.TodoEntry.COLUMN_NAME_TITLE))
                description = cursor.getString(cursor.getColumnIndex(DbContract.TodoEntry.COLUMN_NAME_DESCRIPTION))
                date = cursor.getString(cursor.getColumnIndex(DbContract.TodoEntry.COLUMN_NAME_DATE))
                type = cursor.getString(cursor.getColumnIndex(DbContract.TodoEntry.COLUMN_NAME_TYPE))
                timeHour = cursor.getInt(cursor.getColumnIndex(DbContract.TodoEntry.COLUMN_NAME_TIMEHOUR))
                timeMinute = cursor.getInt(cursor.getColumnIndex(DbContract.TodoEntry.COLUMN_NAME_TIMEMINUTE))
                notif = cursor.getInt(cursor.getColumnIndex(DbContract.TodoEntry.COLUMN_NAME_NOTIFICATION))
                todos.add(TodoModel( title, description, date, type, timeHour, timeMinute, notif))
                cursor.moveToNext()
            }
        }
        return todos
    }

}