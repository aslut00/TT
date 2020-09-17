package com.together.tt;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Network {

    private Socket socket;

    private DataOutputStream dos;
    private DataInputStream dis;

    private String myIP;
    private String servIP = "34.68.141.98";
    private int port = 7942;
    private String macAddr;

    int connectionCheck = 0;

    public Network(){

    }

    void connect(){
       Thread checkUpdate = new Thread(){
           public void run(){
                try{
                    macAddr = getMacAddress();
                    Log.w("서버","맥주소 : "+ macAddr);
                    myIP = getLocalIpAddress();
                    socket = new Socket(servIP, port);
                    Log.w("서버 접속됨","서버 접속됨");
                }catch (IOException e1){
                    Log.w("서버 접속안됨","서버 접속안됨");
                    e1.printStackTrace();
                }

                try{
                    dos = new DataOutputStream((socket.getOutputStream()));
                    dis = new DataInputStream(socket.getInputStream());
                    dos.writeUTF(macAddr);
                    dos.flush();

                }catch (IOException e){
                    e.printStackTrace();
                }

               try{
                   String line = "";
                   byte[] receiver =new byte[1024];
                   while(true) {
                       dis.read(receiver);
                       line = new String(receiver);
                       line = line.substring(0,9);
                       Log.w("서버", "From Server : " + line);
                       if(line.equals("connected")) {
                           Log.w("서버", "From Server : " + line);
                           sock_close();
                           break;
                       }
                   }
               }catch (IOException e){
                   e.printStackTrace();
               }
           }

       };

       checkUpdate.start();

    }

    void sock_close(){
        try {
            socket.close();
            connectionCheck = 1;
            Log.w("서버","connection finished");
        }catch (IOException e){
            e.printStackTrace();
        }
    }




    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();

                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress.isLoopbackAddress()) {
                        Log.i("IPAddress", intf.getDisplayName() + "(loopback) | " + inetAddress.getHostAddress());
                    } else {
                        Log.i("IPAddress", intf.getDisplayName() + " | " + inetAddress.getHostAddress());
                    }
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        }catch (SocketException ex){
            Log.e("Error",ex.toString());
        }
        return null;
    }

    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }

}
