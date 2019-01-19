package com.example.mg156.assignment9

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class loginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListenerLogin? = null

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
        val loginView = inflater.inflate(R.layout.fragment_login, container, false)

        val btnSignUp = loginView.findViewById(R.id.login_page_btn_signup) as Button
        val btnLogin = loginView.findViewById(R.id.login_page_btn_login) as Button

        btnSignUp.setOnClickListener(View.OnClickListener { v -> listener?.onFragmentInteractionLogin(v) })
        btnLogin.setOnClickListener(View.OnClickListener { v -> listener?.onFragmentInteractionLogin(v) })

        return loginView
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListenerLogin) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListenerLogin {
        // TODO: Update argument type and name
        fun onFragmentInteractionLogin(view: View)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                loginFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
