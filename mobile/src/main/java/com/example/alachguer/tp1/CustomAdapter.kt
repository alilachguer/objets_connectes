package com.example.alachguer.tp1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView

class CustomAdapter(context: Context) : BaseAdapter(), Filterable {

    override fun getFilter(): Filter {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val mContext: Context

    lateinit var mList: ArrayList<TodoModel>

    init {
        mContext = context
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return mList.get(position)
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(mContext)
        // layout search_list affiche les les elements de la liste
        val searchList = layoutInflater.inflate(R.layout.search_list, viewGroup, false)

        val title = searchList.findViewById<TextView>(R.id.item_title)
        title.text = mList.get(position).title

        val description = searchList.findViewById<TextView>(R.id.item_description)
        description.text = mList.get(position).description

        val date = searchList.findViewById<TextView>(R.id.item_date)
        date.text = mList.get(position).date + " - " + mList.get(position).timeHour + ":" + mList.get(position).timeMinute

        val type = searchList.findViewById<TextView>(R.id.item_type)
        type.text = mList.get(position).type
        return searchList
    }

    override fun getCount(): Int {
        return mList.size
    }
}
