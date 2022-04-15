package com.mir.furorprogress

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mir.furorprogress.adapters.RvAdapter
import com.mir.furorprogress.databinding.ActivityMainBinding
import com.mir.furorprogress.db.RoomDatabase
import com.mir.furorprogress.interfaces.ItemClicked
import com.mir.furorprogress.interfaces.NetworkState
import com.mir.furorprogress.models.Product
import com.mir.furorprogress.models.ProductEntity
import com.mir.furorprogress.servis.NetworkChangeReceiver
import com.mir.furorprogress.utils.StateProduct
import com.mir.furorprogress.viewModel.MainActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(),ItemClicked,NetworkState {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rvAdapter: RvAdapter
    private lateinit var viewModel: MainActivityViewModel
    lateinit var broadcastReceiver: BroadcastReceiver
    var product = Product()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        RoomDatabase.initDatabase(this)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        initializeRv()
        createUserObserve()
        onClickedCreateButton()
        updateProductObserve()
        broadcastReceiver = NetworkChangeReceiver(this)
        registerNetworkBrocastReciver()


    }


    private fun onClickedCreateButton() {
        binding.btnCreate.setOnClickListener {
            val view = View.inflate(this,R.layout.product_dialog,null)
            val builder = AlertDialog.Builder(this)
            builder.setView(view)
            builder.setCancelable(false)

            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val cancelBtn = view.findViewById<Button>(R.id.dialogBtnCancel)
            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }


            val dialogTypeId = view.findViewById<EditText>(R.id.dialogTypeId)
            val dialogName = view.findViewById<EditText>(R.id.dialogName)
            val dialogCost = view.findViewById<EditText>(R.id.dialogCost)
            val dialogAddress = view.findViewById<EditText>(R.id.dialogAddress)
            val okBtn = view.findViewById<Button>(R.id.dialogBtnOk)
            okBtn.setOnClickListener {
                if (dialogName.text.trim().isNotEmpty() && dialogCost.text.trim().isNotEmpty() && dialogAddress.text.trim().isNotEmpty() && dialogTypeId.text.trim().isNotEmpty()){
                   val typeId = dialogTypeId.text.trim().toString().toInt()
                   val name = dialogName.text.trim().toString()
                    val cost = dialogCost.text.trim().toString().toInt()
                    val address = dialogAddress.text.trim().toString()
                     product = Product(cost,address,id = null,typeId, nameUz = name)
                     viewModel.createUser(product)
                    dialog.dismiss()

                }else{
                    Toast.makeText(this, "The data was not entered in full!", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
    private fun initializeRv(){
        val decoration = DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(decoration)
        rvAdapter = RvAdapter(this)
        binding.recyclerView.adapter = rvAdapter
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun initializeViewModel(){
            viewModel.getProductsObserver().observe(this@MainActivity, Observer {
                if (it == null){
                   // Toast.makeText(applicationContext, "Data not found", Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.deleteAllProducts()
                    viewModel.insertProduct(it as ArrayList<Product>)
                    rvAdapter.productList =it.toMutableList()
                    rvAdapter.notifyDataSetChanged()

                }
            })
            viewModel.getProducts()
    }
    private fun createUserObserve(){
        viewModel.getCreateUserObservable().observe(this, Observer {
            if (it == null){
                val productEntity = ProductEntity(product.cost,product.address,product.id,product.id,product.productTypeId,product.nameUz,product.createdDate,false,StateProduct.CREATE)
                viewModel.insertProductEntity(productEntity)

                viewModel.insertProduct(arrayListOf(product))
                rvAdapter.productList = viewModel.readProduct().toMutableList()
                rvAdapter.notifyDataSetChanged()

                 }else{
                initializeViewModel()
            }
        })
    }
    private fun updateProductObserve(){
        viewModel.getUpdateProductObserverable().observe(this, Observer {
            if (it == null){
                val productEntity = ProductEntity(product.cost,product.address,null,product.id,product.productTypeId,product.nameUz,product.createdDate,false,StateProduct.UPDATE)
                viewModel.insertProductEntity(productEntity)

            }else{
                initializeViewModel()
            }
        })

        }
    private fun deleteProduct(productId: Int) {
        viewModel.deletProduct(productId.toString())
        viewModel.getDeleteProductObserverable().observe(this, Observer {
            if (it == null){
                val productEntity = ProductEntity(product.cost,product.address,productId,product.id,product.productTypeId,product.nameUz,product.createdDate,false,StateProduct.DELETE)
                viewModel.insertProductEntity(productEntity)
                viewModel.deleteProduct(productId)
                rvAdapter.productList =viewModel.readProduct().toMutableList()
                rvAdapter.notifyDataSetChanged()
            }else{
                initializeViewModel()
            }
        })


    }
    private fun updateProduct(pro: Product) {
        val view = View.inflate(this,R.layout.product_dialog,null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val cancelBtn = view.findViewById<Button>(R.id.dialogBtnCancel)
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        val dialogTypeId = view.findViewById<EditText>(R.id.dialogTypeId)
        val dialogName = view.findViewById<EditText>(R.id.dialogName)
        val dialogCost = view.findViewById<EditText>(R.id.dialogCost)
        val dialogAddress = view.findViewById<EditText>(R.id.dialogAddress)
        val okBtn = view.findViewById<Button>(R.id.dialogBtnOk)

        dialogTypeId.setText(pro.productTypeId.toString())
        dialogName.setText(pro.nameUz)
        dialogCost.setText(pro.cost.toString())
        dialogAddress.setText(pro.address.toString())



        okBtn.setOnClickListener {
            if (dialogName.text.trim().isNotEmpty() && dialogCost.text.trim().isNotEmpty() && dialogAddress.text.trim().isNotEmpty() && dialogTypeId.text.trim().isNotEmpty()){
                val typeId = dialogTypeId.text.trim().toString().toInt()
                val name = dialogName.text.trim().toString()
                val cost = dialogCost.text.trim().toString().toInt()
                val address = dialogAddress.text.trim().toString()
                 product = Product(cost,address,pro.id,typeId,nameUz = name)
                viewModel.updateProduct(product)
                viewModel.productUpdate(product)
                dialog.dismiss()

                rvAdapter.productList = viewModel.readProduct().toMutableList()
                rvAdapter.notifyDataSetChanged()

            }else{
                Toast.makeText(this, "The data was not entered in full!", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun stateInternetForToolBar(isOnline: Boolean){
        if (isOnline){
            binding.state.setText("Online")
            binding.state.setTextColor(Color.GREEN)
            binding.lottieLayerName.setAnimation(R.raw.wifi1)
            binding.lottieLayerName.animate()
        }else{
            binding.state.setText("Offline")
            binding.state.setTextColor(Color.RED)
            binding.lottieLayerName.setAnimation(R.raw.wifi2)
            binding.lottieLayerName.animate()
        }
    }
    private fun registerNetworkBrocastReciver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }
    private fun unregisterNetwork(){
        try {
            unregisterReceiver(broadcastReceiver)
        }catch (e:IllegalArgumentException){
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetwork()
    }
    override fun onClickDelete(product: Product,position: Int) {
        this.product =product

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete product")
        builder.setMessage("Do you delete the product?")
        builder.setIcon(R.drawable.ic_baseline_delete_24)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            deleteProduct(position)
        }

        builder.setNegativeButton("No"){dialogInterface, which ->        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }




    override fun onClickUpdate(product: Product) {
        updateProduct(product)
    }
    override fun isCheckNetwork(isOnline: Boolean) {
        if(isOnline) {
            initializeViewModel()
           val create = viewModel.readProductEntity(StateProduct.CREATE)
            val delete = viewModel.readProductEntity(StateProduct.DELETE)
            val update = viewModel.readProductEntity(StateProduct.UPDATE)


            GlobalScope.launch (Dispatchers.IO){
                if (delete.isNotEmpty()){
                    for (i in delete.indices)
                        viewModel.deletProduct(delete[i].productId.toString())
                }
            }
            GlobalScope.launch (Dispatchers.IO){
                if (create.isNotEmpty()){
                    for (i in create.indices){
                        val data =Product(create[i].cost,create[i].address,null,create[i].productTypeId,create[i].nameUz)
                        viewModel.createUser(data)
                        Log.e("MINO", "create: ${create[i]}")
                    }
                }
            }
            GlobalScope.launch (Dispatchers.IO){
                if (update.isNotEmpty()){
                    for (i in update.indices){
                        var data =Product(update[i].cost,update[i].address,update[i].productId,update[i].productTypeId,update[i].nameUz)
                        viewModel.updateProduct(data)


                    }
                }
            }
             viewModel.deleteAllProductEntity()

        }else {
            rvAdapter.productList = viewModel.readProduct().toMutableList()
            rvAdapter.notifyDataSetChanged()
        }
          stateInternetForToolBar(isOnline)
        }


}