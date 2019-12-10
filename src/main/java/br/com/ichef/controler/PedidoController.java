package br.com.ichef.controler;

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
import br.com.ichef.model.Cliente;
import br.com.ichef.model.ClienteEndereco;
import br.com.ichef.model.Configuracao;
import br.com.ichef.model.Derivacao;
import br.com.ichef.model.Empresa;
import br.com.ichef.model.FichaTecnicaPrato;
import br.com.ichef.model.FichaTecnicaPratoTipo;
import br.com.ichef.model.FormaPagamento;
import br.com.ichef.model.Pedido;
import br.com.ichef.service.CardapioService;
import br.com.ichef.service.ClienteService;
import br.com.ichef.service.DerivacaoService;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.FormaPagamentoService;
import br.com.ichef.service.PedidoService;
import br.com.ichef.service.TipoPratoService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.util.JSFUtil;
import br.com.ichef.visitor.CardapioVisitor;
import br.com.ichef.visitor.ClienteVisitor;

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
	private EmpresaService empresaService;

	@Inject
	private CardapioService cardapioService;

	@Inject
	private DerivacaoService derivacaoService;

	@Inject
	private TipoPratoService tipoPratoService;

	private List<Pedido> lista = new ArrayList<Pedido>();

	private List<FormaPagamento> listaFormasPagamento = new ArrayList<>();
	private FormaPagamento formaPagamento;

	private List<FichaTecnicaPrato> listaPratos = new ArrayList<>();
	private FichaTecnicaPrato prato;

	private List<Derivacao> listaDerivacoes = new ArrayList<>();
	private Derivacao derivacao;

	private List<FichaTecnicaPratoTipo> listaTiposPrato = new ArrayList<>();
	private FichaTecnicaPratoTipo fichaTecnicaPratoTipo;

	private List<Empresa> listaEmpresas = new ArrayList<>();
	private Empresa empresa;

	private List<Cliente> listaClientes = new ArrayList<>();
	private Cliente cliente;

	private List<Cardapio> listaCardapio = new ArrayList<>();
	private Cardapio cardapio;

	private List<CardapioFichaPrato> listaCardapioPrato = new ArrayList<>();
	private CardapioFichaPrato cardapioPrato;

	private List<ClienteEndereco> listaEnderecos = new ArrayList<>();
	private ClienteEndereco endereco;

	private Date data;

	private int quantidade;
	private Double preco;

	@PostConstruct
	public void init() {

		setData(new Date());
		verificarCardapio();

		setQuantidade(1);
		obterListas();

	}

	private String verificarCardapio() {

		CardapioVisitor cardapioVisitor = new CardapioVisitor();
		cardapioVisitor.setDataCardapio(getData());
		try {
			listaCardapio = cardapioService.findByParameters(new Cardapio(), cardapioVisitor);
			cardapio = listaCardapio.get(0);
			listaCardapioPrato = cardapio.getPratos();
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

		Configuracao config = (Configuracao) JSFUtil.getSessionMapValue("configuracao");
		// FORMA DE PAGAMENTO PADRAO CARTEIRA
		formaPagamento = config.getFormaPagamento();
		// DERIVACAO PADRAO
		derivacao = config.getDerivacao();
		// empresa logada
		setEmpresa(userLogado.getEmpresaLogada());
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

	public void obterPrecoPrato() {
		try {
			setPreco(getFichaTecnicaPratoTipo().getTipoPrato().getUltimoPreco().get(0).getPreco());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void obterTiposDePratos() {
		if (getCardapioPrato() != null) {
			if (getCardapioPrato().getFichaTecnicaPrato().getFichaTecnicaPratoTipos().size() == 1) {
				setFichaTecnicaPratoTipo(getCardapioPrato().getFichaTecnicaPrato().getFichaTecnicaPratoTipos().get(0));
			} else {
				// obter pelo tipo principal
				Configuracao config = (Configuracao) JSFUtil.getSessionMapValue("configuracao");
				for (FichaTecnicaPratoTipo fichaTipo : getCardapioPrato().getFichaTecnicaPrato()
						.getFichaTecnicaPratoTipos()) {
					if (config.getTipoPrato().getId().equals(fichaTipo.getTipoPrato().getId())) {
						setFichaTecnicaPratoTipo(fichaTipo);
						return;
					} else {
						setFichaTecnicaPratoTipo(null);
					}
				}

			}
		}
	}

	public void obterEnderecoCliente() {
		endereco = null;
		if (!getCliente().isDesabilitado()) {
			if (getCliente() != null) {
				for (ClienteEndereco endCliente : getCliente().getEnderecos()) {
					if (endCliente.getLocalidade().getEmpresa().getId().equals(userLogado.getEmpresaLogada().getId()))
						listaEnderecos.add(endCliente);
					if (endCliente.isEndPrincipal() && endCliente.getLocalidade().getEmpresa().getId()
							.equals(userLogado.getEmpresaLogada().getId()))
						endereco = endCliente;
				}
			}

			if (endereco == null && listaEnderecos.size() == 1) {
				endereco = listaEnderecos.get(0);
			} else if (listaEnderecos.size() < 1) {
				setEndereco(null);
				if (listaEnderecos == null || listaEnderecos.size() == 0) {
					facesMessager.error("Nenhum endereço cadastradado para essa empresa, revise o cadastro do cliente");
				}
			}
		} else {
			setEndereco(null);
			setCliente(null);
			facesMessager.error("Cliente Inativo/Bloqueado");
		}
	}

	public void adicionarCarteira() {

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

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public List<FichaTecnicaPrato> getListaPratos() {
		return listaPratos;
	}

	public void setListaPratos(List<FichaTecnicaPrato> listaPratos) {
		this.listaPratos = listaPratos;
	}

	public FichaTecnicaPrato getPrato() {
		return prato;
	}

	public void setPrato(FichaTecnicaPrato prato) {
		this.prato = prato;
	}

	public List<Derivacao> getListaDerivacoes() {
		return listaDerivacoes;
	}

	public void setListaDerivacoes(List<Derivacao> listaDerivacoes) {
		this.listaDerivacoes = listaDerivacoes;
	}

	public Derivacao getDerivacao() {
		return derivacao;
	}

	public void setDerivacao(Derivacao derivacao) {
		this.derivacao = derivacao;
	}

	public List<FichaTecnicaPratoTipo> getListaTiposPrato() {
		return listaTiposPrato;
	}

	public void setListaTiposPrato(List<FichaTecnicaPratoTipo> listaTiposPrato) {
		this.listaTiposPrato = listaTiposPrato;
	}

	public FichaTecnicaPratoTipo getFichaTecnicaPratoTipo() {
		return fichaTecnicaPratoTipo;
	}

	public void setFichaTecnicaPratoTipo(FichaTecnicaPratoTipo fichaTecnicaPratoTipo) {
		this.fichaTecnicaPratoTipo = fichaTecnicaPratoTipo;
	}

	public List<Empresa> getListaEmpresas() {
		return listaEmpresas;
	}

	public void setListaEmpresas(List<Empresa> listaEmpresas) {
		this.listaEmpresas = listaEmpresas;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<Cliente> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
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

	public CardapioFichaPrato getCardapioPrato() {
		return cardapioPrato;
	}

	public void setCardapioPrato(CardapioFichaPrato cardapioPrato) {
		this.cardapioPrato = cardapioPrato;
	}

	public ClienteEndereco getEndereco() {
		return endereco;
	}

	public void setEndereco(ClienteEndereco endereco) {
		this.endereco = endereco;
	}

	public List<ClienteEndereco> getListaEnderecos() {
		return listaEnderecos;
	}

	public void setListaEnderecos(List<ClienteEndereco> listaEnderecos) {
		this.listaEnderecos = listaEnderecos;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

}
