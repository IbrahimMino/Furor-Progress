package com.mir.furorprogress.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mir.furorprogress.api.ApiService
import com.mir.furorprogress.api.RetrofitInstance
import com.mir.furorprogress.db.RoomDatabase
import com.mir.furorprogress.models.Product
import com.mir.furorprogress.models.ProductEntity
import com.mir.furorprogress.utils.StateProduct
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel(var app: Application):AndroidViewModel(app) {
   private var productList: MutableLiveData<List<Product?>> = MutableLiveData()
   private var createProductLiveData: MutableLiveData<Unit> = MutableLiveData()
   private var deleteProductLiveData: MutableLiveData<Unit> = MutableLiveData()
   private var updateProductLiveData: MutableLiveData<Unit> = MutableLiveData()

    /**Retrofit*/
    fun getProductsObserver(): LiveData<List<Product?>> {
        return productList
    }
    fun getCreateUserObservable(): LiveData<Unit> {
        return createProductLiveData
    }
    fun getDeleteProductObserverable(): LiveData<Unit>{
        return deleteProductLiveData
    }
    fun getUpdateProductObserverable(): LiveData<Unit>{
        return updateProductLiveData
    }

    fun getProducts() {
        val retrofit = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        val call = retrofit.getAllProducts()
        call.enqueue(object : Callback<List<Product>>{
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful){
                    productList.postValue(response.body())
                    Log.d("TAG", "onResponse: $response")
                }
            }
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                productList.postValue(null)
                Log.d("TAG", "OnFailure: $call")
            }
        })
    }
    fun createUser(product: Product){
        val retrofit = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        val call = retrofit.createProduct(product)
        call.enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.code()==200){
                createProductLiveData.postValue(response.body())
                }
            }
            override fun onFailure(call1: Call<Unit>, t: Throwable) {
                createProductLiveData.postValue(null)
            }
        })

    }
    fun deletProduct(productId: String){
        val retrofit = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        val call = retrofit.deleteProduct(productId)
        call.enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.code()==200){
                    deleteProductLiveData.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                deleteProductLiveData.postValue(null)
            }
        })
    }
    fun updateProduct(product: Product){
        val retrofit = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        val call = retrofit.updateProduct(product)
        call.enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.code()==200){
                    updateProductLiveData.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                updateProductLiveData.postValue(null)
            }
        })
    }


    /**Roomdatabase*/
    var base = RoomDatabase.getDatabase().dao()
    fun insertProduct(product: ArrayList<Product>) {
        base.InsertProduct(product)
    }
    fun readProduct(): List<Product> {
        return base.getAllData()
    }

    /**Delete product for Room*/
    fun deleteProduct(id:Int){
        base.deleteProduct(id)
    }

    /** Update*/
    fun productUpdate(product: Product){
        base.updateProduct(product)
    }

    /**Clear All products*/
    fun deleteAllProducts(){
        base.deleteAllProducts()
    }

    /**Room database DaoEntity*/
    val baseEntity = RoomDatabase.getDatabase().daoEntity()
    fun insertProductEntity(productEntity: ProductEntity):Long{
       return baseEntity.insertProduct(productEntity)
    }
    fun readProductEntity(stateProduct: StateProduct): List<ProductEntity> {
        return baseEntity.getAllDataStateProduct(stateProduct)
    }

    fun getAllEntity():List<ProductEntity> {
        return baseEntity.getAllProductEntity()
    }

    fun deleteProductEntity(id:Int){
        baseEntity.deleteProduct(id)
    }
    fun productEntityUpdate(productEntity: ProductEntity){
        baseEntity.updateProduct(productEntity)
    }
    fun deleteAllProductEntity(){
        baseEntity.deleteAllProducts()
    }
}