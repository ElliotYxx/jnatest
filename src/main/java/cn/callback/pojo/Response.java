package cn.callback.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/3 下午6:43
 */
@Setter
@Getter
public class Response {
    /**
     * 响应编号唯一编码（Sign Number）
     */
    private String sn;
    /**
     * 响应发起时间戳，格式为yyyy-MM-dd HH:mm:ss
     */
    private String timestamp;
    /**
     * 响应包含的签名，测试暂时不用写
     */
    private String sign;
    /**
     * 成功显示1， 错误显示错误码
     */
    private String rsp_code;
    /**
     * 如果操作错误，显示错误信息，如果成功则不显示
     */
    private String rsp_msg;
    /**
     * 响应的主体，包含响应的数据
     */
    private ResponseBody body;

}
