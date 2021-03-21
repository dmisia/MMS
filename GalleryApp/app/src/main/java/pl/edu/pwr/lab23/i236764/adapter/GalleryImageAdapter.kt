package pl.edu.pwr.lab23.i236764.adapter

import android.content.Context
<<<<<<< HEAD
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_gallery_image.view.*
import pl.edu.pwr.lab23.i236764.R
=======
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pwr.lab23.i236764.R
import kotlinx.android.synthetic.main.item_gallery_image.view.*
>>>>>>> 9a4f484f8456d3fe03b4b6acc300cdf4f08716e0


class GalleryImageAdapter(private val itemList: List<Image>) : RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>(){
    private var context: Context? = null
    var listener: GalleryImageClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() {
            val image = itemList.get(adapterPosition)
<<<<<<< HEAD
            itemView.ivGalleryImage.setImageBitmap(image.img)
=======

            itemView.ivGalleryImage.setImageBitmap(image.img)

>>>>>>> 9a4f484f8456d3fe03b4b6acc300cdf4f08716e0
            itemView.setOnClickListener {
                listener?.onClick(adapterPosition)
            }
            itemView.setOnLongClickListener {
                listener?.onLongClick(adapterPosition)!!
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_gallery_image, parent,
            false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
<<<<<<< HEAD

=======
>>>>>>> 9a4f484f8456d3fe03b4b6acc300cdf4f08716e0
}