package cn.test;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/2/28 下午6:44
 */
public class JNATest {


    public static void main(String[] args) {
        LgetLib.MyCallback mycall = new LgetLib.MyCallback() {
            public String readCard(String fid,String tidid,
                                   String resp) {
                System.out.println("resp:"+resp);
                if("80B0000020".equals(resp)){
                    System.out.println("true");
                    return "00014845010807100000000000067777EED1E76E59EB123456420F9A520B8C269000";
                }
                System.out.println("false");
                return "9000";
            }
        };
        byte[] reqID = new byte[35];
        int num = LgetLib.INSTANCE.JLRCs("1235678",
                "abacadae", "98541BDA41CA",
                reqID, 0x3D, 2, mycall, 3);
        System.out.println("num: " + num);
        System.out.print("reqID:");
        for (int i = 0; i < reqID.length; i++) {
            System.out.println((char)reqID[i]);
        }
    }
}
