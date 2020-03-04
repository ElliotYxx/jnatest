package cn.callback;

import cn.callback.service.JLRC;
import com.alibaba.fastjson.JSONObject;
import com.sun.jna.Native;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    byte[] ip = "118.145.9.208:80".getBytes();

    Socket socket;
    ServerSocket serverSocket;

    private InputStreamReader isr;
    private BufferedReader br;


    public QueryService(int port)
    {
       try{
           serverSocket = new ServerSocket(port);
           serverSocket.setSoTimeout(100000);
       }catch (Exception e)
       {
           e.printStackTrace();
       }
    }

    public static void main(String[] args) {
        QueryService queryService=new QueryService(2345);
        queryService.start();
    }

    @Override
    public void run() {
        super.run();
        try {
            System.out.println("等待平台连接....");
            socket = serverSocket.accept();
            System.out.println("平台连接成功....");
            //接收平台发送的数据
            isr = new InputStreamReader(socket.getInputStream());
            br = new BufferedReader(isr);
            //接收平台数据
            String str = br.readLine();
            System.out.println("str:" + str);
            //把reqid发送给getinfo处理
            getInfo(str.getBytes());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void getInfo(byte[] reqID){
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


    }


}
