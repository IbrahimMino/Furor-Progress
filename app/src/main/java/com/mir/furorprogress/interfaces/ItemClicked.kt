package com.mir.furorprogress.interfaces

import com.mir.furorprogress.models.Product

interface ItemClicked {
    fun onClickDelete(product: Product,position:Int)
    fun onClickUpdate(product: Product)
}