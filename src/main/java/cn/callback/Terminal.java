package cn.callback;

import cn.callback.constant.Constants;
import cn.callback.pojo.Request;
import cn.callback.pojo.RequestBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/2 下午7:17
 * 终端设备
 */
public class Terminal extends Thread {
    private Boolean flag = true;
    private InputStreamReader isr;
    private BufferedReader br;
    private OutputStreamWriter osw;
    private BufferedWriter bw;
    private Socket socket;

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
                System.out.println(str);

                System.out.println("终端接收到的读卡命令参数：");
                //获取传递过来的参数
                String sn = object.getString("sn");
                String timestamp = object.getString("timestamp");
                String trans_code = object.getJSONObject("body").getString("trans_code");
                int seq = object.getJSONObject("body").getInteger("seq");
                System.out.println("sn: " + sn + "  timestamp: " + timestamp + "  tans_code: " + trans_code + "  seq: " + seq);
                Request cardInfoReq = new Request();
                RequestBody body=new RequestBody();
                body.setSeq(seq+1);
                cardInfoReq.setSn(sn);
//                cardInfoReq.getBody().setSeq(seq + 1);
                cardInfoReq.setTimestamp(timestamp);
                System.out.println("此次接收到的命令是：　" + object.getJSONObject("body").getString("rsp_data"));
                if (("80B0000020").equals(object.getJSONObject("body").getString("rsp_data"))){
                    System.out.println("开始设置身份证数据....");
                    body.setReq_data(Constants.CARD_INFO_TEST);
                    i++;
                    //flag = false;
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
                System.out.println("终端获取的最终结果： ");
                System.out.println("sn: " + object.getString("sn"));
                System.out.println("trans_code: " + object.getJSONObject("body").getString("trans_code"));
                System.out.println("timestamp: " + object.getString("timestamp"));
                System.out.println("seq: " + object.getJSONObject("body").getString("seq"));
                System.out.println("result: " + object.getJSONObject("body").getString("rsp_data"));
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
                decodeRequest.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
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

    public static void main(String[] args) {
        Terminal terminal = new Terminal("127.0.0.12", 1234);
        terminal.start();
    }

}


