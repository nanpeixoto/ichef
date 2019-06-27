package br.com.ichef.controler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import br.com.ichef.arquitetura.BaseEntity;
import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.model.Cidade;
import br.com.ichef.model.Cliente;
import br.com.ichef.model.ClienteTelefone;
import br.com.ichef.model.Empresa;
import br.com.ichef.service.CidadeService;
import br.com.ichef.service.ClienteService;
import br.com.ichef.service.ClienteTelefoneService;
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
	private ClienteTelefoneService clienteTelefoneService;

	private Cliente entity;

	private Long id;

	private List<Cliente> lista = new ArrayList<Cliente>();
	private List<Cliente> listaFiltro = new ArrayList<Cliente>();

	private List<Cliente> listaSelecionadas = new ArrayList<Cliente>();

	private List<Cidade> cidades = new ArrayList<Cidade>();
	private List<Empresa> empresas = new ArrayList<Empresa>();

	private boolean stsTelefonePrincipal;
	private String telefone;

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
			telefone = "5571 ";
		} else {
			System.out.println(telefone);
			setEntity(new Cliente());
			setStsTelefonePrincipal(true);
			getEntity().setAtivo(true);
			getEntity().setCidade(obterCidadeSalvador());
			getEntity().setRecebeMaladireta(true);
			getEntity().setRecebeSMS(true);
			getEntity().setPagaEmCarteira(false);
			getEntity().setEstaBloqueado(false);
		}
		obterListas();
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

	private void obterListas() {
		cidades = cidadeService.listAll(true);
	}

	@PostConstruct
	public void init() {
		telefone = "5571 ";
		lista = service.listAll();
	}

	public void excluirSelecionados() {
		for (BaseEntity entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("Itens excluídos com sucesso");
	}

	public String Salvar() throws Exception {

		if (entity.isEdicao()) {
			entity.setUsuarioAlteracao(getUserLogado());
			entity.setDataAlteracao(new Date());
		} else {
			entity.setUsuarioCadastro(getUserLogado());
			entity.setDataCadastro(new Date());
		}
		service.saveOrUpdade(entity);
		return "lista-cliente.xhtml?faces-redirect=true";

	}

	public String excluir() {
		service.excluir(entity);
		return "lista-cliente.xhtml?faces-redirect=true";
	}

	public void adicionarTelefone() {
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
			FacesUtil.addErroMessage("Já existe um telefone principal para esse cliente");
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
		if (stsTelefonePrincipal) {
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

}
