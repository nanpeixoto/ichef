package br.com.ichef.controler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.ToggleEvent;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.arquitetura.util.RelatorioUtil;
import br.com.ichef.dto.EmailDTO;
import br.com.ichef.model.ClienteEmailAuditoria;
import br.com.ichef.model.Configuracao;
import br.com.ichef.model.VwClienteCarteiraSaldo;
import br.com.ichef.model.VwClienteSaldo;
import br.com.ichef.service.ClienteEmailAuditoriaService;
import br.com.ichef.service.ClienteSaldoService;
import br.com.ichef.service.ClienteService;
import br.com.ichef.service.EmailService;
import br.com.ichef.service.VwClienteCarteiraSaldoService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.util.JSFUtil;
import br.com.ichef.visitor.VwClienteCarteiraSaldoVisitor;
import br.com.ichef.visitor.VwClienteSaldoVisitor;

@Named
@ViewScoped
public class ClienteSaldoController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private ClienteSaldoService service;

	@Inject
	private VwClienteCarteiraSaldoService vwClienteCarteiraSaldoService;

	@Inject
	private ClienteService clienteService;

	@Inject
	private ClienteEmailAuditoriaService clienteEmailAuditoriaService;

	@Inject
	private EmailService emailService;

	private VwClienteSaldo entity;

	private Long id;

	private List<VwClienteSaldo> lista = new ArrayList<VwClienteSaldo>();
	private List<VwClienteSaldo> listaFiltro = new ArrayList<VwClienteSaldo>();

	private List<VwClienteSaldo> listaSelecionadas = new ArrayList<VwClienteSaldo>();

	private List<ClienteEmailAuditoria> listaClienteEmailAuditoria = new ArrayList<ClienteEmailAuditoria>();

	private Configuracao config = (Configuracao) JSFUtil.getSessionMapValue("configuracao");

	private boolean ativo;
	private boolean todos;
	private boolean inativo;
	private boolean bloqueado;
	private boolean carteiraTodos;
	private boolean carteira30Dias;
	private boolean carteira120Dias;
	private boolean carteiraEmDias;

	@PostConstruct
	public void init() {
		ativo 			= false;
		inativo 		= false;
		bloqueado 		= false;
		carteira30Dias 	= false;
		carteira120Dias = false;
		carteiraEmDias 	= false;
		todos 			= true;
		carteiraTodos 	= true;

		VwClienteSaldoVisitor visitor = new VwClienteSaldoVisitor();
		visitor.setAtivo(ativo);
		visitor.setCarteira120Dias(carteira120Dias);
		visitor.setCarteiraEmDias(carteiraEmDias);
		visitor.setCarteira30Dias(carteira30Dias);
		visitor.setInativo(inativo);
		visitor.setBloqueado(bloqueado);
		visitor.setTodos(todos);
		visitor.setCarteiraTodos(carteiraTodos);

		VwClienteSaldo saldo = new VwClienteSaldo();

		saldo.setCodigoEmpresa(userLogado.getEmpresaLogada().getId());
		saldo.setId(id);
		try {
			lista = service.findByParameters(saldo, visitor);
		} catch (Exception e) {
			e.printStackTrace();
			facesMessager.error("N�o foi poss�vel carregar os dados, entre em contato com o administrador do sistema.");
		}

	}

	public void pesquisar() {

		VwClienteSaldoVisitor visitor = new VwClienteSaldoVisitor();
		visitor.setAtivo(ativo);
		visitor.setCarteira120Dias(carteira120Dias);
		visitor.setCarteiraEmDias(carteiraEmDias);
		visitor.setCarteira30Dias(carteira30Dias);
		visitor.setInativo(inativo);
		visitor.setBloqueado(bloqueado);
		visitor.setTodos(todos);
		visitor.setCarteiraTodos(carteiraTodos);

		VwClienteSaldo saldo = new VwClienteSaldo();
		saldo.setCodigoEmpresa(userLogado.getEmpresaLogada().getId());
		saldo.setId(id);
		try {
			lista = service.findByParameters(saldo, visitor);
		} catch (Exception e) {
			e.printStackTrace();
			facesMessager.error("N�o foi poss�vel carregar os dados, entre em contato com o administrador do sistema.");
		}

	}

	public void obterEmailEnviados(VwClienteSaldo clienteCarteira) {
		try {
			ClienteEmailAuditoria auditoria = new ClienteEmailAuditoria();
			auditoria.setCodigoCliente(clienteCarteira.getCodigoCliente());
			setListaClienteEmailAuditoria(clienteEmailAuditoriaService.findByParameters(auditoria));
		} catch (Exception e) {
			e.printStackTrace();
			facesMessager.error("N�o foi poss�vel carregar os dados, entre em contato com o administrador do sistema.");
		}

	}

	public void bloquearCliente(VwClienteSaldo clienteCarteira) {
		try {
			String statusBloqueio = (clienteCarteira.isEstaBloqueado() ? "N" : "S");
			String result = clienteService.atualizarStatusBloqueio(statusBloqueio, clienteCarteira.getCodigoCliente());
			if (result != null)
				facesMessager.error(result);
			else {
				facesMessager.info("Atualizado");
				clienteCarteira.setBloqueado(statusBloqueio);
			}

		} catch (Exception e) {
			e.printStackTrace();
			facesMessager
					.error("N�o foi poss�vel carregar os dados, entre em contato com o administrador do sistema."
							+ e.getMessage());
		}

	}

	public String getIconBloqueadoCliente(VwClienteSaldo clienteCarteira) {
		String iconBloqueado = (clienteCarteira.isEstaBloqueado() ? "fa fa-unlock-alt" : "fa fa-lock");
		return iconBloqueado;
	}

	public void enviarEmailParaTodos() {

		System.out.println("enviarEmailParaTodos - Bot�o Clicado pelo usuario : " + getUserLogado().getNomeAbreviado()
				+ " -  em " + formataDataHora(new Date()));
		new Thread() {
			@Override
			public void run() {
				System.out.println("Envio de Email Iniciado");
				for (VwClienteSaldo vwClienteSaldo : lista) {
					if (vwClienteSaldo.getEmail() != null) {

						EnviarEmail(vwClienteSaldo, "S");
						// count++;
						System.out.println("ENVIANDO E-MAIL CLIENTE: " + vwClienteSaldo.getNome());

					}
				}
			}
		}.start();
		System.out.println("finalizado");

	}

	public void enviarEmailParaLista() {

		System.out.println("enviarEmailParaLista - Bot�o Clicado pelo usuario : " + getUserLogado().getNomeAbreviado()
				+ " -  em " + formataDataHora(new Date()));

		System.out.println("Envio de Email Iniciado");
		if (listaFiltro != null && listaFiltro.size() > 0) {
			for (VwClienteSaldo vwClienteSaldo : listaFiltro) {
				if (vwClienteSaldo.getEmail() != null) {

					EnviarEmail(vwClienteSaldo, "S");
					System.out.println("ENVIANDO E-MAIL CLIENTE: " + vwClienteSaldo.getNome());

				}
			}
			FacesUtil.addInfoMessage("E-mail(s) disparado(s)");
		} else {
			FacesUtil.addErroMessage("Nenhum filtro encontrado");
		}

		System.out.println("finalizado");

	}

	public void EnviarEmail(VwClienteSaldo clienteCarteira, String todos) {
		String mensagem = "";
		if (clienteCarteira.getEmail() != null) {
			String inicio = config.getEmailInicio();
			inicio = inicio.replace("#codigo#", clienteCarteira.getCodigoCliente().toString());
			obterDetalhamento(clienteCarteira);
			mensagem = inicio + clienteCarteira.getListaSaldosEmail() + clienteCarteira.getDescricaoLink()
					+ config.getEmailFim();

			EmailDTO dto = new EmailDTO();
			dto.setAssunto(clienteCarteira.getNomeFantasia() + " - SALDO ATUAL");
			//dto.setDestinatario(clienteCarteira.getEmail());
			 dto.setDestinatario("nanpeixoto@gmail.com");
			dto.setTexto(mensagem);

			dto = emailService.enviarEmailHtml(dto);
			ClienteEmailAuditoria auditoria = null;

			if (!dto.getSituacao().equals("S")) {
				System.out.println("emailEnviado:" + clienteCarteira.getCodigoCliente() + " - Email:"
						+ clienteCarteira.getEmail());
				auditoria = criarAuditoria(clienteCarteira.getCodigoCliente(), clienteCarteira.getEmail(),
						dto.getSituacao(), dto.getLog(), mensagem, clienteCarteira.getCodigoEmpresa(),
						clienteCarteira.getValorSaldo(), clienteCarteira.getValorSaldoOutraEmpresa(),
						clienteCarteira.getNomeFantasia());
				if (todos.equalsIgnoreCase("N"))
					FacesUtil.addErroMessage("Erro ao enviar o e-mail, por favor, verifique o LOG");
			} else {
				auditoria = criarAuditoria(clienteCarteira.getCodigoCliente(), clienteCarteira.getEmail(),
						dto.getSituacao(), dto.getLog(), mensagem, clienteCarteira.getCodigoEmpresa(),
						clienteCarteira.getValorSaldo(), clienteCarteira.getValorSaldoOutraEmpresa(),
						clienteCarteira.getNomeFantasia());
			}

			if (auditoria != null) {
				try {
					clienteEmailAuditoriaService.saveOrUpdade(auditoria);
				} catch (Exception e) {
					e.printStackTrace();
					if (todos.equalsIgnoreCase("N"))
						FacesUtil.addErroMessage("N�o foi poss�vel salvar a auditoria");
				}

			}

		} else {
			String erro = "Cliente sem e-mail";
			criarAuditoria(clienteCarteira.getCodigoCliente(), clienteCarteira.getEmail(), "N", erro, mensagem,
					clienteCarteira.getCodigoEmpresa(), clienteCarteira.getValorSaldo(),
					clienteCarteira.getValorSaldoOutraEmpresa(), clienteCarteira.getNomeFantasia());
			if (todos.equalsIgnoreCase("N"))
				FacesUtil.addErroMessage(erro);
		}

	}

	public ClienteEmailAuditoria criarAuditoria(Long codigoCliente, String email, String enviado, String erro,
			String mensagem, Long codigoEmpresaAtual, BigDecimal saldoEmpresa, BigDecimal saldoTotal,
			String descricaoEmpresa) {
		ClienteEmailAuditoria auditoria = new ClienteEmailAuditoria();
		auditoria.setCodigoCliente(codigoCliente);
		auditoria.setDataEnvio(new Date());
		auditoria.setEmail(email);
		auditoria.setEnviado(enviado);
		auditoria.setErro(erro);
		auditoria.setMensagem(mensagem);
		auditoria.setUsuario(getUserLogado());
		auditoria.setCodigoEmpresaAtual(codigoEmpresaAtual);
		auditoria.setSaldoEmpresa(saldoEmpresa);
		auditoria.setSaldoTotal(saldoTotal);
		auditoria.setDescricaoEmpresa(descricaoEmpresa);

		return auditoria;
	}

	public void expandirLinha(ToggleEvent event) {
		VwClienteSaldo carteira = ((VwClienteSaldo) event.getData());
		obterDetalhamento(carteira);

	}

	private void obterDetalhamento(VwClienteSaldo carteira) {
		VwClienteCarteiraSaldo filter = new VwClienteCarteiraSaldo();
		filter.setCodigoEmpresa(carteira.getCodigoEmpresa());
		filter.setCodigoCliente(carteira.getCodigoCliente());

		VwClienteCarteiraSaldoVisitor visitor = new VwClienteCarteiraSaldoVisitor();
		visitor.setCodigoCarteira(carteira.getCodigoCarteira());

		try {
			carteira.setSaldos(vwClienteCarteiraSaldoService.findByParameters(filter, visitor));

		} catch (Exception e) {

			e.printStackTrace();
			facesMessager.error("N�o foi poss�vel carregar os dados, entre em contato com o administrador do sistema.");

		}
	}

	public void excluirSelecionados() {
		for (BaseEntity entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("VwClienteSaldos exclu�das com sucesso");
	}

	public void preExportar(Object document) {
		RelatorioUtil relatorio = new RelatorioUtil("Carteira de Clientes", document);
		relatorio.preExportar();
	}

	public void preExportarAnalitico(Object document) {
		RelatorioUtil relatorio = new RelatorioUtil("Carteira de Clientes", document);
		relatorio.preExportar();
	}

	public ClienteSaldoService getService() {
		return service;
	}

	public void setService(ClienteSaldoService service) {
		this.service = service;
	}

	public VwClienteSaldo getEntity() {
		return entity;
	}

	public void setEntity(VwClienteSaldo entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<VwClienteSaldo> getLista() {
		return lista;
	}

	public void setLista(List<VwClienteSaldo> lista) {
		this.lista = lista;
	}

	public List<VwClienteSaldo> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<VwClienteSaldo> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public List<VwClienteSaldo> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<VwClienteSaldo> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	public List<ClienteEmailAuditoria> getListaClienteEmailAuditoria() {
		return listaClienteEmailAuditoria;
	}

	public void setListaClienteEmailAuditoria(List<ClienteEmailAuditoria> listaClienteEmailAuditoria) {
		this.listaClienteEmailAuditoria = listaClienteEmailAuditoria;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public ClienteEmailAuditoriaService getClienteEmailAuditoriaService() {
		return clienteEmailAuditoriaService;
	}

	public void setClienteEmailAuditoriaService(ClienteEmailAuditoriaService clienteEmailAuditoriaService) {
		this.clienteEmailAuditoriaService = clienteEmailAuditoriaService;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public Configuracao getConfig() {
		return config;
	}

	public void setConfig(Configuracao config) {
		this.config = config;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public boolean isInativo() {
		return inativo;
	}

	public void setInativo(boolean inativo) {
		this.inativo = inativo;
	}

	public boolean isBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public boolean isCarteira30Dias() {
		return carteira30Dias;
	}

	public void setCarteira30Dias(boolean carteira30Dias) {
		this.carteira30Dias = carteira30Dias;
	}

	public boolean isCarteira120Dias() {
		return carteira120Dias;
	}

	public void setCarteira120Dias(boolean carteira120Dias) {
		this.carteira120Dias = carteira120Dias;
	}

	public boolean isCarteiraEmDias() {
		return carteiraEmDias;
	}

	public void setCarteiraEmDias(boolean carteiraEmDias) {
		this.carteiraEmDias = carteiraEmDias;
	}

	public boolean isTodos() {
		return todos;
	}

	public void setTodos(boolean todos) {
		this.todos = todos;
	}

	public boolean isCarteiraTodos() {
		return carteiraTodos;
	}

	public void setCarteiraTodos(boolean carteiraTodos) {
		this.carteiraTodos = carteiraTodos;
	}
	
	

}
