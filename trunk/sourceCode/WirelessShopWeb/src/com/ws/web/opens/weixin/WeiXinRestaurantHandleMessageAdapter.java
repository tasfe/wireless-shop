package com.ws.web.opens.weixin;

import java.sql.SQLException;
import java.util.Date;

import org.marker.weixin.DefaultSession;
import org.marker.weixin.HandleMessageListener;
import org.marker.weixin.msg.Data4Item;
import org.marker.weixin.msg.Msg;
import org.marker.weixin.msg.Msg4Event;
import org.marker.weixin.msg.Msg4Image;
import org.marker.weixin.msg.Msg4ImageText;
import org.marker.weixin.msg.Msg4Link;
import org.marker.weixin.msg.Msg4Location;
import org.marker.weixin.msg.Msg4Text;

import com.ws.dao.system.IRestaurantDao;
import com.ws.dao.system.impl.RestaurantDao;
import com.ws.pojo.exception.BusinessException;
import com.ws.pojo.system.Restaurant;
import com.ws.util.DBHelper;
import com.ws.util.DateUtil;

public class WeiXinRestaurantHandleMessageAdapter implements HandleMessageListener {
	
	public static final String WEIXIN_ADDRESS_ENTRY = "http://115.28.35.211/wr/mobile";
	public static final String WEIXIN_INDEX = WEIXIN_ADDRESS_ENTRY + "/index.htm";
	public static final String WEIXIN_FOOD = WEIXIN_ADDRESS_ENTRY + "/food.htm";
	public static final String WEIXIN_ABOUT = WEIXIN_ADDRESS_ENTRY + "/about.htm";
	public static final String WEIXIN_SALES = WEIXIN_ADDRESS_ENTRY + "/sales.htm";
	public static final String WEIXIN_MEMBER = WEIXIN_ADDRESS_ENTRY + "/member.htm";
	
	public static final String COMMAND_HELP = "h";
	public static final String COMMAND_MENU = "m";
	
	private DBHelper conn;
	private Restaurant restaurant;
	private Msg4Text text;
	private Msg4ImageText imageText;
	private Data4Item dataItem;
	private Msg msg;
	private int rid = 26;
	private DefaultSession session;
	private String account;
	private IRestaurantDao restaurantDao = new RestaurantDao();
	
	public WeiXinRestaurantHandleMessageAdapter(DefaultSession session, String account){
		this.session = session;
		this.account = account;
	}
	
	/**
	 * 初始化单次消息处理内容
	 * @param msg
	 * @throws SQLException
	 * @throws BusinessException
	 */
	private void init(Msg msg) throws SQLException, BusinessException{
		if(msg == null) throw new NullPointerException("当前时间: " + DateUtil.format(new Date()) + "\n 错误: 接收公众平台回发信息失败.");
		else this.msg = msg;
		System.out.println("FromID: " + msg.getToUserName());
		System.out.println("OpenID: " + msg.getFromUserName());
		System.out.println("account: " + account);
		if(conn == null){
			conn = DBHelper.newInstance();
		}
		// 绑定餐厅和公众平台信息
//		WeixinRestaurantDao.bind(dbCon, msg.getToUserName(), account);
		
		// 获取公众平台对应餐厅信息
		if(restaurant == null){
//			rid = WeixinRestaurantDao.getRestaurantIdByWeixin(conn, msg.getToUserName());
			restaurant = restaurantDao.getById(conn, rid);
		}
	}
	/**
	 * 初始化文本回复信息对象
	 */
	private void initText(){
		text = new Msg4Text();
		text.setFromUserName(msg.getToUserName());
		text.setToUserName(msg.getFromUserName());
		text.setFuncFlag("0");
	}
	/**
	 * 初始化图片文本回复信息对象
	 */
	private void initImageText(){
		imageText = new Msg4ImageText();
		imageText.setFromUserName(msg.getToUserName());
		imageText.setToUserName(msg.getFromUserName()); 
		imageText.setCreateTime(msg.getCreateTime());
		imageText.setFuncFlag("0");
	}
	private String createUrl(String url){
		StringBuilder target = new StringBuilder();
		target.append(url).append("?_d="+Math.random())
			.append("&rid=").append(restaurant.getId())
			.append("&m=").append(imageText.getToUserName())
			.append("&r=").append(imageText.getFromUserName());
		return target.toString();
	}
	
	/**
	 * 解释用户对话操作指令
	 * @param order
	 */
	private void explainOrder(String order){
		if(order == null) return;
		else order = order.trim().toLowerCase();
		if("1".equals(order) || COMMAND_MENU.equals(order)){
			initImageText();
			
			dataItem = new Data4Item("系统主页", "系统主页", "http://115.28.35.211/title.jpg", WEIXIN_INDEX); 
			imageText.addItem(dataItem);
			
			dataItem = new Data4Item();
			dataItem.setTitle("菜品列表");
			dataItem.setUrl(createUrl(WEIXIN_FOOD));
			imageText.addItem(dataItem);
			
			dataItem = new Data4Item();
			dataItem.setTitle("促销信息");
			dataItem.setUrl(createUrl(WEIXIN_SALES));
			imageText.addItem(dataItem);
			
			dataItem = new Data4Item();
			dataItem.setTitle("餐厅简介");
			dataItem.setUrl(createUrl(WEIXIN_ABOUT));
			imageText.addItem(dataItem);
			
			dataItem = new Data4Item();
			dataItem.setTitle("会员资料");
			dataItem.setUrl(createUrl(WEIXIN_MEMBER));
			imageText.addItem(dataItem);
			
			session.callback(imageText);
		}else if(COMMAND_HELP.equals(order)){
			initText();
			text.setContent("餐厅编号:"+restaurant.getId()+"\nToUserName(openID):"+msg.getFromUserName()+"\nFromUserName(开发者微信号):"+msg.getToUserName());
			session.callback(text);
		}else{
			initText();
			text.setContent(new StringBuilder().append("未知命令\n")
					.append("回复【h】获取帮助信息\n")
					.append("回复【m】获得主菜单")
					.toString());
			session.callback(text);
		}
	}
	
	/**
	 * 文本信息, 操作请求指令集
	 */
	public void onTextMsg(Msg4Text msg) {
		System.out.println("微信餐厅收到消息：" + msg.getContent());
		try{
			init(msg);
			explainOrder(msg.getContent());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			safeClose();
		}
	}
	
	/**
	 * 推送事件, 关注或取消关注使用
	 */
	public void onEventMsg(Msg4Event msg) {
		try{
			init(msg);
			if(msg.getEvent().equals(Msg4Event.SUBSCRIBE)){
				System.out.println("微信餐厅->添加关注: " + msg.getToUserName());
				initText();
				text.setContent(new StringBuilder().append("谢谢您的支持.\n")
						.append("回复【h】获取帮助信息\n")
						.append("回复【m】获得主菜单")
						.toString());
				session.callback(text);
			}else if(msg.getEvent().equals(Msg4Event.UNSUBSCRIBE)){
				System.out.println("微信餐厅->取消关注: " + msg.getToUserName());
			}else{
				System.out.println("微信餐厅->菜单回调事件, 推送指令: " + msg.getEventKey());
				explainOrder(msg.getEventKey());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			safeClose();
		}
	}
	
	private void safeClose(){
		DBHelper.safeClose(conn);
	}

	@Override
	public void onImageMsg(Msg4Image msg4image) { }

	@Override
	public void onLinkMsg(Msg4Link msg4link) { }

	@Override
	public void onLocationMsg(Msg4Location msg4location) { }

	@Override
	public void onErrorMsg(int i) { }
	
}
