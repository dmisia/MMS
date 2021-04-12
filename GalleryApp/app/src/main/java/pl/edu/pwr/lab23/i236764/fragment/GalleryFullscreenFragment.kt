package pl.edu.pwr.lab23.i236764.fragment

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.image_fullscreen.*
import kotlinx.android.synthetic.main.image_fullscreen.view.*
import kotlinx.android.synthetic.main.image_fullscreen.view.star
import kotlinx.android.synthetic.main.video_fullscreen.view.*
import pl.edu.pwr.lab23.i236764.EffectActivity
import pl.edu.pwr.lab23.i236764.MainActivity
import pl.edu.pwr.lab23.i236764.R
import pl.edu.pwr.lab23.i236764.VideoActivity
import pl.edu.pwr.lab23.i236764.adapter.Image
import pl.edu.pwr.lab23.i236764.helper.ZoomOutPageTransformer


class GalleryFullscreenFragment : DialogFragment() {

    private var imageList = ArrayList<Image>()
    private var selectedPosition: Int = 0

    lateinit var tvGalleryTitle: TextView
    lateinit var tvGalleryCreated: TextView
    lateinit var tvGalleryType: TextView
    lateinit var tvKeysEdit: EditText
    lateinit var viewPager: ViewPager

    lateinit var galleryPagerAdapter: GalleryPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gallery_fullscreen, container, false)

        viewPager = view.findViewById(R.id.viewPager)
        tvGalleryTitle = view.findViewById(R.id.tvGalleryTitle)
        tvGalleryCreated = view.findViewById(R.id.tvGalleryCreated)
        tvGalleryType = view.findViewById(R.id.tvGalleryType)
        tvKeysEdit = view.findViewById(R.id.tvKeysEdit)

        galleryPagerAdapter = GalleryPagerAdapter()

        imageList = arguments?.getSerializable("images")  as ArrayList<Image>
        selectedPosition = arguments!!.getInt("position")

        viewPager.adapter = galleryPagerAdapter
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())

        setCurrentItem(selectedPosition)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }

    private fun setCurrentItem(position: Int) {
        viewPager.setCurrentItem(position, false)
    }

    internal var viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
            object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            var keysString =  imageList.get(position).keys.toString()
            tvGalleryTitle.text = imageList.get(position).title
            tvGalleryCreated.text = imageList.get(position).created
            tvGalleryType.text = imageList.get(position).type
            tvKeysEdit.setText(keysString.substring(1, keysString.length-1), TextView.BufferType.EDITABLE);

            tvKeysEdit.setOnEditorActionListener(TextView.OnEditorActionListener { view, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE || event.keyCode === KeyEvent.KEYCODE_ENTER
                    && event.action === KeyEvent.ACTION_DOWN) {
                    view.isCursorVisible = false
                    var keys = tvKeysEdit.text
                    (activity as MainActivity?)!!.editKeys(keys.split(","), position)
                    true
                } else {
                    false
                }
            })
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
        }

        override fun onPageScrollStateChanged(arg0: Int) {
        }

    }

    // gallery adapter
    inner class GalleryPagerAdapter : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val layoutInflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val image = imageList.get(position)
            if (image.type == "type: video"){
                val view =
                    layoutInflater.inflate(R.layout.activity_video, container, false)
                val intent = Intent(activity, VideoActivity::class.java)
                val b = Bundle()
                b.putString("path", image.imagePath)
                intent.putExtras(b)
                startActivity(intent)
                container.addView(view)
                return view
            }
            else {
                val view = layoutInflater.inflate(R.layout.image_fullscreen, container, false)
                view.ivFullscreenImage.setImageBitmap(image.img)
                view.star.setOnClickListener {
                    if (!image.isStar) {
                        image.isStar = true
                        star.setBackgroundDrawable(resources.getDrawable(R.drawable.full_star))
                    } else {
                        image.isStar = false
                        star.setBackgroundDrawable(resources.getDrawable(R.drawable.empty_star))
                    }
                }
                view.edit.setOnClickListener{
                    val intent = Intent(activity, EffectActivity::class.java)
                    startActivity(intent)
                }
                container.addView(view)
                return view
            }

        }

        override fun getCount(): Int {
            return imageList.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj as View
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(obj as View)
        }
    }
}
