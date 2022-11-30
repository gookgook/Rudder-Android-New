package com.rudder.model

import ua.naiksoftware.stomp.BuildConfig
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient

class StompSocketClient {

    companion object{
        fun newInstance(): StompClient {
            var socketURL: String
            if (com.rudder.BuildConfig.BASE_URL == "https://test.rudderuni.com") {
                socketURL = "ws://test.rudderuni.com/ws"
            } else {
                socketURL = "ws://api.rudderuni.com/ws"
            }
            return Stomp.over(Stomp.ConnectionProvider.OKHTTP, socketURL)
        }
    }

}