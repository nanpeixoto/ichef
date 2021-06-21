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
import br.com.ichef.model.Empresa;
import br.com.ichef.model.Localidade;
import br.com.ichef.service.CidadeService;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.LocalidadeService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class EmpresaController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private EmpresaService service;

	private Empresa entity;

	@Inject
	private CidadeService cidadeService;
	
	@Inject
	private LocalidadeService localidadeService;

	private Long id;

	private List<Empresa> lista = new ArrayList<Empresa>();

	private List<Empresa> listaSelecionadas = new ArrayList<Empresa>();

	private List<Cidade> cidades = new ArrayList<Cidade>();
	private List<Localidade> localidades = new ArrayList<Localidade>();

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new Empresa());
			getEntity().setAtivo(true);
			getEntity().setCidade(obterCidadeSalvador());
		}

		obterListas();
	}

	private Cidade obterCidadeSalvador() {
		Cidade cidadeSalvador = new Cidade();
		cidadeSalvador.setDescricao("SALVADOR".toUpperCase());

		try {
			return cidadeService.findByParameters(cidadeSalvador).get(0);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	private void obterListas() {
		cidades = cidadeService.listAll(true);
		localidades = localidadeService.listAllResidencial();

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
		FacesUtil.addInfoMessage("Empresas excluídas com sucesso");
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
		return "lista-empresa.xhtml?faces-redirect=true";
	}

	public String excluir() {
		service.excluir(entity);
		return "lista-empresa.xhtml?faces-redirect=true";
	}

	public EmpresaService getService() {
		return service;
	}

	public void setService(EmpresaService service) {
		this.service = service;
	}

	public Empresa getEntity() {
		return entity;
	}

	public void setEntity(Empresa entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Empresa> getLista() {
		return lista;
	}

	public void setLista(List<Empresa> lista) {
		this.lista = lista;
	}

	public List<Empresa> getListaSelecionadas() {
		return listaSelecionadas;
	}
	
	public void setListaSelecionadas(List<Empresa> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

	public List<Localidade> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List<Localidade> localidades) {
		this.localidades = localidades;
	}

}
