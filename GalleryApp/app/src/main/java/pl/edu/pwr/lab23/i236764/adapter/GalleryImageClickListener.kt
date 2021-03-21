package pl.edu.pwr.lab23.i236764.adapter

interface GalleryImageClickListener {

    fun onClick(position: Int)
    fun onLongClick(position: Int): Boolean
}