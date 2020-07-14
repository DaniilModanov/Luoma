package com.luoma

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_auth.view.*

class AuthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_auth, container, false)
        val mainActivity = activity as MainActivity

        view.frAuth_loginWithMail.setOnClickListener {
            mainActivity.loadFragment(MainActivity.fr.AUTH_EMAIL, MainActivity.frAnim.OPEN)
        }

        return view
    }
}