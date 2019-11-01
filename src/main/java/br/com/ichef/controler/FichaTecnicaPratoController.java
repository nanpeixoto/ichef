package br.com.ichef.controler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.model.FichaTecnicaPrato;
import br.com.ichef.model.FichaTecnicaPratoPreparo;
import br.com.ichef.model.FichaTecnicaPratoTipo;
import br.com.ichef.model.FichaTecnicaPreparo;
import br.com.ichef.model.TipoPrato;
import br.com.ichef.service.FichaTecnicaPratoService;
import br.com.ichef.service.FichaTecnicaPreparoService;
import br.com.ichef.service.TipoPratoService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.visitor.FIchaTecnicaPratoVisitor;

@Named
@ViewScoped
public class FichaTecnicaPratoController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private FichaTecnicaPratoService service;

	@Inject
	private TipoPratoService tipoPratoService;

	@Inject
	private FichaTecnicaPreparoService fichaTecnicaPreparoService;

	private FichaTecnicaPrato entity;

	private Long id;

	private List<FichaTecnicaPrato> lista = new ArrayList<FichaTecnicaPrato>();
	private List<FichaTecnicaPrato> listaFiltro = new ArrayList<FichaTecnicaPrato>();

	private List<FichaTecnicaPrato> listaSelecionadas = new ArrayList<FichaTecnicaPrato>();

	private List<FichaTecnicaPreparo> preparos = new ArrayList<FichaTecnicaPreparo>();

	private List<TipoPrato> listaTipoPrato = new ArrayList<TipoPrato>();
	private Long[] listaTiposSelecionados;

	private FichaTecnicaPreparo fichaTecnicaPreparo;
	private BigDecimal qtdLiquida;
	private Long aproveitamento;
	private BigDecimal qtdBruta;
	private Long copia;

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
			inicializarlistaTiposSelecionados(getEntity());
		}
		if (copia != null) {
			criarClone(copia);
		}
		obterListas();
	}

	private void inicializarlistaTiposSelecionados(FichaTecnicaPrato itemAtual) {
		listaTiposSelecionados = new Long[itemAtual.getFichaTecnicaPratoTipos().size()];
		int i = 0;
		for (FichaTecnicaPratoTipo fichaTecnicaPratoTipo : itemAtual.getFichaTecnicaPratoTipos()) {
			listaTiposSelecionados[i] = (Long) fichaTecnicaPratoTipo.getTipoPrato().getId();
			i++;
		}

	}

	@PostConstruct
	public void init() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new FichaTecnicaPrato());
			getEntity().setAtivo("S");
			getEntity().setCopia("N");
			setAproveitamento(100l);
		}
		lista = service.listAll();
		obterListas();
	}

	private void obterListas() {
		try {
			FichaTecnicaPreparo filter = new FichaTecnicaPreparo();
			filter.setAtivo("S");

			//FIchaTecnicaPreparoVisitor visitor = new FIchaTecnicaPreparoVisitor();
			//visitor.setObterPreparoDesvinculadoPrato(true);

			setPreparos(fichaTecnicaPreparoService.findByParameters(filter));//, visitor));

			TipoPrato filterTipoPrato = new TipoPrato();
			filterTipoPrato.setAtivo("S");
			listaTipoPrato = tipoPratoService.findByParameters(filterTipoPrato);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void excluirItensSelecionadas(FichaTecnicaPratoPreparo insumo) {
		List<FichaTecnicaPratoPreparo> temp = new ArrayList<FichaTecnicaPratoPreparo>();
		temp.addAll(entity.getFichaTecnicaPratoPreparos());
		for (FichaTecnicaPratoPreparo item : entity.getFichaTecnicaPratoPreparos()) {
			if (insumo.getFichaTecnicaPreparo().getId().equals(item.getFichaTecnicaPreparo().getId()))
				temp.remove(item);
		}
		entity.getFichaTecnicaPratoPreparos().clear();
		entity.getFichaTecnicaPratoPreparos().addAll(temp);
		service.calcularPercos(entity, configuracao);
		updateComponentes("Stable");
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}

	public String copiar(FichaTecnicaPrato perparo) {
		try {

			// Salvar(false);
			return "cadastro-ficha-tecnica-prato.xhtml?faces-redirect=true&copia=" + perparo.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";

	}

	private void criarClone(Long idCopia) {
		try {
			criarClone(service.getById(idCopia));
			copia = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void criarClone(FichaTecnicaPrato perparo) throws CloneNotSupportedException {
		FichaTecnicaPrato clone = new FichaTecnicaPrato();
		// clone = perparo.clone();
		clone.setId(null);
		clone.setAtivo("S");
		clone.setCopia("S");
		// clone.setTamanho(10l);
		// clone.setClassificacao("A");
		// Integer qtd = obetrQuantidadeFichaByNome();
		clone.setDescricao(perparo.getDescricao());
		clone.setCopia("S");
		clone.setFichaTecnicaPratoPreparos(new ArrayList<>());

		List<FichaTecnicaPratoPreparo> novaListaFicha = new ArrayList<>();

		for (FichaTecnicaPratoPreparo fichaInsumoOld : perparo.getFichaTecnicaPratoPreparos()) {
			FichaTecnicaPratoPreparo fichaInsumo = new FichaTecnicaPratoPreparo();
			fichaInsumo.setAtivo(fichaInsumoOld.getAtivo());
			// fichaInsumo.setCustoBruto(fichaInsumoOld.getCustoBruto());
			fichaInsumo.setCustoTotal(fichaInsumoOld.getCustoTotal());
			fichaInsumo.setFichaTecnicaPreparo(fichaInsumoOld.getFichaTecnicaPreparo());
			fichaInsumo.setQuantidadeBruta(fichaInsumoOld.getQuantidadeBruta());
			fichaInsumo.setQuantidadeLiquida(fichaInsumoOld.getQuantidadeLiquida());
			fichaInsumo.setAproveitamento(fichaInsumoOld.getAproveitamento());
			fichaInsumo.setFichaTecnicaPrato(clone);
			novaListaFicha.add(fichaInsumo);
		}

		List<FichaTecnicaPratoTipo> novosTipos = new ArrayList<>();

		for (FichaTecnicaPratoTipo tipoOld : perparo.getFichaTecnicaPratoTipos()) {
			FichaTecnicaPratoTipo pratoTipo = new FichaTecnicaPratoTipo();
			pratoTipo.setFichaTecnicaPrato(clone);
			pratoTipo.setTipoPrato(tipoOld.getTipoPrato());
			novosTipos.add(pratoTipo);
		}
		
		

		setEntity(null);

		clone.setFichaTecnicaPratoTipos(novosTipos);
		clone.setFichaTecnicaPratoPreparos(novaListaFicha);
		
		
		setEntity(clone);
		
		inicializarlistaTiposSelecionados(getEntity());
	}

	public void adicionarPreparo() {
		boolean existe = false;

		if (getFichaTecnicaPreparo() == null) {
			facesMessager.error(getRequiredMessage("Preparo"));
			return;
		}

		setQtdLiquida(new BigDecimal(1));
		if (getQtdLiquida() == null) {
			facesMessager.error(getRequiredMessage("Qtd Liquida"));
			return;
		}

		setAproveitamento(1l);
		if (getAproveitamento() == null) {
			facesMessager.error(getRequiredMessage("Aproveitamento"));
			return;
		}

		if (getEntity().getFichaTecnicaPratoPreparos() != null) {
			for (FichaTecnicaPratoPreparo fichaInsumo : getEntity().getFichaTecnicaPratoPreparos()) {
				if (getFichaTecnicaPreparo().getId().equals(fichaInsumo.getFichaTecnicaPreparo().getId()))
					existe = true;
			}
		}

		if (!existe) {
			FichaTecnicaPratoPreparo fichaInsumo = new FichaTecnicaPratoPreparo();
			fichaInsumo.setAproveitamento(aproveitamento);
			fichaInsumo.setAtivo(true);
			fichaInsumo.setAtivo("S");
			fichaInsumo.setQuantidadeBruta(new BigDecimal(1));
			// fichaInsumo.setCustoBruto( getFichaTecnicaPreparo().getPrecoCustoReceita() )
			// ;
			fichaInsumo.setCustoTotal(getFichaTecnicaPreparo().getPrecoCustoPorcao());
			fichaInsumo.setFichaTecnicaPrato(getEntity());
			fichaInsumo.setFichaTecnicaPreparo(getFichaTecnicaPreparo());
			fichaInsumo.setQuantidadeLiquida(qtdLiquida);

			if (getEntity().getFichaTecnicaPratoPreparos() == null) {
				getEntity().setFichaTecnicaPratoPreparos(new ArrayList<FichaTecnicaPratoPreparo>());
			}
			getEntity().getFichaTecnicaPratoPreparos().add(fichaInsumo);

			service.calcularPercos(entity, configuracao);
			setFichaTecnicaPreparo(null);
			aproveitamento = 100l;
			qtdLiquida = null;

		} else {
			facesMessager.error("Insumo já cadastrado");
		}

	}

	public void excluirSelecionados() {
		for (BaseEntity entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}

	/*
	 * public void excluirLocalidadesSelecionadas(FichaTecnicaPratoLocalidade local)
	 * { List<FichaTecnicaPratoLocalidade> temp = new ArrayList<>();
	 * temp.addAll(entity.getLocalidades()); for (FichaTecnicaPratoLocalidade
	 * arealoc : entity.getLocalidades()) { if
	 * (local.getLocalidade().getId().equals(arealoc.getLocalidade().getId()))
	 * temp.remove(arealoc); } entity.getLocalidades().clear();
	 * entity.getLocalidades().addAll(temp); updateComponentes("Stable");
	 * FacesUtil.addInfoMessage("Itens excluídos com sucesso"); }
	 */

	public String Salvar(boolean validarNome) throws Exception {

		if (copia != null) {
			criarClone(getEntity());
		}

		if (getListaTiposSelecionados() == null || getListaTiposSelecionados().length == 0 ) {
			FacesUtil.addErroMessage("Selecione o(s) tipo(s) de prato(s)");
			return "";
		} else {
			if (getEntity().getFichaTecnicaPratoTipos() != null) {
				getEntity().getFichaTecnicaPratoTipos().clear();
			}
			for (Long idTipoPrato : getListaTiposSelecionados()) {

				FichaTecnicaPratoTipo tipo = new FichaTecnicaPratoTipo();
				tipo.setTipoPrato(tipoPratoService.getById(idTipoPrato));
				tipo.setFichaTecnicaPrato(getEntity());

				if(getEntity().getFichaTecnicaPratoTipos()==null ) {
					getEntity().setFichaTecnicaPratoTipos( new ArrayList<>() );
				}
				getEntity().getFichaTecnicaPratoTipos().add(tipo);

			}
		}

		Integer qtdFichaMesmoNome = 0;
		qtdFichaMesmoNome = obetrQuantidadeFichaByNome();
		Integer qtdPreparos = 0;
		qtdPreparos = obterQuantidadeFichaByPreparacao();

		if (entity.isEdicao()) {
			entity.setUsuarioAlteracao(getUserLogado());
			entity.setDataAlteracao(new Date());
			if (validarNome) {

				if (qtdPreparos > 1) {
					FacesUtil.addErroMessage("Já existe um prato com estas mesmas preparações");
					return "";
				}
				if (qtdFichaMesmoNome > 1) {
					FacesUtil.addErroMessage("Já existe um item com esse nome");
					return "";
				}
			}
		} else {
			entity.setUsuarioCadastro(getUserLogado());
			entity.setDataCadastro(new Date());
			if (validarNome) {
				if (qtdPreparos > 0) {
					FacesUtil.addErroMessage("Já existe um prato com estas mesmas preparações");
					return "";
				}
				
				if (qtdFichaMesmoNome > 0) {
					FacesUtil.addErroMessage("Já existe um item com esse nome");
					return "";
				}
			}
		}

		service.calcularPercos(entity, getConfiguracao());

		service.saveOrUpdade(entity);

		return "lista-ficha-tecnica-prato.xhtml?faces-redirect=true";

	}

	public String Salvar() throws Exception {
		return Salvar(true);

	}

	private Integer obetrQuantidadeFichaByNome() throws Exception {
		FIchaTecnicaPratoVisitor visitor = new FIchaTecnicaPratoVisitor();
		visitor.setDescricao(entity.getDescricao());
		Integer qtdFichaMesmoNome = (service.findByParameters(new FichaTecnicaPrato(), visitor)).size();
		return qtdFichaMesmoNome;
	}
	
	private Integer obterQuantidadeFichaByPreparacao() throws Exception {
		String listaPreparacoes = "";
		if( getEntity().getFichaTecnicaPratoPreparos()!=null ) {
			for (FichaTecnicaPratoPreparo preparo : getEntity().getFichaTecnicaPratoPreparos()) {
				if(!listaPreparacoes.equalsIgnoreCase("")) {
					listaPreparacoes =listaPreparacoes+",";
				}
				listaPreparacoes =listaPreparacoes+ preparo.getFichaTecnicaPreparo().getId().toString();
			}
		}
			
		Integer qtd = service.findQuantidadeFichaByPreparacao(listaPreparacoes) ;
		return qtd;
	}

	public String excluir() {
		service.excluir(entity);
		return "lista-ficha-tecnica-prato.xhtml?faces-redirect=true";
	}

	public FichaTecnicaPratoService getService() {
		return service;
	}

	public void setService(FichaTecnicaPratoService service) {
		this.service = service;
	}

	public FichaTecnicaPrato getEntity() {
		return entity;
	}

	public void setEntity(FichaTecnicaPrato entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<FichaTecnicaPrato> getLista() {
		return lista;
	}

	public void setLista(List<FichaTecnicaPrato> lista) {
		this.lista = lista;
	}

	public List<FichaTecnicaPrato> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<FichaTecnicaPrato> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	public List<FichaTecnicaPrato> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<FichaTecnicaPrato> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
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

	public Long getCopia() {
		return copia;
	}

	public void setCopia(Long copia) {
		this.copia = copia;
	}

	public TipoPratoService getTipoPratoService() {
		return tipoPratoService;
	}

	public void setTipoPratoService(TipoPratoService tipoPratoService) {
		this.tipoPratoService = tipoPratoService;
	}

	public FichaTecnicaPreparoService getFichaTecnicaPreparoService() {
		return fichaTecnicaPreparoService;
	}

	public void setFichaTecnicaPreparoService(FichaTecnicaPreparoService fichaTecnicaPreparoService) {
		this.fichaTecnicaPreparoService = fichaTecnicaPreparoService;
	}

	public FichaTecnicaPreparo getFichaTecnicaPreparo() {
		return fichaTecnicaPreparo;
	}

	public void setFichaTecnicaPreparo(FichaTecnicaPreparo fichaTecnicaPreparo) {
		this.fichaTecnicaPreparo = fichaTecnicaPreparo;
	}

	public List<FichaTecnicaPreparo> getPreparos() {
		return preparos;
	}

	public void setPreparos(List<FichaTecnicaPreparo> preparos) {
		this.preparos = preparos;
	}

	public List<TipoPrato> getListaTipoPrato() {
		return listaTipoPrato;
	}

	public void setListaTipoPrato(List<TipoPrato> listaTipoPrato) {
		this.listaTipoPrato = listaTipoPrato;
	}

	public Long[] getListaTiposSelecionados() {
		return listaTiposSelecionados;
	}

	public void setListaTiposSelecionados(Long[] listaTiposSelecionados) {
		this.listaTiposSelecionados = listaTiposSelecionados;
	}

}
