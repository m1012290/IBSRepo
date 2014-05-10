package com.shrinfo.ibs.docgen;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.shrinfo.ibs.vo.business.ContactVO;
import com.shrinfo.ibs.vo.business.InsuredVO;
import com.shrinfo.ibs.vo.business.ProductUWFieldVO;
import com.shrinfo.ibs.vo.business.ProductVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;

public class QuoteSlipPDFGenerator {

    public void generatePDF(QuoteDetailVO quoteDetails, InsuredVO insuredDetails,
            ContactVO contacts, String companyName, String filePath, String imagePath) throws Exception {

        try {
            Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,
                      Font.BOLD);
            Font prodFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
                  Font.BOLD);
            Font insFont = new Font(Font.FontFamily.COURIER, 26,
                      Font.BOLD);
            Font lnFont = new Font(Font.FontFamily.COURIER, 24,
                  Font.BOLD);
            
            
            String insuredname = insuredDetails.getName();

            String prodName = quoteDetails.getProductDetails().getName();

            ProductVO products = quoteDetails.getProductDetails();
            java.util.List<ProductUWFieldVO> prodListFields = products.getUwFieldsList();


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

          // Inserting Image in PDF
            /*Image image = Image.getInstance("insimage.jpg");
            image.scaleAbsolute(120f, 60f);// image width,height            
*/            java.util.Iterator<ProductUWFieldVO> itr1 = prodListFields.iterator();

            // Now Insert Every Thing Into PDF Document
            document.open();// PDF document opened........
            
            document.add(new Paragraph("         XYZ INSURANCE  ",insFont));
            
            document.add(new Paragraph("===================================",lnFont));
                       
            
            document.add(new Paragraph(companyName,catFont));
            
            if(contacts.getAddressVO().getAddress()!=null){
                document.add(new Paragraph("   "+contacts.getAddressVO().getAddress()));
            } 
            if(contacts.getAddressVO().getCity()!=null){
                document.add(new Paragraph("   "+contacts.getAddressVO().getCity()));
            } 
          
            document.add(new Paragraph("-----------------------------------------------------------------------------------------------------------------------------"));
            
            document.add(new Paragraph("                                                        "+prodName+"                                        ",prodFont));
            
            document.add(new Paragraph("-----------------------------------------------------------------------------------------------------------------------------"));
            
            document.add(Chunk.NEWLINE); 
            
            PdfPTable table = new PdfPTable(2);
            table.addCell(new Paragraph("Insured Name"));
            table.addCell(new Paragraph(insuredname));
            table.addCell(new Paragraph("Quote Slip No"));
            if(quoteDetails.getQuoteSlipId()!=null){
                table.addCell(new Paragraph(String.valueOf(quoteDetails.getQuoteSlipId())));
            }
            else {
                table.addCell(new Paragraph(""));
            }
            table.addCell(new Paragraph("Quote Slip Date"));
            if(quoteDetails.getQuoteSlipDate()!=null){
                table.addCell(new Paragraph(quoteDetails.getQuoteSlipDate().toString()));

            }
            else {
                table.addCell(new Paragraph(""));
            }            
            
            while (itr1.hasNext()) {
                ProductUWFieldVO prodFields = itr1.next();
                table.addCell(new Paragraph(prodFields.getFieldName()));  
                if(prodFields.getResponse()!=null){
                    table.addCell(new Paragraph(prodFields.getResponse()));
                }
                else {
                    table.addCell(new Paragraph(" "));
                }
            }
           document.add(table);
           document.close();
          

           SendEmail.sendEmail(outputStream.toByteArray(),companyName,contacts,"quoteslip");
            

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void generatePDFForCloseSlip(QuoteDetailVO quoteDetails, InsuredVO insuredDetails,
            ContactVO contacts, String companyName, String filePath, String imagePath) throws Exception{
        
        try {
            
            Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,
                  Font.BOLD);
            Font prodFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
                  Font.BOLD);
            Font insFont = new Font(Font.FontFamily.COURIER, 26,
                  Font.BOLD);
            Font lnFont = new Font(Font.FontFamily.COURIER, 24,
                  Font.BOLD);
            
            
            String insuredname = insuredDetails.getName();

