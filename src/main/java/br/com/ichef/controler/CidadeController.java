package br.com.ichef.controler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.arquitetura.controller.BaseConsultaCRUD;
import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Cidade;
import br.com.ichef.service.CidadeService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class CidadeController  extends BaseConsultaCRUD<Cidade>  {

	private static final long serialVersionUID = 1L;

	@Inject
	private CidadeService service;

	private Cidade entity;

	private Long id;

	private List<Cidade> lista 				= new ArrayList<Cidade>();
	private List<Cidade> listaFiltro		= new ArrayList<Cidade>();

	private List<Cidade> listaSelecionadas 	= new ArrayList<Cidade>();
	
	
	@Override
	protected Cidade newInstance() {
		// TODO Auto-generated method stub
		return new Cidade();
	}

	@Override
	protected AbstractService<Cidade> getService() {
		// TODO Auto-generated method stub
		return service;
	}

	
	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new Cidade());
			getEntity().setAtivo("S");
		}
	}

	@PostConstruct
	public void init() {
		lista = service.listAll();
	}

	public void excluirSelecionados() throws AppException {
		for (Cidade entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("Cidades excluídas com sucesso");
	}

	public String Salvar() throws Exception {
		if (entity.isEdicao()) {
			entity.setUsuarioAlteracao( getUserLogado() );
			entity.setDataAlteracao(new Date());
		} else {
			entity.setUsuarioCadastro(  getUserLogado() );
			entity.setDataCadastro(new Date());
		}
		service.saveOrUpdade(entity);
		return "lista-cidade.xhtml?faces-redirect=true";
	}

	public String excluir() {
		try {
			service.excluir(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "lista-area.xhtml?faces-redirect=true";
	}

 

	public void setService(CidadeService service) {
		this.service = service;
	}

	public Cidade getEntity() {
		return entity;
	}

	public void setEntity(Cidade entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Cidade> getLista() {
		return lista;
	}

	public void setLista(List<Cidade> lista) {
		this.lista = lista;
	}

	public List<Cidade> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<Cidade> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public List<Cidade> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<Cidade> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

}
