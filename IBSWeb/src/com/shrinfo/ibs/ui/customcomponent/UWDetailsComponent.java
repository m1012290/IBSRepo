/**
 * 
 */
package com.shrinfo.ibs.ui.customcomponent;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UISelectItem;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectonemenu.SelectOneMenu;

import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.vo.business.ProductUWFieldVO;
import com.shrinfo.ibs.vo.business.ProductVO;


/**
 * @author Sunil Kumar
 */
@FacesComponent("components.CustomComponent")
@ResourceDependencies({
    @ResourceDependency(library="primefaces", name="primefaces.css"),
    @ResourceDependency(library="primefaces", name="jquery/jquery.js"),
    @ResourceDependency(library="primefaces", name="jquery/jquery-plugins.js"),
    @ResourceDependency(library="primefaces", name="primefaces.js")
})
public class UWDetailsComponent extends UIComponentBase {

    @Override
    public String getFamily() {
        return "com.shrinfo.ibs.ui.customcomponent";
    }
    
    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);
      /*
        Integer productClass = (Integer)getAttributes().get("value");
        encodeMarkUp(context, getUWFields(productClass)); 
        */
        ProductVO productVO = (ProductVO)getAttributes().get("value");
        String tableCols = (String)getAttributes().get("tablecols");
        if(!Utils.isEmpty(productVO) && !Utils.isEmpty(productVO.getUwFieldsList())){
        	encodeMarkUp(context, productVO, tableCols);
        }
    }
    
    //Service call would be done to retrieve applicable product details
    /*
    private ProductVO getUWFields(Integer forProductClass){
        ProductVO productVO = new ProductVO();
        List<ProductUWFieldVO> uwFieldsList = new ArrayList<ProductUWFieldVO>();
        ProductUWFieldVO uwFieldVO = new ProductUWFieldVO();
        uwFieldVO.setFieldName("Test text box");
        uwFieldVO.setFieldOrder(1);
        uwFieldVO.setFieldType("textbox");
        uwFieldVO.setIsMandatory("Y");
        uwFieldVO.setFieldLength(20);
        uwFieldsList.add(uwFieldVO);
        
        
        ProductUWFieldVO uwFieldVO1 = new ProductUWFieldVO();
        uwFieldVO1.setFieldName("Test drop down");
        uwFieldVO1.setFieldOrder(2);
        uwFieldVO1.setFieldType("dropdown");
        uwFieldsList.add(uwFieldVO1);
      
        productVO.setUwFieldsList(uwFieldsList);
        return productVO;
    }*/
    
    private void encodeMarkUp(FacesContext context, BaseVO inputVO, String tableCols) throws IOException{
        if(!Utils.isEmpty(inputVO)){
            int tableColSize = 0;
            try{
                tableColSize = Integer.valueOf(tableCols);
            }catch(NumberFormatException nfe ){
                FacesContext.getCurrentInstance().addMessage("ERROR_ATTRIBUTE_VALUE", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Invalid tablecols value for custom underwriting component", null));
                throw new SystemException("", nfe, "table col size is invalid for the custom component");
            }
            //Iterate through product UW fields and render corresponding markup component
            ProductVO productVO = (ProductVO) inputVO;
            HtmlPanelGrid panelGrid = new HtmlPanelGrid();
            panelGrid.setColumns(tableColSize);
            List<UIComponent> childComponents = new ArrayList<UIComponent>();
            ResponseWriter writer = context.getResponseWriter();
            int i = 0;
            Collections.sort(productVO.getUwFieldsList());
            for(ProductUWFieldVO productUWField : productVO.getUwFieldsList()){
            	if(i!=0){
            		writer.write("&nbsp;&nbsp;");
            	}
            	childComponents.add(encodeOutputText(context, productUWField));
                writer.write("&nbsp;&nbsp;");
                if(productUWField.getFieldType().equalsIgnoreCase("dropdown")){
                    encodeDropDown(context, productUWField);
                }else if(productUWField.getFieldType().equalsIgnoreCase("textbox")){
                    childComponents.add(encodeTextBox(context, productUWField));
                }else if(productUWField.getFieldType().equalsIgnoreCase("radiobutton")){
                    
                }else if(productUWField.getFieldType().equalsIgnoreCase("datepicker")){
                    childComponents.add(encodeDatePicker(context, productUWField));
                }
                i= i + 1;
            }
            panelGrid.getChildren().addAll(childComponents);
            panelGrid.encodeAll(context);
        }
    }
    
    private void encodeDropDown(FacesContext context, BaseVO inputVO) throws IOException{
        ProductUWFieldVO productUWFieldVO = (ProductUWFieldVO) inputVO;
        SelectOneMenu selectOneMenu = new SelectOneMenu();
        selectOneMenu.setId(Utils.concat("field_",String.valueOf(productUWFieldVO.getFieldOrder())));
        UISelectItem selectItem1 = new UISelectItem();
        selectItem1.setItemLabel("Test Label1");
        selectItem1.setItemValue("Test Value1");
        selectOneMenu.getChildren().add(selectItem1);
        UISelectItem selectItem2 = new UISelectItem();
        selectItem2.setItemLabel("Test Label2");
        selectItem2.setItemValue("Test Value2");
        selectOneMenu.getChildren().add(selectItem2);
        selectOneMenu.encodeAll(context);
    }
    
    private UIComponent encodeOutputText(FacesContext context, BaseVO inputVO) throws IOException{
        ProductUWFieldVO productUWFieldVO = (ProductUWFieldVO) inputVO;
        HtmlOutputText outputText = new HtmlOutputText();
        outputText.setValue(productUWFieldVO.getFieldName());
        //outputText.encodeAll(context);
        return outputText;
    }
    
    private void encodeDataTable(FacesContext context){
    	
    }
    
    private UIComponent encodeTextBox(FacesContext context, BaseVO inputVO) throws IOException{
    	
        ProductUWFieldVO productUWFieldVO = (ProductUWFieldVO)inputVO;
        InputText textBox = new InputText();
        textBox.setId(Utils.concat("field_",String.valueOf(productUWFieldVO.getFieldOrder())));
       // textBox.setMaxlength(productUWFieldVO.getFieldLength());
        textBox.setMaxlength(10);
        //textBox.setRequired(productUWFieldVO.getIsMandatory().equalsIgnoreCase("Y")?true:false);
        textBox.setRequiredMessage(Utils.concat(productUWFieldVO.getFieldName(), "cannot be blank or empty"));
        ValueExpression ve = context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{quoteSlipMB.quoteDetailVO.productDetails.uwFieldsList["+productUWFieldVO.getFieldOrder()+"].response}", String.class);
        textBox.setValueExpression("value", ve);
        textBox.setValue(productUWFieldVO.getResponse());
        //textBox.encodeAll(context);
        
        return textBox;
    }
    
    // Adds calendar picker
    private UIComponent encodeDatePicker(FacesContext context, BaseVO inputVO) throws IOException{
        ProductUWFieldVO productUWFieldVO = (ProductUWFieldVO)inputVO;
        Calendar calendar = new Calendar();
        calendar.setEffect("slideDown");
        calendar.setNavigator(true);
        calendar.setPattern("dd/MM/yyyy");
        if(!Utils.isEmpty(productUWFieldVO.getResponse())){
            try {
                calendar.setValue(new SimpleDateFormat("dd/MM/yyyy").parse(productUWFieldVO.getResponse()));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        calendar.setId(Utils.concat("field_",String.valueOf(productUWFieldVO.getFieldOrder())));
        //calendar.encodeAll(context);
        
        return calendar;
    }
}