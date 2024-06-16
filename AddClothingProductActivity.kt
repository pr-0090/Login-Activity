package com.example.task

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.task.databinding.ActivityAddClothingProductBinding
import com.example.task.model.ClothingModel
import com.example.task.repository.ClothingRepositoryImpl
import com.example.task.utils.ImageUtils
import com.example.task.utils.LoadingUtils
import com.example.task.viewmodel.ClothingViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.UUID

class AddClothingProductActivity : AppCompatActivity() {
    lateinit var addClothingProductBinding: ActivityAddClothingProductBinding

    lateinit var loadingUtils: LoadingUtils

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
        addClothingProductBinding = ActivityAddClothingProductBinding.inflate(layoutInflater)
        setContentView(addClothingProductBinding.root)

        loadingUtils = LoadingUtils(this
        )

        imageUtils = ImageUtils(this)
        imageUtils.registerActivity { url ->
            url.let {
                imageUri = it
                Picasso.get().load(it).into(addClothingProductBinding.imageBrowse)
            }

        }

        var repo = ClothingRepositoryImpl()
      clothingViewModel = ClothingViewModel(repo)


        addClothingProductBinding.imageBrowse.setOnClickListener {
            imageUtils.launchGallery(this)
        }

        addClothingProductBinding.button.setOnClickListener {
            if (imageUri != null) {

                uploadImage()
            } else {
                Toast.makeText(
                    applicationContext, "Please upload image first",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    fun uploadImage() {
        loadingUtils.showLoading()
        var imageName = UUID.randomUUID().toString()
        imageUri?.let {
            clothingViewModel.uploadImages(imageName,it) { success, imageUrl,message ->
                if(success){
                    addProduct(imageUrl,imageName)
                }else{
                    Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun addProduct(url: String?, imageName: String?) {
        var productName: String = addClothingProductBinding.editTextName.text.toString()
        var color: String = addClothingProductBinding.editTextColor.text.toString()
        var rate: Int = addClothingProductBinding.editTextRate.text.toString().toInt()

        var data = ClothingModel("",productName,rate,color,
            url.toString(),imageName.toString())

        clothingViewModel.addProducts(data){
                success, message ->
            if(success){
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                finish()
                loadingUtils.dismiss()
            }else{
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                loadingUtils.dismiss()
            }
        }

    }


}