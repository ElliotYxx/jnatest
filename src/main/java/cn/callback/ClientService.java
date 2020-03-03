package cn.callback;
import cn.callback.constant.Constants;
import cn.callback.pojo.Response;
import cn.callback.service.LgetLib;
import cn.callback.service.MyCallback;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/2 下午7:09
 * 门锁（客户端）服务
 */
public class ClientService extends Thread {

    private String cid = "1421800";
    byte[] reqID = new byte[35];

    private InputStreamReader isr;
    private BufferedReader br;
    private OutputStreamWriter osw;
    private BufferedWriter bw;

    private Socket socket;
    private ServerSocket serverSocket;
    /**
     * 读卡请求
     */
    private Response readCardResp = new Response();
    /**
     * 返回最终结果响应
     */
    private Response resultResp = new Response();

    /**
     * 配置端口
     * @param port 端口号
     */
    public ClientService(int port){
        try{
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(10000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    //读取终端发送到平台的信息
    @Override
    public void run() {
        super.run();
        try{
            System.out.println("等待终端连接....");
            socket = serverSocket.accept();
            System.out.println("终端连接成功....");
            //接收终端发送的数据
            isr = new InputStreamReader(socket.getInputStream());
            br = new BufferedReader(isr);
            //接收数据
            String str = br.readLine();
            JSONObject object = JSONObject.parseObject(str);
            //获取传递过来的数据
            String sn = object.getString("sn");
            String timestamp = object.getString("timestamp");
            String trans_code = object.getString("trans_code");

            int seq = object.getInteger("seq");
            System.out.println("sn: " + sn + "  timestamp: " + timestamp + "  tans_code: " + trans_code + "  seq: " + seq);
            readCardResp.setSn(sn);
            readCardResp.setTimestamp(timestamp);
            readCardResp.setSeq(seq);
            readCardResp.setRsp_code("1");
            if ((Constants.DECODE_REQ).equals(trans_code)){
                readCardResp.setTrans_code(Constants.PLAT_TER);
                //接收到解码请求，调用so库
                int result = LgetLib.INSTANCE.JLRCs(cid, socket.getInetAddress().toString(), "98541BDA41CA",
                        reqID, 0x3D, 2, new MyCallback() {
                            public String readCard(String fid, String tidid, String resp) {
                                //设置读卡命令
                                System.out.println("此次生成的读卡命令为：　" + resp);
                                readCardResp.setRsp_data(resp);
                                //发送读卡命令
                                new SendRespThread(readCardResp).start();
                                //接收读卡数据
                                try{
                                    isr = new InputStreamReader(socket.getInputStream());
                                    br = new BufferedReader(isr);
                                    //返还数据
                                    String str = br.readLine();
                                    JSONObject object = JSONObject.parseObject(str);
                                    //获取传递过来的数据
                                    String sn = object.getString("sn");
                                    String timestamp = object.getString("timestamp");
                                    String trans_code = object.getString("trans_code");
                                    int seq = object.getInteger("seq");
                                    String req_data = object.getString("req_data");
                                    System.out.println("终端返回的读卡数据: ");
                                    System.out.println("sn: " + sn + "  timestamp: " + timestamp + "  trans_code: " + trans_code + "  seq: " + seq + "  req_data(身份证数据): " + req_data);
                                    resultResp.setSeq(seq);
                                    resultResp.setSn(sn);
                                    resultResp.setTrans_code(trans_code);
                                    resultResp.setTimestamp(timestamp);
                                    return req_data;
                                }catch (Exception e){
                                    System.out.println("终端返回身份数据失败...");
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        }, 3);
                resultResp.setRsp_data(String.valueOf(result));
                resultResp.setTrans_code(Constants.RESULT_CODE);
                new SendRespThread(resultResp);
                osw.close();
                bw.close();

            }else{
                System.out.println("解码请求错误...");
                System.out.println("关闭连接...");
                socket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class  SendRespThread extends Thread{
        private Response response;
        SendRespThread(Response readCardResp){
            response = readCardResp;
        }

        @Override
        public void run() {
            try{
                osw = new OutputStreamWriter(socket.getOutputStream());
                bw = new BufferedWriter(osw);
                //类型转换
                String data = JSON.toJSONString(readCardResp);
                System.out.println("发送读卡命令...");
                bw.write(data + "\n");
                bw.flush();
            }catch (Exception e){
                System.out.println("发送读卡命令错误....");
                try{
                    socket.close();
                }catch (IOException ioe){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ClientService clientService = new ClientService(1234);
        clientService.start();
        //QueryService queryService = new QueryService();


    }
}
