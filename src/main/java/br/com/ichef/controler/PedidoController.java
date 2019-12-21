package br.com.ichef.controler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
import br.com.ichef.service.CardapioService;
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

	// private Double preco;

	private Pedido entity;

	private Long codigoCliente;

	// relatorio
	private Date dataInicial;
	private Date dataFinal;

	@PostConstruct
	public void init() {

		verificarCardapio();

		newInstance();

		obterListas();

		// obterPedidoDia();

	}

	private void newInstance() {
		setEntity(new Pedido());
		getEntity().setDataPedido(new Date());

		valoresDefault();

	}

	private void valoresDefault() {
		getEntity().setQuantidade(1);
		getEntity().setCardapio(cardapio);
		getEntity().setFormaPagamento(config.getFormaPagamento());
		getEntity().setDerivacao(config.getDerivacao());
		getEntity().setEmpresa(userLogado.getEmpresaLogada());
	}

	public void obterPratosCardapio() {
		getEntity().setTipoPrato(null);
		getEntity().setCardapioFichaPrato(null);

		cardapio = getEntity().getCardapio();
		listaCardapioPrato = cardapio.getPratos();

	}

	private String verificarCardapio() {

		CardapioVisitor cardapioVisitor = new CardapioVisitor();
		cardapioVisitor.setDataCardapio(new Date());
		Cardapio filter = new Cardapio();
		filter.setAtivo("S");
		try {
			cardapio = (cardapioService.findByParameters(filter, cardapioVisitor)).get(0);
			listaCardapioPrato = cardapio.getPratos();

			cardapioVisitor = new CardapioVisitor();
			cardapioVisitor.setDataCardapioPossiveis(new Date());

			listaCardapio = cardapioService.findByParameters(filter, cardapioVisitor);

		} catch (Exception e) {
			FacesUtil.addErroMessage("Nenhum Cardapio encontrado, cadastre um cardapio para continuar");
			updateComponentes("growl");
			return "cadastro-cardapio.xhtml?faces-redirect=true";
		}

		return "pedido.xhtml?faces-redirect=true";

	}

	private void obterListas() {
		listaClientes = clienteService.listAll();

		listaFormasPagamento = formaPagamentoService.listAll(true);
		listaEmpresas = empresaService.listAll(true);
		listaDerivacoes = derivacaoService.listAll(true);

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

	public void adicionarPedido(Boolean apagarCliente) {
		try {

			// CAMPOS OBRIGATORIOS

			// CARDAPIO
			if (getEntity().getCardapio() == null) {
				facesMessager.error("Card�pio n�o encontrado, verifique o cadastro do card�pio");
				return;
			}

			// CLIENTE
			if (getEntity().getCliente() == null) {
				facesMessager.error(getRequiredMessage("Cliente"));
				return;
			}

			// ENDERECO
			if (getEntity().getClienteEndereco() == null) {
				facesMessager.error(getRequiredMessage("Endere�o"));
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
				facesMessager.error(getRequiredMessage("Pre�o"));
				return;
			}

			// valor da diaria do entregador
			if (getEntity().getValorDiariaEntregador() == null) {
				facesMessager.error(getRequiredMessage("Valor da Di�ria"));
				return;
			}

			// DERIVACAO
			if (getEntity().getDerivacao() == null) {
				facesMessager.error(getRequiredMessage("Deriva��o"));
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
				facesMessager.error("Quantidade dispon�vel menor que a quantidade solicitada");
				return;
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
				facesMessager.error("N�o foi poss�vel obter o Pre�o de Custo da Por��o");
				return;
			}

			if (getEntity().getPrecoVendaReceita() == null) {
				facesMessager.error("N�o foi poss�vel obter o Pre�o de Venda da Receita");
				return;
			}

			if (getEntity().getPrecoVendaTipoPrato() == null) {
				facesMessager.error("N�o foi poss�vel obter o Pre�o de Venda por Tipo de Prato");
				return;
			}

			service.saveOrUpdade(getEntity());

			if (getEntity().getId() != null) {
				obterPedidoDia();
			} else {
				facesMessager.error("Houve um erro, entre em contato com o adminstrador do sistema");
			}

			updateComponentes("tabListaPedidos");

			limparPedido();

			if (apagarCliente)
				limparCliente();

		} catch (

		Exception e) {
			facesMessager.error("N�o foi poss�vel executar essa opera��o");
			e.printStackTrace();
		}

	}

	private void limparCliente() {
		getEntity().setCliente(null);
		getEntity().setClienteEndereco(null);
		getEntity().setEntregador(null);
		getEntity().setOrdemEntrega(null);
		getEntity().setValorDiariaEntregador(null);
		setCodigoCliente(null);

	}

	private void limparPedido() {
		getEntity().setId(null);
		getEntity().setCardapioFichaPrato(null);
		getEntity().setTipoPrato(null);
		getEntity().setFichaTecnicaPratoTipo(null);
		getEntity().setDerivacao(null);
		getEntity().setFormaPagamento(null);
		getEntity().setQuantidade(null);
		getEntity().setObservacao(null);
		valoresDefault();
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
			FacesUtil.addInfoMessage("Itens exclu�dos com sucesso");
		} catch (Exception e) {
			FacesUtil.addErroMessage("N�o foi poss�vel executar essa opera��o:" + e.getMessage());
		}

	}

	public void atualizarPedido(Pedido pedido) {
		try {
			service.saveOrUpdade(pedido);
		} catch (Exception e) {
			facesMessager.error("N�o foi poss�vel executar essa opera��o:" + e.getMessage());
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
			setLista(service.findByParameters(filter, pedidoVisitor));
			// setLista(service.findByParameters(filter));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			pedidoVisitor.setDataInicial(getDataInicial());
			pedidoVisitor.setDataFinal(getDataFinal());
			

			List<Pedido> pedidos = new ArrayList<>();

			try {
				pedidos = service.findByParameters(filter, pedidoVisitor);

			} catch (Exception e) {
				FacesUtil.addErroMessage("Erro ao obter os dados do relat�rio");
			}

			if (pedidos.size() == 0) {
				FacesUtil.addErroMessage("Nenhum dado encontrado");
			} else {
				try {
					setParametroReport(REPORT_PARAM_LOGO, getImagem(LOGO));
					escreveRelatorioPDF("Pedidos", true, pedidos);
				} catch (Exception e) {
					FacesUtil.addErroMessage("Erro ao gerar o relat�rio");
				}
			}

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

}
