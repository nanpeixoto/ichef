package br.com.ichef.exception;

import java.util.List;
/**
 * Classe que trata todas as exceptions do negocio 
 * @author jsouzaa
 *
 */
public class BusinessException extends AppException {

	private static final long serialVersionUID = -4806939480794045985L;

	private List<String> erroList;
	
	
    public BusinessException() {
    }
    
    /**
     * Exception que carrega todas as mensagens de validação da regra de negocio.
     * @param message
     */
    public BusinessException(String message) {
        super(message);
    }
    
    /**
     * Exception que carrega todas as mensagens de validação da regra de negocio.
     * @param erroList
     */
    public BusinessException(List<String> erroList) {
    	this.erroList = erroList;
    }
    
    /**
     * Exception que carrega todas as mensagens de validação da regra de negocio.
     * @param message
     * @param erroList
     */
    public BusinessException(String message, List<String> erroList) {
    	super(message);
    	this.erroList = erroList;
    }

	public List<String> getErroList() {
		return erroList;
	}

	public void setErroList(List<String> erroList) {
		this.erroList = erroList;
	}
}
