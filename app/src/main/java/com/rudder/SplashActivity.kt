package com.rudder

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rudder.model.repository.InitDataRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async


class SplashActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)


            GlobalScope.async {
                val guestInitData = InitDataRepository.instance.getGuestInitData(
                    BuildConfig.VERSION_NAME,
                    "ANDROID"
                )
                if (guestInitData.isSuccessful){
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
                }else {
                    MainScope().async {
                        val builder = AlertDialog.Builder(this@SplashActivity)
                        builder.setTitle("Please update!")
                            .setMessage("")
                            .setPositiveButton("OK",
                                DialogInterface.OnClickListener { dialog, id ->
                                    val intent = Intent(Intent.ACTION_VIEW)
                                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                                    intent.data =
                                        Uri.parse("market://details?id=com.rudder.coursereview")
                                    startActivity(intent)
                                    this@SplashActivity.finishAffinity()
                                })
                            .setNegativeButton("Cancel",
                                DialogInterface.OnClickListener { dialog, id ->
                                    this@SplashActivity.finishAffinity()
                                })
                        // 다이얼로그를 띄워주기
                        builder.show()
                    }

                }

            }





    }
}