package cn.test;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/1 上午11:19
 */
public interface LgetLib extends Library {
    LgetLib INSTANCE = (LgetLib) Native.loadLibrary("eid", LgetLib.class);
    int JLRCs(String cid, String fdid, String tdid, byte[] reqid, int len,
              int declevel, MyCallback treadCard, int loglvel);

    interface MyCallback extends Callback {
        String readCard(String fid, String tidid, String resp);
    }
}
