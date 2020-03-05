package cn.callback.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/3 下午6:45
 */
@Getter
@Setter
public class Request {
    /**
     * 请求编号唯一编码（Sign Number）
     */
    private String sn;
    /**
     * 请求发起时间戳，格式为(yyyy-MM-dd)
     */
    private String timestamp;

    /**
     * 请求包含的签名，测试暂时不用写
     */
    private String sign;
    /**
     * 请求的主体，包含请求的内容数据
     */
    private RequestBody body;
}
