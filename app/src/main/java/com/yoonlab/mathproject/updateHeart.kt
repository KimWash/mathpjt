package com.yoonlab.mathproject

import android.content.ContentValues.TAG
import android.os.AsyncTask
import android.util.Log
import com.instacart.library.truetime.TrueTime
import com.instacart.library.truetime.TrueTimeRx
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor


var heart_recharging_interval = 10*1000 // 밀리초 단위기때문에 곱하는 수가 초.
var heart_max:Int? = null
var heart:Int = 5
var heart_recharge_next:Long? = null
var date: Date? = null
var elapsed_time:Long? = null
var rechargable_hearts:Int? = null


class updateHeart(){
    companion object{
        fun update_heart(){
            var thread = Thread(Runnable {
                run{
                        while(true){
                            try{
                                Thread.sleep(10*1000)
                                Thread(Runnable{
                                    TrueTimeRx.build()
                                        .initializeRx("ntp2.kornet.net")
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(
                                            { date: Date ->
                                                Log.e(TAG, "TrueTime was initialized and we have a time: $date")
                                            }
                                        ) { throwable: Throwable -> throwable.printStackTrace() }

                                    date = TrueTimeRx.now()
                                    Log.e("Time", "SEQ1 시간 불러오기 성공. $date")
                                    if (heart_max == null) {
                                    heart = 5
                                    heart_max = 5
                                    Log.e("Debug", "초기화 안되서 추가함")
                                    }else{
                                       // Log.e("heart_recharge_next", SimpleDateFormat("hh:mm:ss").format(Date(heart_recharge_next!!)))
                                    }
                                    Log.e("Debug", "SEQ2")
                                    if (heart_recharge_next == null){
                                        Log.e("Debug", "첫 구동.")
                                    }else{
                                        if (heart_max == heart ) {
                                            Log.e("Debug", "하트가 최대치이거나 충전시간에 도달하지 않았음.")
                                            return@Runnable
                                        }
                                    }


                                    Log.e("하트", heart_recharging_interval.toString() + heart_max.toString() + heart.toString() + heart_recharge_next.toString())
                                }).start()
                            }
                            catch(e:InterruptedException){
                            }

                        }
                }
            }).start()
        }
    }

}