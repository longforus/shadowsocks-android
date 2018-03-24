package com.github.shadowsocks

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.shadowsocks.utils.HttpManager
import com.github.shadowsocks.utils.ToastUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val token: String by lazy {
        "123"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener {
            login()
        }
        btn_register.setOnClickListener {
        startActivity(Intent(this,RegisterActivity::class.java))
        }

    }

    private fun login() {
        val userName = et_account.text.toString().trim()
        val pwd = et_password.text.toString().trim()
        if (userName.isEmpty()) {
            ToastUtil.nonNull(this, "帐号")
            return
        }
        if (pwd.isEmpty()) {
            ToastUtil.nonNull(this, "密码")
            return
        }

        HttpManager.httpService.login(mapOf("userName" to userName, "password" to pwd, "token" to token)).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe {

        }
    }
}
