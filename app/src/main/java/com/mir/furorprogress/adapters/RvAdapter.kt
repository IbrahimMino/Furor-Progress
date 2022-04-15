package com.mir.furorprogress.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mir.furorprogress.R
import com.mir.furorprogress.interfaces.ItemClicked
import com.mir.furorprogress.models.Product

data class RvAdapter(var listener: ItemClicked): RecyclerView.Adapter<RvAdapter.ViewHolder>() {
    var productList  = mutableListOf<Product?>()



    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val pName = itemView.findViewById<TextView>(R.id.pName)
        val pCost = itemView.findViewById<TextView>(R.id.pCost)
        val pAddress = itemView.findViewById<TextView>(R.id.pAddress)
        val pDate = itemView.findViewById<TextView>(R.id.pDate)
        val pEdit = itemView.findViewById<ImageView>(R.id.pEdit)
        val pDelete = itemView.findViewById<ImageView>(R.id.pDelete)

        fun onBind(product: Product){
            pName.text = product.nameUz
            pCost.text = product.cost.toString()
            pAddress.text = product.address
            pDate.text = product.createdDate.toString()

            pDelete.setOnClickListener {
                listener.onClickDelete(product,product.id!!)
            }
            pEdit.setOnClickListener {
                listener.onClickUpdate(product)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(productList[position]!!)
       }

    override fun getItemCount(): Int {
        return productList.size
    }
}