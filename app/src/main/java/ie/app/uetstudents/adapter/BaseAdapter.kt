package ie.app.uetstudents.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(val context: Context) : RecyclerView.Adapter<BaseViewHolder>() {
    protected var items : MutableList<T> = mutableListOf()
    protected var callback: OnclickPdf<T>? = null

    override fun getItemCount(): Int = items.size

    open fun updateList(newList: List<T>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    open fun insertItem(item: T) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    open fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    open fun updateItem(position: Int, newItem: T) {
        notifyItemChanged(position, newItem)
    }

    open fun getItemAt(position: Int): T {
        return items[position]
    }

    open fun setOnClickListener(callback: OnclickPdf<T>) {
        this.callback = callback
    }

    interface OnclickPdf<T> {
        fun onItemClick(position: Int, item: T)
    }
}
