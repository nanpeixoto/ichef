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
import br.com.ichef.model.Unidade;
import br.com.ichef.service.UnidadeService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class UnidadeController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private UnidadeService service;
	

	private Unidade entity;

	private Long id;

	private List<Unidade> lista = new ArrayList<Unidade>();
	private List<Unidade> listaFiltro = new ArrayList<Unidade>();

	private List<Unidade> listaSelecionadas = new ArrayList<Unidade>();


	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new Unidade());
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
		FacesUtil.addInfoMessage("Item excluído com sucesso");
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
		return "lista-unidade.xhtml?faces-redirect=true";
	}

	public String excluir() {
		service.excluir(entity);
		return "lista-unidade.xhtml?faces-redirect=true";
	}

	public UnidadeService getService() {
		return service;
	}

	public void setService(UnidadeService service) {
		this.service = service;
	}

	public Unidade getEntity() {
		return entity;
	}

	public void setEntity(Unidade entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Unidade> getLista() {
		return lista;
	}

	public void setLista(List<Unidade> lista) {
		this.lista = lista;
	}

	public List<Unidade> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<Unidade> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}


	public List<Unidade> getListaFiltro() {
		return listaFiltro;
	}


	public void setListaFiltro(List<Unidade> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	

}
