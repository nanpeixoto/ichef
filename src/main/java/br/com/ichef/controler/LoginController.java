package br.com.ichef.controler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import com.google.gson.Gson;

import br.com.iche.dto.Entregador;
import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.util.JSFUtil;
import br.com.ichef.util.Util;

@Named
@ViewScoped
public class LoginController extends BaseController {

	private static final long serialVersionUID = 1L;

	// Entregador
	private String senhaEntregador;
	private String LoginEntregador;
	private String senhaNova;
	private String senhaNovaConfirmacao;

	private Entregador Entregador;

	public String autenticarEntregador() {

		try {

			if (LoginEntregador == null || LoginEntregador.equalsIgnoreCase("")) {
				Messages.addGlobalError("Login é obrigatório");
				return null;
			}

			if (senhaEntregador == null || senhaEntregador.equalsIgnoreCase("")) {
				Messages.addGlobalError("Login é obrigatório");
				return null;
			}

			String urlLogin = Util.API_ICHEF + Util.API_ICHEF_LOGIN_ENTREGADOR + "/" + LoginEntregador + "/"
					+ senhaEntregador;

			URL url = new URL(urlLogin);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				String erro = "Não foi possíve se conectar ao serviço  ERRO: " + conn.getResponseCode();
				Messages.addGlobalError(erro);
				return null;

			}
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);

			Gson gson = new Gson();

			Entregador entregador = gson.fromJson(br.readLine(), Entregador.class);
			if (entregador == null) {
				facesMessager.error("Login/Senha inválidos");
				return null;
			}
			if (entregador != null && !entregador.getAtivo().equalsIgnoreCase("S")) {
				facesMessager.error("Usuário não está ativo");
				return null;
			}

			JSFUtil.setSessionMapValue("loggedUser", entregador.getId());
			JSFUtil.setSessionMapValue("userLogado", entregador);

			return "/index.xhtml?faces-redirect=true";

		} catch (Exception e) {
			facesMessager.error("Verifique a API nao foi possível conectar");
			Messages.addGlobalError("Verifique a API nao foi possível conectar");
			return null;
		}

	}

	public void logout() {

	}

	public void alterarSenha() {

	}

	public String getSenhaEntregador() {
		return senhaEntregador;
	}

	public void setSenhaEntregador(String senhaEntregador) {
		this.senhaEntregador = senhaEntregador;
	}

	public String getLoginEntregador() {
		return LoginEntregador;
	}

	public void setLoginEntregador(String loginEntregador) {
		LoginEntregador = loginEntregador;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Entregador getEntregador() {
		return Entregador;
	}

	public void setEntregador(Entregador entregador) {
		Entregador = entregador;
	}

	public String getSenhaNovaConfirmacao() {
		return senhaNovaConfirmacao;
	}

	public void setSenhaNovaConfirmacao(String senhaNovaConfirmacao) {
		this.senhaNovaConfirmacao = senhaNovaConfirmacao;
	}

	public String getSenhaNova() {
		return senhaNova;
	}

	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}

}
