package com.example.alachguer.tp1

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.util.*
import kotlin.collections.ArrayList
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.widget.ImageButton
import android.support.v4.view.ViewCompat.setElevation
import android.os.Build
import android.transition.Slide
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.widget.PopupWindow
import android.widget.TextView









class CustomAdapter(context: Context, mlist: ArrayList<TodoModel>) : BaseAdapter() {

    private val mContext: Context
    private var popupWindow: PopupWindow? = null
    var mList: ArrayList<TodoModel> = mlist
    var tempNameVersionList = ArrayList(mList)


    init {
        mContext = context
    }
    var todoDbHelper = TodoDbHelper(context)


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
                if (item.title.toLowerCase().contains(text) && (item.type.toLowerCase().contains(type.toLowerCase())  || type=="Tout" ) ) {
                    //mList.clear()
                    mList.add(tempNameVersionList.get(i))
                }
            }
        }
        //This is to notify that data change in Adapter and Reflect the changes.
        notifyDataSetChanged()
    }

    @SuppressLint("WrongViewCast")
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(mContext)
        // layout search_list affiche les les elements de la liste
        val searchList = layoutInflater.inflate(R.layout.search_list, viewGroup, false)

        val title = searchList.findViewById<TextView>(R.id.item_title)
        title.text = mList.get(position).title

//        val description = searchList.findViewById<TextView>(R.id.item_description)
//        description.text = mList.get(position).description

        val date = searchList.findViewById<TextView>(R.id.item_date)
        date.text = mList.get(position).date + " - " + mList.get(position).timeHour + ":" + mList.get(position).timeMinute

        val type = searchList.findViewById<TextView>(R.id.item_type)
        type.text = mList.get(position).type


        val deleteImageButton = searchList.findViewById<ImageButton>(R.id.buttonRemove) as ImageButton
        deleteImageButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val inflater = mContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

                // Inflate the custom layout/view
                val customView = inflater.inflate(R.layout.remove_layout, null)



                val popupWindow = PopupWindow(
                        customView, // Custom view to show in popup window
                        LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
                        LinearLayout.LayoutParams.WRAP_CONTENT // Window height
                )
                var popText = customView.findViewById<TextView>(R.id.tv) as TextView
                popText.text =  "Voulez-vous vraiment supprimer la tâche "+  mList.get(position).title+" ?"

                // Set an elevation for the popup window
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    popupWindow.elevation = 10.0F
                }


                // If API level 23 or higher then execute the code
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Create a new slide animation for popup window enter transition
                    val slideIn = Slide()
                    slideIn.slideEdge = Gravity.TOP
                    popupWindow.enterTransition = slideIn
                    popupWindow.showAtLocation(searchList, Gravity.CENTER, 0, 0);
                    popupWindow.update(0, 0, popupWindow.getWidth(), popupWindow.getHeight());
                }

                // Get a reference for the custom view close button
                val removeTask = customView.findViewById(R.id.Oui) as Button

                // Set a click listener for the popup window close button
                removeTask.setOnClickListener {
                    // Dismiss the popup window
                    // Slide animation for popup window exit transition
                    val slideOut = Slide()
                    slideOut.slideEdge = Gravity.RIGHT
                    popupWindow.exitTransition = slideOut
                    popupWindow!!.dismiss()
                    //REMOVE HERE
                    todoDbHelper.deleteTodo(mList.get(position).todoId.toString())

                }
            }
        })



        return searchList
    }

    override fun getCount(): Int {
        return mList.size
    }
}
