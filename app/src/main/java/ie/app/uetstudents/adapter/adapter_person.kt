package ie.app.uetstudents.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import ie.app.uetstudents.R
import ie.app.uetstudents.data.response.AccountDto
import kotlinx.android.synthetic.main.person.view.*

class adapter_person(var listperson : List<AccountDto>) : BaseAdapter(){
    override fun getCount(): Int {
        return  listperson.size
    }

    override fun getItem(position: Int): Any {
        return listperson.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view : View = LayoutInflater.from(parent?.context).inflate(R.layout.person,parent,false)

        view.tennguoidung.text = listperson[position].username
        return view
    }

}
