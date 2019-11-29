package br.com.ichef.controler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.model.Empresa;
import br.com.ichef.model.Insumo;
import br.com.ichef.model.InsumoPreco;
import br.com.ichef.model.TipoInsumo;
import br.com.ichef.model.Unidade;
import br.com.ichef.service.EmpresaService;
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
	private EmpresaService empresaService;

	@Inject
	private UnidadeService unidadeService;

	private Insumo entity;

	private Long id;

	private List<Insumo> lista = new ArrayList<Insumo>();

	private List<Insumo> listaSelecionadas = new ArrayList<Insumo>();

	private List<TipoInsumo> tipoInsumos = new ArrayList<TipoInsumo>();
	private List<Unidade> unidades = new ArrayList<Unidade>();
	private List<Empresa> listaEmpresas = new ArrayList<Empresa>();

	private Empresa empresa;
	private Double preco;
	private Date dataVigencia;

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
		}
	}

	@PostConstruct
	public void init() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new Insumo());
			getEntity().setAtivo("S");
			getEntity().setPrecos(new ArrayList<InsumoPreco>());
		}
		dataVigencia = new Date();
		lista = service.listAll();
		setEmpresa(getUserLogado().getEmpresaLogada());
		obterListas();
	}

	private void obterListas() {
		tipoInsumos = tipoInsumoService.listAll(true);
		unidades = unidadeService.listAll(true);
		setListaEmpresas(empresaService.listAll(true));
	}

	public void excluirSelecionados() {
		for (BaseEntity entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}

	public String Salvar() throws Exception {
		if (existePrecoParaTodasAsEmpresas()) {
			if (entity.isEdicao()) {
				entity.setUsuarioAlteracao(getUserLogado());
				entity.setDataAlteracao(new Date());
			} else {
				entity.setUsuarioCadastro(getUserLogado());
				entity.setDataCadastro(new Date());
			}
			service.saveOrUpdade(entity);
			return "lista-insumo.xhtml?faces-redirect=true";
		} else {
			FacesUtil.addErroMessage("Favor informar o valor para todas as empresa");
			return "";
		}
	}

	private boolean existePrecoParaTodasAsEmpresas() {
		List<Empresa> listaEmpresas = empresaService.listAll(true);
		Map<Long, InsumoPreco> mapEmpresaPreco = new HashMap<Long, InsumoPreco>();

		for (InsumoPreco preco : getEntity().getPrecos()) {
			Long key = preco.getEmpresa().getId();
			if (!mapEmpresaPreco.containsKey(key)) {
				mapEmpresaPreco.put(key, preco);
			}
		}

		if (listaEmpresas.size() == mapEmpresaPreco.size()) {
			return true;
		}
		return false;
	}

	public String excluir() {
		service.excluir(entity);
		return "lista-insumo.xhtml?faces-redirect=true";
	}

	public void adicionarPreco() {

		if (getEmpresa() == null) {
			facesMessager.error(getRequiredMessage("Empresa"));
			return;
		}

		if (getDataVigencia() == null) {
			facesMessager.error(getRequiredMessage("Vigência"));
			return;
		}

		if (getPreco() == null) {
			facesMessager.error(getRequiredMessage("Valor"));
			return;
		}

		InsumoPreco obj = new InsumoPreco();
		obj.setDataCadastro(new Date());
		obj.setUsuarioCadastro(getUserLogado());

		obj.setDataVigencia(getDataVigencia());
		obj.setPreco(getPreco());
		obj.setEmpresa(getEmpresa());

		obj.setInsumo(getEntity());

		if (getEntity().getPrecos() == null)
			getEntity().setPrecos(new ArrayList<InsumoPreco>());
		getEntity().getPrecos().add(obj);

		updateComponentes("tbPreco");

		setPreco(null);
		dataVigencia = new Date();
		setEmpresa(getUserLogado().getEmpresaLogada());

	}

	public void excluirItemSelecionado(InsumoPreco insumo) {
		List<InsumoPreco> temp = new ArrayList<InsumoPreco>();
		temp.addAll(entity.getPrecos());
		for (InsumoPreco item : entity.getPrecos()) {
			if (insumo.equals(item))
				temp.remove(item);
		}
		entity.getPrecos().clear();
		entity.getPrecos().addAll(temp);

		updateComponentes("tbPreco");
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
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

	public Date getDataVigencia() {
		return dataVigencia;
	}

	public void setDataVigencia(Date dataVigencia) {
		this.dataVigencia = dataVigencia;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<Empresa> getListaEmpresas() {
		return listaEmpresas;
	}

	public void setListaEmpresas(List<Empresa> listaEmpresas) {
		this.listaEmpresas = listaEmpresas;
	}

}
