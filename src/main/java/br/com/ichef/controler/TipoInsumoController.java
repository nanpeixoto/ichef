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
import br.com.ichef.model.TipoInsumo;
import br.com.ichef.service.TipoInsumoService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class TipoInsumoController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private TipoInsumoService service;
	

	private TipoInsumo entity;

	private Long id;

	private List<TipoInsumo> lista = new ArrayList<TipoInsumo>();
	private List<TipoInsumo> listaFiltro = new ArrayList<TipoInsumo>();

	private List<TipoInsumo> listaSelecionadas = new ArrayList<TipoInsumo>();


	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new TipoInsumo());
			getEntity().setAtivo(true);
		}
		obterListas();
	}

	
	private void obterListas() {
	
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
		FacesUtil.addInfoMessage("TipoInsumos excluídas com sucesso");
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
		return "lista-tipo-insumo.xhtml?faces-redirect=true";
	}

	public String excluir() {
		service.excluir(entity);
		return "lista-tipo-insumo.xhtml?faces-redirect=true";
	}

	public TipoInsumoService getService() {
		return service;
	}

	public void setService(TipoInsumoService service) {
		this.service = service;
	}

	public TipoInsumo getEntity() {
		return entity;
	}

	public void setEntity(TipoInsumo entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<TipoInsumo> getLista() {
		return lista;
	}

	public void setLista(List<TipoInsumo> lista) {
		this.lista = lista;
	}

	public List<TipoInsumo> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<TipoInsumo> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}


	public List<TipoInsumo> getListaFiltro() {
		return listaFiltro;
	}


	public void setListaFiltro(List<TipoInsumo> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	

}
