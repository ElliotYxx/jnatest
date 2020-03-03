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
        byte[] reqID = new byte[35];
        final Terminal terminal = new Terminal();
        //门锁收到解码请求,调用so库返回结果码
        int result = LgetLib.INSTANCE.JLRCs("1235678",
                "abacadae", "98541BDA41CA",
                reqID, 0x3D, 2, new MyCallback() {
                    public String readCard(String fid, String tidid, String resp) {
                        //读卡命令发送给门锁端
                        return terminal.getIdInfo(resp);
                    }
                }, 3);
        //返回结果给终端
        terminal.getResult(result);
        QueryService queryService = new QueryService();
        queryService.getInfo(reqID);

    }
}