            String prodName = quoteDetails.getProductDetails().getName();

            ProductVO products = quoteDetails.getProductDetails();
            java.util.List<ProductUWFieldVO> prodListFields = products.getUwFieldsList();


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            // Inserting Image in PDF
          /*  Image image = Image.getInstance(imagePath);
            image.scaleAbsolute(120f, 60f);// image width,height
*/            
            java.util.Iterator<ProductUWFieldVO> itr1 = prodListFields.iterator();

            // Now Insert Every Thing Into PDF Document
            document.open();// PDF document opened........
            
            document.add(new Paragraph("         XYZ INSURANCE  ",insFont));
            
            document.add(new Paragraph("===================================",lnFont));
 
            document.add(new Paragraph(companyName,catFont));     
            
            if(contacts.getAddressVO().getAddress()!=null){
                document.add(new Paragraph("  "+contacts.getAddressVO().getAddress()));
            } 
            if(contacts.getAddressVO().getCity()!=null){
                document.add(new Paragraph("  "+contacts.getAddressVO().getCity()));
            } 
            
            document.add(new Paragraph("-----------------------------------------------------------------------------------------------------------------------------"));
            
            document.add(new Paragraph("                                                        "+prodName+"                                        ",prodFont));
            
            document.add(new Paragraph("-----------------------------------------------------------------------------------------------------------------------------"));
            
            document.add(Chunk.NEWLINE); 
            
            PdfPTable table = new PdfPTable(2);
            table.addCell(new Paragraph("Insured Name"));
            if(insuredname!=null)
            table.addCell(new Paragraph(insuredname));
            else
            table.addCell("");

          
            table.addCell(new Paragraph("Quotation Number"));
            if(quoteDetails.getQuoteNo()!=null)
            table.addCell(new Paragraph(quoteDetails.getQuoteNo()));
            else
            table.addCell("");
            
            
            table.addCell(new Paragraph("Quoted Premium"));
            if(quoteDetails.getQuoteSlipPrmDetails().getPremium()!=null || quoteDetails.getQuoteSlipPrmDetails().getPremium()!=new BigDecimal(0))
            table.addCell(new Paragraph(String.valueOf(quoteDetails.getQuoteSlipPrmDetails().getPremium())));
            else
            table.addCell("");
            
            
            table.addCell(new Paragraph("Sum  Insured"));
            if(quoteDetails.getSumInsured()!=null || quoteDetails.getSumInsured()!=new BigDecimal(0))
            table.addCell(new Paragraph(String.valueOf(quoteDetails.getSumInsured())));
            else
            table.addCell("");
            
            table.addCell(new Paragraph("Policy Term"));
            if(quoteDetails.getPolicyTerm()!=null || quoteDetails.getPolicyTerm()!=0)
            table.addCell(new Paragraph(String.valueOf(quoteDetails.getPolicyTerm())));
            else
            table.addCell("");
            
            table.addCell(new Paragraph("Cover Description"));
            if(quoteDetails.getQuoteSlipPrmDetails().getCoverDescription()!=null)
            table.addCell(new Paragraph(quoteDetails.getQuoteSlipPrmDetails().getCoverDescription()));
            else
            table.addCell("");
            
            table.addCell(new Paragraph("Recommendation Summary"));
            if(quoteDetails.getRecommendationSummary()!=null)
            table.addCell(new Paragraph(quoteDetails.getRecommendationSummary()));
            else
            table.addCell("");
            
            
            while (itr1.hasNext()) {
                ProductUWFieldVO prodFields = itr1.next();
                table.addCell(new Paragraph(prodFields.getFieldName()));
                if(prodFields.getResponse()!=null)
                table.addCell(new Paragraph(prodFields.getResponse()));
                else
                table.addCell("");  
            }
            
            document.add(table);

            document.close();

            
            
            SendEmail.sendEmail(outputStream.toByteArray(),companyName,contacts,"closeslip");


            System.out.println("Pdf created successfully for close slip.....");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        
    }
}
