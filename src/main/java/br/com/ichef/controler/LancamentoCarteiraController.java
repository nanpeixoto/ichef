package br.com.ichef.controler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.RowEditEvent;

import br.com.ichef.arquitetura.controller.BaseConsultaCRUD;
import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Cliente;
import br.com.ichef.model.ClienteCarteira;
import br.com.ichef.model.Derivacao;
import br.com.ichef.model.Empresa;
import br.com.ichef.model.FichaTecnicaPrato;
import br.com.ichef.model.FichaTecnicaPratoTipo;
import br.com.ichef.model.FormaPagamento;
import br.com.ichef.model.TipoPrato;
import br.com.ichef.service.ClienteCarteiraService;
import br.com.ichef.service.ClienteService;
import br.com.ichef.service.DerivacaoService;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.FichaTecnicaPratoService;
import br.com.ichef.service.FormaPagamentoService;
import br.com.ichef.service.TipoPratoService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.visitor.ClienteCarteiraVisitor;

@Named
@ViewScoped
public class LancamentoCarteiraController extends BaseConsultaCRUD<ClienteCarteira> {

	private static final long serialVersionUID = 1L;

	@Inject
	private ClienteService service;

	@Inject
	private FormaPagamentoService formaPagamentoService;

	@Inject
	private FichaTecnicaPratoService fichaTecnicaPratoService;

	@Inject
	private EmpresaService empresaService;

	@Inject
	private DerivacaoService derivacaoService;

	@Inject
	private TipoPratoService tipoPratoService;

	@Inject
	private ClienteCarteiraService clienteCarteiraService;

	private List<ClienteCarteira> lista = new ArrayList<ClienteCarteira>();

	// carteira
	private String tipoCarteira;
	private Date data;
	private Double valorDevido;
	private Double valorPago;
	private String descricao;

	private List<FormaPagamento> listaFormasPagamento = new ArrayList<FormaPagamento>();
	private FormaPagamento formaPagamento;

	private List<FichaTecnicaPrato> listaPratos = new ArrayList<FichaTecnicaPrato>();
	private FichaTecnicaPrato prato;

	private List<Derivacao> listaDerivacoes = new ArrayList<Derivacao>();
	private Derivacao derivacao;

	private List<TipoPrato> listaTiposPrato = new ArrayList<TipoPrato>();
	private TipoPrato tipoPrato;

	private List<Empresa> listaEmpresas = new ArrayList<Empresa>();
	private Empresa empresa;

	private List<Cliente> listaClientes = new ArrayList<Cliente>();
	private Cliente cliente;

	@Override
	protected ClienteCarteira newInstance() {
		// TODO Auto-generated method stub
		return new ClienteCarteira();
	}

	@Override
	protected AbstractService<ClienteCarteira> getService() {
		// TODO Auto-generated method stub
		return clienteCarteiraService;
	}

