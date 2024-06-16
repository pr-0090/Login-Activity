package com.example.task.repository

import android.net.Uri
import com.example.task.model.ClothingModel

interface ClothingRepository {
    fun addClothingProducts(productModel: ClothingModel, callback:(Boolean, String?)-> Unit)

    fun uploadImages(imageName: String,imageUri : Uri, callback:(Boolean,String?,String?)-> Unit)

    fun getAllClothingProducts(callback: (List<ClothingModel>?, Boolean, String?) -> Unit)

    fun updateClothingProducts(id:String,data: MutableMap<String,Any>?, callback: (Boolean, String?) -> Unit)

    fun deleteClothingProducts(id: String,callback: (Boolean, String?) -> Unit)

    fun deleteImage(imageName: String,callback: (Boolean, String?) -> Unit)
}