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
import br.com.ichef.model.Derivacao;
import br.com.ichef.service.DerivacaoService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class DerivacaoController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private DerivacaoService service;
	

	private Derivacao entity;

	private Long id;

	private List<Derivacao> lista = new ArrayList<Derivacao>();
	private List<Derivacao> listaFiltro = new ArrayList<Derivacao>();

	private List<Derivacao> listaSelecionadas = new ArrayList<Derivacao>();


	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new Derivacao());
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
		return "lista-derivacao.xhtml?faces-redirect=true";
	}

	public String excluir() {
		service.excluir(entity);
		return "lista-derivacao.xhtml?faces-redirect=true";
	}

	public DerivacaoService getService() {
		return service;
	}

	public void setService(DerivacaoService service) {
		this.service = service;
	}

	public Derivacao getEntity() {
		return entity;
	}

	public void setEntity(Derivacao entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Derivacao> getLista() {
		return lista;
	}

	public void setLista(List<Derivacao> lista) {
		this.lista = lista;
	}

	public List<Derivacao> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<Derivacao> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}


	public List<Derivacao> getListaFiltro() {
		return listaFiltro;
	}


	public void setListaFiltro(List<Derivacao> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	

}
