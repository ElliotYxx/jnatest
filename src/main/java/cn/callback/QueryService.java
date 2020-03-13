package cn.callback;

import cn.callback.constant.Constants;
import cn.callback.pojo.Response;
import cn.callback.pojo.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.eidlink.idocr.sdk.constants.PublicParam;
import com.eidlink.idocr.sdk.pojo.request.IdCardCheckParam;
import com.eidlink.idocr.sdk.pojo.result.CommonResult;
import com.eidlink.idocr.sdk.service.EidlinkService;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/2 下午11:08
 */
public class QueryService extends Thread {
    String ip = "testnidocr.eidlink.com";
    int port = 8080;
    String cid = "1421800";
    String appId = "TESTID20200224170526";
    String appKey = "77BAA0434BE6B8AF812356850AC3C3ED";


    Socket socket;
    ServerSocket serverSocket;
    private DataInputStream in;

    private Response sendInfoResp = new Response();

    private OutputStreamWriter osw;
    private BufferedWriter bw;


    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                serverSocket = new ServerSocket(2345);
                serverSocket.setSoTimeout(1000000000);
                System.out.println("等待平台连接....");
                socket = serverSocket.accept();
                System.out.println("平台连接成功....");
                in = new DataInputStream(socket.getInputStream());
                byte[] reqID = new byte[25];
                in.read(reqID);

                String reqid = new String(reqID);
                System.out.println("reqid: " + reqid);
                EidlinkService.initBasicInfo(ip, port, cid, appId, appKey);
                PublicParam publicParam = new PublicParam();
                IdCardCheckParam idCardCheckParam = new IdCardCheckParam(publicParam, reqid);
                CommonResult result = EidlinkService.idCardCheck(idCardCheckParam);
                ResponseBody body = new ResponseBody();
                if ("00".equals(result.getResult())) {
                    System.out.println("=====  文本信息查询返回结果正常：  " + result.getResult() + "====  " + result.getResult_detail());
                    sendInfoResp.setRsp_code("1");
                } else {
                    System.out.println("==== 文本信息查询返回结果异常： " + result.getResult() + "==== " + result.getResult_detail());
                    sendInfoResp.setRsp_msg("查询异常");
                    sendInfoResp.setRsp_code(result.getResult_detail());
                }
                body.setRsp_data(result.getCiphertext());
                body.setPicture(result.getPicture());
                body.setTrans_code(Constants.RESULT_CODE);
                sendInfoResp.setRsp_code(result.getResult());
                System.out.println(JSONObject.fromObject(result).toString());
                sendInfoResp.setBody(body);
                new SendInfoThread(sendInfoResp).start();

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try{
                    serverSocket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    class SendInfoThread extends Thread {
        private Response response;

        SendInfoThread(Response response) {
            this.response = response;
        }

        @Override
        public void run() {
            synchronized (this){
                try {
                    osw = new OutputStreamWriter(socket.getOutputStream());
                    bw = new BufferedWriter(osw);
                    //类型转换
                    System.out.println(socket.isConnected());
                    String data = JSON.toJSONString(response);
                    //System.out.println("发送的result data： " + data);
                    bw.write(data + "\n");
                    bw.flush();
                } catch (Exception e) {
                    System.out.println("发送命令错误....");
                    e.printStackTrace();
                }
            }
        }
    }
}
