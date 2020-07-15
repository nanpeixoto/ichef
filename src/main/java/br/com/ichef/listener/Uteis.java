package br.com.ichef.listener;
 
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
 
public class Uteis {
 
	public static EntityManager JpaEntityManager(){
 
		FacesContext facesContext = FacesContext.getCurrentInstance();
 
		ExternalContext externalContext = facesContext.getExternalContext();
 
		HttpServletRequest request  = (HttpServletRequest)externalContext.getRequest();
 
		return (EntityManager)request.getAttribute("entityManager");
	} 
 
}