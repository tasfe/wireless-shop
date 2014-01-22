package com.ws.socket.javaSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadClient {

    public static void main(String[] args) {

        ExecutorService exec = Executors.newCachedThreadPool();
        
        exec.execute(createTask(1));
        
//        int numTasks = 2;
//        for (int i = 0; i < numTasks; i++) {
//            exec.execute(createTask(i));
//        }

    }

    // 定义一个简单的任务
    private static Runnable createTask(final int taskID) {
        return new Runnable() {
            private Socket socket = null;
            private int port=8888;

            public void run() {
                System.out.println("Task " + taskID + ":start");
                try {
                	if(socket == null)
                		socket = new Socket("127.0.0.1", port);
                	
                	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
                    PrintWriter out = new PrintWriter(socket.getOutputStream());  
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
                    
    				while (true) {
    					System.out.println("向服务器发送信息: ");
    		            String msg = reader.readLine();  
    		            System.out.println("msg: "+msg);
    		            out.println(msg);  
    		            out.flush();  
    		            System.out.println("发送成功.");
    		            if (msg.equals("bye")) {  
    		                break;  
    		            }  
    		            System.out.println(in.readLine());  
    		        }
    				
                    // 接收服务器的反馈
                    BufferedReader br = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                    String msg = null;
                    while ((msg = br.readLine()) != null)
                    	System.out.println(msg);
                } catch (IOException e) {                    
                    e.printStackTrace();
                }
            }

        };
    }
}