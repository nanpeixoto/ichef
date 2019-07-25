package br.com.ichef.controler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.model.TipoPrato;
import br.com.ichef.model.TipoPratoPreco;
import br.com.ichef.service.TipoPratoService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class TipoPratoController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private TipoPratoService service;

	private TipoPrato entity;

	private Long id;

	private List<TipoPrato> lista = new ArrayList<TipoPrato>();
	private List<TipoPrato> listaFiltro = new ArrayList<TipoPrato>();

	private List<TipoPrato> listaSelecionadas = new ArrayList<TipoPrato>();

	private Double preco;
	private Date dataVigencia;

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
		}
	}
	
	@PostConstruct
	public void init() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new TipoPrato());
			getEntity().setAtivo("S");
		}
		dataVigencia  = new Date();
		lista = service.listAll();
		obterListas();
	}

	private void obterListas() {
	}

	

	public void adicionarPreco() {
		TipoPratoPreco tipoPratoPrecoObj = new TipoPratoPreco();
		tipoPratoPrecoObj.setDataCadastro(new Date());
		tipoPratoPrecoObj.setDataVigencia(getDataVigencia());
		tipoPratoPrecoObj.setPreco( getPreco() );
		tipoPratoPrecoObj.setTipoPrato(getEntity());
		tipoPratoPrecoObj.setUsuarioCadastro(getUserLogado());
		if (getEntity().getPrecos() == null)
			getEntity().setPrecos(new ArrayList<TipoPratoPreco>());
		getEntity().getPrecos().add(tipoPratoPrecoObj);
		
		updateComponentes("dttel");

		setPreco(null);
		setDataVigencia(null);

	}

	public void excluirSelecionados() {
		for (BaseEntity entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}

	public String Salvar() throws Exception {

		if (entity.isEdicao()) {
			entity.setUsuarioAlteracao(getUserLogado());
			entity.setDataAlteracao(new Date());
		} else {
			entity.setUsuarioCadastro(getUserLogado());
			entity.setDataCadastro(new Date());
		}
		service.saveOrUpdade(entity);
		return "lista-tipo-prato.xhtml?faces-redirect=true";

	}

	public String excluir() {
		service.excluir(entity);
		return "lista-tipo-prato.xhtml?faces-redirect=true";
	}

	public TipoPratoService getService() {
		return service;
	}

	public void setService(TipoPratoService service) {
		this.service = service;
	}

	public TipoPrato getEntity() {
		return entity;
	}

	public void setEntity(TipoPrato entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<TipoPrato> getLista() {
		return lista;
	}

	public void setLista(List<TipoPrato> lista) {
		this.lista = lista;
	}

	public List<TipoPrato> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<TipoPrato> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	public List<TipoPrato> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<TipoPrato> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public Date getDataVigencia() {
		return dataVigencia;
	}

	public void setDataVigencia(Date dataVigencia) {
		this.dataVigencia = dataVigencia;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

}
