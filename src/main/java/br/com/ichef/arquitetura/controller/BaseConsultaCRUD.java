package br.com.ichef.arquitetura.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.primefaces.context.RequestContext;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.controler.ContextoController;
import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.exception.BusinessException;
import br.com.ichef.exception.RequiredException;
import br.com.ichef.model.Configuracao;
import br.com.ichef.model.Usuario;
import br.com.ichef.util.JSFUtil;
import br.com.ichef.util.MensagemUtil;
import br.com.ichef.util.Util;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public abstract class BaseConsultaCRUD<T extends BaseEntity> extends AppBaseController {

	private static final long serialVersionUID = 1L;

	protected static String DIALOG_CADASTRAR = "cadastrarForm";

	public static String LOGO = "logo.jpg";

	protected static String ERRO_GENERICO = "Erro ao gerar relatório, por favor entre em contato com o Administrator do Sistema.";

	protected Usuario userLogado = (Usuario) JSFUtil.getSessionMapValue("usuario");

	protected Configuracao configuracao = (Configuracao) JSFUtil.getSessionMapValue("configuracao");

	SimpleDateFormat formatarDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy");

	public static final String REPORT_PARAM_SUBREPORT_DIR = "SUBREPORT_DIR";

	public static final String RELATORIO = "resources.properties";

	public static final String REPORT_PARAM_LOGO = "pLogo";

	public static final String DIR_DEFAULT = "resources/";

	private String nomeRotina;

	protected String mensagemPersonalizada;

	private T instanceFilter;

	private T instanceExcluir;
	private T instanceUpload;

	private T instanceFinalizar;

	private T instance;

	private List<T> lista;

	protected boolean disableAssociacao;
	protected boolean desabilitarPesquisarAssociacao;

	@Inject
	protected ContextoController contextoController;

	private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

	/**
	 * 
	 * @return Retorna a instancia utilizada para filtrar a consulta
	 */
	public T getInstanceFilter() {
		if (instanceFilter == null) {
			instanceFilter = newInstance();
		}
		return instanceFilter;
	}

	public void setInstanceFilter(T instanceFilter) {
		this.instanceFilter = instanceFilter;
	}

	public Boolean isNotEmptyOrNull(String valor) {
		if (valor == null || valor.equalsIgnoreCase(""))
			return false;
		return true;
	}

	/**
	 * Metodo responsovel por limpar os campos de filtro
	 */
	public void limparFiltro() {
		instanceFilter = newInstance();
		lista = new ArrayList<T>();
	}

	public void novo() {
		instance = newInstance();
	}

	/**
	 * Busca o objeto para a edicao
	 * 
	 * @param mensagem
	 * @throws AppException
	 */
	public void edita(T t) throws AppException {
		instance = getService().getById(t.getId());
	}

	/**
	 * Metodo responsavel por consultar os registros de acordo aos filtros
	 * informados
	 * 
	 * @throws Exception
	 */
	public void consultar() {
		try {
			lista = getService().findByParameters(instanceFilter);
			contextoController.setObjectFilter(instanceFilter);
			if (Util.isNullOuVazio(lista)) {
				facesMessager.addMessageError("geral.crud.noItemFound");
			}
		} catch (RequiredException re) {
			for (String message : re.getErroList())
				facesMessager.addMessageError(message);
		} catch (BusinessException be) {
			for (String message : be.getErroList())
				facesMessager.addMessageError(message);
		} catch (AppException ae) {
			facesMessager.addMessageError(ae.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	 
	public void reConsultar() {
		try {
			setLista(getService().findByParameters(getInstanceFilter()));
			contextoController.setObjectFilter(getInstanceFilter());
		} catch (AppException e) {
			// logger.error(e);
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Metodo responsavel por excluir o registro selecionado
	 * 
	 * @param t
	 *            - Objeto que repesenta a linha selecionada
	 * @throws BusinessException
	 * @throws Exception
	 */
	public Boolean delete() {
		try {
			getService().excluir(instanceExcluir);
			lista.remove(instanceExcluir);

			if (nomeRotina != null)
				facesMessager.info(MensagemUtil.obterMensagem("geral.crud.excluido", nomeRotina));
			else
				facesMessager.info(MensagemUtil.obterMensagem("geral.crud.excluido", "Registro"));

			return true;

		} catch (BusinessException be) {
			for (String message : be.getErroList())
				facesMessager.addMessageError(message);
			return false;
		} catch (AppException ae) {
			ae.printStackTrace();
			facesMessager.addMessageError(ae.getMessage());
			return false;
		} catch (ConstraintViolationException ae) {
			ae.printStackTrace();
			facesMessager.addMessageError(ae.getMessage());
			return false;

		} catch (PersistenceException ae) {
			ae.printStackTrace();
			facesMessager.addMessageError(ae.getMessage());
			return false;

		} catch (Exception ae) {
			ae.printStackTrace();
			facesMessager.addMessageError(ae.getMessage());
			return false;

		}

	}

	/**
	 * Persiste ou atualiza uma instancia na base de dados.
	 * 
	 * @throws AppException
	 */
	public void save() throws AppException {
		try {
			if (Util.isNullOuVazio(instance.getId())) {
				saveImpl(instance);
			} else {
				updateImpl(instance);
			}

		} catch (RequiredException re) {
			for (String message : re.getErroList())
				facesMessager.addMessageError(message);
		} catch (BusinessException be) {
			for (String message : be.getErroList())
				facesMessager.addMessageError(message);
		} finally {
			System.out.println("FINALLY");
		}
	}

	/**
	 * Finaliza a edicao de um registro Normalmente esse metodo deve ser invocado
	 * chamando um metodo de atualizacao na entidade(update)
	 * 
	 * @return
	 * @throws AppException
	 */
	protected void updateImpl(T referenceValue) throws AppException {
		getService().update(referenceValue);
		if (mensagemPersonalizada != null)
			facesMessager.info(MensagemUtil.obterMensagem(mensagemPersonalizada));
		else if (nomeRotina != null)
			facesMessager.info(MensagemUtil.obterMensagem("geral.crud.atualizado", nomeRotina));
		else
			facesMessager.info(MensagemUtil.obterMensagem("geral.crud.atualizado", "Registro"));
	}

	/**
	 * Finaliza a criacao de um registro Normalmente esse metodo deve ser invocado
	 * chamando um metodo de criacao na entidade(insert)
	 * 
	 * @return
	 * @throws BusinessException
	 * @throws AppException
	 */
	protected void saveImpl(T referenceValue) throws AppException {

		getService().save(referenceValue);

		if (mensagemPersonalizada != null)
			facesMessager.info(MensagemUtil.obterMensagem(mensagemPersonalizada));
		else if (nomeRotina != null)
			facesMessager.info(MensagemUtil.obterMensagem("geral.crud.salvo", nomeRotina));
		else
			facesMessager.info(MensagemUtil.obterMensagem("geral.crud.salvo", "Registro"));

	}

	/**
	 * Lista exibida na grid de resultados da consulta
	 * 
	 * @return List<T>
	 */
	public List<T> getLista() {
		return lista;
	}

	/**
	 * Guarda a lista que sera  exibida na grid de resultados da consulta
	 */
	public void setLista(List<T> lista) {
		this.lista = lista;
	}

	/**
	 * Retorna a instancia do objeto que sera excluir
	 * 
	 * @return T
	 */
	public T getInstanceExcluir() {
		return instanceExcluir;
	}

	public T getInstanceFinalizar() {
		return instanceFinalizar;
	}

	public T getInstanceUpload() {
		return instanceUpload;
	}

	public void setInstanceUpload(T instanceUpload) {
		this.instanceUpload = instanceUpload;
	}

	/**
	 * Guarda uma instncia do objeto que ser excludo
	 */
	public void setInstanceExcluir(T instanceExcluir) {
		this.instanceExcluir = instanceExcluir;
	}

	/**
	 * Retorna a instncia do objeto que ser incluido/alterado
	 * 
	 * @return T
	 */
	public T getInstance() {
		if (instance == null) {
			instance = newInstance();
		}
		return instance;
	}

	/**
	 * Guarda uma instncia do objeto que ser incluido/alterado
	 */
	public void setInstance(T instance) {
		this.instance = instance;
	}

	/**
	 * Guarda uma instncia do objeto que ser incluido/alterado
	 */
	public String getConverterMessage(String label) {
		return MensagemUtil.obterMensagem("geral.number.field", MensagemUtil.obterMensagem(label));
	}

	public void giveFocus(String pIdComponente) {
		RequestContext.getCurrentInstance().execute("giveFocus('" + pIdComponente + "')");
	}

	public String getUsuarioAlteracao() {
		String textoUsuario = "";
		if (getInstance().getUsuarioAlteracao() != null) {
			textoUsuario = getInstance().getUsuarioAlteracao().getNomeAbreviado() + " - "
					+ (getInstance().getDataAlteracao() == null ? "" : format.format(getInstance().getDataAlteracao()));
		} else if (getInstance().getDataAlteracao() != null) {
			textoUsuario = format.format(getInstance().getDataAlteracao());
		}

		return textoUsuario;
	}

	public String getUsuarioCadastro() {
		String textoUsuario = "";
		if (getInstance().getUsuarioCadastro() != null) {
			textoUsuario = getInstance().getUsuarioCadastro().getNomeAbreviado() + " - "
					+ (getInstance().getDataCadastro() == null ? "" : format.format(getInstance().getDataCadastro()));
		} else if (getInstance().getDataCadastro() != null) {
			textoUsuario = format.format(getInstance().getDataCadastro());
		}

		return textoUsuario;
	}

	public Long getNumeroAreatorio() {

		return (long) (1000 + Math.random() * 9999);

	}

	public String getNomeRotina() {
		return nomeRotina;
	}

	public void setNomeRotina(String nomeRotina) {
		this.nomeRotina = nomeRotina;
	}

	public String getMensagemPersonalizada() {
		return mensagemPersonalizada;
	}

	public void setMensagemPersonalizada(String mensagemPersonalizada) {
		this.mensagemPersonalizada = mensagemPersonalizada;
	}

	public boolean isDisableAssociacao() {
		return disableAssociacao;
	}

	public void setDisableAssociacao(boolean disableAssociacao) {
		this.disableAssociacao = disableAssociacao;
	}

	public boolean isDesabilitarPesquisarAssociacao() {
		return desabilitarPesquisarAssociacao;
	}

	public void setDesabilitarPesquisarAssociacao(boolean desabilitarPesquisarAssociacao) {
		this.desabilitarPesquisarAssociacao = desabilitarPesquisarAssociacao;
	}

	public Boolean getPerfilConsulta() {
		return contextoController.getPerfilConsulta();
	}

	public void reset() {
		RequestContext.getCurrentInstance().reset(DIALOG_CADASTRAR);
	}

	public void reset(String id) {
		RequestContext.getCurrentInstance().reset(id);
	}

	public SimpleDateFormat getFormatarData() {
		return formatarData;
	}

	public void setFormatarData(SimpleDateFormat formatarData) {
		this.formatarData = formatarData;
	}

	public Usuario obterUsuarioLogado() {
		return (Usuario) JSFUtil.getSessionMapValue("usuario");
	}

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

	/**
	 * Mtodo responsvel por criar a instancia que ser utilzado pelo filtro da
	 * consulta
	 * 
	 * @return
	 */
	protected abstract T newInstance();

	/**
	 * Mtodo responsvel por vincular o service da Entidade parametrizada
	 * 
	 * @return
	 */
	protected abstract AbstractService<T> getService();

	@PostConstruct
	public void inicializar() {
		lista = new ArrayList<T>();
		instanceFilter = newInstance();
	}

	public Properties getProperties() throws Exception {

		File file = new File(getRealPath(DIR_DEFAULT + RELATORIO));
		Properties props = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			// l os dados que esto no arquivo
			props.load(fis);
			fis.close();
		} catch (IOException ex) {
			throw new Exception("No foi possivel ler o Properties");
		}
		return props;
	}

	protected static String getRealPath(String pArquivo) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.responseComplete();
		ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();

		return scontext.getRealPath(pArquivo);
	}

	@SuppressWarnings("rawtypes")
	protected void escreveRelatorioPDF(String nome, boolean download, Collection colecao) throws Exception {

		byte[] lReportData = null;
		setParametroReport(REPORT_PARAM_SUBREPORT_DIR, getRealPath(getProperties().getProperty("dir.relatorio")));
		setParametroReport(REPORT_PARAM_LOGO, getImagem(LOGO));

		lReportData = JasperRunManager.runReportToPdf(
				getInputStream(getProperties().getProperty("dir.relatorio") + nome + ".jasper"), parametros,
				new JRBeanCollectionDataSource(colecao));

		escreveRelatorio(lReportData, nome, download);
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
		return "O campo " + label + "  obrigatrio.";
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
