package com.ws.util.oss;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import com.aliyun.common.utils.IOUtils;
import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.internal.OSSUtils;
import com.aliyun.openservices.oss.model.CannedAccessControlList;
import com.aliyun.openservices.oss.model.GetObjectRequest;
import com.aliyun.openservices.oss.model.ListObjectsRequest;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.ws.util.DateUtil;

public class OSSImageUtil{
	
	public static final String BUCKET_IMAGE_KEY = "OSS_BUCKET_IMAGE";
	public static final String BUCKET_IMAGE_DEFAULT_KEY = "IMAGE_DEFAULT_FILE";
	
	public static String BUCKET_IMAGE;
	public static String BUCKET_IMAGE_DEFAULT;
	private static OSSClient imgClientInner;
	private static ListObjectsRequest imgListRequest;
	
	/**
	 * 是否初始化 BUCKET_IMAGE
	 * @return
	 */
	private static boolean doscBucketImage(){
		return BUCKET_IMAGE != null && imgClientInner != null;
	}
	
	/**
	 * 设置默认图片地址
	 * @param defaultFile
	 */
	public static void setDefaultFile(String defaultFile){
		BUCKET_IMAGE_DEFAULT = defaultFile;
	}
	
	/**
	 * 初始化
	 * @param bucketName
	 * @throws OSSException
	 * @throws ClientException
	 */
	public static void init(String bucketName){
		BUCKET_IMAGE = bucketName;
		imgClientInner = new OSSClient("http://" + OSSUtil.getParams().OSS_INNER_POINT, OSSUtil.getParams().OSS_ACCESS_ID, OSSUtil.getParams().OSS_ACCESS_KEY);
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println("信息: 图片处理客户端内网连接池初始化成功.");
		
		ensureBucketImage();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println("信息: BUCKET_IMAGE 初始化成功.");
	}
	
	/**
	 * 验证Bucket
	 * @throws OSSException
	 * @throws ClientException
	 */
	private static void ensureBucketImage() throws OSSException, ClientException{
		if(imgClientInner == null)
			throw new NullPointerException("错误: 未初始化客户端内网连接池.");
		OSSUtil.ensureBucket(imgClientInner, BUCKET_IMAGE);
		imgClientInner.setBucketAcl(BUCKET_IMAGE, CannedAccessControlList.PublicRead);
		imgListRequest = new ListObjectsRequest(BUCKET_IMAGE);
		imgListRequest.setMaxKeys(500);
	}
	
	/**
     * 获取图片流
     * @param key
     * @return
     * 	返回图片流信息, 使用后需要手动清理
     * @throws Exception
     */
    public static InputStream getImage(String key) throws Exception{
    	return imgClientInner.getObject(new GetObjectRequest(BUCKET_IMAGE, key)).getObjectContent();
    }
    
    /**
     * 下载图片
     * @param key
     * 	OSS文件名
     * @param keyAlias
     * 	下载文件别名,可以为空,默认为OSS文件名
     * @param filePath
     * 	存在路径,可以为空,默认保存至项目根目录下 aliyunDownload 文件夹
     * @throws Exception
     */
    public static void downloadImage(String key, String keyAlias, String filePath) throws Exception{
    	OutputStream outputStream = null;
        InputStream inputStream = null;
    	try{
    		inputStream = getImage(key);
    		File file = filePath == null || filePath.trim().isEmpty() ? new File("." + File.separator + "aliyunDownload") : new File(filePath);
    		if(!file.exists()){
    			if(file.mkdirs()){
    				System.out.println("下载文件存放路径创建成功: " + file.getAbsolutePath());
    			}
    		}
    		file = new File(file.getAbsoluteFile() + File.separator + (keyAlias == null || keyAlias.trim().isEmpty() ? key : keyAlias));
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            int bufSize = 1024;
            byte buffer[] = new byte[bufSize];
            int bytesRead;
            while((bytesRead = inputStream.read(buffer)) > -1) 
                outputStream.write(buffer, 0, bytesRead);
            System.out.println("下载成功, 文件: " + key);
        }catch(IOException e){
            throw new ClientException(OSSUtils.OSS_RESOURCE_MANAGER.getString("CannotReadContentStream"), e);
        }catch(Exception e){
        	throw new Exception("下载文件失败, <" + key + "> 该文件不存在或已删除.");
        }finally{
        	IOUtils.safeClose(outputStream);
        	IOUtils.safeClose(inputStream);
        }
    }
    
    /**
     * 下载图片
     * @param key OSS文件名
     * @throws Exception
     */
    public static void downloadImage(String key) throws Exception{
    	downloadImage(key, null, null);
    }
    
    
    /**
     * 
     * @param file
     * @throws OSSException
     * @throws ClientException
     * @throws IOException 
     * @throws NullPointerException 
     */
    public static void uploadImage(File file) throws OSSException, ClientException, 
    		NullPointerException, IOException{
		if(file != null && file.exists()){
			if(file.isDirectory()){
				for(File temp : file.listFiles()){
					uploadImage(temp);
				}
			}else{
				ObjectMetadata objectMeta = new ObjectMetadata();
				checkContentType(objectMeta, file.getName());
				objectMeta.setContentLength(file.length());
				OSSUtil.uploadFile(imgClientInner, BUCKET_IMAGE, file.getName(), file, objectMeta);
			}
		}
	}
    
    /**
     * 上传图片
     * @param path	图片名称
     * @throws OSSException
     * @throws ClientException
     * @throws IOException 
     * @throws NullPointerException 
     */
    public static void uploadImage(String path) throws OSSException, ClientException,
    		NullPointerException, IOException{
    	uploadImage(new File(path));
    }
    
