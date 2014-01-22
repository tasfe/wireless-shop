package com.ws.config;

public class WRConfig {
	private WRConfig(){}
	private static String imageDefaultFile = "http://fcr-default.oss-cn-hangzhou.aliyuncs.com/nophoto.png";
	private static int imageUploadMaxSize = 300;
	private static String[] imageUploadType = new String[]{"jpg","gif","png","bmp"};
	
	public static String getImageDefaultFile() {
		return imageDefaultFile;
	}
	static void setImageDefaultFile(String imageDefaultFile) {
		WRConfig.imageDefaultFile = imageDefaultFile;
	}
	public static final int getImageUploadMaxSize() {
		return imageUploadMaxSize;
	}
	void setImageUploadMaxSize(int imageUploadMaxSize) {
		WRConfig.imageUploadMaxSize = imageUploadMaxSize;
	}
	public static final String[] getImageUploadType() {
		return imageUploadType;
	}
	void setImageUploadType(String[] imageUploadType) {
		WRConfig.imageUploadType = imageUploadType;
	}
	
	
}
