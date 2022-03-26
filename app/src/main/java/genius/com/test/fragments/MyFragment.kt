package genius.com.test.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import genius.com.test.BaseFragment
import genius.com.test.MainActivity
import genius.com.test.R

private const val ARG_NUMBER = "fragment_number"


class MyFragment : BaseFragment(R.layout.fragment_one) {

    private var number = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            number = it.getInt(ARG_NUMBER)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val txtNumber = view.findViewById<TextView>(R.id.number)

        txtNumber.text = number.toString()

        val fabMinus = view.findViewById<ImageButton>(R.id.delete)

        if (number==1) {

            fabMinus.visibility = View.INVISIBLE

        }

        
        val fabPlus = view.findViewById<ImageButton>(R.id.add)

        val page = view.findViewById<RelativeLayout>(R.id.page)

        page.setOnClickListener {


            (requireActivity() as MainActivity).createNewNotification(number)
        }


        fabPlus.setOnClickListener {
            (requireActivity() as MainActivity).addFragment()
        }

        fabMinus.setOnClickListener {

                fabMinus.visibility = View.VISIBLE



            (requireActivity() as MainActivity).removeLastFragment()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(number: Int) =
            MyFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_NUMBER, number)
                }
            }
    }

}