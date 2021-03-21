package pl.edu.pwr.lab23.i236764.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import pl.edu.pwr.lab23.i236764.MainActivity
import pl.edu.pwr.lab23.i236764.R


class DeleteInfoFragment : DialogFragment() {

    lateinit var buttonDeleteYes : Button
    lateinit var buttonDeleteNo : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.delete_info, container, false)

        if(view != null) {
            if( arguments != null && arguments!!.getBoolean( "visible")){
                view.setVisibility(View.VISIBLE)
            }
            buttonDeleteNo = view!!.findViewById(R.id.buttonDeleteNo)
            buttonDeleteYes = view!!.findViewById(R.id.buttonDeleteYes)
            buttonDeleteYes.setOnClickListener {
                view.setVisibility(View.INVISIBLE)
                fragmentManager!!.beginTransaction().remove(this).commit()
                (activity as MainActivity?)!!.deleteImage(arguments!!.getInt("position"))
            }
            buttonDeleteNo.setOnClickListener {
                view.setVisibility(View.INVISIBLE)
                fragmentManager!!.beginTransaction().remove(this).commit()
            }
        }
        return view
    }

}
