package cn.callback;

import cn.callback.constant.Constants;
import cn.callback.pojo.Response;
import cn.callback.pojo.ResponseBody;
import cn.callback.service.JLRC;
import com.alibaba.fastjson.JSON;
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

    private Response sendInfoResp = new Response();

    private OutputStreamWriter osw;
    private BufferedWriter bw;


    public QueryService(int port)
    {
       try{
           serverSocket = new ServerSocket(port);
           serverSocket.setSoTimeout(1000000000);
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
            if (ret > 0){
                sendInfoResp.setRsp_code(Integer.valueOf(ret).toString());
            }else{
                sendInfoResp.setRsp_code(Integer.valueOf(ret).toString());
                sendInfoResp.setRsp_msg(errStr);
            }
            ResponseBody body = new ResponseBody();
            body.setRsp_data(infoStr);
            body.setTrans_code(Constants.RESULT_CODE);
            sendInfoResp.setBody(body);
            new SendInfoThread(sendInfoResp).start();

//            in.close();
//            socket.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    class  SendInfoThread extends Thread{
        private Response response;
        SendInfoThread(Response response){
            this.response = response;
        }
        @Override
        public void run() {
            try{
                osw = new OutputStreamWriter(socket.getOutputStream());
                bw = new BufferedWriter(osw);
                //类型转换
                System.out.println(socket.isConnected());
                String data = JSON.toJSONString(response);
                //System.out.println("发送的result data： " + data);
                bw.write(data + "\n");
                bw.flush();
            }catch (Exception e){
                System.out.println("发送命令错误....");
                e.printStackTrace();
            }
        }
    }
}
