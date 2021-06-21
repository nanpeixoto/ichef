package br.com.ichef.arquitetura.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.context.RequestContext;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.model.Configuracao;
import br.com.ichef.model.Usuario;
import br.com.ichef.util.JSFUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

public class BaseController extends AbstratcBaseController implements Serializable {

	private static final long serialVersionUID = 1L;

	protected static String DIALOG_CADASTRAR = "cadastrarForm";

	public static String LOGO = "logo.jpg";

	public static String LOGO_ETIQUETA = "logo_etiqueta.jpg";

	protected static String ERRO_GENERICO = "Erro ao gerar relatório, por favor entre em contato com o Administrator do Sistema.";

	protected Usuario userLogado = (Usuario) JSFUtil.getSessionMapValue("usuario");

	protected Configuracao configuracao = (Configuracao) JSFUtil.getSessionMapValue("configuracao");

	SimpleDateFormat formatarDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy");

	public static final String REPORT_PARAM_SUBREPORT_DIR = "SUBREPORT_DIR";

	public static final String RELATORIO = "resources.properties";

	public static final String REPORT_PARAM_LOGO = "pLogo";

	public static final String DIR_DEFAULT = "resources/";

	protected Map<String, Object> parametros = new HashMap<String, Object>();

	public String getMaskCpf(String cpf) {
		cpf = cpf.replace(".", "");
		cpf = cpf.replace("-", "");
		try {
			return cpf.substring(0, 2) + "." + cpf.substring(2, 5) + "." + cpf.substring(5, 8) + "/"
					+ cpf.substring(8, 12) + "-" + cpf.substring(12, cpf.length());
		} catch (Exception e) {
			return cpf;
		}

	}

