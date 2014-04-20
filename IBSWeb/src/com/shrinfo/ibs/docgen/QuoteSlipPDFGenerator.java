package com.shrinfo.ibs.docgen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.shrinfo.ibs.vo.business.ContactVO;
import com.shrinfo.ibs.vo.business.InsuredVO;
import com.shrinfo.ibs.vo.business.ProductUWFieldVO;
import com.shrinfo.ibs.vo.business.ProductVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;

public class QuoteSlipPDFGenerator {

    public void generatePDF(QuoteDetailVO quoteDetails, InsuredVO insuredDetails,
            ContactVO contacts, String companyName, String filePath, String imagePath) {

        try {

            String insuredname = insuredDetails.getName();

            String prodName = quoteDetails.getProductDetails().getName();

            ProductVO products = quoteDetails.getProductDetails();
            java.util.List<ProductUWFieldVO> prodListFields = products.getUwFieldsList();


            OutputStream file = new FileOutputStream(new File(filePath));
            Document document = new Document();
            PdfWriter.getInstance(document, file);

            // Inserting Image in PDF
          /*  Image image = Image.getInstance(imagePath);
            image.scaleAbsolute(120f, 60f);// image width,height
*/            
            java.util.Iterator<ProductUWFieldVO> itr1 = prodListFields.iterator();

            // Now Insert Every Thing Into PDF Document
            document.open();// PDF document opened........

            document.add(Chunk.NEWLINE); // Something like in HTML :-)

           // document.add(new Paragraph("Document Generated On - " + new Date().toString()));
            
            document.add(new Paragraph("To"));
            
           /* document.add(new Paragraph("  "+companyName));
            
            document.add(new Paragraph("  "+contacts.getAddressVO().getAddress()));
            
            document.add(new Paragraph("  "+contacts.getAddressVO().getCity()));*/
            
            document.add(new Paragraph("  Insurance Comp Name"));
            
            document.add(new Paragraph("  14th Sector ,Main Street"));
            
            document.add(new Paragraph("  Oman"));
                        
            document.add(new Paragraph("_____________________________________________________________________________"));
            
            document.add(new Paragraph("                                                        "+prodName+"                                        "));
            
            document.add(new Paragraph("_____________________________________________________________________________"));
            
            document.add(new Paragraph("Insured Name:             "+insuredname));
            
            document.add(Chunk.NEWLINE); // Something like in HTML :-)
            
            while (itr1.hasNext()) {
                ProductUWFieldVO prodFields = itr1.next();
                document.add(new Paragraph(prodFields.getFieldName()+":                    "+prodFields.getFieldValue()));
                document.add(Chunk.NEWLINE); // Something like in HTML :-)
            }

            document.close();

            file.close();

            System.out.println("Pdf created successfully..");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void generatePDFForCloseSlip(QuoteDetailVO quoteDetails, InsuredVO insuredDetails,
            ContactVO contacts, String companyName, String filePath, String imagePath){
    	
        try {

            String insuredname = insuredDetails.getName();

            String prodName = quoteDetails.getProductDetails().getName();

            ProductVO products = quoteDetails.getProductDetails();
            java.util.List<ProductUWFieldVO> prodListFields = products.getUwFieldsList();


            OutputStream file = new FileOutputStream(new File(filePath));
            Document document = new Document();
            PdfWriter.getInstance(document, file);

            // Inserting Image in PDF
          /*  Image image = Image.getInstance(imagePath);
            image.scaleAbsolute(120f, 60f);// image width,height
*/            
            java.util.Iterator<ProductUWFieldVO> itr1 = prodListFields.iterator();

            // Now Insert Every Thing Into PDF Document
            document.open();// PDF document opened........

            document.add(Chunk.NEWLINE); // Something like in HTML :-)

           // document.add(new Paragraph("Document Generated On - " + new Date().toString()));
            
            document.add(new Paragraph("To"));
            
            document.add(new Paragraph("  "+companyName));
            
            document.add(new Paragraph("_____________________________________________________________________________"));
            
            document.add(new Paragraph("                                                        "+prodName+"                                        "));
            
            document.add(new Paragraph("_____________________________________________________________________________"));
            
            document.add(new Paragraph("Insured Name:             "+insuredname));
            
            document.add(Chunk.NEWLINE); // Something like in HTML :-)
            
            document.add(new Paragraph("Quotation Number:             "+quoteDetails.getQuoteNo()));
            
            document.add(Chunk.NEWLINE); // Something like in HTML :-)
            
            document.add(new Paragraph("Quoted Premium:             "+quoteDetails.getQuoteSlipPrmDetails().getPremium()));
            
            document.add(Chunk.NEWLINE); // Something like in HTML :-)

            document.add(new Paragraph("Sum  Insured:             "+quoteDetails.getSumInsured()));
            
            document.add(Chunk.NEWLINE); // Something like in HTML :-)
            
            document.add(new Paragraph("Policy Term:             "+quoteDetails.getPolicyTerm()));
            
            document.add(Chunk.NEWLINE); // Something like in HTML :-)
            
            document.add(new Paragraph("Cover Description:             "+quoteDetails.getQuoteSlipDescription()));
            
            document.add(Chunk.NEWLINE); // Something like in HTML :-)
            
            document.add(new Paragraph("Recommendation Summary:             "+quoteDetails.getRecommendationSummary()));
            
            document.add(Chunk.NEWLINE); // Something like in HTML :-)


            
            while (itr1.hasNext()) {
                ProductUWFieldVO prodFields = itr1.next();
                document.add(new Paragraph(prodFields.getFieldName()+":                    "+prodFields.getFieldValue()));
                document.add(Chunk.NEWLINE); // Something like in HTML :-)
            }

            document.close();

            file.close();

            System.out.println("Pdf created successfully for close slip.....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	
    	
    }
}
