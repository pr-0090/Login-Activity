package com.example.task

import com.example.task.repository.ClothingRepositoryImpl
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.task.R
import com.example.task.databinding.ActivityUpdateClothingBinding
import com.example.task.model.ClothingModel
import com.example.task.utils.ImageUtils
import com.example.task.viewmodel.ClothingViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.UUID

class UpdateClothingActivity : AppCompatActivity() {
    lateinit var updateClothingBinding: ActivityUpdateClothingBinding
    var id = ""
    var imageName = ""
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null

    lateinit var imageUtils: ImageUtils
    lateinit var clothingViewModel: ClothingViewModel

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        updateClothingBinding = ActivityUpdateClothingBinding.inflate(layoutInflater)
        setContentView(updateClothingBinding.root)

        imageUtils = ImageUtils(this)

        var repo = ClothingRepositoryImpl()
        clothingViewModel = ClothingViewModel(repo)

        imageUtils.registerActivity {url->
            imageUri = url
            Picasso.get().load(imageUri).into(updateClothingBinding.imageUpdate)

        }


        var product : ClothingModel? = intent.getParcelableExtra("product")

        updateClothingBinding.editTextNameUpdate.setText(product?.productName)
        updateClothingBinding.editTextRateUpdate.setText(product?.productRate.toString())
        updateClothingBinding.editTextColorUpdate.setText(product?.productColor)


        Picasso.get().load(product?.url).into(updateClothingBinding.imageUpdate)


        id = product?.id.toString()
        imageName = product?.imageName.toString()

        updateClothingBinding.buttonUpdate.setOnClickListener {
            uploadImage()
        }

        updateClothingBinding.imageUpdate.setOnClickListener {
            imageUtils.launchGallery(this)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun uploadImage(){

        imageUri?.let {
            clothingViewModel.uploadImages(imageName,it) { success, imageUrl,message ->
                if(success){
                    updateProduct(imageUrl.toString())
                }else{
                    Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun updateProduct(url : String){
        var updatedName : String= updateClothingBinding.editTextNameUpdate.text.toString()
        var updatedRate: Int = updateClothingBinding.editTextRateUpdate.text.toString().toInt()
        var updatedColor : String = updateClothingBinding.editTextColorUpdate.text.toString()


        var updatedMap = mutableMapOf<String,Any>()
        updatedMap["productName"] = updatedName
        updatedMap["productRate"] = updatedRate
        updatedMap["productColor"] = updatedColor
        updatedMap["id"] = id
        updatedMap["url"] = url

        clothingViewModel.updateProducts(id,updatedMap){
                success, message ->
            if(success){
                Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()

            }
        }


    }
    fun registerActivityForResult() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                val resultcode = result.resultCode
                val imageData = result.data
                if (resultcode == RESULT_OK && imageData != null) {
                    imageUri = imageData.data
                    imageUri?.let {
                        Picasso.get().load(it).into(updateClothingBinding.imageUpdate)
                    }
                }

            })
    }
}