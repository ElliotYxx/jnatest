package cn.callback.service;

import com.sun.jna.Callback;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/2 下午7:06
 */
public interface MyCallback extends Callback {

    /**
     * 读卡函数
     * @param fid
     * @param tidid
     * @param resp
     * @return
     */
    String readCard(String fid, String tidid, String resp);
}
