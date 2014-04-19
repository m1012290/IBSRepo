package com.shrinfo.ibs.validation;

import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.validate.ClientValidator;

@FacesValidator("custom.stringValidator")
public class IBSValidator implements Validator, ClientValidator {

    private Pattern pattern;

    private static final String STRING_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public IBSValidator() {
        pattern = Pattern.compile(STRING_PATTERN);
    }

    @Override
    public Map<String, Object> getMetadata() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValidatorId() {
        // TODO Auto-generated method stub
        return "custom.stringValidator";
    }

    @Override
    public void validate(FacesContext arg0, UIComponent arg1, Object value)
            throws ValidatorException {
        // TODO Auto-generated method stub
        if (value == null || value.equals("") || value == "" || value.toString().isEmpty()) {
            return;
        }

        if (!pattern.matcher(value.toString()).matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, value
                + ":Invalid email format, Please enter the correct EmailID", ""));
        }

    }

}