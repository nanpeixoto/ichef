package br.com.ichef.controler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.model.Empresa;
import br.com.ichef.model.VwResumoDiarioPedidos;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.VwResumoDiarioPedidosService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class RelControelQuantitativoVendasController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private VwResumoDiarioPedidosService service;

	@Inject
	private EmpresaService empresaService;

	private VwResumoDiarioPedidos entity;

	private List<VwResumoDiarioPedidos> lista = new ArrayList<VwResumoDiarioPedidos>();
	private List<VwResumoDiarioPedidos> listaFiltro = new ArrayList<VwResumoDiarioPedidos>();
	private List<Empresa> empresas = new ArrayList<>();

	// relatorio
	private Date dataInicial;
	private Date dataFinal;
	private Empresa empresa;
	private Boolean analitico;
	
	private Long codigoCliente;
	
	public void imprimir() {
		if (getDataInicial() == null || getDataFinal() == null) {
			FacesUtil.addInfoMessage(getRequiredMessage("Data"));
			return;
		}
		if (getEmpresa() == null) { 
			FacesUtil.addInfoMessage(getRequiredMessage("Empresa"));
			return;
		} else {

			try {
				//lista = /* service.findByParameters(new VwResumoDiarioPedidos(), visitor); */
				//		service.findByParameters(getDataInicial(), getDataFinal(), empresa.getId());

			} catch (Exception e) {
				FacesUtil.addErroMessage("Erro ao obter os dados do relatório");
			}

			//if (lista.size() == 0) {
			//	FacesUtil.addErroMessage("Nenhum dado encontrado");
			//} else {
				try {
					setParametroReport(REPORT_PARAM_LOGO, getImagem(LOGO));
					SimpleDateFormat formatarData = new SimpleDateFormat("dd-MM-yyyy");
					setParametroReport("pDataInicio", formatarData.format(getDataInicial()));
					if(getCodigoCliente()!=null) {
						setParametroReport("pCodigoCliente",  getCodigoCliente());
						setParametroReport("pWhereCodCliente",  " and cd_cliente =  "+ getCodigoCliente());
					}else {
						setParametroReport("pWhereCodCliente", "");
					}
					setParametroReport("pDataFinal", formatarData.format(getDataFinal()));
					setParametroReport("pCodigoEmpresa",getEmpresa().getId());
					//escreveRelatorioPDF("ResumoDiarioPedidos", true, lista);
					if(!analitico)
						escreveRelatorioPDF("ControleQuantitativoVendas", true, service.getConnection());
					else 
						escreveRelatorioPDF("ControleQuantitativoVendasAnalitico", true, service.getConnection());
					
				
				} catch (Exception e) {
					e.printStackTrace();
					FacesUtil.addErroMessage("Erro ao gerar o relatório");
				}
			//}

		}

	}

	public void inicializar() {

	}

	@PostConstruct
	public void init() {
		obterListas();

		empresa = userLogado.getEmpresaLogada();

	}

	private void obterListas() {
		try {

			setEmpresas(empresaService.empresasUsuario(userLogado));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String Salvar() throws Exception {
		return null;

	}

	public String excluir() {
		return null;

	}

	public VwResumoDiarioPedidosService getService() {
		return service;
	}

	public void setService(VwResumoDiarioPedidosService service) {
		this.service = service;
	}

	public VwResumoDiarioPedidos getEntity() {
		return entity;
	}

	public void setEntity(VwResumoDiarioPedidos entity) {
		this.entity = entity;
	}

	public List<VwResumoDiarioPedidos> getLista() {
		return lista;
	}

	public void setLista(List<VwResumoDiarioPedidos> lista) {
		this.lista = lista;
	}

	public List<VwResumoDiarioPedidos> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<VwResumoDiarioPedidos> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Boolean getAnalitico() {
		return analitico;
	}

	public void setAnalitico(Boolean analitico) {
		this.analitico = analitico;
	}

	public Long getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(Long codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

}
