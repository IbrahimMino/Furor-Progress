package com.mir.furorprogress.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "Products")
data class Product(

	@field:SerializedName("cost")
	val cost: Int? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@PrimaryKey(autoGenerate = true)
	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("product_type_id")
	val productTypeId: Int? = null,

	@field:SerializedName("name_uz")
	val nameUz: String? = null,

	@field:SerializedName("created_date")
	val createdDate: Long? = System.currentTimeMillis()
)
