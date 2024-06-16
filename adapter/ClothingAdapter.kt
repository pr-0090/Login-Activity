package com.example.task.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.UpdateClothingActivity
import com.example.task.model.ClothingModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class ClothingAdapter (var context: Context,var data :
ArrayList<ClothingModel>) :
    RecyclerView.Adapter<ClothingAdapter.ClothingViewHolder>(){

    class ClothingViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var productName : TextView = view.findViewById(R.id.lblName)
        var productRate : TextView = view.findViewById(R.id.lblRate)
        var productColor : TextView = view.findViewById(R.id.lblColor)

        var btnEdit : TextView = view.findViewById(R.id.btnEdit)
        var imageView : ImageView = view.findViewById(R.id.imageView45)
        var progressBar : ProgressBar = view.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingViewHolder {
        var view = LayoutInflater.from(parent.context).
        inflate(R.layout.sample_clothingproduct,
            parent,false)

        return ClothingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ClothingViewHolder, position: Int) {
        holder.productName.text = data[position].productName
        holder.productRate.text = data[position].productRate.toString()
        holder.productColor.text = data[position].productColor

        var imageUrl = data[position].url
        Picasso.get().load(imageUrl).into(holder.imageView,object: Callback{
            override fun onSuccess() {
                holder.progressBar.visibility = View.INVISIBLE
            }

            override fun onError(e: Exception?) {
            }

        })

        holder.btnEdit.setOnClickListener {
            var intent = Intent(context, UpdateClothingActivity::class.java)
            intent.putExtra("product",data[position])
            context.startActivity(intent)
        }
    }

    fun getProductID(position: Int) : String {
        return data[position].id
    }

    fun getImageName(position: Int): String{
        return data[position].imageName
    }

    fun updateData(products: List<ClothingModel>){
        data.clear()
        data.addAll(products)
        notifyDataSetChanged()
    }
}