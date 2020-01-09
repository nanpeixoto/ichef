package br.com.ichef.converter;  
  
import java.math.BigDecimal;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;  
  
/** 
* Efetua a valida��o de um valor decimal. 
*/  
@FacesValidator("DecimalConverter")  
public class DecimalConverter implements Validator {  
  
         
   /** 
    * M�todo respons�vel por validar os campos com moeda. Caso ocorra algum erro lan�a uma ValidatorException. 
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
           FacesMessage message = new FacesMessage("Preencha com o valor v�lido maior que zero");  
           message.setSeverity(FacesMessage.SEVERITY_ERROR);  
           throw new ValidatorException(message);  
       } */ 
         
   }  
     
}  


