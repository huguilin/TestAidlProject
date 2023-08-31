package com.hugl.myclient

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hugl.myclient.databinding.ActivityMainBinding
import com.hugl.testaidlproject.BundlerData
import com.hugl.testaidlproject.IMyAidlInterface
import com.hugl.testaidlproject.IReceiveMsgListener
import com.hugl.testaidlproject.Messages

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var iMyAidlInterface: IMyAidlInterface? = null

    private lateinit var iReceiveMsgListener: IReceiveMsgListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.startService.setOnClickListener {
            val intent = Intent("com.hugl.testaidlproject.IMyAidlInterface")
            intent.setPackage("com.hugl.testaidlproject")
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }

        binding.stopService.setOnClickListener {
            if (iMyAidlInterface != null){
                unbindService(serviceConnection)
                iMyAidlInterface = null
            }
        }

        binding.getMessage.setOnClickListener {
//            Log.e("hgl","客户端获取到message---${iMyAidlInterface?.message}")
            iMyAidlInterface?.sendMsg(Messages("人物信息","2023-8-24","我是胡",30))

            val bundle = Bundle()
            bundle.putSerializable("bb",(BundlerData("aa","我是Bundler传过来的名字")))
            bundle.putParcelable("aa",Messages("人物信息","2023-8-24","我是胡",30))
            iMyAidlInterface?.setBundler(bundle)
        }

        binding.setMessage.setOnClickListener {
//            iMyAidlInterface?.insert("哈哈，就是我的信息")
            Log.e("hgl","客户端传递数据")
        }


        iReceiveMsgListener = object : IReceiveMsgListener.Stub(){
            override fun onReceive(msg: Messages?) {
                Log.e("hgl","服务端回调的数据---${msg?.name}")
            }

        }

        binding.addList.setOnClickListener {
            iMyAidlInterface?.setMessagesList(Messages("添加2","2023-08-31","222222",22))
        }

        binding.getList.setOnClickListener {
            iMyAidlInterface?.messagesList?.forEach {
                Log.e("hgl","列表---${it.name}")
            }
        }

    }


    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(p1)
            iMyAidlInterface?.registerReceiveListener(iReceiveMsgListener)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.e("hgl", "客户端断开连接")
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        unbindService(serviceConnection)

        iMyAidlInterface?.unregisterReceiveListener(iReceiveMsgListener)
    }
}