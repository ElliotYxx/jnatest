package cn.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/2 上午9:09
 */
public class Terminal extends Thread {
    Socket socket = null;
    public Terminal(String host, int port){
        try{
            socket = new Socket(host, port);
            socket.setSoTimeout(10000);
        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        InputStream in = null;
        super.run();
        try{
            in = socket.getInputStream();
            byte[] buf = new byte[1024];
            while(in.available() == 0){
                int len = in.read(buf);
                String resp = new String(buf, 0, len);
                System.out.println("本次接收的resp: " + resp);
            }
            //String resp = new String(buf, 0, len);
            TerReq cardInfoReq = new TerReq();

//            while (resp != null){
//
//                if ("80B0000020".equals(resp)){
//                    cardInfoReq.setComm_data("00014845010807100000000000067777EED1E76E59EB123456420F9A520B8C269000");
//                }else{
//                    cardInfoReq.setComm_data("9000");
//                }
//                SendTerReqThread sendResult = new SendTerReqThread(cardInfoReq);
//                sendResult.start();

            //}

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    class SendTerReqThread extends Thread{
        private TerReq terReq;
        SendTerReqThread(TerReq terReq){
            this.terReq = terReq;
        }
        @Override
        public void run() {
            super.run();
            OutputStream os = null;
            ObjectOutputStream oos = null;
            try{
                os = socket.getOutputStream();
                oos = new ObjectOutputStream(os);
                oos.writeObject(terReq);
                oos.writeObject(null);
                oos.flush();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Terminal terminal = new Terminal("127.0.0.1", 1234);
        terminal.start();
    }

}
