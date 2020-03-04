package cn.callback.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @ProjectName: jnatest
 * @Package: cn.callback.pojo
 * @ClassName: RequestBody
 * @Author: jerry
 * @Description: ${description}
 * @Date: 20-3-4 上午10:17
 * @Version: 1.0
 */
@Setter
@Getter
public class RequestBody {
    /**
     * 业务员类型
     * 00 解码初始请求平台
     * 01 平台到终端请求
     * 02 终端到平台请求
     * 03 返回终端结果
     */
    private String trans_code;
    /**
     * 传输的内容数据
     */
    private String req_data;
    /**
     * 请求包含的签名，测试暂时不用写
     */
    private Integer seq;
}
