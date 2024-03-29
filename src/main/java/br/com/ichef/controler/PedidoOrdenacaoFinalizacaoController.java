package br.com.ichef.controler;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.calendar.Calendar;

import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.model.Cardapio;
import br.com.ichef.model.CardapioFichaPrato;
import br.com.ichef.model.CardapioFichaPratoEmpresa;
import br.com.ichef.model.Cliente;
import br.com.ichef.model.ClienteEndereco;
import br.com.ichef.model.Configuracao;
import br.com.ichef.model.Derivacao;
import br.com.ichef.model.Empresa;
import br.com.ichef.model.Entregador;
import br.com.ichef.model.EntregadorLocalidade;
import br.com.ichef.model.FichaTecnicaPrato;
import br.com.ichef.model.FichaTecnicaPratoTipo;
import br.com.ichef.model.FormaPagamento;
import br.com.ichef.model.Pedido;
import br.com.ichef.model.TipoPrato;
import br.com.ichef.model.VwTipoPratoPreco;
import br.com.ichef.service.CardapioService;
import br.com.ichef.service.ClienteCarteiraService;
import br.com.ichef.service.ClienteService;
import br.com.ichef.service.DerivacaoService;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.EntregadorService;
import br.com.ichef.service.FormaPagamentoService;
import br.com.ichef.service.PedidoService;
import br.com.ichef.service.TipoPratoService;
import br.com.ichef.service.VwTipoPratoPrecoService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.util.JSFUtil;
import br.com.ichef.visitor.CardapioVisitor;
import br.com.ichef.visitor.PedidoVisitor;

