package com.luoma

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    // Enum для более удобного определения фрагментов
    enum class fr { MAIN, AUTH }
    // Хранит enum текущего фрагмента
    lateinit var currentFragment: fr

    // Firebase Auth
    val mAuth = FirebaseAuth.getInstance()
    // Firebase Database
    val mReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Проверяем авторизирован ли уже пользователь
        if (mAuth.currentUser == null) {
            loadFragment(fr.AUTH)   // Открываем фрагмент с авторизацией
        } else {
            loadFragment(fr.MAIN)   // Открываем "Главную"
        }
    }

    // Переключение фрагмента
    fun loadFragment(id: fr) {
        lateinit var fragment: Fragment
        when (id) {
            fr.MAIN ->  null
            fr.AUTH -> null
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_frameLayout, fragment, id.toString())
            .commit()
        currentFragment = id
    }


    // Обрабатываем логику нажатия кнопки назад в зависимости от текущего активного фрагмента
    override fun onBackPressed() {
        when (currentFragment) {
            fr.MAIN -> showExitDialog() // Показываем окно с подтверждением выхода из приложения
            fr.AUTH -> showExitDialog() // Показываем окно с подтверждением выхода из приложения
        }
    }


    // Показывает всплывающее окно с вопросом о выходе из приложения
    private fun showExitDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.exit_dialog_title))
            .setMessage(getString(R.string.exit_dialog_messege))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->  finishAffinity() }
            .setNegativeButton(getString(R.string.no)) { d, _ -> d.dismiss() }
        dialog.create().show()
    }
}
