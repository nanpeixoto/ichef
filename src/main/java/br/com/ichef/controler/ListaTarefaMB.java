package br.com.ichef.controler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.com.ichef.arquitetura.controller.BaseConsultaCRUD;
import br.com.ichef.dao.AbstractService;
import br.com.ichef.exception.AppException;
import br.com.ichef.model.Tarefa;
import br.com.ichef.service.TarefaService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class ListaTarefaMB extends BaseConsultaCRUD<Tarefa> {

	private static final long serialVersionUID = 1L;

	@Inject
	private TarefaService service;

	private List<Tarefa> tarefas = new ArrayList<Tarefa>();

	private List<Tarefa> tarefasSelecionadas = new ArrayList<Tarefa>();

	@Override
	protected Tarefa newInstance() {
		// TODO Auto-generated method stub
		return new Tarefa();
	}

	@Override
	protected AbstractService<Tarefa> getService() {
		// TODO Auto-generated method stub
		return service;
	}

	@PostConstruct
	public void init() {
		tarefas = service.listAll();
	}

	public void excluirSelecionados() throws AppException {
		for (Tarefa tarefa : tarefasSelecionadas) {
			service.excluir(tarefa);
			tarefas.remove(tarefa);
		}
		FacesUtil.addInfoMessage("Tarefas excluídas com sucesso");
	}

	public List<Tarefa> getTarefas() {
		return tarefas;
	}

	public void setTarefas(List<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}

	public List<Tarefa> getTarefasSelecionadas() {
		return tarefasSelecionadas;
	}

	public void setTarefasSelecionadas(List<Tarefa> tarefasSelecionadas) {
		this.tarefasSelecionadas = tarefasSelecionadas;
	}

}
