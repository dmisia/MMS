package pl.edu.pwr.lab23.i236764.adapter

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class Image(
    val imagePath: String?,
    val title: String?,
    var img: Bitmap,
    val created: String?,
    val type: String?,
    var isStar: Boolean,
    var keys: ArrayList<String>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable<Bitmap>(Bitmap::class.java.classLoader) as Bitmap,
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        TODO("keys")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imagePath)
        parcel.writeString(title)
        parcel.writeParcelable(img, flags)
        parcel.writeString(created)
        parcel.writeString(type)
        parcel.writeByte(if (isStar) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }
}
