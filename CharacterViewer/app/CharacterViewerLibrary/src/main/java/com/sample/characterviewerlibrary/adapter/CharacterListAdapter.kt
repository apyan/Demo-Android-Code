package com.sample.characterviewerlibrary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.sample.characterviewerlibrary.R
import com.sample.characterviewerlibrary.network.CharacterListModels

class CharacterListAdapter(private val context: Context,
                           private var characterList: List<CharacterListModels.RelatedTopics>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.listview_character_name, parent, false)
        val nameText = rowView.findViewById(R.id.character_name) as TextView
        val characterName : String = characterList[position].Text!!.substring(0, characterList[position].Text!!.indexOf(" - "))
        nameText.text = characterName
        return rowView
    }

    fun refreshCharacterList(characterList: List<CharacterListModels.RelatedTopics>) {
        this.characterList = characterList
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return characterList.size
    }

    override fun getItem(position: Int): Any {
        return characterList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}