package br.com.ichef.controler;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.model.Tarefa;
import br.com.ichef.service.TarefaService;

@Named
@ViewScoped
public class CadastaTarefaMB extends BaseController {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private TarefaService service;
	
	private Tarefa tarefa;
	
	private Long idTarefa;
	
	public void inicializar() {
		if(idTarefa!=null ) {
			setTarefa( service.getById(idTarefa) );
		} else {
			setTarefa(new Tarefa());
		}
	}
	
	public String Salvar() throws Exception {
		service.saveOrUpdade (tarefa);
		return "lista-tarefa.xhtml?faces-redirect=true";
	}
	
	public String excluir() {
		service.excluir(tarefa);
		return "lista-tarefa.xhtml?faces-redirect=true";
	}

	public Tarefa getTarefa() {
		return tarefa;
	}

	public void setTarefa(Tarefa tarefa) {
		this.tarefa = tarefa;
	}

	public Long getIdTarefa() {
		return idTarefa;
	}

	public void setIdTarefa(Long idTarefa) {
		this.idTarefa = idTarefa;
	}
	
	
	
}
