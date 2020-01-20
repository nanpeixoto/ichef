package br.com.ichef.controler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.model.CalendarioPedidos;
import br.com.ichef.model.CalendarioPedidosID;
import br.com.ichef.model.Dashboard;
import br.com.ichef.model.VwUltimosClientes;
import br.com.ichef.service.CalendarioPedidosService;
import br.com.ichef.service.DashboardService;
import br.com.ichef.service.VwUltimosClientesService;

@Named
@ViewScoped
public class DashboardController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private DashboardService service;

	@Inject
	private CalendarioPedidosService calendarioPedidosService;

	@Inject
	private VwUltimosClientesService vwUltimosClientesService;

	private Dashboard entity;

	private List<VwUltimosClientes> clientes = new ArrayList<>();
	private List<CalendarioPedidos> calendario = new ArrayList<>();

	private ScheduleModel eventModel;

	@PostConstruct
	public void init() {
		Dashboard filter = new Dashboard();
		filter.setCodigoEmpresa(getUserLogado().getEmpresaLogada().getId());
		try {
			setEntity((service.findByParameters(filter)).get(0));

			setCalendario(calendarioPedidosService.listAll());

			montarCalendario(getCalendario());

			setClientes(vwUltimosClientesService.listAll());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void montarCalendario(List<CalendarioPedidos> calendarioMontagem) {
		eventModel = new DefaultScheduleModel();
		if (calendarioMontagem != null) {

			for (CalendarioPedidos calendarioPedidos : calendarioMontagem) {
				DefaultScheduleEvent event = new DefaultScheduleEvent();

				event.setDescription( calendarioPedidos.getDescricaoCardapdioFicha() + "(" + calendarioPedidos.getQuantidade() + ")" );
				event.setAllDay(true);
				event.setData( ((CalendarioPedidosID) calendarioPedidos.getId()).getDataEntrega() );
				event.setStartDate( ((CalendarioPedidosID) calendarioPedidos.getId()).getDataEntrega()  );
				event.setEndDate( ((CalendarioPedidosID) calendarioPedidos.getId()).getDataEntrega()  );
				event.setTitle(
						calendarioPedidos.getDescricaoCardapdioFicha() + "(" + calendarioPedidos.getQuantidade() + ")");
				event.setEditable(true);
				
				

				eventModel.addEvent(event);
			}

		}

	}

	public DashboardService getService() {
		return service;
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

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public List<CalendarioPedidos> getCalendario() {
		return calendario;
	}

	public void setCalendario(List<CalendarioPedidos> calendario) {
		this.calendario = calendario;
	}

}
