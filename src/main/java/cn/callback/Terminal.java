package cn.callback;

import cn.callback.service.MyCallback;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/2 下午7:17
 * 终端设备
 */
@Getter
@Setter
public class Terminal {
    public String getIdInfo(String resp){
        System.out.println("本次接收到的请求为： " + resp);
        if (resp.equals("80B0000020")){
            System.out.println("接收到读卡命令....");
            return "00014845010807100000000000067777eed1e76e59eb123456420f9a520b8c269000";
        }else{
            return "9000";
        }
    }

    public void getResult(int result){
        System.out.println("门锁服务返回的结果是： " + result);
    }
}
