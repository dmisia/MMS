package pl.edu.pwr.lab23.i236764.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_gallery_image.view.*
import pl.edu.pwr.lab23.i236764.R


class GalleryImageAdapter(private val itemList: List<Image>) : RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>(){
    private var context: Context? = null
    var listener: GalleryImageClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() {
            val image = itemList.get(adapterPosition)
            itemView.ivGalleryImage.setImageBitmap(image.img)
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
}