package cn.callback.service;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/2 下午11:13
 */
public interface JLRC extends Library {
    JLRC INSTANCE = (JLRC) Native.loadLibrary("query", JLRC.class);
    /**
     * 库函数声明
     * @param cid
     * @param app_id
     * @param appkey
     * @param reqID
     * @param biz_sequence_id
     * @param info
     * @param picture
     * @param dn
     * @param appeidcode
     * @param errMsg
     * @param decodeLevel
     * @param ip
     * @return
     */
    int getInfo(byte[] cid, byte[] app_id, byte[] appkey, byte[] reqID, byte[] biz_sequence_id,
                byte[] info, byte[] picture, byte[] dn, byte[] appeidcode,
                byte[] errMsg, int decodeLevel, byte[] ip);
}
