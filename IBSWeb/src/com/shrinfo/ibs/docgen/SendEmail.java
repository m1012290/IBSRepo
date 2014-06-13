package com.shrinfo.ibs.docgen;
// File Name SendHTMLEmail.java


import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

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
	
    public static void sendEmail(String subject, String body, List<File> attachments, List<String> recipientList) {

        Properties props = System.getProperties();
        props.setProperty("mail.smtp.port", Utils.getSingleValueAppConfig("SMTPPORT"));
        props.setProperty("mail.smtp.host", Utils.getSingleValueAppConfig("SMTPSERVER"));
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.auth", "true");

        Authenticator auth = new MyAuthenticator();
        Session session = Session.getInstance(props, auth);
        session.setDebug(true);
        String from = "hafeezur.r1@gmail.com";
        // Sender's email ID needs to be mentioned

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
           
            for(String recipient : recipientList) {
                message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(recipient));
            }
            message.setSubject(subject);
            // Email body
            Multipart multipart = new MimeMultipart();
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            multipart.addBodyPart(messageBodyPart);
            
            // Email attachments
           /* for(File file : attachments) {
                DataSource source = new ByteArrayDataSource(attachment, "application/pdf");
                BodyPart pdfBodypart = new MimeBodyPart();
                pdfBodypart.setDataHandler(new DataHandler(source));
                System.out.println("filePath=====" + attachment);                
                pdfBodypart.setFileName(companyName + "_quotelslip.pdf");
                multipart.addBodyPart(pdfBodypart);
            }      */     
            

            // Send the complete message parts
            message.setContent(multipart);
            Transport.send(message);


        } catch (FolderNotFoundException ex) {
            ex.printStackTrace();
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
				
	public static void main(String[] args) {
		//sendEmail();
	}
}


