package br.com.ichef.converter;  
  
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;  
  
/** 
* Efetua a validação de um valor decimal. 
*/  
@FacesConverter("ExcelConverter")  
public class ExcelConverter implements Converter {  
 
   public void validate(FacesContext ctx, UIComponent comp, Object val) throws ValidatorException {  
	   
	   System.out.println(val.toString());
		 
	   if(val.toString().startsWith("="))
		   System.out.println();
	 
   }

@Override
public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
	System.out.println(arg2);
	
	if(arg2.toString().startsWith("="))
		   System.out.println();
	return null;
}

@Override
public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
	System.out.println(arg2);
	
	

	if(arg2.toString().startsWith("="))
		   System.out.println();
	return null;
}  
     
}  


