package com.example.alachguer.tp1

import android.provider.BaseColumns

object DbContract {
    // Table contents are grouped together in an anonymous object.
    object TodoEntry : BaseColumns {
        const val TABLE_NAME = "Todos"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DESCRIPTION = "description"
        const val COLUMN_NAME_DATE = "date"
        const val COLUMN_NAME_TIMEHOUR = "timehour"
        const val COLUMN_NAME_TIMEMINUTE = "timeminute"
        const val COLUMN_NAME_NOTIFICATION = "notification"
    }
}