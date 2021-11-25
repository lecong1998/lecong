package ie.app.uetstudents.ui.timkiem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ie.app.uetstudents.R
import ie.app.uetstudents.ui.Entity.Search.PersonDto
import kotlinx.android.synthetic.main.item_search.view.*

class adapter_search_person(var listperson : List<PersonDto> , var CLickItem : OnClickItem_SearchPerson)
    : RecyclerView.Adapter<adapter_search_person.ViewHolder>(){
   inner class ViewHolder(var itemview : View) : RecyclerView.ViewHolder(itemview) {
        fun OnBindata(personDto: PersonDto)
        {
            Glide.with(itemView.context).load(personDto.image).into(itemview.item_search_image)
            itemview.item_search_content.text = personDto.account_username
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemperson = listperson[position]
        holder.OnBindata(itemperson)
        holder.itemview.setOnClickListener {
            CLickItem.ClickItem(itemperson)
        }
    }

    override fun getItemCount(): Int {
        return listperson.size
    }
}

interface OnClickItem_SearchPerson{
    fun ClickItem(personDto: PersonDto)
}