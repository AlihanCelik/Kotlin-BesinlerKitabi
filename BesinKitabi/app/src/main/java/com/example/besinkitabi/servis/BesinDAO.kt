package com.example.besinkitabi.servis

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.besinkitabi.model.Besin

@Dao
interface BesinDAO {
    @Insert
    suspend fun insertALL( vararg besin:Besin):List<Long>
    //Insert-> Room, insert into
    //suspend->coroutine scope
    //vararg-> birden fazla ve istediğimiz sayıda besin
    //List<Long> -> long,id'ler

    @Query("SELECT * FROM besin")
    suspend fun getAllBesin():List<Besin>

    @Query("SELECT * FROM besin WHERE uuid = :besinId")
    suspend fun getBesin(besinId:Int):Besin

    @Query("DELETE FROM besin")
    suspend fun deleteAllBesin()


}