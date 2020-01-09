package br.com.ichef.controler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.com.ichef.arquitetura.controller.BaseConsultaCRUD;
import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.FormaPagamento;
import br.com.ichef.service.FormaPagamentoService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class FormaPagamentoController extends BaseConsultaCRUD<FormaPagamento> {

	private static final long serialVersionUID = 1L;

	@Inject
	private FormaPagamentoService service;

	private FormaPagamento entity;

	private Long id;

	private List<FormaPagamento> lista = new ArrayList<FormaPagamento>();
	private List<FormaPagamento> listaFiltro = new ArrayList<FormaPagamento>();

	private List<FormaPagamento> listaSelecionadas = new ArrayList<FormaPagamento>();

	@Override
	protected FormaPagamento newInstance() {
		// TODO Auto-generated method stub
		return new FormaPagamento();
	}

	@Override
	protected AbstractService<FormaPagamento> getService() {
		// TODO Auto-generated method stub
		return service;
	}

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new FormaPagamento());
			getEntity().setAtivo("S");
			getEntity().setStatusCortesia("N");
		}
	}

	@PostConstruct
	public void init() {
		lista = service.listAll();
	}

	public void excluirSelecionados() throws AppException {
		for (FormaPagamento entity : listaSelecionadas) {
			service.excluir(entity);
			lista.remove(entity);
		}
		FacesUtil.addInfoMessage("FormaPagamentos exclu�das com sucesso");
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
		return "lista-forma-pagamento.xhtml?faces-redirect=true";
	}

	public String excluir() {
		try {
			service.excluir(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "lista-area.xhtml?faces-redirect=true";
	}

	public void setService(FormaPagamentoService service) {
		this.service = service;
	}

	public FormaPagamento getEntity() {
		return entity;
	}

	public void setEntity(FormaPagamento entity) {
		this.entity = entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<FormaPagamento> getLista() {
		return lista;
	}

	public void setLista(List<FormaPagamento> lista) {
		this.lista = lista;
	}

	public List<FormaPagamento> getListaSelecionadas() {
		return listaSelecionadas;
	}

	public void setListaSelecionadas(List<FormaPagamento> listaSelecionadas) {
		this.listaSelecionadas = listaSelecionadas;
	}

	public List<FormaPagamento> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<FormaPagamento> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

}
