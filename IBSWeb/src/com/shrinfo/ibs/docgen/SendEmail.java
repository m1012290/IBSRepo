package com.shrinfo.ibs.docgen;
// File Name SendHTMLEmail.java



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.vo.business.ContactVO;

public class SendEmail {
	
	private Logger logger = Logger.getLogger(SendEmail.class);
	
	public static void sendEmail(String filePath,String companyName,ContactVO contact){
		
			Properties props = System.getProperties();
		    props.setProperty("mail.smtp.port", Utils.getSingleValueAppConfig("SMTPPORT"));
		    props.setProperty("mail.smtp.host", Utils.getSingleValueAppConfig("SMTPSERVER"));
		    props.setProperty("mail.smtp.starttls.enable", "true");
		    props.setProperty("mail.smtp.auth", "true");
		    
		    Authenticator auth = new MyAuthenticator();
		    Session session = Session.getInstance(props, auth);
		    session.setDebug(true);
			String from="hafeezur.r1@gmail.com";
			 // Sender's email ID needs to be mentioned
			
			try{
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress("hafeezur.r1@gmail.com"));
				message.setSubject("Quote Slip Document");
			//	message.setContent("<h1>test mail sent from Java Code ,I have used GMAIL SMTP SERVER Service</h1>","text/html");
				//message.addRecipient(Message.RecipientType.TO, new InternetAddress("hosur.sunil@gmail.com"));
				System.out.println("Emaild ID="+contact.getEmailId());
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(contact.getEmailId()));
				//message.addRecipient(Message.RecipientType.TO, new InternetAddress("Rahul_Reddy@mindtree.com"));
				//Part two 
				// Part two is attachment
				 // Create a multipar message
				// Create the message part
		         BodyPart messageBodyPart = new MimeBodyPart();

		         // Now set the actual message
		         messageBodyPart.setText("Hello "+companyName);
		         messageBodyPart.setText("Hello  "+companyName+"\n Please find the attached QuoteSlipDocument \n Thanks \n InsuranceBroker");
		         Multipart multipart = new MimeMultipart();
               // Set text message part
		         multipart.addBodyPart(messageBodyPart);
		         messageBodyPart = new MimeBodyPart();
		         String filename = filePath;
		         DataSource source = new FileDataSource(filename);
		         messageBodyPart.setDataHandler(new DataHandler(source));
		         System.out.println("filePath====="+filePath);
		         messageBodyPart.setFileName(companyName+"_quotelslip.pdf");
		         
		         multipart.addBodyPart(messageBodyPart);

		         // Send the complete message parts
		         message.setContent(multipart);
				Transport.send(message);

		
					
				//message.setContent("<h1>This is actual message</h1><img src='C:\\my data\\my works\\analytics\\source\\CEP\\WebContent\\resources\\images\\ajax-loader.gif' alt='Smiley face' height='42' width='42'>","text/html" );
				//
			}catch(FolderNotFoundException ex){
				ex.printStackTrace();			}
			catch (MessagingException ex) {
				ex.printStackTrace();			}
			}
				
	public static void main(String[] args) {
		//sendEmail();
	}
}


