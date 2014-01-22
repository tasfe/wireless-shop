package com.ws.service.food.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.ws.config.WRConfig;
import com.ws.dao.food.IFoodDao;
import com.ws.dao.food.impl.FoodDao;
import com.ws.pojo.exception.BusinessException;
import com.ws.pojo.food.Food;
import com.ws.service.food.IFoodService;
import com.ws.util.json.JObject;
import com.ws.util.oss.OSSImageUtil;
import com.ws.util.CompressImage;
import com.ws.util.DBHelper;
import com.ws.util.OBeanBasic;

public class FoodService extends OBeanBasic implements IFoodService{
	
	private IFoodDao foodDao = new FoodDao();
	
	public JObject getByExtra(Integer start, Integer limit, Food body) {
		try{
			jobj = new JObject();
			$conn = DBHelper.newInstance();
			StringBuilder extra = new StringBuilder();
			extra.append(" AND F.r_id = " + body.getRid());
			// 
			if(body.getKitchenId() > 0){
				extra.append(" AND F.kitchen_id = " + body.getKitchenId());
			}
			// 
			if(body.getStatus() > 0){
				List<String> statusList = new ArrayList<String>();
				if(body.isSpecial()){
					statusList.add("(F.status & " + Food.STATUS_SPECIAL + ") <> 0");
				}
				if(body.isRecommend()){
					statusList.add("(F.status & " + Food.STATUS_RECOMMEND + ") <> 0");
				}
				if(body.isStop()){
					statusList.add("(F.status & " + Food.STATUS_STOP + ") <> 0");
				}
				if(body.isGift()){
					statusList.add("(F.status & " + Food.STATUS_GIFT + ") <> 0");
				}
				if(body.isCombo()){
					statusList.add("(F.status & " + Food.STATUS_COMBO + ") <> 0");
				}
				if(!statusList.isEmpty()){
					String strStatus = "";
					for(int i = 0; i < statusList.size(); i++){
						if(i > 0)
							strStatus += " OR ";
						strStatus += statusList.get(i);
					}
					strStatus = (" AND (" + strStatus + ")");
					extra.append(strStatus);
				}
				
			}
			jobj.setTotalProperty(foodDao.getByExtraCount($conn, extra.toString()));
			if(jobj.getTotalProperty() > 0){
				extra.append(" ORDER BY F.alias");
				if(start != null && limit != null)
					extra.append(" LIMIT " + start + ", " + limit);
				List<Food> list = foodDao.getByExtra($conn, extra.toString());
				jobj.setRoot(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			jobj.initTip(e);
		}finally{
			safeClose();
		}
		return jobj;
	}

	public JObject insert(Food insert) {
		try{
			jobj = new JObject();
			$conn = DBHelper.newInstance();
			foodDao.insert($conn, insert);
			jobj.initTip(true, "操作成功, 已添加新菜品资料.");
		} catch (SQLException e) {
			e.printStackTrace();
			jobj.initTip(e);
		} catch (BusinessException e) {
			e.printStackTrace();
			jobj.initTip(e);
		}finally{
			safeClose();
		}
		return jobj;
	}

	public JObject update(Food update) {
		try{
			jobj = new JObject();
			$conn = DBHelper.newInstance();
			foodDao.update($conn, update);
			jobj.initTip(true, "操作成功, 已修改菜品资料.");
		} catch (SQLException e) {
			e.printStackTrace();
			jobj.initTip(e);
		} catch (BusinessException e) {
			e.printStackTrace();
			jobj.initTip(e);
		}finally{
			safeClose();
		}
		return jobj;
	}

	@Override
	public JObject uploadImage(Food ui, File img, String imgName) {
		// 新文件名
    	String newFileName = "", newSmallFileName = "";
		try{
			jobj = new JObject();
			$conn = DBHelper.newInstance();
			if(img != null){
				InputStream uploadStream = new FileInputStream(img);
				if(uploadStream.available() / 1024 > WRConfig.getImageUploadMaxSize()){
					throw new BusinessException("上传失败, 菜品图片大小不能超过 " + WRConfig.getImageUploadMaxSize() + " KB");
				}
				
				$conn.getConn().setAutoCommit(false);
				
				ui = foodDao.getById(ui.getId());
				String oldImg = ui.getImg();
				
				// 过滤图片类型
            	String type = imgName.substring(imgName.lastIndexOf(".") + 1);
            	boolean cs = false;
            	for(String temp : WRConfig.getImageUploadType()){
            		if(temp.toLowerCase().equals(type.toLowerCase())){
            			cs = true;
            			break;
            		}
            	}
            	if(!cs){
            		new BusinessException(9994, "操作失败, 不支持该类型图片!");
            	}
            	// 新文件前缀部分,需要格式化
            	newFileName = ui.getId() + new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
            	
            	MessageDigest md = MessageDigest.getInstance("MD5");
            	md.update(newFileName.getBytes("UTF-8"));
            	
            	StringBuffer sb = new StringBuffer();
            	byte[] byteArray = md.digest();
            	int bi = 0;
            	for(int offset = 0; offset < byteArray.length; offset++){
            		bi = byteArray[offset];
            		if(bi < 0)
            			bi += 256;
            		if(bi < 16)
            			sb.append("0");
            		sb.append(Integer.toHexString(bi));
            	}
            	
            	// 组合新文件名,统一文件后缀为小写
            	newFileName = sb.toString().substring(8,24) + "." + type.toLowerCase();
            	
            	// 更新图片数据库信息
            	ui.setImg(newFileName);
            	foodDao.updateImage($conn, ui);
            	
            	try{
            		// 上传图片至OSS
            		newSmallFileName = ui.getRid() + "/small_" + newFileName;
            		newFileName = ui.getRid() + "/" + newFileName; // 组合oss文件路劲, 这不是数据库路劲
            		OSSImageUtil.uploadImage(uploadStream, newFileName);
//				System.out.println(newFileName+"    :    "+newSmallFileName);
            		
            		// 生成缩略图并上传至oss
            		InputStream smallStream = CompressImage.newInstance().getSmallStream(img, type, 300, 180);
            		OSSImageUtil.uploadImage(smallStream,  newSmallFileName);
            		
            		if(oldImg != null){
            			// 新图片更新成功后删除OSS上原图片信息
            			OSSImageUtil.deleteImage(ui.getRid() + "/" + oldImg);
            			OSSImageUtil.deleteImage(ui.getRid() + "/small_" + oldImg);
            		}
            	}catch(Exception e){
            		System.out.println("图片上传或删除失败, 请确认是否已开启: 图片操作服务.");
            		throw e;
            	}
				
				$conn.commit();
				jobj.initTip(true, "操作成功, 已上传菜品图片.");
			}else{
				throw new BusinessException("上传失败, 图片文件为空.");
			}
		} catch (SQLException e) {
			$conn.rollback();
			e.printStackTrace();
			jobj.initTip(e);
			OSSImageUtil.deleteImage(newFileName);
			OSSImageUtil.deleteImage(newSmallFileName);
		} catch (BusinessException e) {
			$conn.rollback();
			e.printStackTrace();
			jobj.initTip(e);
			OSSImageUtil.deleteImage(newFileName);
			OSSImageUtil.deleteImage(newSmallFileName);
		} catch (Exception e){	
			$conn.rollback();
			e.printStackTrace();
			jobj.initTip(false, "操作失败, 未能处理菜品图片文件信息, 请联系管理员.");
			OSSImageUtil.deleteImage(newFileName);
			OSSImageUtil.deleteImage(newSmallFileName);
		} finally{
			safeClose();
		}
		return jobj;
	}
	
	public JObject deleteImage(Food delete) {
		try{
			jobj = new JObject();
			$conn = DBHelper.newInstance();
			delete = foodDao.getById($conn, delete.getId());
			
			String oldImg = delete.getImg();
			// 更新图片数据库信息
			delete.setImg(null);
			foodDao.updateImage($conn, delete);
			// 更新数据成功后删除OSS上原图片信息
			OSSImageUtil.deleteImage(oldImg);
			
			jobj.initTip(true, "操作成功, 已删除图片信息.");
		} catch (SQLException e) {
			$conn.rollback();
			e.printStackTrace();
			jobj.initTip(e);
		} catch (BusinessException e) {
			$conn.rollback();
			e.printStackTrace();
			jobj.initTip(e);
		} catch (Exception e){	
			$conn.rollback();
			e.printStackTrace();
			jobj.initTip(false, "操作失败, 未能处理菜品图片文件信息, 请联系管理员.");
		}finally{
			safeClose();
		}
		return jobj;
	}

}
