package br.com.ichef.controler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.calendar.Calendar;

import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.model.Cardapio;
import br.com.ichef.model.Cliente;
import br.com.ichef.model.Configuracao;
import br.com.ichef.model.Empresa;
import br.com.ichef.model.Entregador;
import br.com.ichef.model.Pedido;
import br.com.ichef.model.PedidoEtiqueta;
import br.com.ichef.service.CardapioService;
import br.com.ichef.service.ClienteService;
import br.com.ichef.service.DerivacaoService;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.EntregadorService;
import br.com.ichef.service.FormaPagamentoService;
import br.com.ichef.service.PedidoEtiquetaService;
import br.com.ichef.service.PedidoService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.util.JSFUtil;
import br.com.ichef.visitor.PedidoVisitor;

@Named
@ViewScoped
public class RelPedidoEtiqueta extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoService service;

	@Inject
	private EntregadorService entregadorService;

	@Inject
	private PedidoEtiquetaService pedidoEtiquetaService;

	@Inject
	private ClienteService clienteService;

	@Inject
	private FormaPagamentoService formaPagamentoService;

	@Inject
	private EmpresaService empresaService;

	@Inject
	private CardapioService cardapioService;

	@Inject
	private DerivacaoService derivacaoService;

	private List<Pedido> lista = new ArrayList<Pedido>();

	private List<Entregador> listaEntregador = new ArrayList<>();
	private Entregador entregador;

	private List<Empresa> listaEmpresas = new ArrayList<>();
	// private Empresa empresa;

	Configuracao config = (Configuracao) JSFUtil.getSessionMapValue("configuracao");

	private List<Entregador> listaEntregadorCarregada = new ArrayList<>();

	SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatarHora = new SimpleDateFormat("HH:mm");

	private Calendar componenteDataEntrega = new Calendar();

	// private Double preco;

	private Pedido entity;

	private Long codigoCliente;

	// relatorio
	private Date dataInicial;
	private Date dataFinal;
	private Date dataEntrega;
	private Date horarioCorte;

	private boolean entregaDataCardapio;
	private boolean antesNoveEMeia;
	
	private boolean tipoImpressao;

	@PostConstruct
	public void init() {

		obterListas();
		tipoImpressao = true;

	}

	private void obterListas() {
		Entregador filter = new Entregador();
		filter.setAtivo("S");
		filter.setEmpresa(userLogado.getEmpresaLogada());
		try {
			listaEntregadorCarregada = entregadorService.findByParameters(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void imprimirEtiquetaEntrega() {
		setDataFinal(getDataInicial());
		if (getDataInicial() == null || getDataFinal() == null) {
			FacesUtil.addInfoMessage(getRequiredMessage("Data"));
			return;
		} else {

			Cardapio cardapioFilter = new Cardapio();
			cardapioFilter.setAtivo("S");

			PedidoEtiqueta filter = new PedidoEtiqueta();
			filter.setEmpresa(userLogado.getEmpresaLogada());

			PedidoVisitor pedidoVisitor = new PedidoVisitor();
			pedidoVisitor.setDataEtiqueta(getDataInicial());
			if (getHorarioCorte() != null)
				pedidoVisitor.setHorarioCorte(formatarHora.format(getHorarioCorte()));
			// pedidoVisitor.setLimitarImpressaoPorHorarioExtra(true);
			// pedidoVisitor.setAntesNoveeTrinta(isAntesNoveEMeia());

			if (getEntregador() != null) {
				filter.setEntregador(getEntregador());
			}

			if (getCodigoCliente() != null) {
				Cliente cliente = new Cliente();
				cliente.setId(getCodigoCliente());

				filter.setCliente(cliente);
				pedidoVisitor.setHorarioCorte(null);
			}

			List<PedidoEtiqueta> pedidos = new ArrayList<>();

			try {
				pedidos = pedidoEtiquetaService.findByParameters(filter, pedidoVisitor);
			} catch (Exception e) {
				e.printStackTrace();
				FacesUtil.addErroMessage("Erro ao obter os dados do relatório");
			}

			if (pedidos.size() == 0) {
				FacesUtil.addErroMessage("Nenhum dado encontrado");
			} else {
				try {
					setParametroReport("logoEtiqtea", getImagem(LOGO_ETIQUETA));
					setParametroReport("tipoImpressao", tipoImpressao );
					orderBrOrdemPedidoEtiqueta(pedidos);
					escreveRelatorioPDF("EtiquetaEntrega", true, pedidos);
				} catch (Exception e) {
					e.printStackTrace();
					FacesUtil.addErroMessage("Erro ao gerar o relatório");
				}
			}

		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void orderBrOrdemPedidoEtiqueta(List<PedidoEtiqueta> pedido) {

		Collections.sort(pedido, new Comparator() {

			public int compare(Object o1, Object o2) {

				String x1 = ((PedidoEtiqueta) o1).getEntregador().getNome();
				String x2 = ((PedidoEtiqueta) o2).getEntregador().getNome();
				int sComp = x1.compareTo(x2);

				if (sComp != 0) {
					return sComp;
				}

				Integer ordem1 = ((PedidoEtiqueta) o1).getCardapioFichaPrato().getOrdem();
				Integer ordem2 = ((PedidoEtiqueta) o2).getCardapioFichaPrato().getOrdem();
				return ordem1.compareTo(ordem2);
			}
		});
	}

	public void limpar() {

	}

	public PedidoService getService() {
		return service;
	}

	public void setService(PedidoService service) {
		this.service = service;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public FormaPagamentoService getFormaPagamentoService() {
		return formaPagamentoService;
	}

	public void setFormaPagamentoService(FormaPagamentoService formaPagamentoService) {
		this.formaPagamentoService = formaPagamentoService;
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public DerivacaoService getDerivacaoService() {
		return derivacaoService;
	}

	public void setDerivacaoService(DerivacaoService derivacaoService) {
		this.derivacaoService = derivacaoService;
	}

	public List<Pedido> getLista() {
		return lista;
	}

	public void setLista(List<Pedido> lista) {
		this.lista = lista;
	}

	public List<Empresa> getListaEmpresas() {
		return listaEmpresas;
	}

	public void setListaEmpresas(List<Empresa> listaEmpresas) {
		this.listaEmpresas = listaEmpresas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CardapioService getCardapioService() {
		return cardapioService;
	}

	public void setCardapioService(CardapioService cardapioService) {
		this.cardapioService = cardapioService;
	}

	public List<Entregador> getListaEntregador() {
		return listaEntregador;
	}

	public void setListaEntregador(List<Entregador> listaEntregador) {
		this.listaEntregador = listaEntregador;
	}

	public Pedido getEntity() {
		return entity;
	}

	public void setEntity(Pedido entity) {
		this.entity = entity;
	}

	public Long getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(Long codigoCliente) {
		this.codigoCliente = codigoCliente;
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

	public List<Entregador> getListaEntregadorCarregada() {
		return listaEntregadorCarregada;
	}

	public void setListaEntregadorCarregada(List<Entregador> listaEntregadorCarregada) {
		this.listaEntregadorCarregada = listaEntregadorCarregada;
	}

	public boolean isEntregaDataCardapio() {
		return entregaDataCardapio;
	}

	public void setEntregaDataCardapio(boolean entregaDataCardapio) {
		this.entregaDataCardapio = entregaDataCardapio;
	}

	public Calendar getComponenteDataEntrega() {
		return componenteDataEntrega;
	}

	public void setComponenteDataEntrega(Calendar componenteDataEntrega) {
		this.componenteDataEntrega = componenteDataEntrega;
	}

	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public Entregador getEntregador() {
		return entregador;
	}

	public void setEntregador(Entregador entregador) {
		this.entregador = entregador;
	}

	public boolean isAntesNoveEMeia() {
		return antesNoveEMeia;
	}

	public void setAntesNoveEMeia(boolean antesNoveEMeia) {
		this.antesNoveEMeia = antesNoveEMeia;
	}

	public Date getHorarioCorte() {
		return horarioCorte;
	}

	public void setHorarioCorte(Date horarioCorte) {
		this.horarioCorte = horarioCorte;
	}

	public boolean isTipoImpressao() {
		return tipoImpressao;
	}

	public void setTipoImpressao(boolean tipoImpressao) {
		this.tipoImpressao = tipoImpressao;
	}

}
