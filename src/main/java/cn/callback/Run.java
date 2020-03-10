package cn.callback;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/6 上午8:58
 * 项目运行类　　联调的时候记得把Terminal注释掉
 */
public class Run {
    public static void main(String[] args) {
        QueryService queryService = new QueryService(2345);
        ClientService clientService = new ClientService(1234);
        Terminal terminal = new Terminal("127.0.0.1", 1234);
        queryService.start();
        clientService.start();
        terminal.start();
    }
}
