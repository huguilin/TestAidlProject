// IReceiveMsgListener.aidl
package com.hugl.testaidlproject;

// Declare any non-default types here with import statements
import com.hugl.testaidlproject.Messages;


interface IReceiveMsgListener {
    void onReceive(in Messages msg);
}