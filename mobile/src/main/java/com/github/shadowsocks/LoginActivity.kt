package com.github.shadowsocks

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.shadowsocks.utils.Cons
import com.github.shadowsocks.utils.HttpManager
import com.github.shadowsocks.utils.ToastUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener {
            login()
        }
        btn_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
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
        val dialog = ProgressDialog(this)
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setTitle("登录中...")
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        HttpManager.httpService.login(mapOf("userName" to userName, "password" to pwd)).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
            dialog.dismiss()
            when (it.get("code").asString) {
                "200" -> {
                    val params = it.get("params").asJsonObject
                    Cons.token = params?.get("token")?.asString ?: ""
                    Cons.end_date = params?.get("end_date")?.asString ?: ""
                    Cons.start_date = params?.get("start_date")?.asString ?: ""
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                else -> ToastUtil.showShort(this, it.get("remark").asString)
            }
        }, { e ->
            ToastUtil.showShort(this, "登录失败")
            dialog.dismiss()
            e.printStackTrace()
        })
    }
}
