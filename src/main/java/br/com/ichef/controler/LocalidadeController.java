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
import br.com.ichef.model.Empresa;
import br.com.ichef.model.Localidade;
import br.com.ichef.model.TipoLocalidade;
import br.com.ichef.service.CidadeService;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.LocalidadeService;
import br.com.ichef.service.TipoLocalidadeService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class LocalidadeController extends BaseConsultaCRUD<Localidade> {

	private static final long serialVersionUID = 1L;

	@Inject
	private LocalidadeService service;

	@Inject
	private CidadeService cidadeService;

	@Inject
	private EmpresaService empresaService;

	@Inject
	private TipoLocalidadeService tipoLocalidadeService;

	private Localidade entity;

	private Long id;

	private List<Localidade> lista = new ArrayList<Localidade>();

	private List<Localidade> listaSelecionadas = new ArrayList<Localidade>();

	private List<Cidade> cidades = new ArrayList<Cidade>();
	private List<TipoLocalidade> tipoLocalidades = new ArrayList<TipoLocalidade>();
	private List<Empresa> empresas = new ArrayList<Empresa>();

	@Override
	protected Localidade newInstance() {
		// TODO Auto-generated method stub
		return new Localidade();
	}

	@Override
	protected AbstractService<Localidade> getService() {
		// TODO Auto-generated method stub
		return service;
	}

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new Localidade());
			getEntity().setAtivo(true);
			getEntity().setCidade(obterCidadeSalvador());
			getEntity().setEmpresa(userLogado.getEmpresaLogada());
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
		tipoLocalidades = tipoLocalidadeService.listAll(true);
		empresas = empresaService.empresasUsuario(getUserLogado());

	}

	@PostConstruct
	public void init() {
		try {
			Localidade filter = new Localidade();
			filter.setEmpresa(getUserLogado().getEmpresaLogada());
			lista = service.findByParameters(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void excluirSelecionados() throws AppException {
		for (Localidade entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("Itens exclu�dos com sucesso");
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
		return "lista-localidade.xhtml?faces-redirect=true";
	}

	public String excluir() {
		try {
			service.excluir(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "lista-area.xhtml?faces-redirect=true";
	}

	public void setService(LocalidadeService service) {
		this.service = service;
	}

	public Localidade getEntity() {
		return entity;
	}

	public void setEntity(Localidade entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Localidade> getLista() {
		return lista;
	}

	public void setLista(List<Localidade> lista) {
		this.lista = lista;
	}

	public List<Localidade> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<Localidade> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

	public List<TipoLocalidade> getTipoLocalidades() {
		return tipoLocalidades;
	}

	public void setTipoLocalidades(List<TipoLocalidade> tipoLocalidades) {
		this.tipoLocalidades = tipoLocalidades;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

}
