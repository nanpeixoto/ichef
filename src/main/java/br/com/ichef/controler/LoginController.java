package br.com.ichef.controler;

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

	@PostConstruct
	public void init() {

	}

	public void alterarSenha() {

		if (getSenha() != null && getSenhaNova() != null)
			if (!getSenhaNova().equalsIgnoreCase(getSenhaNovaConfirmacao())) {
				facesMessager.error("As senhas digitadas não conferem");
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
						facesMessager.error("Não foi possível efetuar a operação");
					}

				} else {
					facesMessager.error("Senha Atual não confere");
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

	public String autenticar() throws Exception {

		List<Usuario> usuarios = new ArrayList<>();
		usuarios = (List<Usuario>) service.findByLogin(login, StringUtil.criptografa(senha));
		Faces.getFlash().setKeepMessages(true);

		if (usuarios != null && usuarios.size() > 0) {
			usuario = usuarios.get(0);
			if (usuario.getAtivo().equalsIgnoreCase("N")) {
				Messages.addGlobalError("O usuário não está ativo");
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
			Messages.addGlobalError("Usuário ou senha inválidos");
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

}
