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
import br.com.ichef.model.Cidade;
import br.com.ichef.model.TipoLocalidade;
import br.com.ichef.service.TipoLocalidadeService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class TipoLocalidadeController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private TipoLocalidadeService service;
	

	private TipoLocalidade entity;

	private Long id;

	private List<TipoLocalidade> lista = new ArrayList<TipoLocalidade>();

	private List<TipoLocalidade> listaSelecionadas = new ArrayList<TipoLocalidade>();

	private List<Cidade> cidades = new ArrayList<Cidade>();

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new TipoLocalidade());
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
		FacesUtil.addInfoMessage("TipoLocalidades exclu�das com sucesso");
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
		return "lista-tipolocalidade.xhtml?faces-redirect=true";
	}

	public String excluir() {
		service.excluir(entity);
		return "lista-tipolocalidade.xhtml?faces-redirect=true";
	}

	public TipoLocalidadeService getService() {
		return service;
	}

	public void setService(TipoLocalidadeService service) {
		this.service = service;
	}

	public TipoLocalidade getEntity() {
		return entity;
	}

	public void setEntity(TipoLocalidade entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<TipoLocalidade> getLista() {
		return lista;
	}

	public void setLista(List<TipoLocalidade> lista) {
		this.lista = lista;
	}

	public List<TipoLocalidade> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<TipoLocalidade> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

}
