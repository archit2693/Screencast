package com.example.gemswin.screancasttest;

import java.net.Socket;

/**
 * Created by Iron_Man on 14/02/17.
 */

public class SocketHandler {
    private static Socket socket;

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket){
        SocketHandler.socket = socket;
    }
}