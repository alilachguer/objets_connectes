package com.example.alachguer.tp1

import java.util.*

class TodoModel
{
    public var todoId: Int = 0
    public var title: String = "TitleDefault"
    public var description: String = "DescDefault"
    public var date: String = "dateDefault"
    public var type: String="TypeDefault"
    public var timeHour: Int = 0
    public var timeMinute: Int = 0
    public var notification: Int = 0


    constructor(title: String,
                description: String,
                date: String,
                type: String,
                timeHour: Int,
                timeMinute: Int,
                notification: Int){
        this.title = title
        this.description = description
        this.date = date
        this.type = type
        this.timeHour = timeHour
        this.timeMinute = timeMinute
        this.notification = notification

    }

    constructor(todoId: Int,title: String,
                description: String,
                date: String,
                type: String,
                timeHour: Int,
                timeMinute: Int,
                notification: Int){
        this.todoId = todoId
        this.title = title
        this.description = description
        this.date = date
        this.type = type
        this.timeHour = timeHour
        this.timeMinute = timeMinute
        this.notification = notification

    }
}