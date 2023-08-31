// IMyAidlInterface.aidl
package com.hugl.testaidlproject;

import com.hugl.testaidlproject.Messages;
import com.hugl.testaidlproject.IReceiveMsgListener;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
        // 发消息
        oneway void sendMsg(in Messages msg);
        //
        List<Messages> getMessagesList();

        boolean setMessagesList(in Messages msg);

        //Bundle
        void setBundler(in Bundle bundle);
      	// 客户端注册监听回调
        void registerReceiveListener(IReceiveMsgListener listener);
      	// 客户端取消监听回调
        void unregisterReceiveListener(IReceiveMsgListener listener);
}