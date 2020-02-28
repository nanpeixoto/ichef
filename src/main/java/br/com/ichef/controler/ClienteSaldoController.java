package br.com.ichef.controler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.arquitetura.util.RelatorioUtil;
import br.com.ichef.model.VwClienteSaldo;
import br.com.ichef.service.ClienteSaldoService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class ClienteSaldoController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private ClienteSaldoService service;

	private VwClienteSaldo entity;

	private Long id;

	private List<VwClienteSaldo> lista = new ArrayList<VwClienteSaldo>();
	private List<VwClienteSaldo> listaFiltro = new ArrayList<VwClienteSaldo>();

	private List<VwClienteSaldo> listaSelecionadas = new ArrayList<VwClienteSaldo>();
	
	

	@PostConstruct
	public void init() {
		VwClienteSaldo saldo = new VwClienteSaldo();
		saldo.setCodigoEmpresa(userLogado.getEmpresaLogada().getId());
		saldo.setId(id);
		try {
			lista = service.findByParameters(saldo);
		} catch (Exception e) {
			e.printStackTrace();
			facesMessager.error("Não foi possível carregar os dados, entre em contato com o administrador do sistema");
		}

	}

	public void excluirSelecionados() {
		for (BaseEntity entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("VwClienteSaldos excluídas com sucesso");
	}
	
	public void preExportar(Object document) {
		RelatorioUtil relatorio = new RelatorioUtil("Carteira de Clientes", document);
		relatorio.preExportar();
	}
	
	public void preExportarAnalitico(Object document) {
		RelatorioUtil relatorio = new RelatorioUtil("Carteira de Clientes", document);
		relatorio.preExportar();
	}


	public ClienteSaldoService getService() {
		return service;
	}

	public void setService(ClienteSaldoService service) {
		this.service = service;
	}

	public VwClienteSaldo getEntity() {
		return entity;
	}

	public void setEntity(VwClienteSaldo entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<VwClienteSaldo> getLista() {
		return lista;
	}

	public void setLista(List<VwClienteSaldo> lista) {
		this.lista = lista;
	}

	public List<VwClienteSaldo> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<VwClienteSaldo> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public List<VwClienteSaldo> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<VwClienteSaldo> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}
	

}
