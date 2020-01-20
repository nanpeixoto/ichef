package br.com.ichef.controler;

import java.math.BigDecimal;
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
import br.com.ichef.model.CardapioFichaPrato;
import br.com.ichef.model.CardapioFichaPratoEmpresa;
import br.com.ichef.model.Cliente;
import br.com.ichef.model.ClienteCarteira;
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
import br.com.ichef.service.CardapioService;
import br.com.ichef.service.ClienteCarteiraService;
import br.com.ichef.service.ClienteService;
import br.com.ichef.service.DerivacaoService;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.EntregadorService;
import br.com.ichef.service.FormaPagamentoService;
import br.com.ichef.service.PedidoService;
import br.com.ichef.service.TipoPratoService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.util.JSFUtil;
import br.com.ichef.visitor.CardapioVisitor;
import br.com.ichef.visitor.ClienteVisitor;
import br.com.ichef.visitor.PedidoVisitor;

@Named
@ViewScoped
public class PedidoController extends BaseController {

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
	private ClienteCarteiraService clienteCarteiraService;

	@Inject
	private TipoPratoService tipoPratoService;

	private List<Pedido> lista = new ArrayList<Pedido>();

	private List<FormaPagamento> listaFormasPagamento = new ArrayList<>();
	// private FormaPagamento formaPagamento;

	private List<Entregador> listaEntregador = new ArrayList<>();
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

	private Calendar componenteDataEntrega = new Calendar();

	// private Double preco;

	private Pedido entity;

	private Long codigoCliente;

	// relatorio
	private Date dataInicial;
	private Date dataFinal;
	private Date dataEntrega;

	private boolean entregaDataCardapio;

	@PostConstruct
	public void init() {

		obterListas();

		newInstance();

		setEntregaDataCardapio(true);
		setDataEntrega(new Date());

	}

	private void newInstance() {
		setEntity(new Pedido());
		getEntity().setDataEntrega(new Date());
		getEntity().setConfirmado(false);

		valoresDefault();

	}

