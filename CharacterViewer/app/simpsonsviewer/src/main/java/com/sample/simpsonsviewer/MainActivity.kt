package com.sample.simpsonsviewer

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.sample.characterviewerlibrary.CharacterInfoActivity
import com.sample.characterviewerlibrary.adapter.CharacterListAdapter
import com.sample.characterviewerlibrary.manager.DeviceManager
import com.sample.characterviewerlibrary.manager.NetworkManager
import com.sample.characterviewerlibrary.manager.ViewManager
import com.sample.characterviewerlibrary.network.CharacterDownloadInterface
import com.sample.characterviewerlibrary.network.CharacterListModels
import com.sample.characterviewerlibrary.network.ServiceBuilder
import com.sample.characterviewerlibrary.network.ServiceInterface

import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity(), CharacterDownloadInterface, TextWatcher {

    private lateinit var mCharacterListAdapter : CharacterListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup layout based on device type (tablet or phone)
        requestedOrientation = if(DeviceManager.isTabletDevice(this)) {
            setContentView(R.layout.layout_tablet_list_info)
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            setContentView(R.layout.layout_phone_list)
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        // ListView and Adapter Setup
        mCharacterListAdapter = CharacterListAdapter(this, emptyList())
        findViewById<ListView>(R.id.character_listview).adapter = mCharacterListAdapter
        findViewById<ListView>(R.id.character_listview).setOnItemClickListener(){adapterView, view, position, id ->
            if(DeviceManager.isTabletDevice(this)) {
                findViewById<TextView>(R.id.status_selection).visibility = View.GONE
                hideKeyboard()
                ViewManager.setupCharacterInfo(this, findViewById(R.id.character_image),
                    findViewById(R.id.character_title), findViewById(R.id.character_description),
                    mCharacterListAdapter.getItem(position) as CharacterListModels.RelatedTopics
                )
            } else {
                CharacterListModels.mPhoneSelectedTopic =
                    mCharacterListAdapter.getItem(position) as CharacterListModels.RelatedTopics
                startActivity(Intent(this, CharacterInfoActivity::class.java))
            }
        }

        findViewById<EditText>(R.id.character_search).addTextChangedListener(this)

        // If there is connection, start downloading
        if(NetworkManager.isNetworkAvailable(this)) {
            startCharacterListDownload()
            findViewById<TextView>(R.id.status_connection).visibility = View.GONE
            if(DeviceManager.isTabletDevice(this)) findViewById<TextView>(R.id.status_selection).visibility = View.GONE
        } else {
            findViewById<TextView>(R.id.status_connection).visibility = View.VISIBLE
            findViewById<ListView>(R.id.character_listview).visibility = View.GONE
            findViewById<EditText>(R.id.character_search).visibility = View.GONE
            if(DeviceManager.isTabletDevice(this)) findViewById<TextView>(R.id.status_selection).visibility = View.GONE
        }
    }

    private fun startCharacterListDownload() {
        val request = ServiceBuilder.buildService(ServiceInterface::class.java)
        val call = request.getCharacterList(getString(R.string.character_query), getString(R.string.file_format))
        ServiceBuilder.startCharacterListDownload(this, call)
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun onSuccessResponse(call: Call<CharacterListModels.Result>, response: Response<CharacterListModels.Result>) {
        mCharacterListAdapter.refreshCharacterList(CharacterListModels.mTopicsDownloadedList!!)
        findViewById<TextView>(R.id.status_connection).visibility = View.GONE
        findViewById<ListView>(R.id.character_listview).visibility = View.VISIBLE
        findViewById<EditText>(R.id.character_search).visibility = View.VISIBLE
        if(DeviceManager.isTabletDevice(this)) findViewById<TextView>(R.id.status_selection).visibility = View.VISIBLE
    }

    override fun onFailureResponse(call: Call<CharacterListModels.Result>, t: Throwable) {
        findViewById<TextView>(R.id.status_connection).visibility = View.VISIBLE
        findViewById<ListView>(R.id.character_listview).visibility = View.GONE
        findViewById<EditText>(R.id.character_search).visibility = View.GONE
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(CharacterListModels.mTopicsDownloadedList != null) {
            mCharacterListAdapter.refreshCharacterList(
                ViewManager.filterCharacterList(
                    s.toString(),
                    CharacterListModels.mTopicsDownloadedList as ArrayList<CharacterListModels.RelatedTopics>?
                )
            )
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}