package com.example.alachguer.tp1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList

class CustomAdapter(context: Context, mlist: ArrayList<TodoModel>) : BaseAdapter() {

    private val mContext: Context

    var mList: ArrayList<TodoModel> = mlist
    var tempNameVersionList = ArrayList(mList)

    init {
        mContext = context
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return mList.get(position)
    }


    // fonction de filtre sur le texte de la barre de recherche et sur le type de tache
    fun filter(text: String, type: String){

        val text = text!!.toLowerCase(Locale.getDefault())
        // effacer la liste mlist, pour l remplir avec les donnes associes au mot recherche
        mList.clear()

        if (text.length == 0 ) {
            if(type == "Tout"){
                mList.addAll(tempNameVersionList)
            }
            for( i in 0..tempNameVersionList.size-1){
                var item = tempNameVersionList.get(i)
                if(item.type.toLowerCase(Locale.getDefault()).contains(type.toLowerCase())){
                    mList.add(tempNameVersionList.get(i))
                }
            }


           // mList.addAll(tempNameVersionList)
        } else {
            for (i in 0..tempNameVersionList.size - 1) {

                var item = tempNameVersionList.get(i)
                // la tache doit respecter ces deux conditions avant d'etre ajoutee a la liste mList qui sera affichee
                // la tache doit contenir le texte recherche et sont type doit correspondre au type selectionne dans le spinner
                if (item.title.toLowerCase().contains(text) && item.type.toLowerCase().contains(type.toLowerCase()) ) {
                    //mList.clear()
                    mList.add(tempNameVersionList.get(i))
                }
            }
        }
        //This is to notify that data change in Adapter and Reflect the changes.
        notifyDataSetChanged()
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
