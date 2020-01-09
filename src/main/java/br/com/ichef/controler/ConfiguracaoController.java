package br.com.ichef.controler;

import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.com.ichef.arquitetura.controller.BaseConsultaCRUD;
import br.com.ichef.dao.AbstractService;
import br.com.ichef.model.Configuracao;
import br.com.ichef.service.ConfiguracaoService;

@Named
@ViewScoped
public class ConfiguracaoController extends BaseConsultaCRUD<Configuracao> {

	private static final long serialVersionUID = 1L;

	@Inject
	private ConfiguracaoService service;

	private Configuracao entity;

	private Long id;

	protected Configuracao newInstance() {
		// TODO Auto-generated method stub
		return new Configuracao();
	}

	@Override
	protected AbstractService<Configuracao> getService() {
		// TODO Auto-generated method stub
		return service;
	}

	public void inicializar() {
		if (id != null) {
			setEntity(service.getById(id));
		} else {
			setEntity(new Configuracao());
		}
	}

	public String Salvar() throws Exception {
		service.saveOrUpdade(entity);
		return "lista-tarefa.xhtml?faces-redirect=true";
	}

	public Configuracao getEntity() {
		return entity;
	}

	public void setEntity(Configuracao entity) {
		this.entity = entity;
	}

}
