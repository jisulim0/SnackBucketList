package com.jisulim.snackcart.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.jisulim.snackcart.data.room.WishDao
import com.jisulim.snackcart.models.entity.WishEntity
import javax.inject.Inject

class WishRepository @Inject constructor(private val wishDao: WishDao) {

    suspend fun getWish(): List<WishEntity> =
        withContext(Dispatchers.IO) {
            wishDao.getAll()
        }

    suspend fun insert(wishList: WishEntity) =
        withContext(Dispatchers.IO) {
            wishDao.insert(wishList)
        }


    suspend fun update(wishList: WishEntity) =
        withContext(Dispatchers.IO) {
            wishDao.update(wishList)
        }

    suspend fun deleteAll() =
        withContext(Dispatchers.IO) {
            wishDao.deleteAll()
        }

    suspend fun isExist(requester: String) =
        withContext(Dispatchers.IO) {
            wishDao.isExist(requester)
        }

    suspend fun delete(requester: String): Int =
        withContext(Dispatchers.IO) {
            wishDao.delete(requester)
        }


    suspend fun getPrice() =
        withContext(Dispatchers.IO) {
            wishDao.getPrice()
        }


    suspend fun getCount() =
        withContext(Dispatchers.IO) {
            wishDao.getCount()
        }

}