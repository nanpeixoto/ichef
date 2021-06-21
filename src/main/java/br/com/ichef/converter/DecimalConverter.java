package br.com.ichef.converter;

import java.math.BigDecimal;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


@FacesValidator("decimalConverter")
@FacesConverter("decimalConverter")
public class DecimalConverter implements Validator {

	
	@SuppressWarnings("unused")
	public void validate(FacesContext ctx, UIComponent comp, Object val) throws ValidatorException {
		if (val == null)
			return;

		if (val.toString().startsWith("="))
			System.out.println();

		if (val.toString().contains(","))
			val = val.toString().replace(",", ".");

		BigDecimal valor = (BigDecimal) val;

		if (val == null)
			return;


	}

}
