package br.com.ichef.controler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.model.FichaTecnicaPreparo;
import br.com.ichef.model.FichaTecnicaPreparoInsumo;
import br.com.ichef.model.Insumo;
import br.com.ichef.model.TipoInsumo;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.FichaTecnicaPreparoService;
import br.com.ichef.service.InsumoService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.visitor.FIchaTecnicaPreparoVisitor;

@Named
@ViewScoped
public class FichaTecnicaPreparoController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private FichaTecnicaPreparoService service;

	@Inject
	private InsumoService insumoService;

	@Inject
	private EmpresaService empresaService;

	private FichaTecnicaPreparo entity;

	private Long id;

	private List<FichaTecnicaPreparo> lista = new ArrayList<FichaTecnicaPreparo>();
	private List<FichaTecnicaPreparo> listaFiltro = new ArrayList<FichaTecnicaPreparo>();

	private List<FichaTecnicaPreparo> listaSelecionadas = new ArrayList<FichaTecnicaPreparo>();

	// private List<FichaTecnicaPreparoInsumo> listaPreparoInsumosSelecionadas = new
	// ArrayList<FichaTecnicaPreparoInsumo>();
	// private List<FichaTecnicaPreparoInsumo> preparoInsumos = new
	// ArrayList<FichaTecnicaPreparoInsumo>();

	private List<Insumo> insumos = new ArrayList<Insumo>();

	private Insumo insumo;
	private BigDecimal qtdLiquida;
	private Long aproveitamento;
	private BigDecimal qtdBruta;

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
			setAproveitamento(100l);
			/*
			 * getEntity().setPrecoCustoPorcao(new BigDecimal(0));
			 * getEntity().setPrecoCustoReceita(new BigDecimal(0));
			 * getEntity().setPrecoVendaPorcao(new BigDecimal(0));
			 * getEntity().setPrecoVendaReceita(new BigDecimal(0));
			 */
		}
		// localidade = null;
		lista = service.listAll();
		obterListas();
	}

	private void obterListas() {
		try {
			FIchaTecnicaPreparoVisitor visitor = new FIchaTecnicaPreparoVisitor();
			visitor.setCodigoTipoMaterialExcluido(TipoInsumo.COD_INSUMO_MATERIAL);
			insumos = insumoService.findByParameters(new Insumo(), visitor);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void excluirItensSelecionadas(FichaTecnicaPreparoInsumo insumo) {
		List<FichaTecnicaPreparoInsumo> temp = new ArrayList<FichaTecnicaPreparoInsumo>();
		temp.addAll(entity.getInsumos());
		for (FichaTecnicaPreparoInsumo item : entity.getInsumos()) {
			if (insumo.getInsumo().getId().equals(item.getInsumo().getId()))
				temp.remove(item);
		}
		entity.getInsumos().clear();
		entity.getInsumos().addAll(temp);
		service.calcularPercos(entity, configuracao);
		updateComponentes("Stable");
		FacesUtil.addInfoMessage("Itens exclu�dos com sucesso");
	}

	public String copiar(FichaTecnicaPreparo perparo) {
		try {
			FichaTecnicaPreparo clone = new FichaTecnicaPreparo();

			clone = perparo.clone();
			clone.setId(null);
			setEntity(clone);
			//Integer qtd = obetrQuantidadeFichaByNome();
			clone.setDescricao(perparo.getDescricao() );

			List<FichaTecnicaPreparoInsumo> novaListaFichaInsumo = new ArrayList<>();

			for (FichaTecnicaPreparoInsumo fichaInsumoOld : perparo.getInsumos()) {
				FichaTecnicaPreparoInsumo fichaInsumo = new FichaTecnicaPreparoInsumo();
				fichaInsumo.setAtivo(fichaInsumoOld.getAtivo());
				fichaInsumo.setCustoBruto(fichaInsumoOld.getCustoBruto());
				fichaInsumo.setCustoTotal(fichaInsumoOld.getCustoTotal());
				fichaInsumo.setInsumo(fichaInsumoOld.getInsumo());
				fichaInsumo.setQuantidadeBruta(fichaInsumoOld.getQuantidadeBruta());
				fichaInsumo.setQuantidadeLiquida(fichaInsumoOld.getQuantidadeLiquida());
				fichaInsumo.setAproveitamento(fichaInsumoOld.getAproveitamento());
				fichaInsumo.setFichaTecnicaPreparo(clone);
				novaListaFichaInsumo.add(fichaInsumo);
			}

			clone.setInsumos(novaListaFichaInsumo);

			Salvar(false);
			return "cadastro-ficha-tecnica-preparo.xhtml?faces-redirect=true&id=" + getEntity().getId();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";

	}

	public void adicionarInsumo() {
		boolean existe = false;

		if (getInsumo() == null) {
			facesMessager.error(getRequiredMessage("Insumo"));
			return;
		}

		if (getQtdLiquida() == null) {
			facesMessager.error(getRequiredMessage("Qtd Liquida"));
			return;
		}

		if (getAproveitamento() == null) {
			facesMessager.error(getRequiredMessage("Aproveitamento"));
			return;
		}

		if (getEntity().getInsumos() != null) {
			for (FichaTecnicaPreparoInsumo fichaInsumo : getEntity().getInsumos()) {
				if (insumo.getId().equals(fichaInsumo.getInsumo().getId()))
					existe = true;
			}
		}

		if (!existe) {
			FichaTecnicaPreparoInsumo fichaInsumo = new FichaTecnicaPreparoInsumo();
			fichaInsumo.setAproveitamento(aproveitamento);
			fichaInsumo.setAtivo(true);
			;
			fichaInsumo.setAtivo("S");
			fichaInsumo.setQuantidadeBruta(
					(qtdLiquida.divide(new BigDecimal(aproveitamento).divide(new BigDecimal(100)), BigDecimal.ROUND_UP))
							.setScale(2, RoundingMode.CEILING));
			fichaInsumo.setCustoBruto(new BigDecimal(getInsumo().getValor()));
			fichaInsumo
					.setCustoTotal((fichaInsumo.getQuantidadeBruta().multiply(new BigDecimal(getInsumo().getValor())))
							.setScale(2, RoundingMode.CEILING));
			fichaInsumo.setFichaTecnicaPreparo(getEntity());
			fichaInsumo.setInsumo(insumo);
			fichaInsumo.setQuantidadeLiquida(qtdLiquida);

			if (getEntity().getInsumos() == null) {
				getEntity().setInsumos(new ArrayList<FichaTecnicaPreparoInsumo>());
			}
			getEntity().getInsumos().add(fichaInsumo);

			service.calcularPercos(entity, configuracao);
			insumo = null;
			aproveitamento = 100l;
			qtdLiquida = null;

		} else {
			facesMessager.error("Insumo j� cadastrado");
		}

	}

	public void excluirSelecionados() {
		for (BaseEntity entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("Itens exclu�dos com sucesso");
	}

	/*
	 * public void excluirLocalidadesSelecionadas(FichaTecnicaPreparoLocalidade
	 * local) { List<FichaTecnicaPreparoLocalidade> temp = new ArrayList<>();
	 * temp.addAll(entity.getLocalidades()); for (FichaTecnicaPreparoLocalidade
	 * arealoc : entity.getLocalidades()) { if
	 * (local.getLocalidade().getId().equals(arealoc.getLocalidade().getId()))
	 * temp.remove(arealoc); } entity.getLocalidades().clear();
	 * entity.getLocalidades().addAll(temp); updateComponentes("Stable");
	 * FacesUtil.addInfoMessage("Itens exclu�dos com sucesso"); }
	 */

	public String Salvar(boolean validarNome) throws Exception {
		Integer qtdFichaMesmoNome = 0;
		qtdFichaMesmoNome = obetrQuantidadeFichaByNome();

		if (entity.isEdicao()) {
			entity.setUsuarioAlteracao(getUserLogado());
			entity.setDataAlteracao(new Date());
			if (validarNome) {
				if (qtdFichaMesmoNome > 1) {
					FacesUtil.addErroMessage("J� existe um item com esse nome");
					return "";
				}
			}
		} else {
			entity.setUsuarioCadastro(getUserLogado());
			entity.setDataCadastro(new Date());
			if (validarNome) {
				if (qtdFichaMesmoNome > 0) {
					FacesUtil.addErroMessage("J� existe um item com esse nome");
					return "";
				}
			}
		}
		service.calcularPercos(entity, getConfiguracao());

		service.saveOrUpdade(entity);

		return "lista-ficha-tecnica-preparo.xhtml?faces-redirect=true";

	}

	public String Salvar() throws Exception {
		return Salvar(true);

	}

	private Integer obetrQuantidadeFichaByNome() throws Exception {
		FIchaTecnicaPreparoVisitor visitor = new FIchaTecnicaPreparoVisitor();
		visitor.setNomeInsumo(entity.getDescricao());
		Integer qtdFichaMesmoNome = (service.findByParameters(new FichaTecnicaPreparo(), visitor)).size();
		return qtdFichaMesmoNome;
	}

	public String excluir() {
		service.excluir(entity);
		return "lista-ficha-tecnica-preparo.xhtml?faces-redirect=true";
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

	public List<Insumo> getInsumos() {
		return insumos;
	}

	public void setInsumos(List<Insumo> insumos) {
		this.insumos = insumos;
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public InsumoService getInsumoService() {
		return insumoService;
	}

	public void setInsumoService(InsumoService insumoService) {
		this.insumoService = insumoService;
	}

	public Insumo getInsumo() {
		return insumo;
	}

	public void setInsumo(Insumo insumo) {
		this.insumo = insumo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BigDecimal getQtdBruta() {
		return qtdBruta;
	}

	public void setQtdBruta(BigDecimal qtdBruta) {
		this.qtdBruta = qtdBruta;
	}

	public Long getAproveitamento() {
		return aproveitamento;
	}

	public void setAproveitamento(Long aproveitamento) {
		this.aproveitamento = aproveitamento;
	}

	public BigDecimal getQtdLiquida() {
		return qtdLiquida;
	}

	public void setQtdLiquida(BigDecimal qtdLiquida) {
		this.qtdLiquida = qtdLiquida;
	}

}