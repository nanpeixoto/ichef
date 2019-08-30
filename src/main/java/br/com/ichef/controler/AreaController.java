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
import br.com.ichef.model.AreaLocalidade;
import br.com.ichef.model.Localidade;
import br.com.ichef.service.AreaService;
import br.com.ichef.service.LocalidadeService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.visitor.LocalidadeVisitor;

@Named
@ViewScoped
public class AreaController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private AreaService service;

	@Inject
	private LocalidadeService localidadeService;

	private Area entity;

	private Long id;

	private List<Area> lista = new ArrayList<Area>();
	private List<Area> listaFiltro = new ArrayList<Area>();

	private List<Area> listaSelecionadas = new ArrayList<Area>();

	private List<Localidade> listaLocalidadeSelecionadas = new ArrayList<Localidade>();
	private List<Localidade> localidades = new ArrayList<Localidade>();
	private Localidade localidade;

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
			setEntity(new Area());
			getEntity().setAtivo("S");
		}
		localidade = null;
		lista = service.listAll();
		obterListas();
	}

	private void obterListas() {
		LocalidadeVisitor visitor = new LocalidadeVisitor();
		visitor.setListaDesvinculadosDasAreas(true);
		try {
			localidades = localidadeService.findByParameters(new Localidade(), visitor);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void adicionarLocalidade() {
		boolean existe = false;
		ArrayList<Localidade> listaLocal = new ArrayList<>();

		for (AreaLocalidade areaLocalidade : getEntity().getLocalidades()) {
			if( areaLocalidade.getLocalidade().getId().equals( getLocalidade().getId() ))
				existe = true;
		}

		if (!existe) {

			AreaLocalidade obj = new AreaLocalidade();

			obj.setUsuarioCadastro(getUserLogado());
			obj.setDataCadastro(new Date());
			obj.setAtivo("S");

			obj.setLocalidade(getLocalidade());
			obj.setArea(getEntity());

			if (getEntity().getLocalidades() == null)
				getEntity().setLocalidades(new ArrayList<AreaLocalidade>());
			getEntity().getLocalidades().add(obj);

		}

		LocalidadeVisitor visitor = new LocalidadeVisitor();
		visitor.setListaDesvinculadosDasAreas(true);
		visitor.setLocalidadesNotIn(listaLocal);
		try {
			localidades = localidadeService.findByParameters(new Localidade(), visitor);
		} catch (Exception e) {
			e.printStackTrace();
		}

		setLocalidade(null);

		updateComponentes("selectLocalidade");

	}

	public void excluirSelecionados() {
		for (BaseEntity entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}

	public void excluirLocalidadesSelecionadas(AreaLocalidade local) {
		List<AreaLocalidade> temp = new ArrayList<>();
		temp.addAll(entity.getLocalidades());
		for (AreaLocalidade arealoc : entity.getLocalidades()) {
			if( local.getLocalidade().getId().equals(arealoc.getLocalidade().getId()) )
				temp.remove(arealoc);
		}
		entity.getLocalidades().clear();
		entity.getLocalidades().addAll(temp);
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
		return "lista-area.xhtml?faces-redirect=true";

	}

	public String excluir() {
		service.excluir(entity);
		return "lista-area.xhtml?faces-redirect=true";
	}

	public AreaService getService() {
		return service;
	}

	public void setService(AreaService service) {
		this.service = service;
	}

	public Area getEntity() {
		return entity;
	}

	public void setEntity(Area entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Area> getLista() {
		return lista;
	}

	public void setLista(List<Area> lista) {
		this.lista = lista;
	}

	public List<Area> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<Area> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	public List<Area> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<Area> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public Localidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}

	public List<Localidade> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List<Localidade> localidades) {
		this.localidades = localidades;
	}

	public List<Localidade> getListaLocalidadeSelecionadas() {
		return listaLocalidadeSelecionadas;
	}

	public void setListaLocalidadeSelecionadas(List<Localidade> listaLocalidadeSelecionadas) {
		this.listaLocalidadeSelecionadas = listaLocalidadeSelecionadas;
	}

}
