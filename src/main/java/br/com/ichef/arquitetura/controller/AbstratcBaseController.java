package br.com.ichef.arquitetura.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

import br.com.ichef.util.JSFUtil;
import br.com.ichef.util.ReportUtils;

/**
 * Classe base para qualquer outro controller
 */
public abstract class AbstratcBaseController implements Serializable {
	private static final long serialVersionUID = 1L;

	protected String facesRedirect = "?faces-redirect=true";
	
	protected FacesMensager facesMessager;

	/**
	 * Construtor default
	 */
	public AbstratcBaseController() {
		super();
		facesMessager = new FacesMensager();
	}
	
	
	

	/**
	 * Esconde o componente do widgetvar especificado
	 * 
	 * @param widgetvar
	 */
	protected void hideDialog(String widgetvar) {
		//FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("PF('"+widgetvar+"').hide()");
		
		PrimeFaces.current().executeScript("PF('"+widgetvar+"').hide()");

	}

	/**
	 * Método para ordenar string ignorando utilizado no sortFuction
	 * 
	 * @param msg1
	 * @param msg2
	 * @return
	 */
	public int sortByString(Object msg1, Object msg2) {
		return ((String) msg1).compareToIgnoreCase(((String) msg2));
	}

	/**
	 * Mostra o componente do widgetvar especificado
	 * 
	 * @param widgetvar
	 */
	protected void showDialog(String widgetvar) {
		//RequestContext.getCurrentInstance().execute(widgetvar + ".show()");
		PrimeFaces.current().executeScript("PF('"+widgetvar+"').show();");
	}

	/**
	 * Faz update nos componentes dos respectivos ids passados como parâmetro
	 * 
	 * @param idComponente
	 */
	protected void updateComponentes(String... idComponente) {
		for (String id : idComponente) {
			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add(id);
			PrimeFaces.current().ajax().update(id);
		}
	}

	protected String includeRedirect(String url) {
		String formatedUrl = url.concat(facesRedirect);
		return formatedUrl;
	}



	protected void logErroMessage() {
		/*
		 * logger.severe("--------------------------------------------");
		 * logger.severe(
		 * "Ocorreu um erro ao Persistir o registro, verifique o log do servidor."
		 * ); logger.severe("--------------------------------------------");
		 */
	}

	// =======================================================================================================
	// Métodos Relatório
	// =======================================================================================================
	protected void escreveRelatorio(byte[] relatorio, String nome, boolean download) {
		try {
			this.escreveRelatorioResponse(relatorio, nome, ReportUtils.REL_TIPO_PDF, "application/pdf", download);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void escreveRelatorio(byte[] relatorio, String nome, String tipo, String contentType, boolean download) {
		try {
			this.escreveRelatorioResponse(relatorio, nome, tipo, contentType, download);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void escreveRelatorio(byte[] relatorio, String nome, String contentType, boolean download) throws IOException {
		try {
			this.escreveRelatorioResponse(relatorio, nome, null, contentType, download);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void escreveRelatorioResponse(byte[] relatorio, String nome, String tipo, String contentType, boolean download) throws IOException {
		try {
			String contentDisposition = download ? "attachment;filename=" : "inline;filename=";
			String nomeCompleto;

			if (tipo != null) {
				nomeCompleto = contentDisposition + "\"" + nome + "." + tipo.toLowerCase() + "\"";
			} else {
				nomeCompleto = contentDisposition + "\"" + nome + "\"";
			}

			JSFUtil.getServletResponse().setContentType(contentType);
			JSFUtil.getServletResponse().setHeader("Content-Disposition", nomeCompleto);
			JSFUtil.getServletResponse().setContentLength(relatorio.length);
			JSFUtil.getServletResponse().getOutputStream().write(relatorio);
		} catch (IOException e) {
			throw e;
		} finally {
			JSFUtil.getServletResponse().getOutputStream().flush();
			JSFUtil.getServletResponse().getOutputStream().close();
			JSFUtil.getContext().responseComplete();
		}
	}
	
	
	public Boolean isNotEmptyOrNull(String valor){
		if(valor==null || valor.equalsIgnoreCase(""))
			return false;
		return true;
	}
}