package com.ws.server.jsocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.concurrent.*;

public class MultiThreadServer {
    private int port = 8888;
    private ServerSocket serverSocket;
    private ExecutorService executorService;//线程池
    private final int POOL_SIZE = 10;//单个CPU线程池大小
    public static int USER_ACTIVE = 0;

    public MultiThreadServer() throws IOException{
        serverSocket = new ServerSocket(port);
        //Runtime的availableProcessor()方法返回当前系统的CPU数目.
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*POOL_SIZE);
        System.out.println("服务器启动成功, 监听端口: " + port);
    }

    public void service(){
        while(true){
            Socket socket = null;
            try {
                //接收客户连接,只要客户进行了连接,就会触发accept();从而建立连接
                socket=serverSocket.accept();
                USER_ACTIVE++;
                System.out.println("当前接入用户数: " + USER_ACTIVE);
                socket.setKeepAlive(true);
                socket.setTcpNoDelay(true);
                executorService.execute(new Handler(socket));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new MultiThreadServer().service();
    }

}

class Handler implements Runnable{
    private Socket socket;
    public Handler(Socket socket){
        this.socket=socket;
    }
    private PrintWriter getWriter(Socket socket) throws IOException{
        OutputStream socketOut=socket.getOutputStream();
        return new PrintWriter(socketOut,true);
    }
    private BufferedReader getReader(Socket socket) throws IOException{
        InputStream socketIn=socket.getInputStream();
        return new BufferedReader(new InputStreamReader(socketIn, "UTF-8"));
    }

    public String echo(String msg){
        return "服务器接收到的请求信息:   " + msg;
    }
    public void run(){
        try {
            System.out.println("新客户端加入: " + socket.getInetAddress() + ":" + socket.getPort());
            BufferedReader br = getReader(socket);
            PrintWriter pw = getWriter(socket);
            String msg = null;
            while((msg = br.readLine())!=null){
            	System.out.println(msg);
            	pw.println(echo(msg));
            	if(msg.equals("bye"))
                   break;
            }
            System.out.println("client msg:  " + msg);
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
//            PrintWriter out = new PrintWriter(socket.getOutputStream());  
//            while (true) {
//            	try{
//            		String msg = in.readLine();  
//            		System.out.println(msg);  
//            		out.write("Server received: " + msg);  
//            		out.flush();  
//            		if (msg.equals("bye")) {
//            			MultiThreadServer.USER_ACTIVE--;
//            			break;
//            		}  
//            	}catch(Exception e){
//            		MultiThreadServer.USER_ACTIVE--;
//            		e.printStackTrace();
//            		break;
//            	}
//            } 
        } catch (IOException e) {
            e.printStackTrace();
            MultiThreadServer.USER_ACTIVE--;
        }finally{
            try {
                if(socket!=null){
                	socket.close();
                	MultiThreadServer.USER_ACTIVE--;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}