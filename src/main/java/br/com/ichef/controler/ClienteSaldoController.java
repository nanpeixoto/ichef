package br.com.ichef.controler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.arquitetura.util.RelatorioUtil;
import br.com.ichef.dto.EmailDTO;
import br.com.ichef.model.Cliente;
import br.com.ichef.model.ClienteEmailAuditoria;
import br.com.ichef.model.Configuracao;
import br.com.ichef.model.VwClienteSaldo;
import br.com.ichef.model.VwClienteSaldoID;
import br.com.ichef.service.ClienteEmailAuditoriaService;
import br.com.ichef.service.ClienteSaldoService;
import br.com.ichef.service.EmailService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.util.JSFUtil;

@Named
@ViewScoped
public class ClienteSaldoController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private ClienteSaldoService service;

	@Inject
	private Cliente clienteService;

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

	Configuracao config = (Configuracao) JSFUtil.getSessionMapValue("configuracao");

	@PostConstruct
	public void init() {
		VwClienteSaldo saldo = new VwClienteSaldo();
		saldo.setCodigoEmpresa(userLogado.getEmpresaLogada().getId());
		saldo.setId(id);
		try {
			lista = service.findByParameters(saldo);
		} catch (Exception e) {
			e.printStackTrace();
			facesMessager.error("N�o foi poss�vel carregar os dados, entre em contato com o administrador do sistema.");
		}

	}

	public void obterEmailEnviados(VwClienteSaldo clienteCarteira) {
		try {
			ClienteEmailAuditoria auditoria = new ClienteEmailAuditoria();
			auditoria.setCodigoCliente(((VwClienteSaldoID) clienteCarteira.getId()).getCodigoCliente());
			setListaClienteEmailAuditoria(clienteEmailAuditoriaService.findByParameters(auditoria));
		} catch (Exception e) {
			e.printStackTrace();
			facesMessager.error("N�o foi poss�vel carregar os dados, entre em contato com o administrador do sistema.");
		}

	}
	
	public void enviarEmailParaTodos() {
		for (VwClienteSaldo vwClienteSaldo : lista) {
			EnviarEmail(vwClienteSaldo);
		}
	}

	public void EnviarEmail(VwClienteSaldo clienteCarteira) {
		String mensagem = "";
		if (clienteCarteira.getEmail() != null) {
			mensagem = config.getEmailInicio() + clienteCarteira.getListaSaldosEmail() + config.getEmailFim();

			EmailDTO dto = new EmailDTO();
			dto.setAssunto(clienteCarteira.getNomeFantasia() + " - SALDO ATUAL");
			//dto.setDestinatario( clienteCarteira.getEmail() );
			dto.setDestinatario("nanpeixoto@gmail.com");
			dto.setTexto(mensagem);

			dto = emailService.enviarEmailHtml(dto);
			ClienteEmailAuditoria auditoria = null;

			if (!dto.getSituacao().equals("S")) {
				auditoria = criarAuditoria(((VwClienteSaldoID) clienteCarteira.getId()).getCodigoCliente(),
						clienteCarteira.getEmail(), dto.getSituacao(), dto.getLog(), mensagem,
						clienteCarteira.getCodigoEmpresa(), clienteCarteira.getValorSaldo(),
						clienteCarteira.getValorSaldoOutraEmpresa(), clienteCarteira.getNomeFantasia());
				FacesUtil.addErroMessage("Erro ao enviar o e-mail, por favor, verifique o LOG");
			} else {
				auditoria = criarAuditoria(((VwClienteSaldoID) clienteCarteira.getId()).getCodigoCliente(),
						clienteCarteira.getEmail(), dto.getSituacao(), dto.getLog(), mensagem,
						clienteCarteira.getCodigoEmpresa(), clienteCarteira.getValorSaldo(),
						clienteCarteira.getValorSaldoOutraEmpresa(), clienteCarteira.getNomeFantasia());
			}

			if (auditoria != null) {
				try {
					clienteEmailAuditoriaService.saveOrUpdade(auditoria);
				} catch (Exception e) {
					e.printStackTrace();
					FacesUtil.addErroMessage("N�o foi poss�vel salvar a auditoria");
				}

			}

		} else {
			String erro = "Cliente sem e-mail";
			criarAuditoria(((VwClienteSaldoID) clienteCarteira.getId()).getCodigoCliente(), clienteCarteira.getEmail(),
					"N", erro, mensagem, clienteCarteira.getCodigoEmpresa(), clienteCarteira.getValorSaldo(),
					clienteCarteira.getValorSaldoOutraEmpresa(), clienteCarteira.getNomeFantasia());
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

}
