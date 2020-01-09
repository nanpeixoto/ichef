package br.com.ichef.arquitetura.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.validation.ConstraintViolation;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.ichef.util.JSFUtil;
import br.com.ichef.util.MensagemUtil;
import br.com.ichef.util.ReportUtils;
import br.com.ichef.util.Util;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Classe base para qualquer outro controller
 */
public abstract class AppBaseController extends Util implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	protected static final String APPLICATION_PDF = "application/pdf";

	protected static final String REPORT_LOCALE = "REPORT_LOCALE";

	protected static final String PAIS_BR = "BR";

	protected static final String LANG_PT = "PT";

	public static final String REPORT_PARAM_SUBREPORT_DIR = "SUBREPORT_DIR";

	protected Map<String, Object> parametros = new HashMap<String, Object>();

	@Inject
	protected FacesMensager facesMessager;

	private StreamedContent file;

	// @Inject
	// protected transient Logger logger;

	protected String facesRedirect = "?faces-redirect=true";

	/**
	 * Construtor default
	 */
	public AppBaseController() {
		super();
	}

	@SuppressWarnings("rawtypes")
	protected void escreveRelatorioPDF(String nome, boolean download, Collection colecao) throws Exception {

		byte[] lReportData = null;
		setParametroReport(REPORT_PARAM_SUBREPORT_DIR, getRealPath(getProperties().getProperty("dir.relatorio")));

		lReportData = JasperRunManager.runReportToPdf(getInputStream(getProperties().getProperty("dir.relatorio") + nome + ".jasper"), parametros, new JRBeanCollectionDataSource(colecao));

		escreveRelatorio(lReportData, nome, download);
	}

	protected void escreveRelatorioPDF(String nome, boolean download) throws Exception {

		byte[] lReportData = null;

		setParametroReport(REPORT_PARAM_SUBREPORT_DIR, getRealPath(getProperties().getProperty("dir.relatorio")));

		lReportData = JasperRunManager.runReportToPdf(getInputStream(getProperties().getProperty("dir.relatorio") + nome + ".jasper"), parametros);

		escreveRelatorio(lReportData, nome, download);
	}

	protected void escreveRelatorioPDF(String nome, boolean download, Connection connection) throws Exception {

		byte[] lReportData = null;

		// obterRelatorioPDF(getProperties().getProperty("dir.relatorio"), nome,
		// null)

		setParametroReport(REPORT_PARAM_SUBREPORT_DIR, getRealPath(getProperties().getProperty("dir.relatorio")));

		lReportData = JasperRunManager.runReportToPdf(getInputStream(getProperties().getProperty("dir.relatorio") + nome + ".jasper"), parametros, connection);
		escreveRelatorio(lReportData, nome, download);
	}

	protected void escreveRelatorioPDF(String nome, boolean download, Connection connection, boolean temSubreport) throws Exception {

		setParametroReport(REPORT_PARAM_SUBREPORT_DIR, getRealPath(getProperties().getProperty("dir.relatorio")));
		escreveRelatorioPDF(nome, download, connection);

	}

	public void setParametroReport(String nome, Object valor) {
		if (parametros == null) {
			parametros = new HashMap<String, Object>();
		}
		parametros.put(nome, valor);

	}

	/**
	 * Esconde o componente do widgetvar especificado
	 * 
	 * @param widgetvar
	 */
	protected void hideDialog(String widgetvar) {
		RequestContext.getCurrentInstance().execute("PF('" + widgetvar + "').hide()");
	}
	
	/**
	 * Esconde o componente do widgetvar especificado
	 * 
	 * @param widgetvar
	 */
	protected void giveFocus(String form, String componente) {
		RequestContext.getCurrentInstance().execute("$(function(){PrimeFaces.focus('"+form+":"+componente+"');});");
	}

	/**
	 * Método para ordenar string ignorando utilizado no sortFuction
	 * 
	 * @param msg1
	 * @param msg2
	 * @return
	 */
	public int sortByString(Object msg1, Object msg2) {

		if (msg1.getClass() == Integer.class) {
			return ((Integer) msg1).compareTo(((Integer) msg2));
		} else if (msg1.getClass() == Float.class) {
			return ((Float) msg1).compareTo(((Float) msg2));
		} else if (msg1.getClass() == Long.class) {
			return ((Long) msg1).compareTo(((Long) msg2));
		} else if (msg1.getClass() == Double.class) {
			return ((Double) msg1).compareTo(((Double) msg2));
		} else if (msg1.getClass() == java.sql.Timestamp.class) {
			return ((java.sql.Timestamp) msg1).compareTo(((java.sql.Timestamp) msg2));
		}

		return ((String) msg1).compareToIgnoreCase(((String) msg2));

	}

	/**
	 * Mostra o componente do widgetvar especificado
	 * 
	 * @param widgetvar
	 */
	protected void showDialog(String widgetvar) {
		RequestContext.getCurrentInstance().execute(widgetvar + ".show()");
	}

	protected void showStatus() {
		RequestContext.getCurrentInstance().execute("showStatus();");
	}

	protected void hideStatus() {
		RequestContext.getCurrentInstance().execute("hideStatus();");
	}

	/**
	 * Faz update nos componentes dos respectivos ids passados como parâmetro
	 * 
	 * @param idComponente
	 */
	protected void updateComponentes(String... idComponente) {
		for (String id : idComponente) {
			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add(id);
		}
	}

	protected String includeRedirect(String url) {
		String formatedUrl = url.concat(facesRedirect);
		return formatedUrl;
	}

	protected String getRootErrorMessage(Exception e) {
		String errorMessage = MensagemUtil.obterMensagem("geral.crud.rootErrorMessage");
		if (e == null) {
			// This shouldn't happen, but return the default messages
			return errorMessage;
		}

		// JsfUtil.addErrorMessage(errorMessage);

		// Start with the exception and recurse to find the root cause
		Throwable t = e;
		while (t != null) {
			// Get the message from the Throwable class instance
			errorMessage = t.getLocalizedMessage();
			t = t.getCause();
		}
		// This is the root cause message
		return errorMessage;
	}

	protected String createViolationResponse(Set<ConstraintViolation<?>> violations) {
		/*
		 * logger.fine("Validation completed. violations found: " +
		 * violations.size());
		 */

		Map<String, String> responseObj = new HashMap<String, String>();

		for (ConstraintViolation<?> violation : violations) {
			responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
		}

		return responseObj.toString();
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
			getRootErrorMessage(e);
		}
	}

	protected void escreveRelatorio(byte[] relatorio, String nome, String tipo, String contentType, boolean download) {
		try {
			this.escreveRelatorioResponse(relatorio, nome, tipo, contentType, download);
		} catch (Exception e) {
			getRootErrorMessage(e);
		}
	}

	protected void escreveRelatorio(byte[] relatorio, String nome, String contentType, boolean download) throws IOException {
		try {
			this.escreveRelatorioResponse(relatorio, nome, null, contentType, download);
		} catch (Exception e) {
			getRootErrorMessage(e);
		}
	}

	protected void escreveRelatorioResponse(byte[] relatorio, String nome, String tipo, String contentType, boolean download) throws IOException {

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

	protected ServletContext getServlet() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.responseComplete();
		ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
		return scontext;
	}

	protected void criarArquivoPDF(byte[] relatorio, String nome, String tipo, String contentType) // throws
	// IOException
	{

		ByteArrayInputStream stream = new ByteArrayInputStream(relatorio);
		file = new DefaultStreamedContent(stream, contentType, nome + "." + tipo.toLowerCase());

	}

	protected void criarArquivoPDF(String nome, Connection connection) throws Exception {

		setParametroReport(REPORT_PARAM_SUBREPORT_DIR, getRealPath(getProperties().getProperty("dir.relatorio")));
		byte[] lReportData = null;

		lReportData = JasperRunManager.runReportToPdf(getInputStream(getProperties().getProperty("dir.relatorio") + nome + ".jasper"), parametros, connection);

		this.criarArquivoPDF(lReportData, nome, ReportUtils.REL_TIPO_PDF, "application/pdf");
	}
	
	
	protected byte[] criarArquivoPDFRetornaBytes(String nome, Connection connection) throws Exception {

		
		byte[] lReportData = null;

		lReportData = JasperRunManager.runReportToPdf(getInputStream(getProperties().getProperty("dir.relatorio") + nome + ".jasper"), parametros, connection);

		return lReportData;
	}
	
	
	
	protected void criarArquivoPDF(String nome, @SuppressWarnings("rawtypes") Collection colecao) throws Exception {

		setParametroReport(REPORT_PARAM_SUBREPORT_DIR, getRealPath(getProperties().getProperty("dir.relatorio")));
		byte[] lReportData = null;

		lReportData = JasperRunManager.runReportToPdf(getInputStream(getProperties().getProperty("dir.relatorio") + nome + ".jasper"), parametros,  new JRBeanCollectionDataSource(colecao));

		this.criarArquivoPDF(lReportData, nome, ReportUtils.REL_TIPO_PDF, "application/pdf");
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

}