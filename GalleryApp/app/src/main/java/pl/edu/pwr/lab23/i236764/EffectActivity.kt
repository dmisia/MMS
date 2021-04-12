package pl.edu.pwr.lab23.i236764

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mukesh.imageproccessing.OnProcessingCompletionListener
import com.mukesh.imageproccessing.PhotoFilter
import com.mukesh.imageproccessing.filters.*
import kotlinx.android.synthetic.main.activity_effect.*
import pl.edu.pwr.lab23.i236764.adapter.EffectsAdapter
import pl.edu.pwr.lab23.i236764.adapter.EffectsThumbnail
import pl.edu.pwr.lab23.i236764.adapter.OnFilterClickListener
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class EffectActivity : AppCompatActivity(), OnProcessingCompletionListener, OnFilterClickListener {
    private val REQUEST_PERMISSION: Int = 10001
    private lateinit var result: Bitmap
    private var photoFilter: PhotoFilter? = null

    override fun onProcessingComplete(bitmap: Bitmap) {
        result = bitmap
    }

    override fun onFilterClicked(effectsThumbnail: EffectsThumbnail) {
        photoFilter?.applyEffect(
            BitmapFactory.decodeResource(resources, R.drawable.ing), effectsThumbnail.filter
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_effect)

//        image = intent.extras?.getSerializable("image") as Bitmap
//        title = intent.extras?.getString("title") as String
//        val byteArray: ByteArray? = intent.extras?.getByteArray("image")
//        image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
//        image = BitmapFactory.decodeFile("/data/data/pl.edu.pwr.lab23.i236764/files/IMG-2.jpg")
//        val img : Image? =
//            getSavedObjectFromPreference(
//                applicationContext,applicationContext.packageName+"_preferences", title+".jpg", Image::class.java
//            )
//        image = img!!.img
        initialize()
    }

    private fun initialize() {
        photoFilter = PhotoFilter(effectView, this)
        photoFilter?.applyEffect(BitmapFactory.decodeResource(resources, R.drawable.ing), None())
        effectsRecyclerView.layoutManager =
            LinearLayoutManager(this@EffectActivity, RecyclerView.HORIZONTAL, false)
        effectsRecyclerView.setHasFixedSize(true)
        effectsRecyclerView.adapter = EffectsAdapter(getItems(), this@EffectActivity)
        saveButton.setOnClickListener {
            checkPermissionAndSaveImage()
        }
    }
    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }
    private fun checkPermissionAndSaveImage() {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION)
            } else {
                saveImage()
            }
        } else {
            saveImage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveImage()
                } else {
                    Toast.makeText(this@EffectActivity, "Permission Denied", Toast.LENGTH_SHORT)
                        .show()
                }
                return
            }
        }
    }

    private fun saveImage() {
        val fOut: OutputStream?
        val fileName = Date().time
        val file = File(application.applicationContext.filesDir, "$fileName.jpg")
        fOut = FileOutputStream(file)
        result.compress(Bitmap.CompressFormat.JPEG, 85, fOut)
        fOut.flush()
        fOut.close()
        MediaStore.Images.Media.insertImage(contentResolver, file.absolutePath, file.name, file.name)
        Toast.makeText(this@EffectActivity, "ImageSaved", Toast.LENGTH_SHORT)
            .show()
    }

    private fun getItems(): MutableList<EffectsThumbnail> {
        return mutableListOf(
            EffectsThumbnail("None", None()),
            EffectsThumbnail("AutoFix", AutoFix()),
            EffectsThumbnail("Highlight", Highlight()),
            EffectsThumbnail("Brightness", Brightness()),
            EffectsThumbnail("Contrast", Contrast()),
            EffectsThumbnail("Cross Process", CrossProcess()),
            EffectsThumbnail("Documentary", Documentary()),
            EffectsThumbnail("Duo Tone", DuoTone()),
            EffectsThumbnail("Fill Light", FillLight()),
            EffectsThumbnail("Fisheye", FishEye()),
            EffectsThumbnail("Flip Horizontally", FlipHorizontally()),
            EffectsThumbnail("Flip Vertically", FlipVertically()),
            EffectsThumbnail("Grain", Grain()),
            EffectsThumbnail("Grayscale", Grayscale()),
            EffectsThumbnail("Lomoish", Lomoish()),
            EffectsThumbnail("Negative", Negative()),
            EffectsThumbnail("Posterize", Posterize()),
            EffectsThumbnail("Rotate", Rotate()),
            EffectsThumbnail("Saturate", Saturate()),
            EffectsThumbnail("Sepia", Sepia()),
            EffectsThumbnail("Sharpen", Sharpen()),
            EffectsThumbnail("Temperature", Temperature()),
            EffectsThumbnail("Tint", Tint()),
            EffectsThumbnail("Vignette", Vignette())
        )
    }

}
