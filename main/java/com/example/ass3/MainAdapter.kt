package com.example.ass3

import android.app.Activity
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

//creating viewHolder
class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    //Intitialize variables
    var name: TextView = itemView.findViewById(R.id.username)
    var number: TextView= itemView.findViewById(R.id.number)

}

class MainAdapter(activity_: Activity,contacts:Array<Contact>): Adapter<ViewHolder>() {

    var activity: Activity = activity_
    var ContactList = contacts
    //notifyDataSetChanged()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var contact:Contact = ContactList.get(position)
        holder.name.text = contact.getName()
        holder.number.text = contact.getNumber()
    }

    override fun getItemCount(): Int {
        return if (ContactList == null) 0 else ContactList!!.size
    }

}
