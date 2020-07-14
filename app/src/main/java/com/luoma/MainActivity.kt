package com.luoma

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // Enum для более удобного определения фрагментов
    enum class fr { MAIN, AUTH, AUTH_EMAIL }

    // Анимация смены фрагмента
    enum class frAnim { FADE, OPEN, CLOSE }

    // Цвет уведомления
    enum class snackColor { ERROR, INFO }

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
            hideBottomMenu()
        } else {
            loadFragment(fr.MAIN)   // Открываем "Главную"
            hideBottomMenu(false)
            initBottomMenu(R.id.item_home2)
        }
    }

    // Инициализируем логику нижнего меню, в параметр можем передать ID элемента,
    // который должен быть сразу выбран (по умолчанию: Главная)
    fun initBottomMenu(id: Int = R.id.item_home) {
        // Добавляем слушателя при нажатии на элементы нижнего меню
        main_bottomNavigationView.setOnNavigationItemSelectedListener {
            // Смотрим на какой элемент с ID было нажатие
            when (it.itemId) {
                R.id.item_home -> {}
                R.id.item_home2 -> {}
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
        fragment = when (id) {
            fr.MAIN -> MainFragment()
            fr.AUTH -> AuthFragment()
            fr.AUTH_EMAIL -> AuthEmailFragment()
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
            fr.AUTH_EMAIL -> loadFragment(fr.AUTH, frAnim.CLOSE)
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

    // Показ кастомного уведомления
    // color - цвет уведомления
    // text - текст, который нужно будет отобразить в уведомлении
    // delay - время показа уведомления
    fun showSnackCard(color: snackColor, text: String, delay: Long = 1500) {
        // Анимация показа уведомления
        val showAnim = AnimationUtils.loadAnimation(this, R.anim.anim_snack_card_show)
        // Анимация скрытия уведомления
        val hideAnim = AnimationUtils.loadAnimation(this, R.anim.anim_snack_card_hide)
        // Устанавливаем задержку, до запуска анимации скрытия уведомления
        hideAnim.startOffset = delay
        hideAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
                main_snackCardLayout.visibility = View.GONE
            }
        })
        showAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
                main_snackCardLayout.startAnimation(hideAnim)
            }
        })
        // Меняем цвет уведомления
        when (color) {
            snackColor.ERROR -> main_snackCardColor.background =
                ColorDrawable(ContextCompat.getColor(this, R.color.colorError))
            snackColor.INFO -> main_snackCardColor.background =
                ColorDrawable(ContextCompat.getColor(this, R.color.colorInfo))
        }
        main_snackCardLayout.visibility = View.VISIBLE
        main_snackCardTextView.text = text
        // Запускаем нашу анимацию
        main_snackCardLayout.startAnimation(showAnim)
    }
}
