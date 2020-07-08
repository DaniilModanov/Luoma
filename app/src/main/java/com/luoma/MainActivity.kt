package com.luoma

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // Enum для более удобного определения фрагментов
    enum class fr { MAIN, AUTH }

    // Анимация смены фрагмента
    enum class frAnim { FADE, OPEN, CLOSE }

    // Хранит enum текущего фрагмента
    lateinit var currentFragment: fr

    // Firebase Auth
    val mAuth = FirebaseAuth.getInstance()

    // Firebase Database
    val mReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBottomMenu(R.id.item_home2)
        // Проверяем авторизирован ли уже пользователь
        if (mAuth.currentUser == null) {
//            loadFragment(fr.AUTH)   // Открываем фрагмент с авторизацией
            hideBottomMenu(true)
        } else {
//            loadFragment(fr.MAIN)   // Открываем "Главную"
            hideBottomMenu(false)
        }
    }

    // Инициализируем логику нижнего меню, в параметр можем передать ID элемента,
    // который должен быть сразу выбран (по умолчанию: Главная)
    fun initBottomMenu(id: Int = R.id.item_home) {
        // Добавляем слушателя при нажатии на элементы нижнего меню
        main_bottomNavigationView.setOnNavigationItemSelectedListener {
            // Смотрим на какой элемент с ID было нажатие
            when (it.itemId) {
                R.id.item_home -> Log.wtf("TEST", "HOME")
                R.id.item_home2 -> Log.wtf("TEST", "HOME2")
            }
            // Нужно вернуть true потому что мы обработали нажатие
            return@setOnNavigationItemSelectedListener true
        }
        // Задаём какой элемент показывать активным
        main_bottomNavigationView.selectedItemId = id
    }

    // Спрятать нижнее меню (по умолчанию: true)
    fun hideBottomMenu(checker: Boolean = true) {
        if (checker) {
            main_bottomNavigationView.visibility = View.GONE
        } else {
            main_bottomNavigationView.visibility = View.VISIBLE
        }
    }

    // Переключение фрагмента
    // id - какой фрагмент отобразить
    // animType - какую анимацию использовать для смены фрагмента (по умолчанию: FADE)
    fun loadFragment(id: fr, animType: frAnim = frAnim.FADE) {
        // Выбираем какой фрагмент следует загрузить
        lateinit var fragment: Fragment
        when (id) {
            fr.MAIN -> null
            fr.AUTH -> null
        }
        val sfm = supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_frameLayout, fragment, id.toString())
        // Добавляем анимацию смены фрагмента
        when (animType) {
            frAnim.FADE -> sfm.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            frAnim.OPEN -> sfm.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            frAnim.CLOSE -> sfm.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
        }
        sfm.commit() // Публикуем изменение
        currentFragment = id // Сохраняем id текущего фрагмента
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
            .setTitle(getString(R.string.exit_dialog_title)) // Заголовок
            .setMessage(getString(R.string.exit_dialog_messege)) // Текст сообщения
            .setPositiveButton(getString(R.string.yes)) { _, _ -> finishAffinity() } // Закрываем приложение
            .setNegativeButton(getString(R.string.no)) { d, _ -> d.dismiss() } // Закрываем диалоговое окно
        dialog.create().show()
    }


    // Показать загрузку, а также отключить клики по экрану (по умолчанию: true)
    fun showLoadingLayout(checker: Boolean = true) {
        if (checker) {
            main_loadingLayout.visibility = View.VISIBLE
            // Делаем обработчик нажатия, чтобы никакие клики не работали во время загрузки
            main_loadingLayout.setOnClickListener {}
        } else {
            main_loadingLayout.visibility = View.GONE
        }
    }
}
