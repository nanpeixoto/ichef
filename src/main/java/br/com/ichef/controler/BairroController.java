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
import br.com.ichef.model.Bairro;
import br.com.ichef.model.Cidade;
import br.com.ichef.service.BairroService;
import br.com.ichef.service.CidadeService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class BairroController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private BairroService service;
	
	@Inject
	private CidadeService cidadeService;

	private Bairro entity;

	private Long id;

	private List<Bairro> lista = new ArrayList<Bairro>();

	private List<Bairro> listaSelecionadas = new ArrayList<Bairro>();

	private List<Cidade> cidades = new ArrayList<Cidade>();

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new Bairro());
			getEntity().setAtivo(true);
			getEntity().setCidade(obterCidadeSalvador());
		}
		obterListas();
	}

	private Cidade obterCidadeSalvador() {
		Cidade cidadeSalvador = new Cidade();
		cidadeSalvador.setDescricao("SALVADOR");
		
		try {
			return cidadeService.findByParameters(cidadeSalvador).get(0);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	private void obterListas() {
		cidades = cidadeService.listAll();

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
		FacesUtil.addInfoMessage("Bairros excluídas com sucesso");
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
		return "lista-bairro.xhtml?faces-redirect=true";
	}

	public String excluir() {
		service.excluir(entity);
		return "lista-bairro.xhtml?faces-redirect=true";
	}

	public BairroService getService() {
		return service;
	}

	public void setService(BairroService service) {
		this.service = service;
	}

	public Bairro getEntity() {
		return entity;
	}

	public void setEntity(Bairro entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Bairro> getLista() {
		return lista;
	}

	public void setLista(List<Bairro> lista) {
		this.lista = lista;
	}

	public List<Bairro> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<Bairro> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

}
