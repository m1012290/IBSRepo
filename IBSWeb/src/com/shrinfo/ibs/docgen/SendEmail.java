package com.shrinfo.ibs.docgen;
// File Name SendHTMLEmail.java


import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import javax.activation.*;

import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.vo.business.ContactVO;

public class SendEmail {
	
	private Logger logger = Logger.getLogger(SendEmail.class);
	
	public static void sendEmail(byte[] file,String companyName,ContactVO contact,String slipname) {
		
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
				//message.addRecipient(Message.RecipientType.TO, new InternetAddress("hosur.sunil@gmail.com"));
				System.out.println("Emaild ID="+contact.getEmailId());
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(contact.getEmailId()));
				//message.addRecipient(Message.RecipientType.TO, new InternetAddress("Rahul_Reddy@mindtree.com"));
			
		        
		         Multipart multipart = new MimeMultipart();
		         DataSource  source = new ByteArrayDataSource(file,"application/pdf");
		         BodyPart pdfBodypart = new MimeBodyPart();
		         pdfBodypart.setDataHandler(new DataHandler(source));
		         System.out.println("filePath====="+file);
		         
		         BodyPart messageBodyPart = new MimeBodyPart();
		         if(slipname.equalsIgnoreCase("quoteslip")){
		        	 messageBodyPart.setText("Hello  "+companyName+"\n \n Please find the attached QuoteSlipDocument \n \n \nThanks \n InsuranceBroker");
			         pdfBodypart.setFileName(companyName+"_quotelslip.pdf");
					 message.setSubject("Quote Slip Document");


		         }
		         else {
		        	 messageBodyPart.setText("Hello  "+companyName+"\n \n Please find the attached CloseSlipDocument \n \n \nThanks \n InsuranceBroker");
			         pdfBodypart.setFileName(companyName+"_closelslip.pdf");
					 message.setSubject("Close Slip Document");
		         }
		         
		         multipart.addBodyPart(pdfBodypart);
		         multipart.addBodyPart(messageBodyPart);

		         // Send the complete message parts
		         message.setContent(multipart);
				 Transport.send(message);

		
			}catch(FolderNotFoundException ex){
				ex.printStackTrace();			}
			catch (MessagingException ex) {
				ex.printStackTrace();			}
			}
				
	public static void main(String[] args) {
		//sendEmail();
	}
}


