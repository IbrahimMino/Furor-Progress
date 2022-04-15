package com.mir.furorprogress.db

import androidx.room.*
import androidx.room.Dao
import com.mir.furorprogress.models.Product

@Dao
interface Dao {
    /**Insert*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun InsertProduct(product: ArrayList<Product>)

    /**Get data*/
    @Query("SELECT * FROM Products")
    fun getAllData(): List<Product>

    /**Update a product*/
    @Update
    fun updateProduct(product: Product)


    /**Delete a product*/
    @Query("DELETE FROM Products where id =:id")
    fun deleteProduct(id:Int): Int

    /**Clear*/
    @Query("DELETE FROM Products")
    fun deleteAllProducts()

}