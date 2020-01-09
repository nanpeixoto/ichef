package br.com.ichef.controler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.ReorderEvent;

import br.com.ichef.arquitetura.controller.BaseConsultaCRUD;
import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Cardapio;
import br.com.ichef.model.CardapioFichaPrato;
import br.com.ichef.model.CardapioFichaPratoEmpresa;
import br.com.ichef.model.Empresa;
import br.com.ichef.model.FichaTecnicaPrato;
import br.com.ichef.service.CardapioFichaPratoService;
import br.com.ichef.service.CardapioService;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.FichaTecnicaPratoService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.visitor.CardapioVisitor;

@Named
@ViewScoped
public class CardapioController extends BaseConsultaCRUD<Cardapio> {

	private static final long serialVersionUID = 1L;

	@Inject
	private CardapioService service;

	@Inject
	private CardapioFichaPratoService cardapioFichaPratoService;

	@Inject
	private EmpresaService empresaService;

	@Inject
	private FichaTecnicaPratoService fichaTecnicaPratoService;

	private Cardapio entity;

	private Long id;

	private List<Cardapio> lista = new ArrayList<Cardapio>();
	private List<Cardapio> listaFiltro = new ArrayList<Cardapio>();

	private List<Cardapio> listaSelecionadas = new ArrayList<Cardapio>();

	private List<FichaTecnicaPrato> pratos = new ArrayList<FichaTecnicaPrato>();

	private FichaTecnicaPrato prato;
	private Integer quantidade;
	private String descricaoCardapioFicha;
	private boolean podeVenderAcimaDoLimite;
	private String observacao;

