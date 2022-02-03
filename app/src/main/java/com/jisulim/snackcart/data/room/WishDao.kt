package com.jisulim.snackcart.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jisulim.snackcart.models.entity.WishEntity

@Dao
interface WishDao {

    @Query("SELECT * FROM Wish")
    suspend fun getAll() : List<WishEntity>

    @Insert
    suspend fun insert(wishList: WishEntity)

    @Update
    suspend fun update(wishList: WishEntity)

    @Query("DELETE FROM Wish")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM Wish WHERE requester = :requester)")
    suspend fun isExist(requester: String) : Boolean

    @Query("DELETE FROM Wish WHERE requester=:requester")
    suspend fun delete(requester: String) : Int

    @Query("SELECT SUM(price) FROM Wish")
    suspend fun getPrice() : Int?

    @Query("SELECT COUNT(requester) FROM Wish")
    suspend fun getCount() : Int?

}