package com.example.alachguer.tp1
import android.content.Context
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*
import kotlinx.android.synthetic.main.fragment_search.listTodos
import kotlinx.android.synthetic.main.search_list.view.*
import org.w3c.dom.NodeList


class SearchFragment : Fragment(){

    lateinit var listView: ListView
    private var listNotes = ArrayList<TodoModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_search, null)
        listNotes.add(TodoModel(1, "JavaSampleApproach",
                "Java technology", "date sdsd", 221, 54, 65))
        listNotes.add(TodoModel(1, "sdsd",
                "desc", "date sdsd", 221, 54, 65))
        listNotes.add(TodoModel(1, "sdssdsdsdsd",
                "desc dfds", "date sdsd", 221, 54, 65))

        listView = view.findViewById<ListView>(R.id.listTodos)
        // val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, listNotes)

        val customAdapter = CustomAdapter(view.context)
        customAdapter.mList = listNotes
        listView.adapter = customAdapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            Toast.makeText(context, "Item clicked: " + customAdapter.mList.get(id.toInt()).title, Toast.LENGTH_LONG).show()
        }

        return view

    }


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