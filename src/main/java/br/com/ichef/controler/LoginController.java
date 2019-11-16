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
	private Empresa empresa;
	private List<Empresa> empresas;
	private String login;

	@PostConstruct
	public void init() {

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

}
