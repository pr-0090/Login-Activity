package com.example.task.viewmodel

import com.example.task.model.ClothingModel
import com.example.task.repository.ClothingRepository
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ClothingViewModel(val repository: ClothingRepository) : ViewModel() {

    fun deleteProducts(id: String,callback: (Boolean, String?) -> Unit){
        repository.deleteClothingProducts(id,callback)

    }

    fun deleteImage(imageName: String,callback: (Boolean, String?) -> Unit){
        repository.deleteImage(imageName,callback)

    }

    fun updateProducts(id:String,data: MutableMap<String,Any>?,
                       callback: (Boolean, String?) -> Unit){
        repository.updateClothingProducts(id,data,callback)
    }

    fun uploadImages(imageName:String,imageUri: Uri, callback: (Boolean, String?,String?) -> Unit) {
        repository.uploadImages(imageName,imageUri) { success, imageUrl,message ->
            callback(success, imageUrl,message)
        }
    }
    fun addProducts(productModel: ClothingModel, callback: (Boolean, String?) -> Unit){
        repository.addClothingProducts(productModel,callback)
    }

    var _productList = MutableLiveData<List<ClothingModel>?>()

    var productList = MutableLiveData<List<ClothingModel>?>()
        get() = _productList

    var _loadingState = MutableLiveData<Boolean>()
    var loadingState = MutableLiveData<Boolean>()
        get() = _loadingState

    fun fetchAllProducts(){
        _loadingState.value = true
        repository.getAllClothingProducts { products, success,message ->
            if(products!=null){
                _loadingState.value = false
                _productList.value = products
            }
        }
    }
}