package com.example.mg156.assignment9

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class registerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListenerRegister? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val registerView = inflater.inflate(R.layout.fragment_register, container, false)

        val btnRegister = registerView.findViewById(R.id.register_page_sign_up_button) as Button
        val btnLogin = registerView.findViewById(R.id.register_page_sign_in_button) as Button
        val profileImage = registerView.findViewById(R.id.register_page_image) as ImageView

        btnRegister.setOnClickListener(View.OnClickListener { v -> listener?.onFragmentInteractionRegister(v) })
        btnLogin.setOnClickListener(View.OnClickListener { v -> listener?.onFragmentInteractionRegister(v) })
        profileImage.setOnClickListener(View.OnClickListener { v -> listener?.onFragmentInteractionRegister(v) })

        return registerView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListenerRegister) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListenerRegister {
        // TODO: Update argument type and name
        fun onFragmentInteractionRegister(view: View)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                registerFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
