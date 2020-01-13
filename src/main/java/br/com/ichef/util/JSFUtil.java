package br.com.ichef.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.webapp.MultipartRequest;

public class JSFUtil {
	
	public static String getRequestParameter(String name) {
        return (String) getContext().getExternalContext().getRequestParameterMap().get(name);
    }
	
	public static Object getSessionMapValue(String key) {
		if (getContext() != null) {
			return getContext().getExternalContext().getSessionMap().get(key);
		} else {
			return null;
		}
    }

    public static void setSessionMapValue(String key, Object value) {
        getContext().getExternalContext().getSessionMap().put(key, value);
    }
    
    public static Object getApplicationMapValue(String key) {
        return getContext().getExternalContext().getApplicationMap().get(key);
    }

    public static void setApplicationMapValue(String key, Object value) {
        getContext().getExternalContext().getApplicationMap().put(key, value);
    }
    
    public static HttpServletResponse getServletResponse() {
		FacesContext context = getContext();

		HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();

		return response;
	}

	public static FacesContext getContext() {
		return FacesContext.getCurrentInstance();
	}
	
	public static void addErrorMessage(Exception ex, String defaultMsg) {
		String msg = ex.getLocalizedMessage();
		if (msg != null && msg.length() > 0) {
			addErrorMessage(msg);
		} else {
			addErrorMessage(defaultMsg);
		}	
	}

	  
	public static void addErrorMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
		getContext().addMessage(null, facesMsg);
	}

    
 	public static MultipartRequest getMultipartRequestInChain() {
		Object request = getContext().getExternalContext().getRequest();
		
		while(request instanceof ServletRequestWrapper) {
			if(request instanceof MultipartRequest) {
				return (MultipartRequest) request;
			} else {
				request = ((ServletRequestWrapper) request).getRequest();
			}
		}
		
		return null;
	}

}
