package com.example.besinkitabi.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.besinkitabi.model.Besin
import com.example.besinkitabi.servis.BesinAPIServis
import com.example.besinkitabi.servis.BesinDatabase
import com.example.besinkitabi.util.OzelSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class BesinListesiViewModel(application: Application):BaseViewModel(application) {
    val  besinler=MutableLiveData<List<Besin>>()
    val besinHataMesaji=MutableLiveData<Boolean>()
    val besinYukleniyor=MutableLiveData<Boolean>()

    private val besinApiServis=BesinAPIServis()
    private val disposable=CompositeDisposable()
    private val ozelSharedPreferences=OzelSharedPreferences(getApplication())
    private var guncellemeZamani=10*60*1000*1000*1000L


    fun refreshData(){
        val kaydedilmeZamani=ozelSharedPreferences.zamaniAl()
        if(kaydedilmeZamani != null && kaydedilmeZamani !=0L && System.nanoTime()-kaydedilmeZamani<guncellemeZamani){
            verileriSQLitetanAl()
        }else{
            verileriInternettenAl()
        }


    }
    fun refreshFromInternet(){
        verileriInternettenAl()
    }
    private fun verileriSQLitetanAl(){
        besinYukleniyor.value=true
        launch {
            val besinListesi=BesinDatabase(getApplication()).besinDao().getAllBesin()
            besinleriGoster(besinListesi)
            Toast.makeText(getApplication(),"Besinleri Room'dan Aldık",Toast.LENGTH_LONG).show()
        }
    }
    private fun verileriInternettenAl(){
        besinYukleniyor.value=true

        disposable.add(
            besinApiServis.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object:DisposableSingleObserver<List<Besin>>(){
                    override fun onSuccess(t: List<Besin>) {

                        sqlSakla(t)
                        Toast.makeText(getApplication(),"Besinleri İnternet'ten Aldık",Toast.LENGTH_LONG).show()



                    }

                    override fun onError(e: Throwable) {
                        besinHataMesaji.value=true
                        besinYukleniyor.value=false
                        e.printStackTrace()
                    }

                })
        )

    }
    private fun besinleriGoster(besinlerListesi:List<Besin>){
        besinler.value=besinlerListesi
        besinHataMesaji.value=false
        besinYukleniyor.value=false
    }
    private fun sqlSakla(besinlerListesi: List<Besin>){
        launch {
            val dao=BesinDatabase(getApplication()).besinDao()
            dao.deleteAllBesin()
            val uuidListesi=dao.insertALL(*besinlerListesi.toTypedArray())
            var i=0
            while(i<besinlerListesi.size) {
                besinlerListesi[i].uuid = uuidListesi[i].toInt()
                i=i+1
            }
            besinleriGoster(besinlerListesi)
        }

        ozelSharedPreferences.zamaniKaydet(System.nanoTime())
    }

}