package com.rudder.model

import ua.naiksoftware.stomp.BuildConfig
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient

class StompSocketClient {

    companion object{
        fun newInstance(): StompClient {
            return Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://test.rudderuni.com/ws")
        }
    }

}