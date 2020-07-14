package com.luoma

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_auth_email.view.*
import java.lang.Exception

class AuthEmailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_auth_email, container, false)
        val mainActivity = activity as MainActivity

        view.frAuthEmail_signBtn.setOnClickListener {
            if (checkFields(view, mainActivity)) {
                mainActivity.mAuth.createUserWithEmailAndPassword(
                    view.frAuthEmail_emailET.text.toString(),
                    view.frAuthEmail_passET.text.toString()
                ).addOnSuccessListener {
                    // TODO: Сделать загрузку фрагмента
                }.addOnFailureListener {
                    showError(it, mainActivity)
                }
            }
        }

        view.frAuthEmail_logBtn.setOnClickListener {
            if (checkFields(view, mainActivity)) {
                mainActivity.mAuth.signInWithEmailAndPassword(
                    view.frAuthEmail_emailET.text.toString(),
                    view.frAuthEmail_passET.text.toString()
                ).addOnSuccessListener {
                    // TODO: Сделать загрузку фрагмента
                }.addOnFailureListener {
                    showError(it, mainActivity)
                }
            }
        }

        return view
    }

    // Показать уведомление с ошибкой
    private fun showError(it: Exception, mainActivity: MainActivity) {
        val msg = if (it.localizedMessage == null) it.message else it.localizedMessage
        mainActivity.showSnackCard(MainActivity.snackColor.ERROR, msg.toString())
    }

    // Проверяем корректно ли заполнены EMAIL и PASS
    private fun checkFields(view: View, mainActivity: MainActivity): Boolean {
        // Проверяем ввёл ли свой email пользователь
        if (view.frAuthEmail_emailET.text.isEmpty()) {
            mainActivity.showSnackCard(
                MainActivity.snackColor.ERROR,
                "Вы не ввели электронную почту!"
            )
            return false
        } else if (
        // Првоеряем ввёл ли пользователь корректный формат email
            !android.util.Patterns.EMAIL_ADDRESS
                .matcher(view.frAuthEmail_emailET.text)
                .matches()
        ) {
            mainActivity.showSnackCard(MainActivity.snackColor.ERROR, "Введите корректный EMAIL!")
            return false
            // Проверяем, ввёл ли пользватель пароль
        } else if (view.frAuthEmail_passET.text.isEmpty()) {
            mainActivity.showSnackCard(MainActivity.snackColor.ERROR, "Вы не ввели пароль!")
            return false
            // Проверяем, больше ли пароль 6 символов
        } else if (view.frAuthEmail_passET.text.length < 6) {
            mainActivity.showSnackCard(
                MainActivity.snackColor.ERROR,
                "Пароль должен содержать не менее 6 символов!"
            )
            return false
        }
        return true
    }
}