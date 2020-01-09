package br.com.ichef.controler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.com.ichef.arquitetura.controller.BaseConsultaCRUD;
import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Cidade;
import br.com.ichef.model.TipoLocalidade;
import br.com.ichef.service.TipoLocalidadeService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class TipoLocalidadeController extends BaseConsultaCRUD<TipoLocalidade> {

	private static final long serialVersionUID = 1L;

	@Inject
	private TipoLocalidadeService service;

	private TipoLocalidade entity;

	private Long id;

	private List<TipoLocalidade> lista = new ArrayList<TipoLocalidade>();

	private List<TipoLocalidade> listaSelecionadas = new ArrayList<TipoLocalidade>();

	private List<Cidade> cidades = new ArrayList<Cidade>();

	@Override
	protected TipoLocalidade newInstance() {
		// TODO Auto-generated method stub
		return new TipoLocalidade();
	}

	@Override
	protected AbstractService<TipoLocalidade> getService() {
		// TODO Auto-generated method stub
		return service;
	}

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

	public void excluirSelecionados() throws AppException {
		for (TipoLocalidade entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("TipoLocalidades excluídas com sucesso");
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
		try {
			service.excluir(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "lista-area.xhtml?faces-redirect=true";
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
