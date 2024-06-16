package com.example.task

import com.example.task.adapter.ClothingAdapter
import com.example.task.databinding.ActivityMainBinding
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task.repository.ClothingRepositoryImpl
import com.example.task.viewmodel.ClothingViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    lateinit var clothingAdapter: ClothingAdapter
    lateinit var clothingViewModel: ClothingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)


        clothingAdapter= ClothingAdapter(this@MainActivity,ArrayList())

        var repo = ClothingRepositoryImpl()
        clothingViewModel = ClothingViewModel(repo)

        clothingViewModel.fetchAllProducts()

        clothingViewModel.productList.observe(this){
            it?.let { products ->
                clothingAdapter.updateData(products)
            }
        }
        clothingViewModel.loadingState.observe(this){loadingState->
            if(loadingState){
                mainBinding.progressMain.visibility = View.VISIBLE
            }else{
                mainBinding.progressMain.visibility = View.GONE
            }
        }


        mainBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = clothingAdapter
        }


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var id = clothingAdapter.getProductID(viewHolder.adapterPosition)
                var imageName = clothingAdapter.getImageName(viewHolder.adapterPosition)

                clothingViewModel.deleteProducts(id){success,message->
                    if (success){
                        clothingViewModel.deleteImage(imageName){
                                success,message->
                        }
                        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                    }else{


                        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                    }
                }


            }
        }).attachToRecyclerView(mainBinding.recyclerView)



        mainBinding.floatingActionButton.setOnClickListener {
            var intent = Intent(this@MainActivity,
                AddClothingProductActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}