package com.ws.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 
 * @author WuZY
 *
 */
public final class DateUtil {
	private DateUtil(){}
	
	public static enum Pattern{
		MO_SEQ("yyyyMMddHHmmss", "member operation seq"),
		DATE_TIME("yyyy-MM-dd HH:mm:ss", "local host"),
		DATE("yyyy-MM-dd", "date");
		
		private final String pattern;
		private final String desc;
		
		@Override
		public String toString(){
			return this.desc;
		}
		
		Pattern(String pattern, String desc){
			this.pattern = pattern;
			this.desc = desc;
		}
		
		public String getPattern(){
			return this.pattern;
		}
		
		public String getDesc(){
			return this.desc;
		}
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String now(){
		return new SimpleDateFormat(Pattern.DATE_TIME.getPattern(), Locale.getDefault()).format(new Date());
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date){
		return new SimpleDateFormat(Pattern.DATE_TIME.getPattern(), Locale.getDefault()).format(date);
	}
	
	/**
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern){
		return new SimpleDateFormat(pattern, Locale.getDefault()).format(date);
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String format(long date){
		return new SimpleDateFormat(Pattern.DATE_TIME.getPattern(), Locale.getDefault()).format(date);
	}
	
	/**
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(long date, Pattern pattern){
		return new SimpleDateFormat(pattern.getPattern(), Locale.getDefault()).format(date);
	}
	
	/**
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(long date, String pattern){
		return new SimpleDateFormat(pattern, Locale.getDefault()).format(date);
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatToDate(Date date){
		return DateUtil.format(date, Pattern.DATE.getPattern());
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatToDate(long date){
		return DateUtil.format(date, Pattern.DATE.getPattern());
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static long parseDate(String date){
		try{
			return new SimpleDateFormat(Pattern.DATE_TIME.getPattern(), Locale.getDefault()).parse(date).getTime(); 
		}catch(ParseException e){
			try{
				return new SimpleDateFormat(Pattern.DATE.getPattern(), Locale.getDefault()).parse(date).getTime();
			}catch(ParseException e2){
				return 0;
			}
		}
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static long parseDate(String date, Pattern pattern) throws ParseException{
		return new SimpleDateFormat(pattern.getPattern(), Locale.getDefault()).parse(date).getTime();
	}
	
}
