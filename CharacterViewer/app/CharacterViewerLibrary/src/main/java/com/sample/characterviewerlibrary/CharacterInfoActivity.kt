package com.sample.characterviewerlibrary

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sample.characterviewerlibrary.manager.ViewManager
import com.sample.characterviewerlibrary.network.CharacterListModels

class CharacterInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.layout_phone_info)
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        ViewManager.setupCharacterInfo(this, findViewById(R.id.character_image),
            findViewById(R.id.character_title), findViewById(R.id.character_description),
            CharacterListModels.mPhoneSelectedTopic
        )
    }
}