	// relatorio
	private Date dataInicial;
	private Date dataFinal;

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
			setEntity(new Cardapio());
			getEntity().setAtivo("S");
			getEntity().setData(new Date());
			setQuantidade(100);
			setPodeVenderAcimaDoLimite(true);
			setDescricaoCardapioFicha(null);

		}
		lista = service.listAll();
		obterListas();
		setQuantidade(100);
		setPodeVenderAcimaDoLimite(true);
	}

	private void obterListas() {
		try {

			setPratos(fichaTecnicaPratoService.listAll(true));// , visitor));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void excluirItensSelecionadas(CardapioFichaPrato insumo) {
		List<CardapioFichaPrato> temp = new ArrayList<CardapioFichaPrato>();
		temp.addAll(entity.getPratos());
		for (CardapioFichaPrato item : entity.getPratos()) {
			if (insumo.getFichaTecnicaPrato().getId().equals(item.getFichaTecnicaPrato().getId()))
				temp.remove(item);
		}
		entity.getPratos().clear();
		entity.getPratos().addAll(temp);

		updateComponentes("StableCardapio");

		FacesUtil.addInfoMessage("Itens exclu�dos com sucesso");
	}

	public void adicionarPrato() {

		if (!isNotEmptyOrNull(getDescricaoCardapioFicha()) || getDescricaoCardapioFicha() == null) {
			facesMessager.error(getRequiredMessage("Descri��o da Ficha"));
			return;
		}

		if (getPrato() == null) {
			facesMessager.error(getRequiredMessage("Prato"));
			return;
		}

		if (getQuantidade() == null) {
			facesMessager.error(getRequiredMessage("Quantidade"));
			return;
		}

		boolean existe = false;

		if (getEntity().getPratos() != null) {
			for (CardapioFichaPrato ficha : getEntity().getPratos()) {
				if (getPrato().getId().equals(ficha.getFichaTecnicaPrato().getId()))
					existe = true;
			}
		}

		if (!existe) {
			if (getEntity().getPratos() == null) {
				getEntity().setPratos(new ArrayList<CardapioFichaPrato>());
			}

			CardapioFichaPrato ficha = new CardapioFichaPrato();
			ficha.setCardapio(entity);
			ficha.setOrdem((getEntity().getPratos().size() + 1));
			ficha.setObservacao(getObservacao());
			ficha.setDescricao(getDescricaoCardapioFicha());
			ficha.setQuantidade(getQuantidade());
			ficha.setPodeVenderAcimaDoLimite(isPodeVenderAcimaDoLimite());
			ficha.setDataCadastro(new Date());
			ficha.setFichaTecnicaPrato(getPrato());
			ficha.setUsuarioCadastro(userLogado);

			getEntity().getPratos().add(ficha);

			/* EMPRESAS QUE O USU�RIO TEM ACESSO */
			List<Empresa> listaEmpresa = empresaService.listAll(true);

			for (Empresa empresa : listaEmpresa) {
				CardapioFichaPratoEmpresa pratoEmpresa = new CardapioFichaPratoEmpresa();
				pratoEmpresa.setCardapioFichaPrato(ficha);
				pratoEmpresa.setEmpresa(empresa);
				pratoEmpresa.setPodeVenderAcimaDoLimite(isPodeVenderAcimaDoLimite());
				pratoEmpresa.setQuantidade(getQuantidade());
				if (ficha.getFichaPratoEmpresa() == null) {
					ficha.setFichaPratoEmpresa(new ArrayList<CardapioFichaPratoEmpresa>());
				}
				ficha.getFichaPratoEmpresa().add(pratoEmpresa);

			}

			if (getEntity().getId() != null) {
				try {
					cardapioFichaPratoService.saveOrUpdade(ficha);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			setPrato(null);
			quantidade = 100;
			setPodeVenderAcimaDoLimite(true);
			setDescricaoCardapioFicha(null);

		} else {
			facesMessager.error("Prato j� cadastrado");
		}

	}

	public void excluirSelecionados() throws AppException {
		for (Cardapio entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("Itens exclu�dos com sucesso");
	}

	public void atualizarListaPreparo() {
		obterListas();
	}

	/*
	 * public void excluirLocalidadesSelecionadas(CardapioLocalidade local) {
	 * List<CardapioLocalidade> temp = new ArrayList<>();
	 * temp.addAll(entity.getLocalidades()); for (CardapioLocalidade arealoc :
	 * entity.getLocalidades()) { if
	 * (local.getLocalidade().getId().equals(arealoc.getLocalidade().getId()))
	 * temp.remove(arealoc); } entity.getLocalidades().clear();
	 * entity.getLocalidades().addAll(temp); updateComponentes("Stable");
	 * FacesUtil.addInfoMessage("Itens exclu�dos com sucesso"); }
	 */

	public String Salvar() throws Exception {

		if (getEntity().getPratos() == null || getEntity().getPratos().size() == 0) {
			FacesUtil.addErroMessage("N�o � poss�vel adicionar um card�pio sem prato");
			return "";
		}

		if (existeCardapio()) {
			FacesUtil.addErroMessage("J� existe um card�pio cadastrado nesse dia");
			return "";
		}

		if (getEntity().getPratos() != null) {
			for (Cardapio cardapio : lista) {
				cardapio.setUsuarioAlteracao(userLogado);
				cardapio.setDataAlteracao(new Date());
			}
		}

		if (entity.isEdicao()) {
			entity.setUsuarioAlteracao(getUserLogado());
			entity.setDataAlteracao(new Date());

		} else {
			entity.setUsuarioCadastro(getUserLogado());
			entity.setDataCadastro(new Date());

		}

		service.saveOrUpdade(entity);

		return "lista-cardapio.xhtml?faces-redirect=true";

	}

	private boolean existeCardapio() {
		CardapioVisitor visitor = new CardapioVisitor();
		if (getEntity().getId() != null) {
			visitor.setIdDiferenteDe((Long) getEntity().getId());
		}
		visitor.setDataCardapio(getEntity().getData());

		List<Cardapio> listaCardapio = new ArrayList<Cardapio>();

		try {
			listaCardapio = service.findByParameters(new Cardapio(), visitor);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (listaCardapio.size() > 0)
			return true;
		return false;
	}

	public void imprimir() {
		if (getDataInicial() == null || getDataFinal() == null) {
			FacesUtil.addInfoMessage(getRequiredMessage("Data"));
			return;
		} else {
			CardapioVisitor visitor = new CardapioVisitor();
			visitor.setDataInicio(getDataInicial());
			visitor.setDataFim(getDataFinal());

			List<Cardapio> cardapios = new ArrayList<Cardapio>();

			try {
				cardapios = service.findByParameters(new Cardapio(), visitor);

				for (Cardapio cardapio : cardapios) {
					java.util.Collections.sort(cardapio.getPratos());
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Erro ao obter os dados do relat�rio");
			}

			if (cardapios.size() == 0) {
				FacesUtil.addErroMessage("Nenhum dado encontrado");
			} else {
				try {
					setParametroReport(REPORT_PARAM_LOGO, getImagem(LOGO));
					escreveRelatorioPDF("Cardapio", true, cardapios);
				} catch (Exception e) {
					FacesUtil.addErroMessage("Erro ao gerar o relat�rio");
				}
			}

		}

	}

	public void onRowReorder(ReorderEvent event) {
		DataTable dataTable = (DataTable) event.getSource();
		dataTable.setRowIndex(event.getFromIndex());
		CardapioFichaPrato origem = null;
		CardapioFichaPrato destino = null;
		destino = (CardapioFichaPrato) dataTable.getRowData();
		dataTable.setRowIndex(event.getToIndex());
		origem = (CardapioFichaPrato) dataTable.getRowData();

		try {
			destino.setOrdem(event.getFromIndex() + 1);
			destino.setUsuarioAlteracao(userLogado);
			destino.setDataAlteracao(new Date());
			cardapioFichaPratoService.atualizarOrdem(destino);

			origem.setOrdem(event.getToIndex() + 1);
			origem.setUsuarioAlteracao(userLogado);
			origem.setDataAlteracao(new Date());
			cardapioFichaPratoService.atualizarOrdem(origem);

		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErroMessage("Erro, entre em contato com a administrador do sistema:" + e.getMessage());
		}

	}

	public String excluir() {
		try {
			service.excluir(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "lista-area.xhtml?faces-redirect=true";
	}

	 

	public void setService(CardapioService service) {
		this.service = service;
	}

	public Cardapio getEntity() {
		return entity;
	}

	public void setEntity(Cardapio entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Cardapio> getLista() {
		return lista;
	}

	public void setLista(List<Cardapio> lista) {
		this.lista = lista;
	}

	public List<Cardapio> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<Cardapio> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	public List<Cardapio> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<Cardapio> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<FichaTecnicaPrato> getPratos() {
		return pratos;
	}

	public void setPratos(List<FichaTecnicaPrato> pratos) {
		this.pratos = pratos;
	}

	public FichaTecnicaPrato getPrato() {
		return prato;
	}

	public void setPrato(FichaTecnicaPrato prato) {
		this.prato = prato;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getDescricaoCardapioFicha() {
		return descricaoCardapioFicha;
	}

	public void setDescricaoCardapioFicha(String descricaoCardapioFicha) {
		this.descricaoCardapioFicha = descricaoCardapioFicha;
	}

	public boolean isPodeVenderAcimaDoLimite() {
		return podeVenderAcimaDoLimite;
	}

	public void setPodeVenderAcimaDoLimite(boolean podeVenderAcimaDoLimite) {
		this.podeVenderAcimaDoLimite = podeVenderAcimaDoLimite;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Override
	protected Cardapio newInstance() {
		// TODO Auto-generated method stub
		return new Cardapio();
	}

	@Override
	protected AbstractService<Cardapio> getService() {
		// TODO Auto-generated method stub
		return service;
	}

}
