package cn.callback.service;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/2 下午7:07
 */
public interface LgetLib extends Library {
    LgetLib INSTANCE = (LgetLib) Native.loadLibrary("eid_V4.0.0", LgetLib.class);
    int JLRCs(String cid, String fdid, String tdid, byte[] reqid, int len,
              int declevel, MyCallback treadCard, int logvel, int type);
}
