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
import br.com.ichef.model.Area;
import br.com.ichef.model.Entregador;
import br.com.ichef.model.EntregadorArea;
import br.com.ichef.service.AreaService;
import br.com.ichef.service.EntregadorService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.visitor.AreaVisitor;

@Named	
@ViewScoped
public class EntregadorController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntregadorService service;

	@Inject
	private AreaService areaService;

	private Entregador entity;

	private Long id;

	private List<Entregador> lista = new ArrayList<Entregador>();
	private List<Entregador> listaFiltro = new ArrayList<Entregador>();

	private List<Entregador> listaSelecionadas = new ArrayList<Entregador>();

	private List<Area> listaAreaSelecionadas = new ArrayList<Area>();
	private List<Area> localidades = new ArrayList<Area>();
	private Area localidade;

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));

		}
		obterListas();
	}

	@PostConstruct
	public void init() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new Entregador());
			getEntity().setAtivo("S");
		}
		localidade = null;
		lista = service.listAll();
		obterListas();
	}

	private void obterListas() {
		AreaVisitor visitor = new AreaVisitor();
		visitor.setListaDesvinculados(true);
		try {
			localidades = areaService.findByParameters(new Area(), visitor);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void adicionarArea() {
		boolean existe = false;
		ArrayList<Area> listaLocal = new ArrayList<>();

		if(getEntity().getAreas()!=null )
			for (EntregadorArea areaArea : getEntity().getAreas()) {
				if( areaArea.getArea().getId().equals( getArea().getId() ))
					existe = true;
			}

		if (!existe) {

			EntregadorArea obj = new EntregadorArea();

			obj.setUsuarioCadastro(getUserLogado());
			obj.setDataCadastro(new Date());
			obj.setAtivo("S");

			obj.setArea(getArea());
			obj.setEntregador(getEntity());

			if (getEntity().getAreas() == null)
				getEntity().setAreas(new ArrayList<EntregadorArea>());
			getEntity().getAreas().add(obj);

		}

		AreaVisitor visitor = new AreaVisitor();
		visitor.setListaDesvinculados(true);
		visitor.setListaNotIn(listaLocal);
		try {
			localidades = areaService.findByParameters(new Area(), visitor);
		} catch (Exception e) {
			e.printStackTrace();
		}

		setArea(null);

		updateComponentes("selectArea");

	}

	public void excluirSelecionados() {
		for (BaseEntity entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}

	public void excluirAreasSelecionadas(EntregadorArea local) {
		List<EntregadorArea> temp = new ArrayList<>();
		temp.addAll(entity.getAreas());
		for (EntregadorArea arealoc : entity.getAreas()) {
			if( local.getArea().getId().equals(arealoc.getArea().getId()) )
				temp.remove(arealoc);
		}
		entity.getAreas().clear();
		entity.getAreas().addAll(temp);
		updateComponentes("Stable");
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
		return "lista-entregador.xhtml?faces-redirect=true";

	}

	public String excluir() {
		service.excluir(entity);
		return "lista-entregador.xhtml?faces-redirect=true";
	}

	public EntregadorService getService() {
		return service;
	}

	public void setService(EntregadorService service) {
		this.service = service;
	}

	public Entregador getEntity() {
		return entity;
	}

	public void setEntity(Entregador entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Entregador> getLista() {
		return lista;
	}

	public void setLista(List<Entregador> lista) {
		this.lista = lista;
	}

	public List<Entregador> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<Entregador> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	public List<Entregador> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<Entregador> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public Area getArea() {
		return localidade;
	}

	public void setArea(Area localidade) {
		this.localidade = localidade;
	}

	public List<Area> getAreas() {
		return localidades;
	}

	public void setAreas(List<Area> localidades) {
		this.localidades = localidades;
	}

	public List<Area> getListaAreaSelecionadas() {
		return listaAreaSelecionadas;
	}

	public void setListaAreaSelecionadas(List<Area> listaAreaSelecionadas) {
		this.listaAreaSelecionadas = listaAreaSelecionadas;
	}

}
