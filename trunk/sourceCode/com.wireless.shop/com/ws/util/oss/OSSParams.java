package com.ws.util.oss;

public final class OSSParams {
	public static final String OSS_ACCESS_ID_KEY = "OSS_ACCESS_ID";
	public static final String OSS_ACCESS_KEY_KEY = "OSS_ACCESS_KEY";
	public static final String OSS_INNER_POINT_KEY = "OSS_INNER_POINT";
	public static final String OSS_OUTER_POINT_KEY = "OSS_OUTER_POINT";
	
	public final String OSS_ACCESS_ID;
	public final String OSS_ACCESS_KEY;
	public final String OSS_INNER_POINT;
	public final String OSS_OUTER_POINT;
    
	private static OSSParams instance;
	
    private OSSParams(String accessId, String accessKey, String innerPoint, String outerPoint){
    	this.OSS_ACCESS_ID = accessId;
    	this.OSS_ACCESS_KEY = accessKey;
    	this.OSS_INNER_POINT = innerPoint.startsWith("http://") ? innerPoint.substring(7) : innerPoint;
    	this.OSS_OUTER_POINT = outerPoint.startsWith("http://") ? outerPoint.substring(7) : outerPoint;
    }
    
    public static OSSParams init(String accessId, String accessKey, String innerPoint, String outerPoint){
    	instance = new OSSParams(accessId, accessKey, innerPoint, outerPoint);
    	return instance;
    }
    
}