	public Properties getProperties() throws Exception {

		File file = new File(getRealPath(DIR_DEFAULT + RELATORIO));
		Properties props = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			// lê os dados que estão no arquivo
			props.load(fis);
			fis.close();
		} catch (IOException ex) {
			throw new Exception("Não foi possivel ler o Properties");
		}
		return props;
	}

	protected static String getRealPath(String pArquivo) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.responseComplete();
		ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();

		return scontext.getRealPath(pArquivo);
	}

	protected void escreveRelatorioPDF(String nome, boolean download, Connection connection, boolean temSubreport)
			throws Exception {

		setParametroReport(REPORT_PARAM_SUBREPORT_DIR, getRealPath(getProperties().getProperty("dir.relatorio")));
		escreveRelatorioPDF(nome, download, connection);

	}

	protected void escreveRelatorioPDF(String nome, boolean download, Connection connection) throws Exception {

		byte[] lReportData = null;

		setParametroReport(REPORT_PARAM_SUBREPORT_DIR, getRealPath(getProperties().getProperty("dir.relatorio")));

		lReportData = JasperRunManager.runReportToPdf(
				getInputStream(getProperties().getProperty("dir.relatorio") + nome + ".jasper"), parametros,
				connection);
		escreveRelatorioPDF(lReportData, nome, download);
	}

	@SuppressWarnings("deprecation")
	public void geraRelatorioXLS(Connection con, String nome) throws JRException, Exception {

		JasperDesign desenho = JRXmlLoader
				.load(getRealPath(getProperties().getProperty("dir.relatorio") + nome + ".jrxml"));

		// compila o relatório
		JasperReport relatorio = JasperCompileManager.compileReport(desenho);

		JasperPrint impressao = JasperFillManager.fillReport(relatorio, parametros, con);
		
		setParametroReport(REPORT_PARAM_SUBREPORT_DIR, getRealPath(getProperties().getProperty("dir.relatorio")));

		JRXlsExporter exporter = new JRXlsExporter();
		ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
		exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, impressao);
		exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, xlsReport);
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, "c:/relatorio.xls");

		exporter.exportReport();
		byte[] bytes = xlsReport.toByteArray();

		escreveRelatorioEXCEL(bytes, nome, true);

	}

	@SuppressWarnings({  "deprecation" })
	public void geraRelatorioXLS(Collection collecao, String nome) throws JRException, Exception {

		JasperDesign desenho = JRXmlLoader
				.load(getRealPath(getProperties().getProperty("dir.relatorio") + nome + ".jrxml"));

		// compila o relatório
		JasperReport relatorio = JasperCompileManager.compileReport(desenho);

		JasperPrint impressao = JasperFillManager.fillReport(relatorio, parametros,
				new JRBeanCollectionDataSource(collecao));

		JRXlsExporter exporter = new JRXlsExporter();
		ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
		exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, impressao);
		exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, xlsReport);
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, "c:/relatorio.xls");

		exporter.exportReport();
		byte[] bytes = xlsReport.toByteArray();

		escreveRelatorioEXCEL(bytes, nome, true);

	}

	@SuppressWarnings("rawtypes")
	protected void escreveRelatorioPDF(String nome, boolean download, Collection colecao) throws Exception {

		byte[] lReportData = null;
		setParametroReport(REPORT_PARAM_SUBREPORT_DIR, getRealPath(getProperties().getProperty("dir.relatorio")));
		setParametroReport(REPORT_PARAM_LOGO, getImagem(LOGO));

		lReportData = JasperRunManager.runReportToPdf(
				getInputStream(getProperties().getProperty("dir.relatorio") + nome + ".jasper"), parametros,
				new JRBeanCollectionDataSource(colecao));

		escreveRelatorioPDF(lReportData, nome, download);
	}

	public InputStream getInputStream(String arquivo) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.responseComplete();
		ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
		return scontext.getResourceAsStream(arquivo);
	}

	public void setParametroReport(String nome, Object valor) {
		if (parametros == null) {
			parametros = new HashMap<String, Object>();
		}
		parametros.put(nome, valor);

	}

	public String criptografa(String senha) {

		/**
		 * Criptografando Senha
		 */
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			md.update(senha.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			senha = hash.toString(16);

			System.out.println(senha);
		}

		catch (NoSuchAlgorithmException ns) {
			ns.printStackTrace();
		}

		return senha;
	}

	@SuppressWarnings("unused")
	private void criaArquivo(byte[] bytes, String arquivo) {
		FileOutputStream fos;

		try {
			if (bytes != null) {
				fos = new FileOutputStream(arquivo);
				fos.write(bytes);

				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	protected ServletContext getServlet() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.responseComplete();
		ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
		return scontext;
	}

	protected ServletContext getContext() {
		return getServlet();
	}

	public String getRequiredMessage(String label) {
		return "O campo " + label + " é obrigatório.";
	}

	public String getImagem(String imagem) throws Exception {
		return getServlet().getRealPath(getProperties().getProperty("dir.imagem") + imagem);
	}

	public String getSubRelatorio(String imagem) {
		return getCaminhoSubRelatorio() + imagem;
	}

	public String getCaminhoSubRelatorio() {
		return getServlet().getRealPath("/WEB-INF/report/") + "/";
	}

	public Object formataValor(Object valor) {
		try {
			Locale meuLocal = new Locale("pt", "BR");
			NumberFormat real = NumberFormat.getCurrencyInstance(meuLocal);

			if (valor != null) {
				return real.format(valor);
				// return "R$ " + valor.toString().replaceAll(",", ".").replace(".", ",");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			return valor.toString();
		} catch (Exception e) {
			return valor;
		}

	}

	public Object formataData(Object valor) {
		try {
			if (valor != null)
				return formatarData.format(valor);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	public Object formataDataHora(Object valor) {
		try {
			if (valor != null)
				return formatarDataHora.format(valor);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	protected HttpServletResponse getResponse() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.responseComplete();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		return response;
	}

	protected HttpServletRequest getRequest() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.responseComplete();
		HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		return request;
	}

	public Usuario getUserLogado() {
		return userLogado;
	}

	public void reset() {
		RequestContext.getCurrentInstance().reset(DIALOG_CADASTRAR);
	}

	public String getUsuarioCadastro(BaseEntity entity) {

		if (entity != null && entity.getUsuarioCadastro() != null && entity.getDataCadastro() != null) {
			return "por " + entity.getUsuarioCadastro().getNomeAbreviado() + " em: "
					+ formatarDataHora.format(entity.getDataCadastro());
		}

		return "";
	}

	public String getUsuarioAlteracao(BaseEntity entity) {

		if (entity != null && entity.getUsuarioAlteracao() != null && entity.getDataAlteracao() != null) {
			return "por " + entity.getUsuarioAlteracao().getNomeAbreviado() + " em: "
					+ formatarDataHora.format(entity.getDataAlteracao());
		}

		return "";
	}

	public Configuracao getConfiguracao() {
		return configuracao;
	}

}
