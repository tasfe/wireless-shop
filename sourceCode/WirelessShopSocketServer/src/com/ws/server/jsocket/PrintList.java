package com.ws.server.jsocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ws.util.log.LogUtil;

public class PrintList {
	
	private List<PrintSocket> tasks = new ArrayList<PrintSocket>();
	
	/**
	 * 广播信息
	 * @param msg
	 * @throws IOException
	 */
	public void radio(String msg) throws IOException{
		for(PrintSocket temp : tasks){
			temp.sendMsg(msg);
		}
	}
	/**
	 * 添加新信息队列
	 * @param add
	 * @return
	 */
	public boolean add(PrintSocket add) {
		if(add == null || add.getAccount() == null){
			throw new NullPointerException();
		}
		boolean has = false;
		for(PrintSocket temp : tasks){
			if(temp.getAccount().getId() == add.getAccount().getId()){
				has = true;
				break;
			}
		}
		if(!has){
			tasks.add(add);
			logAdd(add);
			logSize();
			try {
				PrintServerMainHandler.radio("用户加入 -> " + add.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		else 
			throw new IllegalArgumentException("操作失败, 该账号已经登录, 未添加到消息队列.");
	}
	/**
	 * 获取消息队列
	 * @param find
	 * @return
	 */
	public PrintSocket get(PrintSocket find) {
		for(PrintSocket temp : tasks){
			if(temp.getAccount().getId() == find.getAccount().getId()){
				return temp;
			}
		}
		throw new IllegalArgumentException("查找失败, 该账号未登录, 找不到相关消息队列.");
	}
	/**
	 * 移除消息队列
	 * @param remove
	 * @return
	 */
	public boolean remove(PrintSocket remove) {
		for(PrintSocket temp : tasks){
			if(temp.getAccount().getId() == remove.getAccount().getId()){
				tasks.remove(temp);
				logRemove(remove);
				try {
					remove.getSocket().close();
					PrintServerMainHandler.radio("用户退出 -> " + remove.toString());
				} catch (IOException e) {
					LogUtil.append(e);
				}
				temp = null;
				remove = null;
				logSize();
				return true;
			}
		}
		throw new IllegalArgumentException("删除失败, 已该账号未登录, 或消息队列已删除相关信息.");
	}
	/**
	 * 获取当前接入数
	 * @return
	 */
	public int size(){
		return tasks.size();
	}
	public void logSize(){
		LogUtil.append("当前接入数: " + size());
	}
	public void logAdd(PrintSocket add){
		LogUtil.append("新接入地址 -> " + add.getSocket().getInetAddress() + ":" + add.getSocket().getPort());
	}
	public void logRemove(PrintSocket remove){
		LogUtil.append("接入断开 -> " + remove.getSocket().getInetAddress() + ":" + remove.getSocket().getPort());
	}
}
