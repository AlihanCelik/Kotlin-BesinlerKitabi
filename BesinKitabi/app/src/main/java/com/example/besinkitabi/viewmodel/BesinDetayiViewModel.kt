package com.example.besinkitabi.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.besinkitabi.model.Besin
import com.example.besinkitabi.servis.BesinDatabase
import kotlinx.coroutines.launch

class BesinDetayiViewModel(application: Application):BaseViewModel(application) {
    val besinLiveData=MutableLiveData<Besin>()

    fun roomVerisibiAl(uuid:Int){
        launch {
            val dao=BesinDatabase(getApplication()).besinDao()
            val besin=dao.getBesin(uuid)
            besinLiveData.value=besin
        }
    }
}