@Named
@ViewScoped
public class PedidoOrdenacaoFinalizacaoController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoService service;

	@Inject
	private ClienteService clienteService;

	@Inject
	private FormaPagamentoService formaPagamentoService;

	@Inject
	private EntregadorService entregadorService;

	@Inject
	private EmpresaService empresaService;

	@Inject
	private CardapioService cardapioService;

	@Inject
	private DerivacaoService derivacaoService;

	@Inject
	private VwTipoPratoPrecoService vwTipoPratoPrecoService;

	@Inject
	private ClienteCarteiraService clienteCarteiraService;

	@Inject
	private TipoPratoService tipoPratoService;

	private List<Pedido> lista = new ArrayList<Pedido>();

	private List<FormaPagamento> listaFormasPagamento = new ArrayList<>();
	// private FormaPagamento formaPagamento;

	private List<Entregador> listaEntregador = new ArrayList<>();
	private Entregador entregador;
	// private Entregador entregador;

	private List<FichaTecnicaPrato> listaPratos = new ArrayList<>();
	// private FichaTecnicaPrato prato;

	private List<Derivacao> listaDerivacoes = new ArrayList<>();
	// private Derivacao derivacao;

	private List<FichaTecnicaPratoTipo> listaTiposPrato = new ArrayList<>();
	// private FichaTecnicaPratoTipo fichaTecnicaPratoTipo;

	private List<Empresa> listaEmpresas = new ArrayList<>();
	// private Empresa empresa;

	private List<Cliente> listaClientes = new ArrayList<>();
	// private Cliente cliente;

	private List<Cardapio> listaCardapio = new ArrayList<>();
	private Cardapio cardapio;

	private List<CardapioFichaPrato> listaCardapioPrato = new ArrayList<>();
	// private CardapioFichaPrato cardapioPrato;

	private List<ClienteEndereco> listaEnderecos = new ArrayList<>();
	// private ClienteEndereco endereco;

	Configuracao config = (Configuracao) JSFUtil.getSessionMapValue("configuracao");

	private List<Entregador> listaEntregadorCarregada = new ArrayList<>();

	SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatarDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private Calendar componenteDataEntrega = new Calendar();

	// private Double preco;

	private Pedido entity;

	private Long codigoCliente;

	// relatorio
	private Date dataInicial;
	private Date dataFinal;
	private Date dataEntrega;

	private boolean entregaDataCardapio;

	private boolean listarApenasNaoConfirmados;

	@PostConstruct
	public void init() {

		setEntregaDataCardapio(true);
		setDataEntrega(new Date());
		setEntregador(null);

		obterListas();

		// obterEntregasDia();

	}

	public void filtrarPedidos() {

		if (getDataEntrega() == null) {
			facesMessager.error("Informe a data de Entrega para continuar");
			return;
		}

		if (getEntregador() == null && getCodigoCliente() == null) {
			facesMessager.error("Informe um dos par�metros para continuar");
			return;
		}
		obterEntregasDia();
	}

	public void obterEntregasDia() {

		obterEntrega(null);

	}

	private void obterEntrega(Date data) {

		if (data != null) {
			setDataEntrega(data);
		}
		Pedido filter = new Pedido();
		filter.setEmpresa(userLogado.getEmpresaLogada());

		PedidoVisitor pedidoVisitor = new PedidoVisitor();
		pedidoVisitor.setDataEntrega(getDataEntrega());

		if (listarApenasNaoConfirmados) {
			filter.setConfirmado(false);
		}

		if (getEntregador() != null) {
			filter.setEntregador(getEntregador());
		}

		if (getCodigoCliente() != null) {
			pedidoVisitor.setCodigoCliente(getCodigoCliente());
		}

		try {
			setLista(service.findByParameters(filter, pedidoVisitor));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void obterListas() {
		listaFormasPagamento = formaPagamentoService.listAll(true);

		Entregador filter = new Entregador();
		filter.setAtivo("S");
		filter.setEmpresa(userLogado.getEmpresaLogada());

		try {
			listaEntregadorCarregada = entregadorService.findByParameters(filter);
			listaEntregador = getListaEntregadorCarregada();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void obterPratosCardapio() {
		getEntity().setTipoPrato(null);
		getEntity().setCardapioFichaPrato(null);

		cardapio = getEntity().getCardapio();
		listaCardapioPrato = cardapio.getPratos();

		if (isEntregaDataCardapio())
			getEntity().setDataEntrega(cardapio.getData());

	}

	public Cardapio getCardapioDia() {
		CardapioVisitor cardapioVisitor = new CardapioVisitor();
		cardapioVisitor.setDataCardapio(new Date());
		Cardapio filter = new Cardapio();
		filter.setAtivo("S");

		try {
			List<Cardapio> CardHoje = (cardapioService.findByParameters(filter, cardapioVisitor));
			if (CardHoje != null && CardHoje.size() > 0) {
				cardapio = CardHoje.get(0);
				return cardapio;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public void alterarEntregador() {
		getEntity().setOrdemEntrega(1);
		ObterValorDiariaEntregador();

	}

	public void ObterValorDiariaEntregador() {
		if (getEntity().getEntregador() != null) {
			getEntity().setValorDiariaEntregador(getEntity().getEntregador().getValorDiaria());
		}
	}

	public void obterEntregador() {

		if (getEntity().getClienteEndereco() != null) {

			Entregador filter = new Entregador();
			filter.setAtivo("S");
			filter.setEmpresa(userLogado.getEmpresaLogada());

			try {
				listaEntregador = entregadorService.findByParameters(filter);
				for (Entregador entregador : listaEntregador) {
					for (EntregadorLocalidade entregadorLocalidade : entregador.getLocalidades()) {
						if (entregadorLocalidade.getLocalidade().getId()
								.equals(getEntity().getClienteEndereco().getLocalidade().getId())) {
							getEntity().setEntregador(entregador);
							getEntity().setOrdemEntrega(Integer.parseInt(entregadorLocalidade.getOrdem().toString()));
						}
					}
				}

				if (getEntity().getEntregador() == null) {
					getEntity().setOrdemEntrega(1);
					facesMessager.error("Localidade n�o associada a um entregador, por favor, revisar o cadastro");
				}

				ObterValorDiariaEntregador();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public BigDecimal obterValorPratoBytipo() {
		try {
			VwTipoPratoPreco filterPrecoPrato = new VwTipoPratoPreco();
			TipoPrato tp = new TipoPrato();
			tp.setId(getEntity().getFichaTecnicaPratoTipo().getTipoPrato().getId());
			filterPrecoPrato.setTipoPrato(tp);
			return ((List<VwTipoPratoPreco>) vwTipoPratoPrecoService.findByParameters(filterPrecoPrato)).get(0)
					.getPreco();

		} catch (Exception e) {
			e.printStackTrace();
			facesMessager.error("Nenhum Valor encontrado");
		}
		return null;

	}

	public void obterPrecoPrato() {
		try {
			if (getEntity().getFormaPagamento() != null) {
				if (!getEntity().getFormaPagamento().isCortesia()) {
					if (getEntity().getFichaTecnicaPratoTipo() != null) {

						getEntity().setValorUnitarioPedido(obterValorPratoBytipo());
					}
				} else {
					getEntity().setValorUnitarioPedido(new BigDecimal(0));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mudarDataPrato() {
		if (componenteDataEntrega != null) {
			getEntity().setDataEntrega((Date) componenteDataEntrega.getValue());
		}
		obterTiposDePratos();

	}

	public void obterTiposDePratos() {
		if (getEntity().getCardapioFichaPrato() != null) {
			if (getEntity().getCardapioFichaPrato().getFichaTecnicaPrato().getFichaTecnicaPratoTipos().size() == 1) {
				getEntity().setFichaTecnicaPratoTipo(
						getEntity().getCardapioFichaPrato().getFichaTecnicaPrato().getFichaTecnicaPratoTipos().get(0));
				obterPrecoPrato();
			} else {
				// obter pelo tipo principal
				Configuracao config = (Configuracao) JSFUtil.getSessionMapValue("configuracao");
				for (FichaTecnicaPratoTipo fichaTipo : getEntity().getCardapioFichaPrato().getFichaTecnicaPrato()
						.getFichaTecnicaPratoTipos()) {
					if (!formatarData.format(getEntity().getDataEntrega())
							.equals(formatarData.format(getEntity().getCardapio().getData()))) {
						if ((TipoPrato.TIPO_PRATO_CONGELADO) == (Long) fichaTipo.getTipoPrato().getId()) {
							getEntity().setFichaTecnicaPratoTipo(fichaTipo);
							getEntity().setTipoPrato(fichaTipo.getTipoPrato());
							obterPrecoPrato();
							return;
						}
					}
					if (config.getTipoPrato().getId().equals(fichaTipo.getTipoPrato().getId())) {
						getEntity().setFichaTecnicaPratoTipo(fichaTipo);
						getEntity().setTipoPrato(fichaTipo.getTipoPrato());
						obterPrecoPrato();
						return;
					} else {
						getEntity().setFichaTecnicaPratoTipo(null);
					}
				}

			}
		}
	}

	public Long getQuantidadeDisponivel() {
		try {
			CardapioFichaPratoEmpresa fichaEmpresaLoga = null;
			int totalPedido;
			if (getEntity().getCardapioFichaPrato() != null) {
				fichaEmpresaLoga = obterFichaEmpresaLogada();

				totalPedido = service.findTotalPedidoPrato(entity);

				return (long) (fichaEmpresaLoga.getQuantidade() - totalPedido);

			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return 0l;

	}

	private CardapioFichaPratoEmpresa obterFichaEmpresaLogada() {
		CardapioFichaPratoEmpresa fichaEmpresaLoga = null;
		for (CardapioFichaPratoEmpresa fichaEmpresa : getEntity().getCardapioFichaPrato().getFichaPratoEmpresa()) {
			if (fichaEmpresa.getEmpresa().getId().equals(userLogado.getEmpresaLogada().getId())) {
				fichaEmpresaLoga = fichaEmpresa;
			}
		}
		return fichaEmpresaLoga;
	}

	public void obterCliente() {
		Cliente cliente = null;
		if (getCodigoCliente() != null) {
			cliente = (Cliente) clienteService.getById(getCodigoCliente());
			if (cliente == null) {
				facesMessager.error("Nenhum cliente encontrado");
				return;
			} else {
				getEntity().setCliente(cliente);
				obterEnderecoCliente();
			}
		}
	}

	public void obterEnderecoCliente() {
		getEntity().setClienteEndereco(null);
		listaEnderecos = new ArrayList<>();
		if (!getEntity().getCliente().isDesabilitado()) {
			if (getEntity().getCliente() != null) {
				for (ClienteEndereco endCliente : getEntity().getCliente().getEnderecos()) {
					if (endCliente.getLocalidade().getEmpresa().getId().equals(userLogado.getEmpresaLogada().getId()))
						listaEnderecos.add(endCliente);
					if (endCliente.isEndPrincipal() && endCliente.getLocalidade().getEmpresa().getId()
							.equals(userLogado.getEmpresaLogada().getId()))
						getEntity().setClienteEndereco(endCliente);
				}
			}

			if (getEntity().getClienteEndereco() == null && listaEnderecos.size() == 1) {
				getEntity().setClienteEndereco(listaEnderecos.get(0));

			} else if (listaEnderecos.size() < 1) {
				getEntity().setClienteEndereco(null);
				if (listaEnderecos == null || listaEnderecos.size() == 0) {
					facesMessager.error("Nenhum endere�o cadastradado para essa empresa, revise o cadastro do cliente");
				}
			}

			obterEntregador();

		} else {
			getEntity().setClienteEndereco(null);
			getEntity().setCliente(null);
			facesMessager.error("Cliente Inativo/Bloqueado");
		}
	}

	public void atualizarPedido(Pedido pedido, String tipoAlteracao) {
		try {
			//System.out.println("atualizarPedido1 " + formatarDataHora.format(new Date()));

			if (tipoAlteracao.equals("E")) {
				if (pedido.getEntregador() != null) {
					if (pedido.getEntregador().getValorDiaria() == null) {
						facesMessager.error("Entregador sem di�ria cadastrada");
						return;
					}
					pedido.setValorDiariaEntregador(pedido.getEntregador().getValorDiaria());
				}
			} else if (tipoAlteracao.equals("F") || tipoAlteracao.equals("Q")) {
				//System.out.println("atualizarPedido1.1 " + formatarDataHora.format(new Date()));
				if (pedido.getValorUnitarioPedido() == null) {
					facesMessager.error(getRequiredMessage("Pre�o Unit�rio"));
					System.out.println("atualizarPedido1.2 " + formatarDataHora.format(new Date()));
					return;

				} else {
					System.out.println("atualizarPedido1.3 " + formatarDataHora.format(new Date()));

					BigDecimal valorTotalPedido = pedido.getValorUnitarioPedido()
							.multiply(new BigDecimal(pedido.getQuantidade()));
					pedido.setValorPedido(valorTotalPedido);
					if (tipoAlteracao.equals("Q")) {
						pedido.setValorPago(valorTotalPedido);
					}
					//System.out.println("atualizarPedido1.4 " + formatarDataHora.format(new Date()));

				}
			}

			//System.out.println("atualizarPedido2 " + formatarDataHora.format(new Date()));

			pedido.setDataAlteracao(new Date());
			pedido.setUsuarioAlteracao(userLogado);

			if (tipoAlteracao.equals("F")) {
				service.atualizarFormaPagamentoValorPago(pedido);
			} else {

				service.saveOrUpdade(pedido);
			}

			//System.out.println("atualizarPedido3 " + formatarDataHora.format(new Date()));

		} catch (Exception e) {
			facesMessager.error("N�o foi poss�vel executar essa opera��o:" + e.getMessage());
			e.printStackTrace();
		}

	}

	public void imprimirRotaDia() {
		setDataInicial(new Date());
		imprimirRota();

	}

	public void imprimirRota() {
		setDataFinal(getDataInicial());
		if (getDataInicial() == null || getDataFinal() == null) {
			FacesUtil.addInfoMessage(getRequiredMessage("Data"));
			return;
		} else {

			try {
				obterEntregasDia();
			} catch (Exception e) {
				e.printStackTrace();
				FacesUtil.addErroMessage("Erro ao obter os dados do relat�rio");
			}

			if (getLista().size() == 0) {
				FacesUtil.addErroMessage("Nenhum dado encontrado");
			} else {
				try {
					setParametroReport(REPORT_PARAM_LOGO, getImagem(LOGO));
					escreveRelatorioPDF("Rota", true, getLista());
				} catch (Exception e) {
					e.printStackTrace();
					FacesUtil.addErroMessage("Erro ao gerar o relat�rio");
				}
			}
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

			Pedido filter = new Pedido();
			filter.setCardapio(cardapioFilter);
			filter.setEmpresa(userLogado.getEmpresaLogada());

			PedidoVisitor pedidoVisitor = new PedidoVisitor();
			pedidoVisitor.setDataEntregaInicial(getDataInicial());
			pedidoVisitor.setDataEntregaFinal(getDataFinal());

			List<Pedido> pedidos = new ArrayList<>();

			try {
				pedidos = service.findByParameters(filter, pedidoVisitor);

			} catch (Exception e) {
				e.printStackTrace();
				FacesUtil.addErroMessage("Erro ao obter os dados do relat�rio");
			}

			if (pedidos.size() == 0) {
				FacesUtil.addErroMessage("Nenhum dado encontrado");
			} else {
				try {
					setParametroReport(REPORT_PARAM_LOGO, getImagem(LOGO));
					escreveRelatorioPDF("EtiquetaEntrega", true, pedidos);
				} catch (Exception e) {
					e.printStackTrace();
					FacesUtil.addErroMessage("Erro ao gerar o relat�rio");
				}
			}

		}

	}

	private Pedido criarFiltroPedidosFinalizar() {

		Pedido filter = new Pedido();
		filter.setEmpresa(userLogado.getEmpresaLogada());

		filter.setDataEntrega(getDataEntrega());

		filter.setConfirmado(false);

		if (getEntregador() != null) {
			filter.setEntregador(getEntregador());
		}

		return filter;
	}

	private List<Pedido> obterPedidosFinalizacao() {

		Pedido filter = new Pedido();
		filter.setEmpresa(userLogado.getEmpresaLogada());

		PedidoVisitor pedidoVisitor = new PedidoVisitor();
		pedidoVisitor.setDataEntrega(getDataEntrega());

		filter.setConfirmado(false);

		if (getEntregador() != null) {
			filter.setEntregador(getEntregador());
		}

		try {
			return service.findByParameters(filter, pedidoVisitor);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * public void finalizarListaPedidos() { try {
	 * 
	 * if (getDataEntrega() == null) {
	 * facesMessager.error("Informe a data de Entrega para continuar"); return; }
	 * 
	 * if (getEntregador() == null) {
	 * facesMessager.error("Selecione o Entregador para finalizar"); return; }
	 * 
	 * List<Pedido> pedidosFinalizar = getLista();
	 * 
	 * int countPedidoConfirmado = 0;
	 * 
	 * for (Pedido pedido : pedidosFinalizar) { String log = null;
	 * 
	 * if (!pedido.isConfirmado()) { try {
	 * 
	 * pedido.setUsuarioFinalizacao(userLogado); pedido.setDataFinalizacao(new
	 * Date()); pedido.setSnConfirmado("S");
	 * 
	 * countPedidoConfirmado++;
	 * 
	 * // service.finalizarPedido(pedido); service.saveOrUpdade(pedido); } catch
	 * (Exception e) { // pedido.setSnConfirmado("N"); //
	 * pedido.setLogLancamentoCarteira(e.getMessage()); //
	 * service.finalizarPedido(pedido); e.printStackTrace(); return; } }
	 * 
	 * }
	 * 
	 * if (countPedidoConfirmado > 0) { obterEntregasDia(); }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } }
	 */

	public void finalizarListaPedidos() {
		try {

			if (getDataEntrega() == null) {
				facesMessager.error("Informe a data de Entrega para continuar");
				return;
			}

			if (getEntregador() == null) {
				facesMessager.error("Selecione o Entregador para finalizar");
				return;
			}
			
			String listaPedidos = service.findPedidosFinalizacao(criarFiltroPedidosFinalizar());
			
			if(listaPedidos!=null) {
				service.finalizarPedido(listaPedidos, getUserLogado());
				obterEntregasDia();
			}
			
			
			/*List<Pedido> pedidosFinalizar = obterPedidosFinalizacao();

			int countPedidoConfirmado = 0;

			for (Pedido pedido : pedidosFinalizar) {
				String log = null;

				if (!pedido.isConfirmado()) {
					try {

						// if (pedido.getFormaPagamento().isCarteira()) {
						ClienteCarteira carteira = new ClienteCarteira();
						carteira.setCardapio(pedido.getCardapio());
						carteira.setCliente(pedido.getCliente());
						carteira.setData(pedido.getDataEntrega());
						carteira.setDataCadastrado(new Date());
						carteira.setDerivacao(pedido.getDerivacao());
						carteira.setDescricao("Pedido REALIZADO em: " + formatarData.format(pedido.getDataCadastro()));
						carteira.setEmpresa(pedido.getEmpresa());
						carteira.setEmpresaLogada(userLogado.getEmpresaLogada());
						carteira.setFichaTecnicaPrato(pedido.getCardapioFichaPrato().getFichaTecnicaPrato());
						carteira.setFormaPagamento(pedido.getFormaPagamento());
						carteira.setTipoCarteira("P");
						carteira.setTipoPrato(pedido.getTipoPrato());
						carteira.setUsuarioCadastro(userLogado);
						carteira.setValorDevido(pedido.getValorPedido());
						if (!pedido.getFormaPagamento().isCarteira() && !pedido.getFormaPagamento().isCortesia()) {
							carteira.setValorPago(pedido.getValorPago());
						}
						carteira.setPedido(pedido);
						log = "Lan�amento Efetuado em Carteira";
						pedido.setLogLancamentoCarteira(log);

						pedido.setUsuarioFinalizacao(userLogado);
						pedido.setDataFinalizacao(new Date());
						pedido.setSnConfirmado("S");

						countPedidoConfirmado++;

						clienteCarteiraService.saveOrUpdade(carteira);
					//	service.finalizarPedido(pedido);
					} catch (Exception e) {
						//pedido.setSnConfirmado("N");
						//pedido.setLogLancamentoCarteira(e.getMessage());
						//service.finalizarPedido(pedido);
						e.printStackTrace();
						return;
					}
				}

			}

			if (countPedidoConfirmado > 0) {
				obterEntregasDia();
			}*/

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void atualizarTodosEntregadores(Pedido pedido) {

		try {

			if (pedido.getEntregador() != null) {
				if (pedido.getEntregador().getValorDiaria() == null) {
					facesMessager.error("Entregador sem di�ria cadastrada");
					return;
				}
				List<Pedido> listaPedidosEntregador = obterPedidosDoEntregador(pedido.getEntregador());
				BigDecimal valorDiariaEntregador = pedido.getEntregador().getValorDiaria();

				for (Pedido pedidoEntregador : listaPedidosEntregador) {
					pedidoEntregador.setEntregador(pedido.getEntregador());
					pedidoEntregador.setValorDiariaEntregador(valorDiariaEntregador);
					pedidoEntregador.setDataAlteracao(new Date());
					pedidoEntregador.setUsuarioAlteracao(userLogado);
					service.saveOrUpdade(pedidoEntregador);

				}

			}

		} catch (Exception e) {
			facesMessager.error("N�o foi poss�vel executar essa opera��o:" + e.getMessage());
			e.printStackTrace();
		}

	}

	public List<Pedido> obterPedidosDoEntregador(Entregador entregador) {

		Pedido filter = new Pedido();
		filter.setEmpresa(userLogado.getEmpresaLogada());
		filter.setEntregador(entregador);

		PedidoVisitor pedidoVisitor = new PedidoVisitor();
		pedidoVisitor.setDataEntrega(new Date());

		try {
			return service.findByParameters(filter, pedidoVisitor);
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
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

	public TipoPratoService getTipoPratoService() {
		return tipoPratoService;
	}

	public void setTipoPratoService(TipoPratoService tipoPratoService) {
		this.tipoPratoService = tipoPratoService;
	}

	public List<Pedido> getLista() {
		return lista;
	}

	public void setLista(List<Pedido> lista) {
		this.lista = lista;
	}

	public List<FormaPagamento> getListaFormasPagamento() {
		return listaFormasPagamento;
	}

	public void setListaFormasPagamento(List<FormaPagamento> listaFormasPagamento) {
		this.listaFormasPagamento = listaFormasPagamento;
	}

	public List<FichaTecnicaPrato> getListaPratos() {
		return listaPratos;
	}

	public void setListaPratos(List<FichaTecnicaPrato> listaPratos) {
		this.listaPratos = listaPratos;
	}

	public List<Derivacao> getListaDerivacoes() {
		return listaDerivacoes;
	}

	public void setListaDerivacoes(List<Derivacao> listaDerivacoes) {
		this.listaDerivacoes = listaDerivacoes;
	}

	public List<FichaTecnicaPratoTipo> getListaTiposPrato() {
		return listaTiposPrato;
	}

	public void setListaTiposPrato(List<FichaTecnicaPratoTipo> listaTiposPrato) {
		this.listaTiposPrato = listaTiposPrato;
	}

	public List<Empresa> getListaEmpresas() {
		return listaEmpresas;
	}

	public void setListaEmpresas(List<Empresa> listaEmpresas) {
		this.listaEmpresas = listaEmpresas;
	}

	public List<Cliente> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
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

	public List<Cardapio> getListaCardapio() {
		return listaCardapio;
	}

	public void setListaCardapio(List<Cardapio> listaCardapio) {
		this.listaCardapio = listaCardapio;
	}

	public Cardapio getCardapio() {
		return cardapio;
	}

	public void setCardapio(Cardapio cardapio) {
		this.cardapio = cardapio;
	}

	public List<CardapioFichaPrato> getListaCardapioPrato() {
		return listaCardapioPrato;
	}

	public void setListaCardapioPrato(List<CardapioFichaPrato> listaCardapioPrato) {
		this.listaCardapioPrato = listaCardapioPrato;
	}

	public List<ClienteEndereco> getListaEnderecos() {
		return listaEnderecos;
	}

	public void setListaEnderecos(List<ClienteEndereco> listaEnderecos) {
		this.listaEnderecos = listaEnderecos;
	}

	// public Double getPreco() {
	// return preco;
	// }

	// public void setPreco(Double preco) {
	// this.preco = preco;
	// }

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

	public EntregadorService getEntregadorService() {
		return entregadorService;
	}

	public void setEntregadorService(EntregadorService entregadorService) {
		this.entregadorService = entregadorService;
	}

	public ClienteCarteiraService getClienteCarteiraService() {
		return clienteCarteiraService;
	}

	public void setClienteCarteiraService(ClienteCarteiraService clienteCarteiraService) {
		this.clienteCarteiraService = clienteCarteiraService;
	}

	public Configuracao getConfig() {
		return config;
	}

	public void setConfig(Configuracao config) {
		this.config = config;
	}

	public SimpleDateFormat getFormatarData() {
		return formatarData;
	}

	public void setFormatarData(SimpleDateFormat formatarData) {
		this.formatarData = formatarData;
	}

	public boolean isListarApenasNaoConfirmados() {
		return listarApenasNaoConfirmados;
	}

	public void setListarApenasNaoConfirmados(boolean listarApenasNaoConfirmados) {
		this.listarApenasNaoConfirmados = listarApenasNaoConfirmados;
	}

}
