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
//    /**
//     * 业务员类型
//     * 00 解码初始请求平台
//     * 01 平台到终端请求
//     * 02 终端到平台请求
//     * 03 返回终端结果
//     */
//    private String trans_code;
//    /**
//     * 传输的内容数据
//     */
//    private String req_data;
//    /**
//     * 指令序列，初始值为1，每次请求提交到平台值加1
//     */
//    private int seq;
    /**
     * 请求包含的签名，测试暂时不用写
     */
    private String sign;
    /**
     * 请求的主体，包含请求的内容数据
     */
    private RequestBody body;
}
