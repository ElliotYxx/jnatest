package cn.callback;

import cn.callback.service.JLRC;
import com.sun.jna.Native;
import sun.misc.BASE64Encoder;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/2 下午11:08
 */
public class QueryService {

    byte[] cid = "1190707".getBytes();
    byte[] app_id = "B1R7gJTeiC87k2MU5TJk".getBytes();
    byte[] appKey = "25BEE6EDB&63AF512345852A8342D5A1".getBytes();
    byte[] errMsg = new byte[50];
    byte[] dn = new byte[35];
    byte[] appeidcode = new byte[50];
    byte[] info = new byte[500];
    byte[] picture = new byte[100000];
    byte[] ip = "118.145.9.208:80".getBytes();

    JLRC lib = (JLRC) Native.loadLibrary("query", JLRC.class);
    public void getInfo(byte[] reqID){
        int ret = lib.getInfo(cid, app_id, appKey, reqID, "jWEeQkfogZSJvrS2iDZ".getBytes(), info,
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
