package cn.callback;

import cn.callback.service.LgetLib;
import cn.callback.service.MyCallback;
import com.sun.jna.Native;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/2 下午7:09
 */
public class ClientService {


    public static void main(String[] args) {
        //门锁收到解码请求
        //导入so库
        final Terminal terminal = new Terminal();
        LgetLib INSTANCE = (LgetLib) Native.loadLibrary("eid", LgetLib.class);
        MyCallback callback = new MyCallback() {
            public String readCard(String fid, String tidid, String resp) {
                return terminal.getIdInfo(resp);
            }
        };

        byte[] reqID = new byte[35];
        int result = INSTANCE.JLRCs("1235678",
                "abacadae", "98541BDA41CA",
                reqID, 0x3D, 2, callback, 3);
        //返回结果给终端
        terminal.getResult(result);
        QueryService queryService = new QueryService();
        queryService.getInfo(reqID);

    }
}
