package com.mir.furorprogress.db

import androidx.room.*
import androidx.room.Dao
import com.mir.furorprogress.models.ProductEntity
import com.mir.furorprogress.utils.StateProduct

@Dao
interface DaoEntity {
    /**Insert*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(productEntity: ProductEntity):Long

    /**Get data*/
    @Query("SELECT * FROM ProductEntity where state_product =:stateProduct")
    fun getAllDataStateProduct(stateProduct: StateProduct): List<ProductEntity>

    /**Get*/
    @Query("SELECT * FROM ProductEntity")
    fun getAllProductEntity(): List<ProductEntity>

    /**Update a product*/
    @Update
    fun updateProduct(product: ProductEntity)

    /**Delete a product*/
    @Query("DELETE FROM ProductEntity where productid =:id")
    fun deleteProduct(id:Int): Int

    /**Clear*/
    @Query("DELETE FROM ProductEntity")
    fun deleteAllProducts()


}