package br.com.ichef.converter;  
  
import java.math.BigDecimal;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;  
  
/** 
* Efetua a validação de um valor decimal. 
*/  
@FacesValidator("DecimalConverter")  
public class DecimalConverter implements Validator {  
  
         
   /** 
    * Método responsável por validar os campos com moeda. Caso ocorra algum erro lança uma ValidatorException. 
    */  
   public void validate(FacesContext ctx, UIComponent comp, Object val) throws ValidatorException {  
	   if(val == null)  
           return;      
	   
	   if(val.toString().startsWith("="))
		   System.out.println();
	   
	   if(val.toString().contains(","))
		   val = val.toString().replace(",", ".");
	   
       BigDecimal valor = (BigDecimal) val;  
         
       if(val == null)  
            return;               
         
       /* if ( valor.equals(new BigDecimal(0))  ) {  
           FacesMessage message = new FacesMessage("Preencha com o valor válido maior que zero");  
           message.setSeverity(FacesMessage.SEVERITY_ERROR);  
           throw new ValidatorException(message);  
       } */ 
         
   }  
     
}  


