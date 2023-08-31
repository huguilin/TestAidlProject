package com.hugl.testaidlproject

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteCallbackList
import android.util.Log
import java.util.concurrent.CopyOnWriteArrayList

class MyService : Service() {

    val remoteList = RemoteCallbackList<IReceiveMsgListener>()

//    val list = Vector<Messages>()
    val list = CopyOnWriteArrayList<Messages>()

    private val binder = object : IMyAidlInterface.Stub(){
        override fun sendMsg(msg: Messages?) {
            msg?.name?.let { Log.e("hgl", "服务端接收Messages对象---$it") }
            val n = remoteList.beginBroadcast()
            for (i in 0 until n){
                val listener = remoteList.getBroadcastItem(i)
                listener?.onReceive(msg)
            }
            remoteList.finishBroadcast()

        }

        override fun getMessagesList(): MutableList<Messages> {
            return list
        }

        override fun setMessagesList(msg: Messages?): Boolean {
            return list.add(msg)
        }

        override fun setBundler(bundle: Bundle?) {
            if (bundle != null){
                bundle.classLoader = classLoader
                val b = bundle.getSerializable("bb") as BundlerData
                Log.e("hgl",b.name)
                val a = bundle.getParcelable("aa") as Messages?
                a?.name?.let { Log.e("hgl", "服务端接收Messages对象---$it") }
            }
        }

        override fun registerReceiveListener(listener: IReceiveMsgListener?) {
            remoteList.register(listener)
        }

        override fun unregisterReceiveListener(listener: IReceiveMsgListener?) {
            remoteList.unregister(listener)
        }

    }


    override fun onCreate() {
        super.onCreate()
        Log.e("hgl","创建服务")
    }

    override fun onBind(intent: Intent): IBinder {
        Log.e("hgl","服务端连接")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e("hgl","服务端断开连接")
        return super.onUnbind(intent)
    }
}