package com.github.shadowsocks

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.github.shadowsocks.utils.Cons
import com.github.shadowsocks.utils.HttpManager
import com.github.shadowsocks.utils.ToastUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.FormBody

class RegisterActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        btn_register.setOnClickListener {
            register()
        }

    }

    private fun register() {
        val userName = et_username.text.toString().trim()
        val pwd = et_pwd.text.toString().trim()
        val phone = et_phone.text.toString().trim()
        if (phone.isEmpty()) {
            ToastUtil.nonNull(this, "手机")
            return
        }
        if (userName.isEmpty()) {
            ToastUtil.nonNull(this, "用户名")
            return
        }
        if (pwd.isEmpty()) {
            ToastUtil.nonNull(this, "密码")
            return
        }
        val dialog = ProgressDialog(this)
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setTitle("注册中...")
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val build = FormBody.Builder().addEncoded("userName", userName).addEncoded("password", pwd).addEncoded("mobilePhone", phone).build()
        HttpManager.httpService.register(build).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
            if (it.get("code").asString == "200") {
                ToastUtil.showShort(this,"注册成功")
                val params = it.get("params").asJsonObject
                Cons.token = params?.get("token")?.asString ?: ""
                Cons.end_date = params?.get("end_date")?.asString ?: ""
                Cons.start_date = params?.get("start_date")?.asString ?: ""
                dialog.dismiss()
            }
        },{e->
            ToastUtil.showShort(this,"注册失败")
            e.printStackTrace()
            dialog.dismiss()
        })
    }
}
