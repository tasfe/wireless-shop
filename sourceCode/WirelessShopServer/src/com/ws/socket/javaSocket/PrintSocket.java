package com.ws.socket.javaSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import com.ws.pojo.system.PrintServerAccount;
import com.ws.util.log.LogUtil;

public class PrintSocket{
	
	private PrintServerAccount account;
	private Socket socket;
	private BufferedReader br;
	private OutputStream out;
//	private PrintWriter pw = new PrintWriter(socket.getOutputStream()); 
	
	public PrintSocket(PrintServerAccount account, Socket socket) throws IOException, IOException{
		this.account = account;
		this.socket = socket;
		socket.setKeepAlive(true);
		socket.setTcpNoDelay(true);
		
		br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		out = socket.getOutputStream();
	}
	
	public void sendMsg(String msg) throws IOException{
		LogUtil.append(this.toString() + " 说3: " + msg);
		out.write(("服务器接回发: " + msg).getBytes("UTF-8"));
		out.flush();
	}
	
	public void run(){
		try {
			while(true){
            	try{
            		String msg = "", data = "";
            		while(!(data = br.readLine()).endsWith("<end>")){
            			msg += data;
            		}
            		// TODO 业务逻辑
            		sendMsg(msg);
            		PrintServerMainHandler.radio("广播信息: " + msg);
            	}catch(Exception e){
            		break;
            	}
            } 
		} finally {
			PrintServerMainHandler.safeRemove(this);
        }
	}
	
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public PrintServerAccount getAccount() {
		return account;
	}
	public void setAccount(PrintServerAccount account) {
		this.account = account;
	}

	@Override
	public int hashCode() {
		return account.getId() + 17;
	}
	@Override
	public String toString() {
		return socket.getInetAddress() + ":" + socket.getPort();
	}
	
}
