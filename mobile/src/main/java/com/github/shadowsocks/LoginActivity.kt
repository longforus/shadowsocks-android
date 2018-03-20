package com.github.shadowsocks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.shadowsocks.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener {
            login()
        }

    }

    private fun login() {
        val userName = et_account.text.toString().trim()
        val pwd = et_password.text.toString().trim()
        if (userName.isEmpty()) {
            ToastUtil.nonNull(this,"帐号")
            return
        }
        if (pwd.isEmpty()) {
            ToastUtil.nonNull(this,"密码")
            return
        }

    }
}
