package br.com.ichef.controler;

import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.com.ichef.arquitetura.controller.BaseConsultaCRUD;
import br.com.ichef.dao.AbstractService;
import br.com.ichef.model.Tarefa;
import br.com.ichef.service.TarefaService;

@Named
@ViewScoped
public class CadastaTarefaMB extends BaseConsultaCRUD<Tarefa> {

	private static final long serialVersionUID = 1L;

	@Inject
	private TarefaService service;

	private Tarefa tarefa;

	private Long idTarefa;

	public void inicializar() {
		if (idTarefa != null) {
			setTarefa(service.getById(idTarefa));
		} else {
			setTarefa(new Tarefa());
		}
	}

	public String Salvar() throws Exception {
		service.saveOrUpdade(tarefa);
		return "lista-tarefa.xhtml?faces-redirect=true";
	}

	public String excluir() {
		try {
			service.excluir(tarefa);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "lista-area.xhtml?faces-redirect=true";
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

}
