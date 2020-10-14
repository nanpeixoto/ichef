package br.com.ichef.controler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import br.com.iche.dto.Entregador;
import br.com.iche.dto.RotaDetalhe;
import br.com.iche.dto.response.RotaDetalheResponse;
import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.util.JSFUtil;
import br.com.ichef.util.Util;

@Named
@ViewScoped
public class RotaDetalheController extends BaseController {

	private static final long serialVersionUID = 1L;

	private Entregador usuarioLogado = (Entregador) JSFUtil.getSessionMapValue("userLogado");

	private List<RotaDetalhe> lista = new ArrayList<RotaDetalhe>();

	private Long id;
	private String descricaoRota;

	public void inicializar() {
		if (id != null) {
			obterLista(id);
		} else {
			FacesUtil.addErroMessage("Não foi possível obter a localidade");
		}
	}

	private void obterLista(Long idLocalidade) {
		try {

			String URI = Util.API_ICHEF + Util.API_ICHEF_ROTA_DETALHE + "/" + usuarioLogado.getId() + "/"
					+ idLocalidade;

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

			RotaDetalheResponse response = gson.fromJson(br.readLine(), RotaDetalheResponse.class);
			setLista(response.getRotaDetalhes());

		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErroMessage("Erro ao obter os dados: DashboardController.java:: obterLista");
		}

	}
	
	public void confirmarEntrega(RotaDetalhe rotaDetalhe) {
		try {

			String URI = Util.API_ICHEF + Util.API_ICHEF_ROTA_CONFIRMAR_ENTREGA + "/" + rotaDetalhe.getCodigoCliente() + "/" + rotaDetalhe.getCodigoLocalidade()
			+ "/" + rotaDetalhe.getCodigoEntregador();

			URL url = new URL(URI);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				String erro = "Não foi possíve se conectar ao serviço  ERRO: " + conn.getResponseCode();
				FacesUtil.addErroMessage(erro);
			}

			 

			obterLista(Long.parseLong(rotaDetalhe.getCodigoLocalidade().toString()));
			
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErroMessage("Erro ao obter os dados: DashboardController.java:: obterLista");
		}

	}

	public void abrirMaps(RotaDetalhe rota) throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.redirect("https://waze.com/ul?q="+rota.getEndereco());
	}
	
	public void abrirDiscador(RotaDetalhe rota) throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.redirect("tel:"+rota.getTelefone());
	}
	
	public void abrirWhatsApp(RotaDetalhe rota) throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		//https://api.whatsapp.com/send?phone=5571991819993&text=J%C3%A1%20cheguei%20
		//"http://wa.me/"+rota.getTelefone()
		externalContext.redirect("https://api.whatsapp.com/send?phone="+rota.getTelefone()+"&text=Olá é do *Cozinha de Chef*, estou aqui para entregar seu pedido.");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDescricaoRota() {
		return descricaoRota;
	}

	public void setDescricaoRota(String descricaoRota) {
		this.descricaoRota = descricaoRota;
	}

	public Entregador getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Entregador usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public List<RotaDetalhe> getLista() {
		return lista;
	}

	public void setLista(List<RotaDetalhe> lista) {
		this.lista = lista;
	}

}
