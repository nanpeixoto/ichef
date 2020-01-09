package br.com.ichef.controler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.arquitetura.controller.BaseConsultaCRUD;
import br.com.ichef.dao.AbstractService;
import br.com.ichef.model.VwClienteSaldo;
import br.com.ichef.service.ClienteSaldoService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class ClienteSaldoController extends BaseConsultaCRUD<VwClienteSaldo> {

	private static final long serialVersionUID = 1L;

	@Inject
	private ClienteSaldoService service;

	private VwClienteSaldo entity;

	private Long id;

	private List<VwClienteSaldo> lista = new ArrayList<VwClienteSaldo>();
	private List<VwClienteSaldo> listaFiltro = new ArrayList<VwClienteSaldo>();

	private List<VwClienteSaldo> listaSelecionadas = new ArrayList<VwClienteSaldo>();

	@Override
	protected VwClienteSaldo newInstance() {
		// TODO Auto-generated method stub
		return new VwClienteSaldo();
	}

	@Override
	protected AbstractService<VwClienteSaldo> getService() {
		// TODO Auto-generated method stub
		return service;
	}

	@PostConstruct
	public void init() {
		lista = service.listAll();
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
