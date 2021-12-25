package ie.app.uetstudents.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.app.uetstudents.R
import ie.app.uetstudents.Entity.Question.get.ImageDto
import kotlinx.android.synthetic.main.itemdatasubject.view.*

class adapter_pdf(var listfile : List<ImageDto>,var onClick : OnclickImageDto)
    : RecyclerView.Adapter<adapter_pdf.Viewholder>(){
    class Viewholder (var itemview : View) : RecyclerView.ViewHolder(itemview){

            fun onBindata(imageDto: ImageDto)
            {
                val filename : String = imageDto.image.substring(imageDto.image.lastIndexOf("/")+1)
                itemview.datasubject_name.text = filename
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            LayoutInflater.from(parent.context).inflate(R.layout.itemdatasubject,parent,false)
        )
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val datamodel = listfile.get(position)
        holder.onBindata(datamodel)
        holder.itemview.setOnClickListener {
            onClick.OnclickItem(datamodel)
        }
    }

    override fun getItemCount(): Int {
        return listfile.size
    }
}
interface OnclickImageDto{
    fun OnclickItem(imageDto: ImageDto)
}