    /**
     * 设置上传文件类型
     * @param om
     * @param key
     * @return
     */
    static ObjectMetadata checkContentType(ObjectMetadata om, String key){
    	if(om == null)
    		om = new ObjectMetadata();
    	if(key.toLowerCase().endsWith(".jpg")){
			om.setContentType("image/jpeg");					
		}else if(key.toLowerCase().endsWith(".gif")){
			om.setContentType("image/gif");
		}else if(key.toLowerCase().endsWith(".png")){
			om.setContentType("image/png");
		}
    	return om;
    }
    
    /**
     * 上传图片
     * @param fis	图片文件流
     * @param key	图片名称
     * @throws OSSException
     * @throws ClientException
     * @throws NullPointerException
     * @throws IOException
     */
    public static void uploadImage(InputStream fis, String key) throws OSSException, ClientException,
			NullPointerException, IOException{
    	ObjectMetadata objectMeta = new ObjectMetadata();
    	objectMeta.setContentLength(fis.available());
		checkContentType(objectMeta, key);
    	imgClientInner.putObject(BUCKET_IMAGE, key, fis, objectMeta);
	}
    
    /**
     * 删除图片
     * @param key
     */
    public static void deleteImage(String key) throws OSSException, ClientException{
    	imgClientInner.deleteObject(BUCKET_IMAGE, key);
    }
    
    /**
     * 删除 BUCKET_IMAGE
     * @throws Exception
     */
    public static void deleteBucketToImage() throws OSSException, ClientException{
    	OSSUtil.deleteBucket(imgClientInner, BUCKET_IMAGE);
    }
    
    /**
     * 
     * @return
     */
    public static List<String> getKeyListForImg(){
    	List<String> kl = new ArrayList<String>();
    	if(doscBucketImage()){
    		List<OSSObjectSummary> listObjects = imgClientInner.listObjects(imgListRequest).getObjectSummaries();
    		if(listObjects.size() >= 500)
    			System.out.println("bucketName: " + BUCKET_IMAGE + ", 文件列表前500个开始读取.");
    		else
    			System.out.println("bucketName: " + BUCKET_IMAGE + ", 文件总数:" + listObjects.size());
    		for (int i = 0; i < listObjects.size(); i++) {
    			kl.add(listObjects.get(i).getKey());
//    			System.out.println(i+1+":"+listObjects.get(i).getKey());
            }
    		System.out.println("文件列表读取完成.");
    	}
    	return kl;
    }
    
    
    /**
	 * 
	 * @param args
	 * 	输入参数下标意义, 默认查询.
	 * 	0: 配置文件路径
	 * 	配置文件参数说明:
	 * 	  OTYPE:
	 * 		1 : 上传指定目录或文件  配置文件key: UPLOAD_PATH
	 * 		2 : 删除指定目录或文件  配置文件key: DELETE_BUCKET
	 * 		3 : 删除图片bucket
	 * 		4 : 删除图片, 配置文件key: DELETE_IMAGES
	 */
	public static void main(String[] args) {
		try{
			if(args.length == 0){
				System.out.println("请输入 JSON 格式配置文件路径.");
				return;
			}
			JSONObject cj = null;
			try{
				cj = OSSUtil.formatConfig(args[0]);
				
				OSSUtil.init(OSSParams.init(cj.getString("ACCESS_ID"), cj.getString("ACCESS_KEY"), cj.getString("INNER_POINT"), cj.getString("OUTER_POINT")));
				OSSImageUtil.init(cj.getString("BUCKET_IMAGE"));
				
				int otype = cj.getInt("OTYPE");
				if(otype <= 0){
					getKeyListForImg();
				}else{
					long start = 0, end = 0;
					switch(otype){
					case 1:
						start = 0;
						end = 0;
						start = System.currentTimeMillis();
						System.out.println("开始上传文件/目录, 当前时间:" + DateUtil.format(start));
						for(Object temp : cj.getJSONArray("UPLOAD_PATH")){
							uploadImage(new File(temp.toString()));
							System.out.println("成功上传一个文件/目录.");
						}
						end = System.currentTimeMillis();
						System.out.println("成功上传 总耗时: " + (double)((end - start) / 1000) + " 秒, 当前时间:" + DateUtil.format(end));
						System.out.println("----------------------------------------");
						break;
					case 2:
						for(Object temp : cj.getJSONArray("DELETE_BUCKET")){
							start = 0;
							end = 0;
							start = System.currentTimeMillis();
							System.out.println("开始删除 Bucket<" + temp.toString() + ">, 当前时间:" + DateUtil.format(start));
							OSSUtil.deleteBucket(temp.toString());
							end = System.currentTimeMillis();
							System.out.println("成功删除 Bucket<" + temp.toString() + ">, 当前时间:" + DateUtil.format(end));
							System.out.println("总耗时: " + (double)((end - start) / 1000) + " 秒");
							System.out.println("----------------------------------------");
						}
						break;
					case 3:
						deleteBucketToImage();
						break;
					case 4:
						for(Object temp : cj.getJSONArray("DELETE_IMAGES")){
							try{
								deleteImage(temp.toString());
								System.out.println("图片: " + temp.toString() + ", 删除成功.");
							}catch(Exception e){
								e.printStackTrace();
								System.out.println("图片: " + temp.toString() + ", 删除失败.");
							}
						}
						break;
					case 5:
						for(Object temp : cj.getJSONArray("DOWNLOAD_FILES")){
							downloadImage(temp.toString(), null, cj.getString("DOWNLOAD_PATH"));
						}
						break;
					default:
						getKeyListForImg();
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cj = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
