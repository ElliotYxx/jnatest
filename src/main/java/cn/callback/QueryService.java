package cn.callback;

import cn.callback.service.JLRC;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/2 下午11:08
 */
public class QueryService extends Thread{
    byte[] cid = "1421800".getBytes();
    byte[] app_id = "TESTID20200224170526".getBytes();
    byte[] appKey = "77BAA0434BE6B8AF812356850AC3C3ED".getBytes();
    byte[] errMsg = new byte[50];
    byte[] dn = new byte[35];
    byte[] appeidcode = new byte[50];
    byte[] info = new byte[500];
    byte[] picture = new byte[100000];
    byte[] ip = "118.244.229.17:8080".getBytes();
    Socket socket;
    ServerSocket serverSocket;
    private DataInputStream in;


    public QueryService(int port)
    {
       try{
           serverSocket = new ServerSocket(port);
           serverSocket.setSoTimeout(1000000);
       }catch (Exception e)
       {
           e.printStackTrace();
       }
    }


    @Override
    public void run() {
        super.run();
        try {
            System.out.println("等待平台连接....");
            socket = serverSocket.accept();
            System.out.println("平台连接成功....");
            in = new DataInputStream(socket.getInputStream());
            byte[] reqID = new byte[35];
            in.read(reqID);
            System.out.println("接收到的reqID: " );
            for (int i = 0; i < reqID.length; i++) {
                System.out.println((char)reqID[i]);
            }

            //把reqid发送给getinfo处理
            int ret = JLRC.INSTANCE.getInfo(cid, app_id, appKey, reqID, "jWEeQkfogZSJvrS2iDZ".getBytes(), info,
                    picture, dn, appeidcode, errMsg,
                    2, ip);
            BASE64Encoder enc = new BASE64Encoder();
            String errStr = enc.encode(errMsg);
            String dnStr = enc.encode(dn);
            String appeidcodeStr = enc.encode(appeidcode);
            String infoStr = enc.encode(info);
            String picStr = enc.encode(picture);
            System.out.println("ret: " + ret);
            System.out.println("errMsg： " + errStr);
            System.out.println("dn: " + dnStr);
            System.out.println("appeidcode: " + appeidcodeStr);
            System.out.println("info: " + infoStr);
            System.out.println("picture: " + picStr);
            in.close();
            socket.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
