package com.example.alachguer.tp1
import android.content.Context
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*


class SearchFragment : Fragment(){

    lateinit var listView: ListView
    private var listTodos = ArrayList<TodoModel>()
    lateinit var todoDbHelper: TodoDbHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_search, null)
        listTodos.add(TodoModel(1, "JavaSampleApproach",
                "Java technology", "date sdsd", 221, 54, 65))
        listTodos.add(TodoModel(1, "sdsd",
                "desc", "date sdsd", 221, 54, 65))
        listTodos.add(TodoModel(1, "sdssdsdsdsd",
                "desc dfds", "date sdsd", 221, 54, 65))

        listView = view.findViewById<ListView>(R.id.listTodos)

        // intiation de la base de donnee
        todoDbHelper = TodoDbHelper(view.context)

        // insertion a la base de donnees
        /*
        todoDbHelper.insertTodo(listTodos.get(0))
        todoDbHelper.insertTodo(listTodos.get(1))
        todoDbHelper.insertTodo(listTodos.get(2))
        */

        // ajouter toutes les lignes de la bdd a la liste pour les lire
        todoDbHelper.readAllTodos().forEach{
            listTodos.add(it)
        }

        val customAdapter = CustomAdapter(view.context)
        customAdapter.mList = listTodos
        listView.adapter = customAdapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            Toast.makeText(context, "Item clicked: " + customAdapter.mList.get(id.toInt()).title, Toast.LENGTH_LONG).show()
        }

        return view

    }

    // class adapteur permet de personnaliser l'affichade des donnees grace a un layout
    private class CustomAdapter(context: Context) : BaseAdapter() {

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

            return searchList
        }

        override fun getCount(): Int {
            return mList.size
        }
    }
}