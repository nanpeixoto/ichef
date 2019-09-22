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
import br.com.ichef.model.ClienteEndereco;
import br.com.ichef.model.ClienteTelefone;
import br.com.ichef.model.Empresa;
import br.com.ichef.model.Localidade;
import br.com.ichef.service.CidadeService;
import br.com.ichef.service.ClienteEnderecoService;
import br.com.ichef.service.ClienteService;
import br.com.ichef.service.ClienteTelefoneService;
import br.com.ichef.service.LocalidadeService;
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
	private ClienteEnderecoService clienteEnderecoService;

	@Inject
	private ClienteTelefoneService clienteTelefoneService;

	@Inject
	private LocalidadeService localidadeService;

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

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
			telefone = "5571 ";
		} else {
			if (telefone != null) {
				adicionarTelefone();
				telefone = "5571 ";
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

	private void obterListas() {
		cidades = cidadeService.listAll(true);
		localidades = localidadeService.listAll(true);
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
		updateComponentes("dttel");
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
		updateComponentes("dtendereco");
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
		if (!getTelefone().equals("5571 ")) {
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
	}

	public void adicionarEndereco() {
		// if (!existeTelefonePrincipal(null)) {
		ClienteEndereco endereco = new ClienteEndereco();
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
		// } else {
		// FacesUtil.addErroMessage("Já existe um telefone principal para esse
		// cliente");
		// }
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

	public void editarLinhaEndereco(RowEditEvent event) throws Exception {
		ClienteEndereco itemEditado = (ClienteEndereco) event.getObject();
		// if (itemEditado.isTelefonePrincipal()) {
		// if (!existeTelefonePrincipal(telefoneEditado)) {
		itemEditado.setUsuarioAlteracao(getUserLogado());
		itemEditado.setDataAlteracao(new Date());
		clienteEnderecoService.saveOrUpdade(itemEditado);
		// } else {
		// FacesUtil.addErroMessage("Já existe um telefone principal para esse
		// cliente");
		// }
		// }
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

}
