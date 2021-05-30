package com.example.navigationcomponentapp.ui.start

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.navigationcomponentapp.R
import com.example.navigationcomponentapp.databinding.FragmentStartBinding

class StartFragment : Fragment() {
    private lateinit var listener: OnButtonClicked
    private var binding: FragmentStartBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

       binding = FragmentStartBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.buttonNext?.setOnClickListener {
            listener.onclicked()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnButtonClicked){
            listener = context
        }
    }
    companion object{
        fun newInstance(): StartFragment{
            return StartFragment()
        }
    }

    interface OnButtonClicked{
        fun onclicked()
    }
}