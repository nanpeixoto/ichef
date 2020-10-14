package br.com.ichef.controler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import com.google.gson.Gson;

import br.com.iche.dto.Entregador;
import br.com.iche.dto.RotaEntregador;
import br.com.iche.dto.response.RotaEntregadorResponse;
import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.util.JSFUtil;
import br.com.ichef.util.Util;

@Named
@ViewScoped
public class DashboardController extends BaseController {

	private static final long serialVersionUID = 1L;

	private Entregador usuarioLogado = (Entregador) JSFUtil.getSessionMapValue("userLogado");
	
	private List<RotaEntregador> lista = new ArrayList<RotaEntregador>();

	public Entregador getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Entregador usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@PostConstruct
	public void init() {
		obterLista();
	}

	private void obterLista() {
		try {

			String URI = Util.API_ICHEF + Util.API_ICHEF_ROTA_ENTREGADOR + "/" + usuarioLogado.getId();

			URL url = new URL(URI);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				String erro = "Não foi possíve se conectar ao serviço  ERRO: " + conn.getResponseCode();
				FacesUtil.addErroMessage(erro);
			}

			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);

			Gson gson = new Gson();

			RotaEntregadorResponse rotaEntregadorResponse = gson.fromJson(br.readLine(), RotaEntregadorResponse.class);
			setLista(rotaEntregadorResponse.getRotas());
			

		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErroMessage("Erro ao obter os dados: DashboardController.java:: obterLista");
		}
	}

	public List<RotaEntregador> getLista() {
		return lista;
	}

	public void setLista(List<RotaEntregador> lista) {
		this.lista = lista;
	}

}