	private void valoresDefault() {
		getEntity().setQuantidade(1);
		if (listaCardapio != null && listaCardapio.size() > 0) {
			getEntity().setCardapio(listaCardapio.get(0));
			listaCardapioPrato = listaCardapio.get(0).getPratos();
		} else {
			FacesUtil.addErroMessage("Nenhum Cardapio encontrado, cadastre um cardapio para continuar");

		}
		getEntity().setFormaPagamento(config.getFormaPagamento());
		getEntity().setDerivacao(config.getDerivacao());
		getEntity().setEmpresa(userLogado.getEmpresaLogada());
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

	private void obterListas() {
		listaClientes = clienteService.listAll();

		listaFormasPagamento = formaPagamentoService.listAll(true);
		listaEmpresas = empresaService.listAll(true);
		listaDerivacoes = derivacaoService.listAll(true);

		Cardapio cardapiofilter = new Cardapio();
		cardapiofilter.setAtivo("S");
		CardapioVisitor cardapioVisitor = new CardapioVisitor();
		cardapioVisitor.setDataCardapioPossiveis(new Date());

		Entregador filter = new Entregador();
		filter.setAtivo("S");
		filter.setEmpresa(userLogado.getEmpresaLogada());

		try {
			listaCardapio = cardapioService.findByParameters(cardapiofilter, cardapioVisitor);
			listaEntregadorCarregada = entregadorService.findByParameters(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<Cliente> autoCompleteCliente(String query) {
		List<Cliente> allThemes = new ArrayList<>();

		ClienteVisitor visitor = new ClienteVisitor();
		visitor.setLikeNomeTelefone(query);

		try {
			allThemes = clienteService.findByParameters(new Cliente(), visitor);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return allThemes;

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

	public String getDataHojeFormatada() {

		return formatarData.format(new Date());
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
					facesMessager.error("Localidade não associada a um entregador, por favor, revisar o cadastro");
				}

				ObterValorDiariaEntregador();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void obterPrecoPrato() {
		try {
			if (getEntity().getFormaPagamento() != null) {
				if (!getEntity().getFormaPagamento().isCortesia()) {
					if (getEntity().getFichaTecnicaPratoTipo() != null) {
						getEntity().setValorPedido(new BigDecimal(getEntity().getFichaTecnicaPratoTipo().getTipoPrato()
								.getUltimoPreco().get(0).getPreco()));
					}
				} else {
					getEntity().setValorPedido(new BigDecimal(0));
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
					facesMessager.error("Nenhum endereço cadastradado para essa empresa, revise o cadastro do cliente");
				}
			}

			obterEntregador();

		} else {
			getEntity().setClienteEndereco(null);
			getEntity().setCliente(null);
			facesMessager.error("Cliente Inativo/Bloqueado");
		}
	}

	public void adicionarPedido(Boolean apagarCliente) {
		try {

			// CAMPOS OBRIGATORIOS
			getEntity().setConfirmado(false);

			// CARDAPIO
			if (getEntity().getCardapio() == null) {
				facesMessager.error("Cardápio não encontrado, verifique o cadastro do cardápio");
				return;
			}

			// CLIENTE
			if (getEntity().getCliente() == null) {
				facesMessager.error(getRequiredMessage("Cliente"));
				return;
			}

			// ENDERECO
			if (getEntity().getClienteEndereco() == null) {
				facesMessager.error(getRequiredMessage("Endereço"));
				return;
			} else {
				getEntity().setLocalidade(getEntity().getClienteEndereco().getLocalidade());
			}

			// PRATO
			if (getEntity().getCardapioFichaPrato() == null) {
				facesMessager.error(getRequiredMessage("Prato"));
				return;
			}

			// QTD E MAIOR QUE 1
			if (getEntity().getQuantidade() == null || getEntity().getQuantidade() < 0) {
				facesMessager.error(getRequiredMessage("Quantidade"));
				return;
			}

			// TIPO DE PRATO
			if (getEntity().getFichaTecnicaPratoTipo() == null) {
				facesMessager.error(getRequiredMessage("Tipo de Prato"));
				return;
			} else {
				getEntity().setTipoPrato(getEntity().getFichaTecnicaPratoTipo().getTipoPrato());
			}

			// PRECO MAIOR QUE ZERO
			if (getEntity().getValorPedido() == null) {
				facesMessager.error(getRequiredMessage("Preço"));
				return;
			} else {
				getEntity().setValorPedido(
						getEntity().getValorPedido().multiply(new BigDecimal(getEntity().getQuantidade())));
				getEntity().setValorPago(getEntity().getValorPedido());
			}

			// valor da diaria do entregador
			if (getEntity().getValorDiariaEntregador() == null) {
				facesMessager.error(getRequiredMessage("Valor da Diária"));
				return;
			}

			// DERIVACAO
			if (getEntity().getDerivacao() == null) {
				facesMessager.error(getRequiredMessage("Derivação"));
				return;
			}

			// FORMA PAGAMENTO
			if (getEntity().getFormaPagamento() == null) {
				facesMessager.error(getRequiredMessage("Forma de Pagamento"));
				return;
			}

			// ENTREGADOR
			if (getEntity().getEntregador() == null) {
				facesMessager.error(getRequiredMessage("Entregador"));
				return;
			}

			// ORDEM
			if (getEntity().getOrdemEntrega() == null) {
				facesMessager.error(getRequiredMessage("Ordem"));
				return;
			}

			// VERFICO A QUANTIDADE
			Long quantidadeJaPedida = getQuantidadeDisponivel();
			CardapioFichaPratoEmpresa fichaEmpresaLogada = obterFichaEmpresaLogada();

			if (!fichaEmpresaLogada.isPodeVenderAcimaDoLimite()
					&& (quantidadeJaPedida - getEntity().getQuantidade()) < 0) {
				facesMessager.error("Quantidade disponível menor que a quantidade solicitada");
				return;
			}

			if ((quantidadeJaPedida - getEntity().getQuantidade()) <= 5) {
				FacesUtil.addInfoMessage(
						"Quantdade disponível para o prato:" + (quantidadeJaPedida - getEntity().getQuantidade()));
			}

			getEntity().setDataCadastro(new Date());
			getEntity().setUsuarioCadastro(userLogado);

			// setar os precos de venda e custo
			getEntity().setPrecoCustoPorcao(
					getEntity().getCardapioFichaPrato().getFichaTecnicaPrato().getPrecoCustoPorcao());
			getEntity().setPrecoVendaReceita(
					getEntity().getCardapioFichaPrato().getFichaTecnicaPrato().getPrecoVendaReceita());
			getEntity().setPrecoVendaTipoPrato(getEntity().getCardapioFichaPrato().getFichaTecnicaPrato()
					.getPercoPorTipoPrato(getEntity().getTipoPrato()));

			if (getEntity().getPrecoCustoPorcao() == null) {
				facesMessager.error("Não foi possível obter o Preço de Custo da Porção");
				return;
			}

			if (getEntity().getPrecoVendaReceita() == null) {
				facesMessager.error("Não foi possível obter o Preço de Venda da Receita");
				return;
			}

			if (getEntity().getPrecoVendaTipoPrato() == null) {
				facesMessager.error("Não foi possível obter o Preço de Venda por Tipo de Prato");
				return;
			}

			if (getEntity().getFormaPagamento().isCortesia()) {
				getEntity().setValorPedido(new BigDecimal(0));
			}

			service.saveOrUpdade(getEntity());

			if (getEntity().getId() == null) {
				facesMessager.error("Houve um erro, entre em contato com o adminstrador do sistema");
			} else {
				getLista().add(getEntity());
				orderbyId(getLista());;
			}

			updateComponentes("tabListaPedidos");

			limparPedido();

			if (apagarCliente)
				limparCliente();

		} catch (

		Exception e) {
			facesMessager.error("Não foi possível executar essa operação");
			e.printStackTrace();
		}

	}

	private void limparCliente() {
		newInstance();
		setCodigoCliente(null);

	}

	private void limparPedido() {
		ClienteEndereco enderecoAtual = getEntity().getClienteEndereco();
		Cliente clienteAtual = getEntity().getCliente();
		Long codigoClienteAtual = getCodigoCliente();
		Entregador entregadorAtual = getEntity().getEntregador();
		BigDecimal valorDiariaEntregadorAtual = getEntity().getValorDiariaEntregador();
		Integer ordemEntregaAtual = getEntity().getOrdemEntrega();

		newInstance();
		setCodigoCliente(null);

		getEntity().setClienteEndereco(enderecoAtual);
		getEntity().setCliente(clienteAtual);
		setCodigoCliente(codigoClienteAtual);
		getEntity().setEntregador(entregadorAtual);
		getEntity().setValorDiariaEntregador(valorDiariaEntregadorAtual);
		getEntity().setOrdemEntrega(ordemEntregaAtual);
	}

	public void excluirItem(Pedido itemExcluir) {
		try {
			List<Pedido> temp = new ArrayList<Pedido>();
			temp.addAll(lista);
			for (Pedido item : lista) {
				if (itemExcluir.getId().equals(item.getId()))
					temp.remove(item);
			}
			service.excluir(itemExcluir);

			lista.clear();
			lista.addAll(temp);
			// service.calcularPercos(entity, configuracao);
			updateComponentes("tabListaPedidos");
			FacesUtil.addInfoMessage("Itens excluídos com sucesso");
		} catch (Exception e) {
			FacesUtil.addErroMessage("Não foi possível executar essa operação:" + e.getMessage());
		}

	}

	public void atualizarPedido(Pedido pedido, String tipoAlteracao) {
		try {
			if (tipoAlteracao.equals("E")) {
				if (pedido.getEntregador() != null) {
					if (pedido.getEntregador().getValorDiaria() == null) {
						facesMessager.error("Entregador sem diária cadastrada");
						return;
					}
					pedido.setValorDiariaEntregador(pedido.getEntregador().getValorDiaria());
				}
			}

			if (tipoAlteracao.equals("F")) {
				obterPrecoPrato();
			}
			pedido.setDataAlteracao(new Date());
			pedido.setUsuarioAlteracao(userLogado);

			service.saveOrUpdade(pedido);

		} catch (Exception e) {
			facesMessager.error("Não foi possível executar essa operação:" + e.getMessage());
			e.printStackTrace();
		}

	}

	public void obterPedidoDia() {

		Cardapio cardapioFilter = new Cardapio();
		cardapioFilter.setAtivo("S");

		Pedido filter = new Pedido();
		filter.setCardapio(cardapioFilter);
		filter.setEmpresa(userLogado.getEmpresaLogada());

		PedidoVisitor pedidoVisitor = new PedidoVisitor();
		pedidoVisitor.setData(new Date());
		pedidoVisitor.setDataCardapio(new Date());

		try {
			setLista(service.findByParameters(filter, pedidoVisitor) );
			orderbyId(getLista());
			// setLista(service.findByParameters(filter));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void orderbyId(List<Pedido> itens) {

		Collections.sort(itens, new Comparator() {

			public int compare(Object o1, Object o2) {

				Long x1 = (Long) ((Pedido) o1).getId();
				Long x2 = (Long) ((Pedido) o2).getId();
				int sComp = x2.compareTo(x1);
				return sComp;

			}
		});
	}

	public void obterCardapioPedidosDia() {
		Cardapio cardapioFilter = new Cardapio();
		cardapioFilter.setAtivo("S");

		Pedido filter = new Pedido();
		filter.setCardapio(cardapioFilter);
		filter.setEmpresa(userLogado.getEmpresaLogada());

		PedidoVisitor pedidoVisitor = new PedidoVisitor();
		// pedidoVisitor.setData(new Date());
		pedidoVisitor.setDataCardapio(new Date());

		try {
			setLista(service.findByParameters(filter, pedidoVisitor));
			// setLista(service.findByParameters(filter));
		} catch (Exception e) {
			e.printStackTrace();
		}
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

		try {
			setLista(service.findByParameters(filter, pedidoVisitor));
			// setLista(service.findByParameters(filter));
			order(getLista());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void order(List<Pedido> persons) {

		Collections.sort(persons, new Comparator() {

			public int compare(Object o1, Object o2) {

				String x1 = ((Pedido) o1).getEntregador().getNome();
				String x2 = ((Pedido) o2).getEntregador().getNome();
				int sComp = x1.compareTo(x2);

				if (sComp != 0) {
					return sComp;
				}

				Integer ordem1 = ((Pedido) o1).getOrdemEntrega();
				Integer ordem2 = ((Pedido) o2).getOrdemEntrega();
				return ordem1.compareTo(ordem2);
			}
		});
	}

	public void imprimir() {
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

				order(pedidos);

			} catch (Exception e) {
				e.printStackTrace();
				FacesUtil.addErroMessage("Erro ao obter os dados do relatório");
			}

			if (pedidos.size() == 0) {
				FacesUtil.addErroMessage("Nenhum dado encontrado");
			} else {
				try {
					setParametroReport(REPORT_PARAM_LOGO, getImagem(LOGO));
					escreveRelatorioPDF("Pedidos", true, pedidos);
				} catch (Exception e) {
					e.printStackTrace();
					FacesUtil.addErroMessage("Erro ao gerar o relatório");
				}
			}

		}

	}

	public void imprimirRotaHoje() {
		setDataInicial(new Date());
		imprimirRota();

	}

	public void imprimirRota() {
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

				order(pedidos);

			} catch (Exception e) {
				e.printStackTrace();
				FacesUtil.addErroMessage("Erro ao obter os dados do relatório");
			}

			if (pedidos.size() == 0) {
				FacesUtil.addErroMessage("Nenhum dado encontrado");
			} else {
				try {
					setParametroReport(REPORT_PARAM_LOGO, getImagem(LOGO));
					escreveRelatorioPDF("Rota", true, pedidos);
				} catch (Exception e) {
					e.printStackTrace();
					FacesUtil.addErroMessage("Erro ao gerar o relatório");
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
				FacesUtil.addErroMessage("Erro ao obter os dados do relatório");
			}

			if (pedidos.size() == 0) {
				FacesUtil.addErroMessage("Nenhum dado encontrado");
			} else {
				try {
					setParametroReport(REPORT_PARAM_LOGO, getImagem(LOGO));
					escreveRelatorioPDF("EtiquetaEntrega", true, pedidos);
				} catch (Exception e) {
					e.printStackTrace();
					FacesUtil.addErroMessage("Erro ao gerar o relatório");
				}
			}

		}

	}

	public void imprimirCardapioHoje() {
		setDataFinal(new Date());
		setDataInicial(new Date());
		imprimir();

	}

	public void finalizarListaPedidos() {
		try {

			obterEntregasDia();
			// List<ClienteCarteira> listaCarteiras = new ArrayList<>();

			for (Pedido pedido : lista) {
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
						log = "Lançamento Efetuado em Carteira";
						pedido.setLogLancamentoCarteira(log);

						pedido.setUsuarioFinalizacao(userLogado);
						pedido.setDataFinalizacao(new Date());
						pedido.setSnConfirmado("S");

						clienteCarteiraService.saveOrUpdade(carteira);
						service.finalizarPedido(pedido);
					} catch (Exception e) {
						pedido.setSnConfirmado("N");
						pedido.setLogLancamentoCarteira(e.getMessage());
						service.finalizarPedido(pedido);
						e.printStackTrace();
						return;
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void atualizarTodosEntregadores(Pedido pedido) {

		try {

			if (pedido.getEntregador() != null) {
				if (pedido.getEntregador().getValorDiaria() == null) {
					facesMessager.error("Entregador sem diária cadastrada");
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
			facesMessager.error("Não foi possível executar essa operação:" + e.getMessage());
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

}