	@PostConstruct
	public void init() {
		setData(new Date());
		obterListas();
		ClienteCarteiraVisitor visitor = new ClienteCarteiraVisitor();
		visitor.setDataSemHora(getData());
		try {
			lista = clienteCarteiraService.findByParameters(new ClienteCarteira(), visitor);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void obterTiposPrato() {
		listaTiposPrato = new ArrayList<TipoPrato>();
		if (getPrato().getFichaTecnicaPratoTipos() != null) {
			for (FichaTecnicaPratoTipo fichaTecnicaPratoTipo : getPrato().getFichaTecnicaPratoTipos()) {
				listaTiposPrato.add(fichaTecnicaPratoTipo.getTipoPrato());
			}
		}
		setTipoPrato(null);
		setValorDevido(null);
	}

	private void obterListas() {
		listaClientes = service.listAll(true);
		listaFormasPagamento = formaPagamentoService.listAll(true);
		listaPratos = fichaTecnicaPratoService.listAll(true);
		listaEmpresas = empresaService.listAll(true);
		listaDerivacoes = derivacaoService.listAll(true);
		setEmpresa(userLogado.getEmpresaLogada());
		// listaTiposPrato = tipoPratoService.listAll(true);
	}

	public void obterValorPrato() {
		if (getTipoPrato() != null)
			setValorDevido(getTipoPrato().getPrecoAtual().getPreco());
		else
			setValorDevido(null);
	}

	public void adicionarCarteira() {

		if (getEmpresa() != null && (userLogado.getEmpresaLogada().getId() != getEmpresa().getId())) {// SE O LANCAMENTO
																										// FOR PARA
																										// OUTRA EMPRESA
			if (!getTipoCarteira().equalsIgnoreCase("C")) { // SE O SELECIONADO N�O FOR CREDITO
				facesMessager.error("O tipo de Lan�amento para outra empresa s� pode ser Cr�dito");
				return;
			}
		}

		if (getTipoCarteira() == null || getTipoCarteira().equals("")) {// TIPO DE PAGAMENTO PRECISA ESTAR PREENCHIDO
			facesMessager.error(getRequiredMessage("Tipo"));
			return;
		}

		if (getTipoCarteira().equalsIgnoreCase("C")) { // SE O SELECIONADO FOR CREDITO
			if (getDescricao() == null || getDescricao().equals("")) {// descricao precisa estar preenhida
				facesMessager.error(getRequiredMessage("Descri��o"));
				return;
			}
			if (getData() == null) {// data precisa estar preenhida
				facesMessager.error(getRequiredMessage("Data"));
				return;
			}
			if (getValorPago() == null) {// valor pago precisa estar preenhida
				facesMessager.error(getRequiredMessage("Valor Pago"));
				return;
			}
			if (getFormaPagamento() == null) {// forma de pagamento pago precisa estar preenhida
				facesMessager.error(getRequiredMessage("Forma de Pagamento"));
				return;
			}
		}

		if (getTipoCarteira().equalsIgnoreCase("P")) { // SE O SELECIONADO FOR CREDITO
			if (getPrato() == null) {// descricao precisa estar preenhida
				facesMessager.error(getRequiredMessage("Prato"));
				return;
			}
			if (getTipoPrato() == null) {// descricao precisa estar preenhida
				facesMessager.error(getRequiredMessage("Tipo de Prato"));
				return;
			}
			// if (getDerivacao() == null) {// descricao precisa estar preenhida
			// facesMessager.error(getRequiredMessage("Deriva��o"));
			// return;
			// }
			if (getData() == null) {// data precisa estar preenhida
				facesMessager.error(getRequiredMessage("Data"));
				return;
			}
			if (getValorDevido() == null) {// valor pago precisa estar preenhida
				facesMessager.error(getRequiredMessage("Valor Devido"));
				return;
			}
		}

		if (getTipoCarteira().equalsIgnoreCase("D")) { // SE O SELECIONADO FOR CREDITO
			if (getDescricao() == null || getDescricao().equals("")) {// descricao precisa estar preenhida
				facesMessager.error(getRequiredMessage("Descri��o"));
				return;
			}

			if (getData() == null) {// data precisa estar preenhida
				facesMessager.error(getRequiredMessage("Data"));
				return;

			}
			if (getValorDevido() == null) {// valor pago precisa estar preenhida
				facesMessager.error(getRequiredMessage("Valor Devido"));
				return;
			}

		}

		if (getValorPago() != null) {// SE O VALOR PAGO FOR INFORMADO PRECISA INFORMAR A FORMA DE PAGAMENTO
			if (getFormaPagamento() == null) {// forma de pagamento pago precisa estar preenhida
				facesMessager.error(getRequiredMessage("Forma de Pagamento"));
				return;
			}

		}

		ClienteCarteira carteira = new ClienteCarteira();
		carteira.setCliente(service.getById(getCliente().getId()));
		carteira.setData(getData());
		carteira.setEmpresaLogada(userLogado.getEmpresaLogada());
		carteira.setDescricao(getDescricao());
		carteira.setFichaTecnicaPrato(getPrato());
		carteira.setFormaPagamento(getFormaPagamento());
		carteira.setDerivacao(getDerivacao());
		carteira.setTipoPrato(getTipoPrato());
		carteira.setUsuarioCadastro(getUserLogado());
		carteira.setDataCadastrado(new Date());
		carteira.setEmpresa(getEmpresa());
		carteira.setTipoCarteira(getTipoCarteira());
		carteira.setUsuarioCadastro(getUserLogado());
		carteira.setDataCadastrado(new Date());
		if (getValorDevido() != null)
			carteira.setValorDevido(new BigDecimal(getValorDevido().toString()));
		if (getValorPago() != null)
			carteira.setValorPago(new BigDecimal(getValorPago().toString()));

		try {
			clienteCarteiraService.saveOrUpdade(carteira);
		} catch (Exception e) {
			e.printStackTrace();
		}

		lista.add(carteira);

		limparCarteira();

	}

	public void limparCarteira() {
		setData(null);
		setDescricao(null);
		setPrato(null);
		setFormaPagamento(null);
		setTipoCarteira(null);
		setValorDevido(null);
		setValorPago(null);
		setDerivacao(null);
		setTipoPrato(null);
		setData(new Date());
		setCliente(null);
	}

	public boolean getExibirValorCredito() {
		if (getTipoCarteira() == null)
			return false;
		if (getTipoCarteira().equalsIgnoreCase("C") || getTipoCarteira().equalsIgnoreCase("P"))
			return true;
		if (getTipoCarteira().equalsIgnoreCase("D"))
			return false;
		else
			return false;
	}

	public boolean getExibirFormaPagamento() {
		if (getTipoCarteira() != null
				&& (getTipoCarteira().equalsIgnoreCase("C") || getTipoCarteira().equalsIgnoreCase("P")))
			return true;

		return false;
	}

	public Boolean getExibirPrato() {
		if (getTipoCarteira() == null)
			return false;
		if (getTipoCarteira().equalsIgnoreCase("P"))
			return true;
		else
			return false;

	}

	public void excluirCarteira(ClienteCarteira obj) throws AppException {
		List<ClienteCarteira> temp = new ArrayList<ClienteCarteira>();
		temp.addAll(lista);
		for (ClienteCarteira item : lista) {
			if (obj.equals(item)) {
				temp.remove(item);
				clienteCarteiraService.excluir(item);
			}

		}
		lista.clear();
		lista.addAll(temp);
		// updateComponentes(":form:tabCarteira:tableCarteira");
		FacesUtil.addInfoMessage("Itens exclu�dos com sucesso");
	}

	public void editarLinhaCarteira(RowEditEvent event) throws Exception {
		ClienteCarteira itemEditado = (ClienteCarteira) event.getObject();

		itemEditado.setUsuarioAlteracao(getUserLogado());
		itemEditado.setDataAlteracao(new Date());
		clienteCarteiraService.saveOrUpdade(itemEditado);

	}

	public void setService(ClienteService service) {
		this.service = service;
	}

	public String getTipoCarteira() {
		return tipoCarteira;
	}

	public void setTipoCarteira(String tipoCarteira) {
		this.tipoCarteira = tipoCarteira;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getValorDevido() {
		return valorDevido;
	}

	public void setValorDevido(Double valorDevido) {
		this.valorDevido = valorDevido;
	}

	public Double getValorPago() {
		return valorPago;
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public List<FormaPagamento> getListaFormasPagamento() {
		return listaFormasPagamento;
	}

	public void setListaFormasPagamento(List<FormaPagamento> listaFormasPagamento) {
		this.listaFormasPagamento = listaFormasPagamento;
	}

	public FichaTecnicaPrato getPrato() {
		return prato;
	}

	public void setPrato(FichaTecnicaPrato prato) {
		this.prato = prato;
	}

	public List<FichaTecnicaPrato> getListaPratos() {
		return listaPratos;
	}

	public void setListaPratos(List<FichaTecnicaPrato> listaPratos) {
		this.listaPratos = listaPratos;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
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

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<ClienteCarteira> getLista() {
		return lista;
	}

	public void setLista(List<ClienteCarteira> lista) {
		this.lista = lista;
	}

}
