package br.com.ichef.controler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.model.Cidade;
import br.com.ichef.model.Cliente;
import br.com.ichef.model.ClienteCarteira;
import br.com.ichef.model.ClienteEndereco;
import br.com.ichef.model.ClienteTelefone;
import br.com.ichef.model.Derivacao;
import br.com.ichef.model.Empresa;
import br.com.ichef.model.FichaTecnicaPrato;
import br.com.ichef.model.FichaTecnicaPratoTipo;
import br.com.ichef.model.FormaPagamento;
import br.com.ichef.model.Localidade;
import br.com.ichef.model.TipoPrato;
import br.com.ichef.service.CidadeService;
import br.com.ichef.service.ClienteCarteiraService;
import br.com.ichef.service.ClienteEnderecoService;
import br.com.ichef.service.ClienteService;
import br.com.ichef.service.ClienteTelefoneService;
import br.com.ichef.service.DerivacaoService;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.FichaTecnicaPratoService;
import br.com.ichef.service.FormaPagamentoService;
import br.com.ichef.service.LocalidadeService;
import br.com.ichef.service.TipoPratoService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class ClienteController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private ClienteService service;

	@Inject
	private CidadeService cidadeService;

	@Inject
	private FormaPagamentoService formaPagamentoService;

	@Inject
	private ClienteEnderecoService clienteEnderecoService;

	@Inject
	private ClienteTelefoneService clienteTelefoneService;

	@Inject
	private LocalidadeService localidadeService;

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

	private Cliente entity;

	private Long id;

	private List<Cliente> lista = new ArrayList<Cliente>();
	private List<Cliente> listaFiltro = new ArrayList<Cliente>();

	private List<Cliente> listaSelecionadas = new ArrayList<Cliente>();

	private List<Cidade> cidades = new ArrayList<Cidade>();
	private List<Empresa> empresas = new ArrayList<Empresa>();
	private List<Localidade> localidades = new ArrayList<Localidade>();

	private boolean stsTelefonePrincipal;
	private boolean stsEnderecoPrincipal;
	private Localidade localidade;
	private String endereco;
	private String telefone;

	// carteira
	private String tipoCarteira;
	private Date data;
	private Double valorDevido;
	private Double valorPago;
	private String descricao;

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

	private ClienteCarteira carteiraSelecionada;

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
			telefone = "5571 ";
			setData(new Date());
		} else {
			if (telefone != null) {
				adicionarTelefone();
				telefone = "5571 ";
				setData(new Date());
			}
		}
	}

	@PostConstruct
	public void init() {

		if (id != null) {
			setEntity(service.getById(id));
			telefone = "5571 ";
		} else {
			if (telefone == null) {
				newInstance();
				telefone = "5571 ";
				lista = service.listAll();
			}
		}
		obterListas();
	}

	private void newInstance() {
		setEntity(new Cliente());
		setStsTelefonePrincipal(true);
		setStsEnderecoPrincipal(true);
		getEntity().setAtivo(true);
		getEntity().setCidade(obterCidadeSalvador());
		getEntity().setRecebeMaladireta(true);
		getEntity().setRecebeSMS(true);
		getEntity().setPagaEmCarteira(false);
		getEntity().setEstaBloqueado(false);
	}

	private Cidade obterCidadeSalvador() {
		Cidade cidadeSalvador = new Cidade();
		cidadeSalvador.setDescricao("SALVADOR".toUpperCase());

		try {
			return cidadeService.findByParameters(cidadeSalvador).get(0);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	public void obterTiposPrato() {
		listaTiposPrato = new ArrayList<>();
		if (getPrato().getFichaTecnicaPratoTipos() != null) {
			for (FichaTecnicaPratoTipo fichaTecnicaPratoTipo : getPrato().getFichaTecnicaPratoTipos()) {
				listaTiposPrato.add(fichaTecnicaPratoTipo.getTipoPrato());
			}
		}
		setTipoPrato(null);
		setValorDevido(null);
	}

	private void obterListas() {
		cidades = cidadeService.listAll(true);
		localidades = localidadeService.listAll(true);
		listaFormasPagamento = formaPagamentoService.listAll(true);
		listaPratos = fichaTecnicaPratoService.listAll(true);
		listaEmpresas = empresaService.listAll(true);
		listaDerivacoes = derivacaoService.listAll(true);
		setEmpresa(userLogado.getEmpresaLogada());
		// listaTiposPrato = tipoPratoService.listAll(true);
	}

	public void excluirSelecionados() {
		for (BaseEntity entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}

	public void excluirTelefonesSelecionados(ClienteTelefone obj) {
		List<ClienteTelefone> temp = new ArrayList<>();
		temp.addAll(entity.getTelefones());
		for (ClienteTelefone item : entity.getTelefones()) {
			if (obj.getTelefone().equals(item.getTelefone()))
				temp.remove(item);
		}
		entity.getTelefones().clear();
		entity.getTelefones().addAll(temp);
		updateComponentes(":form:tabs:dttel");
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}

	public void excluirLocalidadesSelecionadas(ClienteEndereco obj) {
		List<ClienteEndereco> temp = new ArrayList<>();
		temp.addAll(entity.getEnderecos());
		for (ClienteEndereco item : entity.getEnderecos()) {
			if (obj.getLocalidade().getId().equals(item.getLocalidade().getId()))
				temp.remove(item);
		}
		entity.getEnderecos().clear();
		entity.getEnderecos().addAll(temp);
		updateComponentes(":form:tabs:dtendereco");
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}

	public String Salvar() throws Exception {

		if (entity.isEdicao()) {
			entity.setUsuarioAlteracao(getUserLogado());
			entity.setDataAlteracao(new Date());
		} else {
			entity.setUsuarioCadastro(getUserLogado());
			entity.setDataCadastro(new Date());
			
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			FacesUtil.addInfoMessage("Novo cliente criado código: " + getEntity().getId());
		}
		
		entity.setDescricaoTelefone(entity.getAllTelefones());
		service.saveOrUpdade(entity);

		return "lista-cliente.xhtml?faces-redirect=true";

	}

	public void obterValorPrato() {
		if (getTipoPrato() != null)
			setValorDevido(getTipoPrato().getPrecoAtual().getPreco());
		else
			setValorDevido(null);
	}

	public String excluir() {
		service.excluir(entity);
		return "lista-cliente.xhtml?faces-redirect=true";
	}

	public void adicionarTelefone() {
		if (!getTelefone().equals("5571 ")) {
			// if (existeTelefone(getTelefone())) {
			if (!existeTelefonePrincipal(null)) {
				ClienteTelefone telefone = new ClienteTelefone();
				telefone.setDataCadastro(new Date());
				if (stsTelefonePrincipal)
					telefone.setPrincipal("S");
				else
					telefone.setPrincipal("N");
				telefone.setTelefone(getTelefone().replace("(", "").replace(")", "").replace("-", "").replace(" ", ""));
				telefone.setUsuarioCadastro(getUserLogado());
				telefone.setCliente(getEntity());
				telefone.setUsuarioCadastro(getUserLogado());
				telefone.setDataCadastro(new Date());

				if (getEntity().getTelefones() == null)
					getEntity().setTelefones(new ArrayList<ClienteTelefone>());
				getEntity().getTelefones().add(telefone);

				setStsTelefonePrincipal(false);
				setTelefone(null);
			} else {
				facesMessager.error("Já existe um telefone principal para esse cliente");
			}
			// } else {
			// facesMessager.error("Telefone já adicionado para essa cliente");
			// }
		}
	}

	private boolean existeTelefone(String telefone2) {
		if (getEntity().getTelefones() != null) {
			for (ClienteTelefone tel : getEntity().getTelefones()) {
				if (tel.getTelefone().equals(telefone2))
					return true;
			}
		}

		return false;
	}

	private boolean existeLocalidade(Localidade localidade) {
		if (getEntity().getEnderecos() != null) {
			for (ClienteEndereco end : getEntity().getEnderecos()) {
				if (end != null && end.getLocalidade() != null && localidade != null && localidade.getId() != null)
					if (end.getLocalidade().getId().equals(localidade.getId()))
						return true;
			}

			return false;
		} else {
			return false;
		}
	}

	public String obterCor(BigDecimal valor) {
		if (valor.compareTo(new BigDecimal(0)) > 0)
			return "green";
		if (valor.compareTo(new BigDecimal(0)) == -1)
			return "red";
		return "black";
	}

	public void adicionarCarteira() {

		if (getTipoCarteira() == null || getTipoCarteira().equals("")) {// TIPO DE PAGAMENTO PRECISA ESTAR PREENCHIDO
			facesMessager.error(getRequiredMessage("Tipo"));
			return;
		}

		if (getTipoCarteira().equalsIgnoreCase("C")) { // SE O SELECIONADO FOR CREDITO
			if (getDescricao() == null || getDescricao().equals("")) {// descricao precisa estar preenhida
				facesMessager.error(getRequiredMessage("Descrição"));
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
			// facesMessager.error(getRequiredMessage("Derivação"));
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
				facesMessager.error(getRequiredMessage("Descrição"));
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
		carteira.setCliente(getEntity());
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

		if (getEntity().getCarteiras() == null) {
			getEntity().setCarteiras(new ArrayList<>());
		}

		try {
			if (getEntity().getId() != null)
				clienteCarteiraService.saveOrUpdade(carteira);
		} catch (Exception e) {
			e.printStackTrace();
		}

		getEntity().getCarteiras().add(carteira);

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

	public BigDecimal getSaldoEmpresaAtual() {
		BigDecimal saldoCliente = new BigDecimal(0);
		if (getEntity().getCarteiras() != null)
			for (ClienteCarteira carteira : getEntity().getCarteiras()) {
				if (carteira.getEmpresa().getId().equals(userLogado.getEmpresaLogada().getId())) {
					if (carteira.getValorDevido() != null)
						saldoCliente = saldoCliente.subtract(carteira.getValorDevido());
					if (carteira.getValorPago() != null)
						saldoCliente = saldoCliente.add(carteira.getValorPago());
				}
			}
		return saldoCliente;
	}

	public void adicionarEndereco() {
		// if (!existeTelefonePrincipal(null)) {
		ClienteEndereco endereco = new ClienteEndereco();
		if (!existeLocalidade(getLocalidade())) {
			if (!existeEnderecoPrincipal(null)) {
				endereco.setDataCadastro(new Date());

				if (stsEnderecoPrincipal)
					endereco.setPrincipal("S");
				else
					endereco.setPrincipal("N");
				endereco.setEndereco(getEndereco());
				endereco.setLocalidade(getLocalidade());
				endereco.setUsuarioCadastro(getUserLogado());
				endereco.setCliente(getEntity());
				endereco.setUsuarioCadastro(getUserLogado());
				endereco.setDataCadastro(new Date());

				if (getEntity().getEnderecos() == null)
					getEntity().setEnderecos(new ArrayList<ClienteEndereco>());
				getEntity().getEnderecos().add(endereco);

				setStsEnderecoPrincipal(false);
				setEndereco(null);
				setLocalidade(null);
			} else {
				facesMessager.error("Já existe um endereco principal para esse cliente");
			}
		} else {
			facesMessager.error("Endereco já adicionado para essa cliente");
		}

	}

	public String validarCliente() throws Exception {
		System.out.println(telefone);

		ClienteTelefone filter = new ClienteTelefone();
		filter.setTelefone(telefone);
		List<ClienteTelefone> telefonesClientes = clienteTelefoneService.findByParameters(filter);
		if (telefonesClientes != null && telefonesClientes.size() > 0) {
			id = telefonesClientes.get(0).getCliente().getId();
			return "cadastro-cliente.xhtml?faces-redirect=true&id=" + id;
		} else {
			return "cadastro-cliente.xhtml?faces-redirect=true&telefone=" + telefone.replace("+", "");
		}
	}

	private boolean existeTelefonePrincipal(ClienteTelefone telefoneEditado) {
		if (stsTelefonePrincipal && entity.getTelefones() != null) {
			for (ClienteTelefone telefone : entity.getTelefones()) {
				if (telefone.isTelefonePrincipal())
					if (telefoneEditado != null && telefone.getId().equals(telefoneEditado.getId()))
						return false;
					else if (telefoneEditado == null)
						return true;
			}
		}

		return false;
	}

	private boolean existeEnderecoPrincipal(ClienteEndereco endrecoEditador) {
		if (stsEnderecoPrincipal && entity.getEnderecos() != null) {
			for (ClienteEndereco item : entity.getEnderecos()) {
				if (item.isEndPrincipal())
					if (endrecoEditador != null && item.getId().equals(endrecoEditador.getId()))
						return false;
					else if (endrecoEditador == null)
						return true;
			}
		}

		return false;
	}

	public void editarLinhaTelefone(RowEditEvent event) throws Exception {
		ClienteTelefone telefoneEditado = (ClienteTelefone) event.getObject();
		if (telefoneEditado.isTelefonePrincipal()) {
			if (!existeTelefonePrincipal(telefoneEditado)) {
				telefoneEditado.setUsuarioAlteracao(getUserLogado());
				telefoneEditado.setDataAlteracao(new Date());
				clienteTelefoneService.saveOrUpdade(telefoneEditado);
			} else {
				FacesUtil.addErroMessage("Já existe um telefone principal para esse cliente");
			}
		}

	}

	public void excluirCarteira(ClienteCarteira obj) {
		List<ClienteCarteira> temp = new ArrayList<>();
		temp.addAll(entity.getCarteiras());
		for (ClienteCarteira item : entity.getCarteiras()) {
			if (obj.equals(item))
				temp.remove(item);
		}
		entity.getCarteiras().clear();
		entity.getCarteiras().addAll(temp);
		// updateComponentes(":form:tabCarteira:tableCarteira");
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}

	public void editarLinhaEndereco(RowEditEvent event) throws Exception {
		ClienteEndereco itemEditado = (ClienteEndereco) event.getObject();

		itemEditado.setUsuarioAlteracao(getUserLogado());
		itemEditado.setDataAlteracao(new Date());
		clienteEnderecoService.saveOrUpdade(itemEditado);

	}

	public void editarLinhaCarteira(RowEditEvent event) throws Exception {
		ClienteCarteira itemEditado = (ClienteCarteira) event.getObject();

		itemEditado.setUsuarioAlteracao(getUserLogado());
		itemEditado.setDataAlteracao(new Date());
		clienteCarteiraService.saveOrUpdade(itemEditado);

	}

	public ClienteService getService() {
		return service;
	}

	public void setService(ClienteService service) {
		this.service = service;
	}

	public Cliente getEntity() {
		return entity;
	}

	public void setEntity(Cliente entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Cliente> getLista() {
		return lista;
	}

	public void setLista(List<Cliente> lista) {
		this.lista = lista;
	}

	public List<Cliente> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<Cliente> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public List<Cliente> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<Cliente> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public boolean isStsTelefonePrincipal() {
		return stsTelefonePrincipal;
	}

	public void setStsTelefonePrincipal(boolean stsTelefonePrincipal) {
		this.stsTelefonePrincipal = stsTelefonePrincipal;
	}

	public boolean isStsEnderecoPrincipal() {
		return stsEnderecoPrincipal;
	}

	public void setStsEnderecoPrincipal(boolean stsEnderecoPrincipal) {
		this.stsEnderecoPrincipal = stsEnderecoPrincipal;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public List<Localidade> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List<Localidade> localidades) {
		this.localidades = localidades;
	}

	public Localidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
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

	public ClienteCarteira getCarteiraSelecionada() {
		return carteiraSelecionada;
	}

	public void setCarteiraSelecionada(ClienteCarteira carteiraSelecionada) {
		this.carteiraSelecionada = carteiraSelecionada;
	}

}
