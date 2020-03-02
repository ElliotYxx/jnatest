package cn.test;

import com.sun.jna.Native;
import jdk.internal.util.xml.impl.Input;
import jdk.net.Sockets;
import sun.java2d.pipe.SpanClipRenderer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/2 上午9:09
 */
public class Server extends Thread {

    ServerSocket server = null;
    Socket socket = null;
    byte[] reqID = new byte[35];

    public Server(int port){
        try{
            server = new ServerSocket(port);
            server.setSoTimeout(10000);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //new SendReadThread().start();
        super.run();
        try{
            System.out.println("等待客户端连接...");
            socket = server.accept();
            System.out.println("客户端（" + socket.getInetAddress().getHostAddress() + "） 连接成功...");
            LgetLib INSTANCE = (LgetLib) Native.loadLibrary("eid", LgetLib.class);
            LgetLib.MyCallback mycall  =new LgetLib.MyCallback() {
                public String readCard(String fid, String tidid, String resp) {
                    try{
                            OutputStream os = socket.getOutputStream();
                            System.out.println("本次的resp： " + resp);
                            os.write(resp.getBytes());
                            os.flush();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return "000";
                }
            };
            int num = INSTANCE.JLRCs("1235678", "abacadae", "98541BDA41CA", reqID,
                    0x3D, 2, mycall, 3);
            System.out.println("num: " + num);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class SendReadThread extends Thread{
        OutputStream os = null;
        @Override
        public void run() {
            super.run();


        }
    }

    public static void main(String[] args) {
        Server  server = new Server(1234);
        server.start();
    }
}
