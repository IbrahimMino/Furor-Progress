package com.mir.furorprogress.api


import com.mir.furorprogress.models.Product
import retrofit2.Call

import retrofit2.http.*



interface ApiService {
    /**Get All Users*/
    //@Headers("Content-Type: application/json")
    @GET("product")
    fun getAllProducts(): Call<List<Product>>


    /** Creating  a new product*/
    @POST("product")
     fun createProduct(@Body params: Product):Call<Unit>

    /** Updating a product*/
     @PUT("product")
      fun updateProduct(@Body params: Product): Call<Unit>

    /** Deleting a product*/
    @DELETE("product/{id}")
     fun deleteProduct(@Path("id") product_id:String): Call<Unit>







}