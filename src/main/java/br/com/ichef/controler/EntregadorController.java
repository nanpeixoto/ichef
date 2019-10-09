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
import br.com.ichef.model.Empresa;
import br.com.ichef.model.Entregador;
import br.com.ichef.model.EntregadorLocalidade;
import br.com.ichef.model.Localidade;
import br.com.ichef.service.AreaService;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.EntregadorService;
import br.com.ichef.service.LocalidadeService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.visitor.LocalidadeVisitor;

@Named
@ViewScoped
public class EntregadorController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntregadorService service;
	@Inject
	private LocalidadeService localidadeService;
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

	@Inject
	private EmpresaService empresaService;
	private List<Empresa> empresas = new ArrayList<Empresa>();

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
			getEntity().setEmpresa(userLogado.getEmpresaLogada());
		}
		localidade = null;
		try {
			Entregador filter = new Entregador();
			filter.setEmpresa(getUserLogado().getEmpresaLogada());
			lista = service.findByParameters(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		obterListas();
	}

	private void obterListas() {

		Area filter = new Area();
		filter.setEmpresa(getUserLogado().getEmpresaLogada());
		filter.setAtivo(true);

		/*AreaVisitor visitor = new AreaVisitor();
		visitor.setListaDesvinculados(true);*/
		
		empresas = empresaService.listAll(true);
		try {
			localidades = areaService.findByParameters(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void adicionarArea() {
		boolean existe = false;
		boolean itemAdicionado = false;
		
		// OBTER AS LOCALIDADES QUE NAO POSSUEM ENTREGADOR
		LocalidadeVisitor visitor = new LocalidadeVisitor();
		visitor.setArea(getArea());
		visitor.setListaDesvinculadosDosEntregadores(true);
		visitor.setListaDesvinculadosDasAreas(false);
		List<Localidade> localidades;
		try {
			localidades = localidadeService.findByParameters(new Localidade(), visitor);

			existe = false;

			for (Localidade localidade : localidades) {
				if (getEntity().getLocalidades() != null && getEntity().getLocalidades().size() > 0) {
					for (EntregadorLocalidade entregadorLocalidade : (getEntity().getLocalidades())) {
						if (localidade.getId().equals(entregadorLocalidade.getLocalidade().getId())) {
							existe = true;
						}
					}
				}

				if (!existe) {
					
					if (getEntity().getLocalidades() == null)
						getEntity().setLocalidades(new ArrayList<EntregadorLocalidade>());
					
					EntregadorLocalidade obj = new EntregadorLocalidade();
					obj.setUsuarioCadastro(getUserLogado());
					obj.setDataCadastro(new Date());
					obj.setAtivo("S");
					obj.setLocalidade(localidade);
					obj.setEntregador(getEntity());
					obj.setOrdem( (long) ( getEntity().getLocalidades().size()+1 ) );
					
					itemAdicionado = true;

					getEntity().getLocalidades().add(obj);

				}
			}
			
			if ( localidades==null || localidades.size() ==0 ) {
				facesMessager.error("Nenhuma localidade encontrada, verifique se a área informada não está vinculada a outro entregador e/ou se área possui localidades vinculadas");
				return ;
			}
			
			if(!itemAdicionado) {
				facesMessager.error("Localidade(s) não vinculada(s) verifique duplicidade(s)");
			}

			setArea(null);

			updateComponentes("selectArea");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void excluirSelecionados() {
		for (BaseEntity entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}

	public void excluirItensSelecionadas(EntregadorLocalidade local) {
		
		List<EntregadorLocalidade> temp = new ArrayList<>();
		temp.addAll(entity.getLocalidades());
		for (EntregadorLocalidade arealoc : entity.getLocalidades()) {
			if (local.getLocalidade().getId().equals(arealoc.getLocalidade().getId()))
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
		List<String> mensagem = service.validaRegras(getEntity());
		if (mensagem == null  ) {
			service.saveOrUpdade(entity);
		} else {
			FacesUtil.addErroMessage(mensagem.get(0));
			return "";
		}
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

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

}
