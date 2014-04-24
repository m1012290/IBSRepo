package com.shrinfo.ibs.docgen;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import com.shrinfo.ibs.cmn.utils.Utils;




public  class MyAuthenticator extends Authenticator {

    public PasswordAuthentication getPasswordAuthentication() {
    	String username="";
    String password="" ;
    	try{
         username = Utils.getSingleValueAppConfig("SMTPUSERNAME");
         password = Utils.getSingleValueAppConfig("SMTPPASSWORD");
         //System.out.println("username/pwd=" + username + "--"+ password);
        
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return new PasswordAuthentication(username, password);
    }
}