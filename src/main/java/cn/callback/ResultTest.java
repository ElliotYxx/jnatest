package cn.callback;
import cn.callback.constant.Constants;
import cn.callback.pojo.Response;
import cn.callback.pojo.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/2 下午7:09
 * 门锁（客户端）服务
 */
public class ResultTest extends Thread {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    private InputStreamReader isr;
    private BufferedReader br;
    private OutputStreamWriter osw;
    private BufferedWriter bw;
    private Socket socket;
    private ServerSocket serverSocket;

    /**
     * 配置端口
     * @param port 端口号
     */
    public ResultTest(int port){
        try{
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(100000000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 读取终端发送到平台的信息
     */
    @Override
    public void run() {
        super.run();
        try {
            System.out.println("等待终端连接....");
            socket = serverSocket.accept();
            System.out.println("终端连接成功....");
            //接收终端发送的数据
            isr = new InputStreamReader(socket.getInputStream());
            br = new BufferedReader(isr);
            //接收数据
            String str = br.readLine();
            JSONObject object = JSONObject.parseObject(str);
            System.out.println("接收到终端解码请求的时间： " + formatter.format(new Date()));
            System.out.println("平台接收到的解码请求参数：\n" + str + "\n");
            String timestamp = object.getString("timestamp");
            //int seq = Integer.parseInt(object.getString("seq"));
            //获取传递过来的数据
            System.out.println("str: " + str);
            Response resultResp = new Response();
            ResponseBody body = new ResponseBody();
            body.setTrans_code("03");
            //body.setSeq(seq);
            String info = "{\"classify\":\"1\",\"idType\":\"01\",\"birthDate\":\"19890110\",\"address\":\"福建省连江县苔菉镇后湾村光明路2号\",\"nation\":\"汉\",\"sex\":\"女\",\"signingOrganization\":\"连江县公安局\",\"endTime\":\"20360706\",\"name\":\"黄素珊\",\"beginTime\":\"20160706\",\"idnum\":\"350122198901106524\"}";
            String deInfo = info.replaceAll("\\\\", "");
            String rspData = Base64.getEncoder().encodeToString(deInfo.getBytes("gbk"));
            body.setRsp_data(rspData);

            resultResp.setBody(body);
            resultResp.setRsp_code("1");
            resultResp.setTimestamp(timestamp);
            resultResp.setRsp_msg("");
            resultResp.setSn(object.getString("sn"));
            System.out.println("发送的结果数据：" + net.sf.json.JSONObject.fromObject(resultResp).toString());
            new SendRespThread(resultResp).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class  SendRespThread extends Thread{
        private Response response;
        SendRespThread(Response response){
            this.response = response;
        }
        @Override
        public void run() {
            try{
                osw = new OutputStreamWriter(socket.getOutputStream());
                bw = new BufferedWriter(osw);
                //类型转换
                String data = JSON.toJSONString(response);
                System.out.println("发送给终端读卡命令的时间： " + formatter.format(new Date()));
                bw.write(data + "\n");
                bw.flush();
            }catch (Exception e){
                System.out.println("发送命令错误....");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ResultTest test = new ResultTest(1234);
        test.start();
    }
}
