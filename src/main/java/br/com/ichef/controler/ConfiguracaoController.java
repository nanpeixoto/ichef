package br.com.ichef.controler;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.model.Configuracao;
import br.com.ichef.service.ConfiguracaoService;

@Named
@ViewScoped
public class ConfiguracaoController extends BaseController {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private ConfiguracaoService service;
	
	private Configuracao entity;
	
	private Long id;
	
	public void inicializar() {
		if(id!=null ) {
			setEntity( service.getById(id) );
		} else {
			setEntity(new Configuracao());
		}
	}
	
	public String Salvar() throws Exception {
		service.saveOrUpdade (entity);
		return "lista-tarefa.xhtml?faces-redirect=true";
	}
	
	public String excluir() {
		service.excluir(entity);
		return "lista-tarefa.xhtml?faces-redirect=true";
	}

	public Configuracao getEntity() {
		return entity;
	}

	public void setEntity(Configuracao entity) {
		this.entity = entity;
	}
 
	
	
}
