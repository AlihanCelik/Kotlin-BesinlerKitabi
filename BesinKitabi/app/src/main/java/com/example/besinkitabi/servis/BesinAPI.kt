package com.example.besinkitabi.servis

import com.example.besinkitabi.model.Besin
import io.reactivex.Single
import retrofit2.http.GET

interface BesinAPI {
    //Get, Post
    //https://raw.githubusercontent.com/atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json
    // Base_Url-> https://raw.githubusercontent.com/
    //atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json
    @GET("atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json")

    fun getBesin() :Single<List<Besin>>

}