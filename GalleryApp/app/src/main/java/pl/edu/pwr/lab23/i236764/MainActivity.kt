package pl.edu.pwr.lab23.i236764

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Build
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import pl.edu.pwr.lab23.i236764.adapter.GalleryImageAdapter
import pl.edu.pwr.lab23.i236764.adapter.GalleryImageClickListener
import pl.edu.pwr.lab23.i236764.adapter.Image
import pl.edu.pwr.lab23.i236764.fragment.DeleteInfoFragment
import pl.edu.pwr.lab23.i236764.fragment.GalleryFullscreenFragment
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private val imageList = ArrayList<Image>()
private val imageListBackUp = ArrayList<Image>()
private const val FILE_NAME = "photo.jpg"
private const val FILE_VID_NAME = "vid.mp4"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File
private lateinit var videoFile: File
private var n : Int = 1
private var m : Int = 1

class MainActivity : AppCompatActivity(), GalleryImageClickListener {
    private val SPAN_COUNT = 3

    lateinit var galleryAdapter: GalleryImageAdapter
    lateinit var deleteInfoFragment: DeleteInfoFragment
    lateinit var keysSearch: EditText
    lateinit var type: String


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
        keysSearch = findViewById(R.id.tvKeysSearch)
        // init recyclerview
        recyclerView.layoutManager = GridLayoutManager(this, SPAN_COUNT)
        recyclerView.adapter = galleryAdapter
        val toolbar =
            findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        btnTakePicture.setOnClickListener {
            type = "picure"
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            val fileProvider =
                FileProvider.getUriForFile(this, "pl.edu.pwr.lab23.i236764.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }

        btnTakeVideo.setOnClickListener {
            type = "video"
            val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            videoFile = getVideoFile(FILE_VID_NAME)

            val fileProvider =
                FileProvider.getUriForFile(this, "pl.edu.pwr.lab23.i236764.fileprovider", videoFile)
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            if (takeVideoIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_CODE)
            } else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }

        search.setOnClickListener {
            imageList.clear()
            imageList.addAll(imageListBackUp)
            imageList.clear()
            imageList.addAll(imageListBackUp.filter { i ->
                i.keys.containsAll(
                    keysSearch.text.split(
                        ","
                    )
                )
            })
            imageList.addAll(imageListBackUp.filter { i -> i.title!!.contains((keysSearch.text)) })
            galleryAdapter.notifyDataSetChanged()
        }

        loadImages()
    }
    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }
    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    private fun getVideoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".mp4", storageDirectory)
    }


    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && type == "picture") {
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            val title = "IMG-" + n.toString()
            val imagePath = applicationContext.filesDir.absolutePath + "/" + title + ".jpg"
            val image = Image(
                imagePath, title, takenImage,
                getDate(), "type: photo", false, ArrayList()
            )
            storeImage(takenImage, title)
            saveObjectToSharedPreference(
                applicationContext,
                applicationContext.packageName + "_preferences",
                title + ".jpg",
                image
            );

            imageList.add(image)
            imageListBackUp.add(image)
            n++
            val file = File(photoFile.absolutePath)
            file.writeBitmap(takenImage, Bitmap.CompressFormat.JPEG, 85)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && type == "video") {
            val previewImage: Bitmap = ThumbnailUtils.createVideoThumbnail(
                videoFile.absolutePath,
                MediaStore.Video.Thumbnails.MICRO_KIND
            )
            val title = "VID-" + m.toString()
            val imagePath = applicationContext.filesDir.absolutePath + "/" + title + ".mp4"
            val image = Image(
                imagePath, title, previewImage,
                getDate(), "type: video", false, ArrayList()
            )
            saveObjectToSharedPreference(
                applicationContext,
                applicationContext.packageName + "_preferences",
                title + ".mp4",
                image
            );

            imageList.add(image)
            imageListBackUp.add(image)
            m++
            saveVideoToInternalStorage(videoFile.absolutePath, title + ".mp4")
//            val file = File(videoFile.absolutePath)
//            file.writeBitmap(previewImage, Bitmap.CompressFormat.JPEG, 85)
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

        for (i in applicationContext.filesDir!!.list()) {
            println(i)
            val img: Image? =
                getSavedObjectFromPreference(
                    applicationContext,
                    applicationContext.packageName + "_preferences",
                    i,
                    Image::class.java
                )
            if (img != null) {
                if (img.type == "type: photo") {
                    img.img = BitmapFactory.decodeFile(img.imagePath)
                } else {
                    img.img = ThumbnailUtils.createVideoThumbnail(
                        img.imagePath,
                        MediaStore.Video.Thumbnails.MICRO_KIND
                    )
                }
                imageList.add(img)
                imageListBackUp.add(img)
            }
        }
        val maxTitle = imageList.get(imageList.size - 1).title
        n = maxTitle!!.substring(4, maxTitle!!.length).toInt() + 1
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
        imageListBackUp.removeAt(position)
        galleryAdapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_sort_date_desc -> {
                imageList.sortByDescending { image -> image.created }
                galleryAdapter.notifyDataSetChanged()
                return true
            }
            R.id.action_sort_date_asc -> {
                imageList.sortBy { image -> image.created }
                galleryAdapter.notifyDataSetChanged()
                return true
            }
            R.id.action_sort_name_desc -> {
                imageList.sortByDescending { image -> image.title }
                galleryAdapter.notifyDataSetChanged()
                return true
            }
            R.id.action_sort_name_asc -> {
                imageList.sortBy { image -> image.title }
                galleryAdapter.notifyDataSetChanged()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun storeImage(image: Bitmap, fileName: String) {
        try {
            val mImageName = "$fileName.jpg"
            val fos =
                openFileOutput(mImageName, Context.MODE_PRIVATE)
            image.compress(Bitmap.CompressFormat.JPEG, 85, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun saveVideoToInternalStorage(filePath: String, title: String) {
        val newfile: File
        try {
            val currentFile = File(filePath)
            val cw = ContextWrapper(applicationContext)
            newfile = File(applicationContext.filesDir.absolutePath, title)
            if (currentFile.exists()) {
                val `in`: InputStream = FileInputStream(currentFile)
                val out: OutputStream = FileOutputStream(newfile)

                // Copy the bits from instream to outstream
                val buf = ByteArray(1024)
                var len: Int = 0
                while (`in`.read(buf).also({ len = it }) > 0) {
                    out.write(buf, 0, len)
                }
                `in`.close()
                out.close()
                Log.v("", "Video file saved successfully.")
            } else {
                Log.v("", "Video saving failed. Source file missing.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveObjectToSharedPreference(
        context: Context, preferenceFileName: String?,
        serializedObjectKey: String?, `object`: Any?
    ): Unit {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE)
        val sharedPreferencesEditor: SharedPreferences.Editor = sharedPreferences.edit()
        val gson = Gson()
        val serializedObject: String = gson.toJson(`object`)
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject)
        sharedPreferencesEditor.apply()
    }

    fun <GenericClass> getSavedObjectFromPreference(
        context: Context, preferenceFileName: String?,
        preferenceKey: String?, classType: Class<GenericClass>?
    ): GenericClass? {
        val sharedPreferences =
            context.getSharedPreferences(preferenceFileName, 0)
        if (sharedPreferences.contains(preferenceKey)) {
            val gson = Gson()
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType)
        }
        return null
    }

    fun editKeys(list: List<String>, position: Int) {
        imageList.get(position).keys.clear()
        imageList.get(position).keys.addAll(list)
        imageListBackUp.get(position).keys.clear()
        imageListBackUp.get(position).keys.addAll(list)
        galleryAdapter.notifyDataSetChanged()
    }

}