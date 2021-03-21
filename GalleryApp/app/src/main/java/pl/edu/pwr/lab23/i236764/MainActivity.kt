package pl.edu.pwr.lab23.i236764

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import pl.edu.pwr.lab23.i236764.adapter.GalleryImageAdapter
import pl.edu.pwr.lab23.i236764.adapter.GalleryImageClickListener
import pl.edu.pwr.lab23.i236764.adapter.Image
import pl.edu.pwr.lab23.i236764.fragment.DeleteInfoFragment
import pl.edu.pwr.lab23.i236764.fragment.GalleryFullscreenFragment
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private val imageList = ArrayList<Image>()
private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File
private var n : Int = 1

class MainActivity : AppCompatActivity(), GalleryImageClickListener {
    private val SPAN_COUNT = 2

    lateinit var galleryAdapter: GalleryImageAdapter
    lateinit var deleteInfoFragment : DeleteInfoFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // init adapter
        galleryAdapter = GalleryImageAdapter(imageList)
        galleryAdapter.listener = this
        //init delete fragment
        deleteInfoFragment = DeleteInfoFragment()
        deleteInfoFragment.buttonDeleteYes = findViewById(R.id.buttonDeleteYes)
        deleteInfoFragment.buttonDeleteNo = findViewById(R.id.buttonDeleteNo)
        // init recyclerview
        recyclerView.layoutManager = GridLayoutManager(this, SPAN_COUNT)
        recyclerView.adapter = galleryAdapter
        val toolbar =
            findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        btnTakePicture.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            val fileProvider = FileProvider.getUriForFile(this, "pl.edu.pwr.lab23.i236764.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }
        loadImages()
    }

    private fun getPhotoFile(fileName: String): File {
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        val storageDirectory = getExternalFilesDir(DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)

    }


    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            imageList.add(
                Image(
                photoFile.absolutePath, "IMG-"+ n.toString(), takenImage,
                getDate(), "photo", false)
            )
            n++
            File(photoFile.absolutePath).writeBitmap(takenImage, Bitmap.CompressFormat.JPEG, 85)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate(): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatDateTime = now.format(formatter)
        return formatDateTime
    }

    private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
        outputStream().use { out ->
            bitmap.compress(format, quality, out)
            out.flush()
        }
    }

    private fun loadImages() {
        galleryAdapter.notifyDataSetChanged()
    }

    override fun onClick(position: Int) {

        val bundle = Bundle()
        bundle.putSerializable("images", imageList)
        bundle.putInt("position", position)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val galleryFragment = GalleryFullscreenFragment()
        galleryFragment.setArguments(bundle)
        galleryFragment.show(fragmentTransaction, "gallery")
    }

    override fun onLongClick(position: Int): Boolean {
        val bundle = Bundle()
        bundle.putBoolean("visible", true)
        bundle.putInt("position", position)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        deleteInfoFragment.setArguments(bundle)
        deleteInfoFragment.show(fragmentTransaction, "delete")
        return true
    }

    fun deleteImage(position: Int) {
       imageList.removeAt(position)
        galleryAdapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_sort_date_desc -> {
                imageList.sortByDescending{ image -> image.created }
                galleryAdapter.notifyDataSetChanged()
                return true
            }
            R.id.action_sort_date_asc -> {
                imageList.sortBy { image -> image.created }
                galleryAdapter.notifyDataSetChanged()
                return true
            }
            R.id.action_sort_name_asc -> {
                imageList.sortBy { image -> image.created }
                galleryAdapter.notifyDataSetChanged()
                return true
            }
            R.id.action_sort_name_asc -> {
                imageList.sortBy { image -> image.created }
                galleryAdapter.notifyDataSetChanged()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}

