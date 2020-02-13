package br.com.ichef.controler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import br.com.ichef.model.Cardapio;
import br.com.ichef.model.Dashboard;
import br.com.ichef.model.Pedido;
import br.com.ichef.model.VwUltimosClientes;
import br.com.ichef.service.CalendarioPedidosService;
import br.com.ichef.service.DashboardService;
import br.com.ichef.service.PedidoService;
import br.com.ichef.service.VwUltimosClientesService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.visitor.PedidoVisitor;

@Named
@ViewScoped
public class DashboardController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private DashboardService service;

	@Inject
	private PedidoService pedidoService;

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

			CalendarioPedidos calendarioFilter = new CalendarioPedidos();
			calendarioFilter.setCodigoEmpresa(userLogado.getEmpresaLogada().getId());

			setCalendario(calendarioPedidosService.findByParameters(calendarioFilter));

			montarCalendario(getCalendario());

			setClientes(vwUltimosClientesService.listAll());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void order(List<Pedido> persons) {

		Collections.sort(persons, new Comparator() {

			public int compare(Object o1, Object o2) {
				
				Date d1 = ((Pedido) o1).getDataEntrega();
				Date d2 = ((Pedido) o2).getDataEntrega();
				int sComp = d1.compareTo(d2);

				if (sComp != 0) {
					return sComp;
				}

				String x1 = ((Pedido) o1).getEntregador().getNome();
				String x2 = ((Pedido) o2).getEntregador().getNome();
				sComp = x1.compareTo(x2);

				if (sComp != 0) {
					return sComp;
				}

				Integer ordem1 = ((Pedido) o1).getOrdemEntrega();
				Integer ordem2 = ((Pedido) o2).getOrdemEntrega();
				return ordem1.compareTo(ordem2);
			}
		});
	}

	public void imprimirPedidosNaoConfirmados() {

		Pedido filter = new Pedido();
		filter.setEmpresa(userLogado.getEmpresaLogada());
		filter.setSnConfirmado("N");

		List<Pedido> pedidos = new ArrayList<>();

		try {
			pedidos = pedidoService.findByParameters(filter);

			order(pedidos);

		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErroMessage("Erro ao obter os dados do relatório");
		}

		if (pedidos.size() == 0) {
			FacesUtil.addErroMessage("Nenhum dado encontrado");
		} else {
			try {
				setParametroReport(REPORT_PARAM_LOGO, getImagem(LOGO));
				escreveRelatorioPDF("Pedidos", true, pedidos);
			} catch (Exception e) {
				e.printStackTrace();
				FacesUtil.addErroMessage("Erro ao gerar o relatório");
			}
		}

	}

	private void montarCalendario(List<CalendarioPedidos> calendarioMontagem) {
		eventModel = new DefaultScheduleModel();
		if (calendarioMontagem != null) {

			for (CalendarioPedidos calendarioPedidos : calendarioMontagem) {
				DefaultScheduleEvent event = new DefaultScheduleEvent();

				event.setDescription(
						calendarioPedidos.getDescricaoCardapdioFicha() + "(" + calendarioPedidos.getQuantidade() + ")");
				event.setAllDay(true);
				event.setData(((CalendarioPedidosID) calendarioPedidos.getId()).getDataEntrega());
				event.setStartDate(((CalendarioPedidosID) calendarioPedidos.getId()).getDataEntrega());
				event.setEndDate(((CalendarioPedidosID) calendarioPedidos.getId()).getDataEntrega());
				event.setTitle(
						calendarioPedidos.getDescricaoCardapdioFicha() + "(" + calendarioPedidos.getQuantidade() + ")");
				event.setEditable(true);

				eventModel.addEvent(event);
			}

		}

	}

	public String getCor() {
		
		if (getEntity().getQtdTotalPedisoNaoConfirmados() !=null && getEntity().getQtdTotalPedisoNaoConfirmados() > 0)
			return "red";
		else
			return "green";
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
