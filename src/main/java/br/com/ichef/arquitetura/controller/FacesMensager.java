package br.com.ichef.arquitetura.controller;

import java.io.Serializable;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.ichef.util.MensagemUtil;

@RequestScoped
public class FacesMensager implements Serializable {

	// ************************************************************************************************
	// Metodos de Mensagem
	// ************************************************************************************************

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 


	/**
	 * M�todo que cria uma mensagem de erro e adiciona no contexto corrente
	 * 
	 * @param msg
	 */
	public void addMessageError(Exception e) {
		addMessageError(e.getMessage());
	}

	/**
	 * M�todo que cria uma mensagem de informacao e adiciona no contexto corrente
	 * 
	 * @param codMensagem
	 */
	public void addMessageError(String codMensagem) {
		String msg = getMensagem(codMensagem);
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
		 
	}

	 

	/**
	 * Recupera a mensagem ou do arquivo de propriedades ou a mensagem passada como
	 * par�metro.
	 * 
	 * @param msg
	 *            - {@link String}
	 * @return msg tratada - {@link String}
	 */
	private static String getMensagem(String msg) {
		// if (msg.startsWith("MN")){
		return MensagemUtil.obterMensagem(msg);
		// }
		// return msg;
	}

	/**
	 * Gera Mensagem de informacao para o usuario.
	 * 
	 * @param String
	 *            - Mensagem a ser exibida.
	 */
	public void info(String message) {
		FacesContext.getCurrentInstance().addMessage(message,
				new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
	}

	/**
	 * Gera Mensagem de informacao para o usuario.
	 * 
	 * @param String
	 *            - Mensagem a ser exibida.
	 */
	public void infoSemTitulo(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, ""));

	}

	public void info(UIComponent component, String message) {
		FacesContext.getCurrentInstance().addMessage(component.getClientId(FacesContext.getCurrentInstance()),
				new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
	}

	/**
	 * Gera Mensagem de informacao para o usuario.
	 * 
	 * @param String
	 *            - Mensagem a ser exibida.
	 */
	public void infoComTitulo(String Titulo, String message) {
		FacesContext.getCurrentInstance().addMessage(Titulo,
				new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
	}

	/**
	 * Gera Mensagem de erro para o usuario.
	 * 
	 * @param String
	 *            - Mensagem a ser exibida.
	 */
	public void error(String message) {
		FacesContext.getCurrentInstance().addMessage(message,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
	}

	/**
	 * Gera Mensagem de erro para o usuario.
	 * 
	 * @param String
	 *            - Mensagem a ser exibida.
	 */
	public void aviso(String message) {
		FacesContext.getCurrentInstance().addMessage(message,
				new FacesMessage(FacesMessage.SEVERITY_WARN, message, null));
	}

	/**
	 * Gera Mensagem de erro para o usuario.
	 * 
	 * @param String
	 *            - Mensagem a ser exibida.
	 */
	public void error(String Titulo, String message) {
		FacesContext.getCurrentInstance().addMessage(Titulo,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
	}

	/**
	 * Gera Mensagem de erro fatal para o usuario.
	 * 
	 * @param String
	 *            - Mensagem a ser exibida.
	 */
	public void fatal(String message) {
		FacesContext.getCurrentInstance().addMessage(message,
				new FacesMessage(FacesMessage.SEVERITY_FATAL, message, null));
	}

	/**
	 * Gera Mensagem de aviso para o usuario.
	 * 
	 * @param String
	 *            - Mensagem a ser exibida.
	 */
	public void warn(String message) {
		FacesContext.getCurrentInstance().addMessage(message,
				new FacesMessage(FacesMessage.SEVERITY_WARN, message, null));
	}

	/**
	 * Verifica se existe alguma mensagem de erro no {@link FacesMessage}
	 * 
	 * @return <code>true</code> caso exista, e <code>false</code> caso contr�rio.
	 */
	public boolean possuiMensagemErro() {
		Iterator<FacesMessage> messages = FacesContext.getCurrentInstance().getMessages();
		while (messages.hasNext()) {
			FacesMessage msg = messages.next();

			if (msg.getSeverity().equals(FacesMessage.SEVERITY_ERROR)) {
				return true;
			}
		}
		return false;
	}
}
