package com.ws.util.oss;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import com.aliyun.common.utils.IOUtils;
import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.CannedAccessControlList;
import com.aliyun.openservices.oss.model.ListObjectsRequest;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectMetadata;

public class OSSUtil {

	private OSSUtil(){}
	
	private static OSSParams params;
    public static OSSClient clientInner;
    private static ListObjectsRequest listObjectRequest;
    
    /**
     * 初始化客户端连接池基础信息
     * @param id
     * @param key
     * @param bucketImage
     * @throws Exception
     */
    public static void init(OSSParams ossParams) {
    	params = ossParams;
    	clientInner = new OSSClient("http://" + params.OSS_INNER_POINT, params.OSS_ACCESS_ID, params.OSS_ACCESS_KEY);
    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    	System.out.println("信息: 文件处理客户端内网连接池初始化成功.");
    }
    
    /**
     * 
     * @param bucketName
     * @throws OSSException
     * @throws ClientException
     */
	public static void ensureBucket(String bucketName) throws OSSException, ClientException{
    	ensureBucket(clientInner, bucketName);
    }
	
	/**
     * 创建自定义 bucket
     * @param bucketName
     * @throws OSSException
     * @throws ClientException
     */
	public static void ensureBucket(OSSClient client, String bucketName) throws OSSException, ClientException{
    	if (!client.doesBucketExist(bucketName)){
    		client.createBucket(bucketName);
    		client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
		}
    	if(listObjectRequest == null){
    		listObjectRequest = new ListObjectsRequest(bucketName);
    	}
    	listObjectRequest.setMaxKeys(500);
	}
    
	/**
	 * 
	 * @param client
	 * @param bucketName
	 * @param key
	 * @param file
	 * @param objectMeta
	 * @throws OSSException
	 * @throws ClientException
	 * @throws NullPointerException
	 * @throws IOException 
	 */
    public static void uploadFile(OSSClient client, String bucketName, String key, File file, ObjectMetadata objectMeta) 
    		throws OSSException, ClientException, NullPointerException, IOException{
    	if(file != null && file.exists()){
    		ensureBucket(client, bucketName);
    		if(objectMeta == null)
    			objectMeta = new ObjectMetadata();
    		objectMeta.setContentLength(file.length());
    		System.out.println("开始上传文件: " + file.getName() + ", 大小:" + file.length() / 1024 + " KB");
    		long start = 0, end = 0;
    		start = System.currentTimeMillis();
    		InputStream inputStream = new FileInputStream(file);
    		client.putObject(bucketName, key, inputStream, objectMeta);
    		IOUtils.safeClose(inputStream);
    		end = System.currentTimeMillis();
    		System.out.println("成功上传, 耗时: " + (end - start) + " 毫秒");
    	}else{
    		throw new NullPointerException("错误: 上传文件不能为空.");
    	}
    }
    
    public static void uploadFile(OSSClient client, String bucketName, String key, String path) 
    		throws OSSException, ClientException, NullPointerException, IOException{
    	uploadFile(client, bucketName, key, new File(path), null);
    }
    public static void uploadFile(String bucketName, String key, File file, ObjectMetadata objectMeta) 
    		throws OSSException, ClientException, NullPointerException, IOException{
    	uploadFile(clientInner, bucketName, key, file, objectMeta);
    }
    public static void uploadFile(String bucketName, String key, String path) 
    		throws OSSException, ClientException, NullPointerException, IOException{
    	uploadFile(clientInner, bucketName, key, new File(path), null);
    }
    
    /**
     * 删除指定 Bucket
     * @param client
     * @param bucketName
     * @throws Exception
     */
    public static void deleteBucket(OSSClient client, String bucketName) throws OSSException, ClientException{
    	if(client.doesBucketExist(bucketName)){
    		ListObjectsRequest lor = new ListObjectsRequest(bucketName);
    		lor.setMaxKeys(500);
    		List<OSSObjectSummary> listDeletes = client.listObjects(lor).getObjectSummaries();
    		if(listDeletes.size() > 0){
    			int success = 0;
    			for (int i = 0; i < listDeletes.size(); i++) {
    				String objectName = listDeletes.get(i).getKey();
    				try{
    					client.deleteObject(bucketName, objectName);
    					System.out.println("删除文件成功: " + objectName);
    					success++;
    				}catch(Exception e){
    					System.out.println("删除文件失败: " + objectName);
    				}
    			}
    			System.out.println("bucket<" + bucketName + "> 删除文件成功数: " + success);
    		}
    		if(client.listObjects(lor).getObjectSummaries().size() > 0){
    			deleteBucket(client, bucketName);
    		}else{
    			client.deleteBucket(bucketName);
    			System.out.println("bucket<" + bucketName + "> 删除成功.");
    		}
    	}
    }
    public static void deleteBucket(String bucketName) throws OSSException, ClientException{
    	deleteBucket(clientInner, bucketName);
    }
    
    /**
     * 
     * @param in
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream changeStreamToOut(InputStream in) throws IOException{
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {   
        	out.write(ch);   
        }
        return out;
    }
    
    /**
     * 
     * @param path
     * @return
     * @throws Exception
     */
    public static JSONObject formatConfig(String path) throws Exception{
    	BufferedReader br = new BufferedReader(new FileReader(new File(path))); 
		String config = "", data = null;
		while((data = br.readLine()) != null){
			config += data;
		} 
		return JSONObject.fromObject(config);
    }

	public static OSSParams getParams() {
		return params;
	}
    
}
