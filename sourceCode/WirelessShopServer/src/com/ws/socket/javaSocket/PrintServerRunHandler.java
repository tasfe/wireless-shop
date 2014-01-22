package com.ws.socket.javaSocket;

public class PrintServerRunHandler implements Runnable{
	private PrintSocket task;
	public PrintServerRunHandler(PrintSocket task){
        this.task = task;
    }
	
	public void run() {
		task.run();
	}

}
