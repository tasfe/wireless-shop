package com.ws.server.jsocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ws.pojo.system.PrintServerAccount;
import com.ws.util.log.LogUtil;

public final class PrintServerMainHandler {
	public PrintServerMainHandler(int port, int poolSize){
		this.PORT = port;
		this.POOL_SIZE = poolSize;
	}
	public PrintServerMainHandler(int port){
		this.PORT = port;
	}
	
    private ServerSocket serverSocket;
    private ExecutorService executorService;//线程池
    private int PORT;
    private int POOL_SIZE = 0;//单个CPU线程池大小
    private static PrintList tasks = new PrintList();
	
    /**
     * 移除消息队列
     * @param remove
     */
    public static void safeRemove(PrintSocket remove){
    	tasks.remove(remove);
    }
    /**
     * 广播信息
     * @param msg
     * @throws IOException
     */
    public static void radio(String msg) throws IOException{
    	tasks.radio(msg);
    }
    
    public void start() throws IOException{
    	serverSocket = new ServerSocket(PORT);
    	if(POOL_SIZE > 0){
	    	executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOL_SIZE);
    	}else{
    		executorService = Executors.newCachedThreadPool();
    	}
    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println("信息: 打印服务启动成功, 监听端口: " + PORT);
    	LogUtil.append("打印服务启动成功, 监听端口: " + PORT);
        while(true){
        	PrintSocket join = null;
            try {
            	join = new PrintSocket(new PrintServerAccount(), serverSocket.accept());
            	tasks.add(join);
                executorService.execute(new PrintServerRunHandler(join));
            } catch (Exception e) {
            	join = null;
            	LogUtil.append(e);
            }
        }
    }
    /*
	public static void main(String[] args){
		try {
//			JSONObject conf = readConfig(args[0]);
//			new PrintServerMainHandler(conf.getInt("PORT"), conf.getInt("POOL_SIZE")).start();
			new PrintServerMainHandler(8888, 3).start();
//			new PrintServerMainHandler(conf.getInt("PORT")).start();
		} catch (Exception e) {
			LogUtil.append(e);
		}
	}
	*/
}
