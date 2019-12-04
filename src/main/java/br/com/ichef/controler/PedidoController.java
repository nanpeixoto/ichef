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
import br.com.ichef.model.Derivacao;
import br.com.ichef.model.Empresa;
import br.com.ichef.model.FichaTecnicaPrato;
import br.com.ichef.model.FormaPagamento;
import br.com.ichef.model.Pedido;
import br.com.ichef.model.TipoPrato;
import br.com.ichef.service.CardapioService;
import br.com.ichef.service.ClienteService;
import br.com.ichef.service.DerivacaoService;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.FormaPagamentoService;
import br.com.ichef.service.PedidoService;
import br.com.ichef.service.TipoPratoService;
import br.com.ichef.visitor.CardapioVisitor;

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

	private List<TipoPrato> listaTiposPrato = new ArrayList<>();
	private TipoPrato tipoPrato;

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

	@PostConstruct
	public void init() {

		verificarCardapio();

		setData(new Date());
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
			e.printStackTrace();
			return "cadastro-cardapio.xhtml?faces-redirect=true";
		}

		return "pedido.xhtml?faces-redirect=true";

	}

	private void obterListas() {
		listaClientes = clienteService.listAll(true);
		listaFormasPagamento = formaPagamentoService.listAll(true);
		listaEmpresas = empresaService.listAll(true);
		listaDerivacoes = derivacaoService.listAll(true);

		// obter Dados Do Cardapio

		setEmpresa(userLogado.getEmpresaLogada());
	}

	public void obterEnderecoCliente() {
		if (getCliente() != null) {
			listaEnderecos = getCliente().getEnderecos();
			if (listaEnderecos.size() == 1) {
				endereco = listaEnderecos.get(0);
			}
		} else {
			setEndereco(null);
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

	public List<TipoPrato> getListaTiposPrato() {
		return listaTiposPrato;
	}

	public void setListaTiposPrato(List<TipoPrato> listaTiposPrato) {
		this.listaTiposPrato = listaTiposPrato;
	}

	public TipoPrato getTipoPrato() {
		return tipoPrato;
	}

	public void setTipoPrato(TipoPrato tipoPrato) {
		this.tipoPrato = tipoPrato;
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

}
