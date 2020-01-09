package br.com.ichef.controler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.com.ichef.arquitetura.controller.BaseConsultaCRUD;
import br.com.ichef.dao.AbstractService;
import br.com.ichef.model.Dashboard;
import br.com.ichef.model.VwUltimosClientes;
import br.com.ichef.service.DashboardService;
import br.com.ichef.service.VwUltimosClientesService;

@Named
@ViewScoped
public class DashboardController extends BaseConsultaCRUD<Dashboard> {

	private static final long serialVersionUID = 1L;

	@Inject
	private DashboardService service;

	@Inject
	private VwUltimosClientesService vwUltimosClientesService;

	private Dashboard entity;

	private List<VwUltimosClientes> clientes = new ArrayList<VwUltimosClientes>();

	@Override
	protected Dashboard newInstance() {
		// TODO Auto-generated method stub
		return new Dashboard();
	}

	@Override
	protected AbstractService<Dashboard> getService() {
		// TODO Auto-generated method stub
		return service;
	}

	@PostConstruct
	public void init() {
		setEntity(service.getById(1));
		setClientes(vwUltimosClientesService.listAll());

	}

	public void setService(DashboardService service) {
		this.service = service;
	}

	public Dashboard getEntity() {
		return entity;
	}

	public void setEntity(Dashboard entity) {
		this.entity = entity;
	}

	public VwUltimosClientesService getVwUltimosClientesService() {
		return vwUltimosClientesService;
	}

	public void setVwUltimosClientesService(VwUltimosClientesService vwUltimosClientesService) {
		this.vwUltimosClientesService = vwUltimosClientesService;
	}

	public List<VwUltimosClientes> getClientes() {
		return clientes;
	}

	public void setClientes(List<VwUltimosClientes> clientes) {
		this.clientes = clientes;
	}

}
