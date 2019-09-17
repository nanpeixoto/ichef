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
import br.com.ichef.model.AreaLocalidade;
import br.com.ichef.model.FichaTecnicaPreparo;
import br.com.ichef.model.FichaTecnicaPreparoInsumo;
import br.com.ichef.model.Insumo;
import br.com.ichef.model.Localidade;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.FichaTecnicaPreparoService;
import br.com.ichef.service.LocalidadeService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.visitor.LocalidadeVisitor;

@Named
@ViewScoped
public class FichaTecnicaPreparoController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private FichaTecnicaPreparoService service;

	@Inject
	private LocalidadeService localidadeService;

	@Inject
	private EmpresaService empresaService;

	private FichaTecnicaPreparo entity;

	private Long id;

	private List<FichaTecnicaPreparo> lista = new ArrayList<FichaTecnicaPreparo>();
	private List<FichaTecnicaPreparo> listaFiltro = new ArrayList<FichaTecnicaPreparo>();

	private List<FichaTecnicaPreparo> listaSelecionadas = new ArrayList<FichaTecnicaPreparo>();

	private List<FichaTecnicaPreparoInsumo> listaPreparoInsumosSelecionadas = new ArrayList<FichaTecnicaPreparoInsumo>();
	private List<FichaTecnicaPreparoInsumo> preparoInsumos = new ArrayList<FichaTecnicaPreparoInsumo>();
	private FichaTecnicaPreparoInsumo insumo;

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
			setEntity(new FichaTecnicaPreparo());
			getEntity().setAtivo("S");
		}
		// localidade = null;
		lista = service.listAll();
		obterListas();
	}

	private void obterListas() {

		Localidade filter = new Localidade();
		filter.setEmpresa(getUserLogado().getEmpresaLogada());

		LocalidadeVisitor visitor = new LocalidadeVisitor();
		// visitor.setListaDesvinculadosDasFichaTecnicaPreparos(true);

		try {
			// localidades = localidadeService.findByParameters(filter, visitor);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void excluirItensSelecionadas(FichaTecnicaPreparoInsumo insumo) {
		/*
		 * List<AreaLocalidade> temp = new ArrayList<>();
		 * temp.addAll(entity.getLocalidades()); for (AreaLocalidade arealoc :
		 * entity.getLocalidades()) { if
		 * (local.getLocalidade().getId().equals(arealoc.getLocalidade().getId()))
		 * temp.remove(arealoc); } entity.getLocalidades().clear();
		 * entity.getLocalidades().addAll(temp); updateComponentes("Stable");
		 */
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}

	public void adicionarInsumo() {
		boolean existe = false;
		ArrayList<FichaTecnicaPreparoInsumo> listaLocal = new ArrayList<>();

		/*
		 * if (getEntity().getLocalidades() != null) for (FichaTecnicaPreparoLocalidade
		 * areaLocalidade : getEntity().getLocalidades()) { if
		 * (areaLocalidade.getLocalidade().getId().equals(getLocalidade().getId()))
		 * existe = true; }
		 * 
		 * if (!existe) {
		 * 
		 * FichaTecnicaPreparoLocalidade obj = new FichaTecnicaPreparoLocalidade();
		 * 
		 * obj.setUsuarioCadastro(getUserLogado()); obj.setDataCadastro(new Date());
		 * obj.setAtivo("S");
		 * 
		 * obj.setLocalidade(getLocalidade()); obj.setFichaTecnicaPreparo(getEntity());
		 * 
		 * if (getEntity().getLocalidades() == null) getEntity().setLocalidades(new
		 * ArrayList<FichaTecnicaPreparoLocalidade>());
		 * getEntity().getLocalidades().add(obj);
		 * 
		 * }
		 * 
		 * LocalidadeVisitor visitor = new LocalidadeVisitor();
		 * visitor.setListaDesvinculadosDasFichaTecnicaPreparos(true);
		 * visitor.setLocalidadesNotIn(listaLocal); try { localidades =
		 * localidadeService.findByParameters(new Localidade(), visitor); } catch
		 * (Exception e) { e.printStackTrace(); }
		 * 
		 * setLocalidade(null);
		 * 
		 * updateComponentes("selectLocalidade");
		 */

	}

	public void excluirSelecionados() {
		for (BaseEntity entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}

	/*
	 * public void excluirLocalidadesSelecionadas(FichaTecnicaPreparoLocalidade
	 * local) { List<FichaTecnicaPreparoLocalidade> temp = new ArrayList<>();
	 * temp.addAll(entity.getLocalidades()); for (FichaTecnicaPreparoLocalidade
	 * arealoc : entity.getLocalidades()) { if
	 * (local.getLocalidade().getId().equals(arealoc.getLocalidade().getId()))
	 * temp.remove(arealoc); } entity.getLocalidades().clear();
	 * entity.getLocalidades().addAll(temp); updateComponentes("Stable");
	 * FacesUtil.addInfoMessage("Itens excluídos com sucesso"); }
	 */

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

	public FichaTecnicaPreparoService getService() {
		return service;
	}

	public void setService(FichaTecnicaPreparoService service) {
		this.service = service;
	}

	public FichaTecnicaPreparo getEntity() {
		return entity;
	}

	public void setEntity(FichaTecnicaPreparo entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<FichaTecnicaPreparo> getLista() {
		return lista;
	}

	public void setLista(List<FichaTecnicaPreparo> lista) {
		this.lista = lista;
	}

	public List<FichaTecnicaPreparo> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<FichaTecnicaPreparo> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	public List<FichaTecnicaPreparo> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<FichaTecnicaPreparo> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public LocalidadeService getLocalidadeService() {
		return localidadeService;
	}

	public void setLocalidadeService(LocalidadeService localidadeService) {
		this.localidadeService = localidadeService;
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public List<FichaTecnicaPreparoInsumo> getListaPreparoInsumosSelecionadas() {
		return listaPreparoInsumosSelecionadas;
	}

	public void setListaPreparoInsumosSelecionadas(List<FichaTecnicaPreparoInsumo> listaPreparoInsumosSelecionadas) {
		this.listaPreparoInsumosSelecionadas = listaPreparoInsumosSelecionadas;
	}

	public List<FichaTecnicaPreparoInsumo> getPreparoInsumos() {
		return preparoInsumos;
	}

	public void setPreparoInsumos(List<FichaTecnicaPreparoInsumo> preparoInsumos) {
		this.preparoInsumos = preparoInsumos;
	}

	public FichaTecnicaPreparoInsumo getInsumo() {
		return insumo;
	}

	public void setInsumo(FichaTecnicaPreparoInsumo insumo) {
		this.insumo = insumo;
	}

}
