package br.com.ichef.arquitetura.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.context.RequestContext;

import br.com.ichef.model.Configuracao;
import br.com.ichef.model.Usuario;
import br.com.ichef.util.JSFUtil;

public class BaseController extends AbstratcBaseController implements Serializable {

	private static final long serialVersionUID = 1L;

	protected static String DIALOG_CADASTRAR = "cadastrarForm";

	protected static String ERRO_GENERICO = "Erro ao gerar relatï¿½rio, por favor entre em contato com o Administrator do Sistema.";

	protected Usuario userLogado = (Usuario) JSFUtil.getSessionMapValue("usuario");

	protected Configuracao configuracao = (Configuracao) JSFUtil.getSessionMapValue("configuracao");

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
		return "O campo " + label + " é de preenchimento obrigatório.";
	}

	public String getImagem(String imagem) {
		return getServlet().getRealPath("/img/" + imagem);
	}

	public String getSubRelatorio(String imagem) {
		return getCaminhoSubRelatorio() + imagem;
	}

	public String getCaminhoSubRelatorio() {
		return getServlet().getRealPath("/WEB-INF/report/") + "/";
	}

	public Object formataValor(Object valor) {
		try {

			if (valor != null) {
				return "R$ " + valor.toString().replaceAll(",", ".").replace(".", ",");
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

	public Configuracao getConfiguracao() {
		return configuracao;
	}

}
