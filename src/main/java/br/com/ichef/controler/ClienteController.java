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
import br.com.ichef.model.Cliente;
import br.com.ichef.model.Empresa;
import br.com.ichef.service.CidadeService;
import br.com.ichef.service.ClienteService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class ClienteController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private ClienteService service;

	@Inject
	private CidadeService cidadeService;

	private Cliente entity;

	private Long id;

	private List<Cliente> lista = new ArrayList<Cliente>();
	private List<Cliente> listaFiltro = new ArrayList<Cliente>();

	private List<Cliente> listaSelecionadas = new ArrayList<Cliente>();

	private List<Cidade> cidades = new ArrayList<Cidade>();
	private List<Empresa> empresas = new ArrayList<Empresa>();

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new Cliente());
			getEntity().setAtivo(true);
			getEntity().setCidade(obterCidadeSalvador());
			getEntity().setRecebeMaladireta(true);
			getEntity().setRecebeSMS(true);
			getEntity().setPagaEmCarteira(false);
			getEntity().setEstaBloqueado(false);
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
		return "lista-cliente.xhtml?faces-redirect=true";
	}

	public String excluir() {
		service.excluir(entity);
		return "lista-cliente.xhtml?faces-redirect=true";
	}

	public ClienteService getService() {
		return service;
	}

	public void setService(ClienteService service) {
		this.service = service;
	}

	public Cliente getEntity() {
		return entity;
	}

	public void setEntity(Cliente entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Cliente> getLista() {
		return lista;
	}

	public void setLista(List<Cliente> lista) {
		this.lista = lista;
	}

	public List<Cliente> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<Cliente> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public List<Cliente> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<Cliente> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

}
