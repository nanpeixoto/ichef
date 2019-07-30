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
import br.com.ichef.model.Insumo;
import br.com.ichef.model.TipoInsumo;
import br.com.ichef.model.Unidade;
import br.com.ichef.service.InsumoService;
import br.com.ichef.service.TipoInsumoService;
import br.com.ichef.service.UnidadeService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class InsumoController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private InsumoService service;

	@Inject
	private TipoInsumoService tipoInsumoService;
	
	@Inject
	private UnidadeService unidadeService;

	private Insumo entity;

	private Long id;

	private List<Insumo> lista = new ArrayList<Insumo>();

	private List<Insumo> listaSelecionadas = new ArrayList<Insumo>();

	private List<TipoInsumo> tipoInsumos = new ArrayList<TipoInsumo>();
	private List<Unidade> unidades = new ArrayList<Unidade>();

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new Insumo());
			getEntity().setAtivo(true);
		}
		obterListas();
	}

	private void obterListas() {
		tipoInsumos = tipoInsumoService.listAll(true);
		unidades = unidadeService.listAll(true);
	}

	@PostConstruct
	public void init() {
		lista = service.listAll();
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
		return "lista-insumo.xhtml?faces-redirect=true";
	}

	public String excluir() {
		service.excluir(entity);
		return "lista-insumo.xhtml?faces-redirect=true";
	}

	public InsumoService getService() {
		return service;
	}

	public void setService(InsumoService service) {
		this.service = service;
	}

	public Insumo getEntity() {
		return entity;
	}

	public void setEntity(Insumo entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Insumo> getLista() {
		return lista;
	}

	public void setLista(List<Insumo> lista) {
		this.lista = lista;
	}

	public List<Insumo> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<Insumo> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public List<TipoInsumo> getTipoInsumos() {
		return tipoInsumos;
	}

	public void setTipoInsumos(List<TipoInsumo> tipoInsumos) {
		this.tipoInsumos = tipoInsumos;
	}

	public List<Unidade> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<Unidade> unidades) {
		this.unidades = unidades;
	}
	
	

}
