package ie.app.uetstudents.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder(val itemView : View)  : RecyclerView.ViewHolder(itemView) {
    fun <T : View?> findViewById(id: Int): T = itemView.findViewById(id)
}
