package cn.test;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/1 下午6:37
 */
@Getter
@Setter
public class TerReq implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 业务类型　01解码请求（终端）  02指令交互（平台、终端）  03返回结果（平台）
     */
    private String trans_code;
    /**
     * 设备序列号
     */
    private String devsn;
    /**
     * 通信数据
     */
    private String comm_data;
    /**
     * 错误码
     * 0 成功
     * -43002错误
     */
    private String errcode;
    /**
     * 错误信息
     */
    private String errmsg;

    public TerReq(){}

    public TerReq(String trans_code, String devsn, String comm_data, String errcode, String errmsg) {
        this.trans_code = trans_code;
        this.devsn = devsn;
        this.comm_data = comm_data;
        this.errcode = errcode;
        this.errmsg = errmsg;
    }
}
