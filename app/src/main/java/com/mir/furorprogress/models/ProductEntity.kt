package com.mir.furorprogress.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mir.furorprogress.utils.StateProduct
@Entity(tableName = "ProductEntity")
data class ProductEntity(
    @ColumnInfo(name = "cost")
    val cost: Int? = null,

    @ColumnInfo(name = "address")
    val address: String? = null,

    @ColumnInfo(name = "id")
    val id: Int?=null,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "productid")
    val productId: Int? = null,

    @ColumnInfo(name = "product_type_id")
    val productTypeId: Int? = null,

    @ColumnInfo(name = "name_uz")
    val nameUz: String? = null,

    @ColumnInfo(name = "created_date")
    val createdDate: Long? = System.currentTimeMillis(),

    @ColumnInfo(name = "status")
    val status: Boolean? = null,

    @ColumnInfo(name = "state_product")
    val stateProduct: StateProduct? = null





)