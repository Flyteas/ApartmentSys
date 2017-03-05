package com.flyteas.ApartmentSys.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* MD5摘要算法工具类 */
public class MD5Encryptor 
{
	public static String md5Encrypt(String val) //md5加密，返回32位小写
	{
		MessageDigest md5;
		try 
		{
			md5 = MessageDigest.getInstance("MD5"); //摘要算法选择MD5
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
			return null;
		}  
		md5.update(val.getBytes());  
		byte[] md5EncryptByte = md5.digest(); //获取MD5后的值
		StringBuffer md5EncryptBuf = new StringBuffer();    
		for(int i=0;i<md5EncryptByte.length;i++) //转换成String 格式
		{    
			int a = md5EncryptByte[i];
			if(a<0)
				a += 256;
			if(a < 16)
				md5EncryptBuf.append("0");
			md5EncryptBuf.append(Integer.toHexString(a));
		}            
		return md5EncryptBuf.toString();
	}
}