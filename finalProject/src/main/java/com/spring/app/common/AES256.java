package com.spring.app.common;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


// === 양방향 암호화 알고리즘인 AES256 암호화를 지원하는 클래스 생성하기 
//    (기본생성자가 없으므로 @Component 를 쓰면 오류가 발생한다.!!
//     그래서 com.spring.app.config.AES256_Configuration 클래스에서 빈으로 생성해준다.!!) ==== 

/**
 * 양방향 암호화 알고리즘인 AES256 암호화를 지원하는 클래스
 */
//@Component
/*
Description:

Parameter 0 of constructor in com.spring.app.common.AES256 required a bean of type 'java.lang.String' that could not be found.


Action:

Consider defining a bean of type 'java.lang.String' in your configuration.

@Component 를 사용하면 기본 생성자가 존재하지 않다는 오류 발생
AES256 은 기본생성자를 만들 수 없다. 왜냐하면 항상 파라미터 값 암호가 적혀있어야 하기 때문이다.
*/
public class AES256 {
    private String iv;
    private Key keySpec;

    /**
     * 16자리의 키값을 입력하여 객체를 생성한다.
     * @param key 암호화/복호화를 위한 키값
     * @throws UnsupportedEncodingException 키값의 길이가 16미만일 경우 발생
     */
    public AES256() {
    	
    }
    
    public AES256(String key) throws UnsupportedEncodingException {
        this.iv = key.substring(0, 16);
        byte[] keyBytes = new byte[16]; 
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if(len > keyBytes.length){
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        this.keySpec = keySpec;
    }

    /**
     * AES256 으로 암호화 한다.
     * @param str 암호화할 문자열
     * @return
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public String encrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException{
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
        String enStr = new String(Base64.encodeBase64(encrypted));
        return enStr;
    }

    /**
     * AES256으로 암호화된 txt 를 복호화한다.
     * @param str 복호화할 문자열
     * @return
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public String decrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        byte[] byteStr = Base64.decodeBase64(str.getBytes());
        return new String(c.doFinal(byteStr), "UTF-8");
    }

}// end of class AES256///////////////////////////////////////

