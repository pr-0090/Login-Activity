package com.example.task.model

import android.os.Parcel
import android.os.Parcelable

class ClothingModel(
    var id : String = "",
    var productName : String = "",
    var productRate : Int = 0,
    var productColor : String = "",
    var url : String = "",
    var imageName : String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(productName)
        parcel.writeInt(productRate)
        parcel.writeString(productColor)
        parcel.writeString(url)
        parcel.writeString(imageName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ClothingModel> {
        override fun createFromParcel(parcel: Parcel): ClothingModel {
            return ClothingModel(parcel)
        }

        override fun newArray(size: Int): Array<ClothingModel?> {
            return arrayOfNulls(size)
        }
    }
}