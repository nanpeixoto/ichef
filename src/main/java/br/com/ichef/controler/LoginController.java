package br.com.ichef.controler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.model.Configuracao;
import br.com.ichef.model.Empresa;
import br.com.ichef.model.LogAcesso;
import br.com.ichef.model.Usuario;
import br.com.ichef.service.ConfiguracaoService;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.LogAcessoService;
import br.com.ichef.service.UsuarioService;
import br.com.ichef.util.JSFUtil;
import br.com.ichef.util.StringUtil;
import br.com.ichef.util.Util;

@Named
@ViewScoped
public class LoginController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioService service;

	@Inject
	private LogAcessoService logAcessoService;

	@Inject
	private EmpresaService empresaService;

	@Inject
	private ConfiguracaoService configuracaoService;

	private Usuario usuario = new Usuario();

	private String senha;
	private String senhaNova;
	private String senhaNovaConfirmacao;
	private Empresa empresa;
	private List<Empresa> empresas;
	private String login;

	// Entregador
	private String senhaEntregador;
	private String LoginEntregador;

	@PostConstruct
	public void init() {

	}

	public void alterarSenha() {

		if (getSenha() != null && getSenhaNova() != null)
			if (!getSenhaNova().equalsIgnoreCase(getSenhaNovaConfirmacao())) {
				facesMessager.error("As senhas digitadas n�o conferem");
				return;

			}
		if (getSenhaNova().equalsIgnoreCase(getSenha())) {
			facesMessager.error("A nova senha corresponde a senha atual");
		} else {
			Usuario usuario = service.getById(getUserLogado().getId());
			if (usuario.getSenha().equalsIgnoreCase(StringUtil.criptografa(getSenha()))) {
				try {
					usuario.setSenha(StringUtil.criptografa(getSenhaNova()));
					service.alterarSenha(usuario);
					facesMessager.info("Senha sucesso");
				} catch (Exception e) {
					e.printStackTrace();
					facesMessager.error("N�o foi poss�vel efetuar a opera��o");
				}

			} else {
				facesMessager.error("Senha Atual n�o confere");
			}

		}

	}

	public void buscarEmpresa() {
		empresas = new ArrayList<Empresa>();
		List<Usuario> usuarios;
		try {
			if (login != null) {
				usuarios = service.findByLogin(login);
				if (usuarios != null && usuarios.size() > 0) {
					usuario = usuarios.get(0);

					empresas = empresaService.empresasUsuario(usuario);

					if (empresas.size() == 1)
						empresa = empresas.get(0);
					else
						empresa = null;

					// empresas = empresaService.findByParameters(filterEmpresa);
				} else {
					empresas = new ArrayList<Empresa>();
				}
			} else {
				empresas = new ArrayList<Empresa>();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			empresas = new ArrayList<Empresa>();
		}

	}

	public Usuario getUsuarioLogado() {
		return getUserLogado();
	}

	public String autenticarEntregador() throws IOException {

		if (LoginEntregador == null || LoginEntregador.equalsIgnoreCase("")) {
			Messages.addGlobalError("Login � obrigat�rio");
			return null;
		}

		if (senhaEntregador == null || senhaEntregador.equalsIgnoreCase("")) {
			Messages.addGlobalError("Login � obrigat�rio");
			return null;
		}

		String urlLogin = Util.API_ICHEF + Util.API_ICHEF_LOGIN_ENTREGADOR + LoginEntregador + "/" + senhaEntregador;

		URL url = new URL(urlLogin);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(in);
        String output;
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }
	

		return "/index.xhtml?faces-redirect=true";

	}

	public String autenticar() throws Exception {

		List<Usuario> usuarios = new ArrayList<>();
		usuarios = (List<Usuario>) service.findByLogin(login, StringUtil.criptografa(senha));
		Faces.getFlash().setKeepMessages(true);

		if (usuarios != null && usuarios.size() > 0) {
			usuario = usuarios.get(0);
			if (usuario.getAtivo().equalsIgnoreCase("N")) {
				Messages.addGlobalError("O usu�rio n�o est� ativo");
				return null;
			}
			if (empresa == null) {
				Messages.addGlobalError("Seleciona a empresa para entrar");
				return null;

			} else {
				Configuracao config = configuracaoService.getById(1);
				usuario.setEmpresaLogada(empresa);
				JSFUtil.setSessionMapValue("loggedUser", usuario.getLogin());
				JSFUtil.setSessionMapValue("usuario", usuario);
				JSFUtil.setSessionMapValue("loggedUserPassword", usuario.getSenha());
				// JsfUtil.setSessionMapValue("perfisUsuario",usuario.getPapel());
				JSFUtil.setSessionMapValue("loggedMatricula", usuario.getLogin());
				JSFUtil.setSessionMapValue("configuracao", config);

				LogAcesso log = new LogAcesso();
				log.setDataLogin(new Date());
				log.setUsuario(usuario);

				logAcessoService.saveOrUpdade(log);

				return "/index.xhtml?faces-redirect=true";

			}
		} else {
			Messages.addGlobalError("Usu�rio ou senha inv�lidos");
			return null;
		}

	}

	public String logout() {
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		response.resetBuffer();

		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		// RequestContext.getCurrentInstance().execute("hideStatus();");
		return "/index.xhtml?faces-redirect=true";
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public UsuarioService getService() {
		return service;
	}

	public void setService(UsuarioService service) {
		this.service = service;
	}

	public LogAcessoService getLogAcessoService() {
		return logAcessoService;
	}

	public void setLogAcessoService(LogAcessoService logAcessoService) {
		this.logAcessoService = logAcessoService;
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public ConfiguracaoService getConfiguracaoService() {
		return configuracaoService;
	}

	public void setConfiguracaoService(ConfiguracaoService configuracaoService) {
		this.configuracaoService = configuracaoService;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getSenhaNova() {
		return senhaNova;
	}

	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}

	public String getSenhaNovaConfirmacao() {
		return senhaNovaConfirmacao;
	}

	public void setSenhaNovaConfirmacao(String senhaNovaConfirmacao) {
		this.senhaNovaConfirmacao = senhaNovaConfirmacao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getLoginEntregador() {
		return LoginEntregador;
	}

	public void setLoginEntregador(String loginEntregador) {
		LoginEntregador = loginEntregador;
	}

	public String getSenhaEntregador() {
		return senhaEntregador;
	}

	public void setSenhaEntregador(String senhaEntregador) {
		this.senhaEntregador = senhaEntregador;
	}

}
