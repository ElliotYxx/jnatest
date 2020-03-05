package cn.callback.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @ProjectName: jnatest
 * @Package: cn.callback.pojo
 * @ClassName: ResponseBody
 * @Author: jerry
 * @Description: ${description}
 * @Date: 20-3-4 上午10:18
 * @Version: 1.0
 */
@Getter
@Setter
public class ResponseBody {
    /**
     * 业务类型
     * 00 解码初始请求平台
     * 01 平台到终端请求
     * 02 终端到平台请求
     * 03 返回终端结果
     */
    private String trans_code;
    /**
     * 传输的文件内容（调用so库的响应信息）
     */
    private String rsp_data;
    /**
     * 指令序列，初始值为1， 每次请求提交到平台值加1
     */
    private Integer seq;

    @Override
    public String toString() {
        return "ResponseBody{" +
                "trans_code='" + trans_code + '\'' +
                ", rsp_data='" + rsp_data + '\'' +
                ", seq=" + seq +
                '}';
    }
}
