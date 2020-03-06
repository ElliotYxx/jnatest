package cn.callback;

import cn.callback.constant.Constants;
import cn.callback.pojo.Request;
import cn.callback.pojo.RequestBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/2 下午7:17
 * 终端设备
 */
public class Terminal extends Thread {
    private InputStreamReader isr;
    private BufferedReader br;
    private OutputStreamWriter osw;
    private BufferedWriter bw;
    private Socket socket;

    private SimpleDateFormat format = new SimpleDateFormat("YYYY-mm-dd HH:mm:ss:SSSS");

    /**
     * 解码请求
     */
    public Request decodeRequest = new Request();

    /**
     * 构造方法配置端口与ip
     */
    public Terminal(String host, int port){
        try{
            socket = new Socket(host, port);
            socket.setSoTimeout(5000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            //发送解码请求
            new SendDecodeThread().start();
            //接收平台传过来的数据
            int i = 0;
            while(i < 5){
                isr = new InputStreamReader(socket.getInputStream());
                br = new BufferedReader(isr);
                String str = br.readLine();
                JSONObject object = JSONObject.parseObject(str);
                System.out.println("终端接收到的读卡命令参数： \n" + str);
                //获取传递过来的参数
                String sn = object.getString("sn");
                String timestamp = object.getString("timestamp");
                int seq = object.getJSONObject("body").getInteger("seq");
                Request cardInfoReq = new Request();
                RequestBody body=new RequestBody();
                body.setSeq(seq+1);
                cardInfoReq.setSn(sn);
                cardInfoReq.setTimestamp(timestamp);
                if (("80B0000020").equals(object.getJSONObject("body").getString("rsp_data"))){
                    System.out.println("开始设置身份证数据....");
                    body.setReq_data(Constants.CARD_INFO_TEST);
                    i++;
                }else{
                    body.setReq_data("9000");
                    i++;
                }
                body.setTrans_code(Constants.TER_PLAT);
                cardInfoReq.setBody(body);
                new SendReqThread(cardInfoReq).start();
            }

            new GetResultThread().start();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class SendReqThread extends Thread{
        private Request request;
        SendReqThread(Request request){
            this.request = request;
        }
        @Override
        public void run() {
            try{
                String data = JSON.toJSONString(request);
                bw.write(data + "\n");
                bw.flush();

            }catch (Exception e){
                System.out.println("发送身份证信息错误...");
                e.printStackTrace();
            }
        }
    }

    class GetResultThread extends Thread{
        @Override
        public void run() {
            try{
                isr = new InputStreamReader(socket.getInputStream());
                br = new BufferedReader(isr);
                //平台结果返回
                Thread.sleep(5000);
                String str = br.readLine();
                JSONObject object = JSONObject.parseObject(str);
                System.out.println(str);
                //获取传输的数据
                System.out.println("终端获取的最终结果： \n" + str + "\n");
            }catch (Exception e){
                System.out.println("接收结果失败...");
                e.printStackTrace();
            }
        }
    }


    /**
     * 发送解码请求的线程
     */
    class SendDecodeThread extends Thread{
        @Override
        public void run() {
            try{
                osw = new OutputStreamWriter(socket.getOutputStream());
                bw = new BufferedWriter(osw);
                System.out.println("终端发送解码请求...");
                //注入sn
                decodeRequest.setSn("123456789");
                //注入时间戳
                decodeRequest.setTimestamp(format.format(new Date()));
                //注入业务类型
                RequestBody body=new RequestBody();
                body.setReq_data("解码请求");
                body.setSeq(1);
                body.setTrans_code(Constants.DECODE_REQ);
                decodeRequest.setBody(body);
//                decodeRequest.getBody().setTrans_code(Constants.DECODE_REQ);
//                //注入指令序列
//                decodeRequest.getBody().setSeq(1);
//                decodeRequest.getBody().setReq_data("解码请求");
                String data = JSON.toJSONString(decodeRequest);
                bw.write(data + "\n");
                bw.flush();
            }catch (Exception e){
                System.out.println("发送解码请求出错...");
                e.printStackTrace();
            }
        }
    }


}


