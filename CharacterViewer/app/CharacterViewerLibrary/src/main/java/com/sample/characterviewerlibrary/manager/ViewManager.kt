package com.sample.characterviewerlibrary.manager

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.sample.characterviewerlibrary.R
import com.sample.characterviewerlibrary.network.CharacterListModels

object ViewManager {

    fun setupCharacterInfo(context: Context, icon: ImageView, name: TextView, desc: TextView,
                           relatedTopics: CharacterListModels.RelatedTopics?) {

        Glide.with(context)
            .load(context.getString(R.string.base_url) + relatedTopics?.Icon!!.URL)
            .placeholder(R.drawable.ic_default_image)
            .into(icon)

        val characterTitle = relatedTopics.Text!!.substring(0, relatedTopics.Text.indexOf(" - "))
        name.text = characterTitle
        desc.text = relatedTopics.Text
    }

    fun filterCharacterList(filterText : String, characterList : ArrayList<CharacterListModels.RelatedTopics>?) :
            List<CharacterListModels.RelatedTopics> {
        var newCharacterList : MutableList<CharacterListModels.RelatedTopics> = ArrayList()

        if(filterText.trim().isEmpty()) {
            return characterList!!
        }

        for(character in characterList!!) {
            if(character.Text!!.toLowerCase().contains(filterText.trim().toLowerCase())) {
                newCharacterList.add(character)
            }
        }

        return newCharacterList
    }
}