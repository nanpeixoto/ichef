package br.com.ichef.controler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ichef.arquitetura.controller.BaseController;
import br.com.ichef.model.Empresa;
import br.com.ichef.model.VwCarteiraFormaPagamento;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.VwCarteiraFormaPagamentoService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.util.Util;

@Named
@ViewScoped
public class RelCarteiraFormaPagamentoController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private VwCarteiraFormaPagamentoService service;

	@Inject
	private EmpresaService empresaService;

	private VwCarteiraFormaPagamento entity;

	private List<VwCarteiraFormaPagamento> lista = new ArrayList<VwCarteiraFormaPagamento>();
	private List<VwCarteiraFormaPagamento> listaFiltro = new ArrayList<VwCarteiraFormaPagamento>();
	private List<Empresa> empresas = new ArrayList<>();

	private boolean agrupamento;

	// relatorio
	private Date dataInicial;
	private Date dataFinal;
	private Empresa empresa;

	public void imprimir(Boolean excel) {
		if (getDataInicial() == null || getDataFinal() == null) {
			FacesUtil.addInfoMessage(getRequiredMessage("Data"));
			return;
		} else {

			try {
				lista = /* service.findByParameters(new VwCarteiraFormaPagamento(), visitor); */
						service.findByParameters(getDataInicial(), getDataFinal(), empresa.getId());

			} catch (Exception e) {
				FacesUtil.addErroMessage("Erro ao obter os dados do relatório");
			}

			if (lista.size() == 0) {
				FacesUtil.addErroMessage("Nenhum dado encontrado");
			} else {
				try {
					setParametroReport(REPORT_PARAM_LOGO, getImagem(LOGO));
					setParametroReport("pDataInicio", Util.formataData(getDataInicial()));
					setParametroReport("pDataFinal", Util.formataData(getDataFinal()));
					setParametroReport("pCdEmpresa", new Integer(getEmpresa().getId().toString()));
					if (!agrupamento) {
						if (excel) {
							geraRelatorioXLS(lista, "CarteiraFormaPagamentoData");
						} else {
							escreveRelatorioPDF("CarteiraFormaPagamentoData", true, lista);
						}
					} else if (excel)
						geraRelatorioXLS(service.getConnection(), "CarteiraFormaPagamento");
					else
						escreveRelatorioPDF("CarteiraFormaPagamento", true, service.getConnection());

				} catch (Exception e) {
					e.printStackTrace();
					FacesUtil.addErroMessage("Erro ao gerar o relatório");
				}
			}

		}

	}

	public void inicializar() {

	}

	@PostConstruct
	public void init() {
		setAgrupamento(true);
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

	public VwCarteiraFormaPagamentoService getService() {
		return service;
	}

	public void setService(VwCarteiraFormaPagamentoService service) {
		this.service = service;
	}

	public VwCarteiraFormaPagamento getEntity() {
		return entity;
	}

	public void setEntity(VwCarteiraFormaPagamento entity) {
		this.entity = entity;
	}

	public List<VwCarteiraFormaPagamento> getLista() {
		return lista;
	}

	public void setLista(List<VwCarteiraFormaPagamento> lista) {
		this.lista = lista;
	}

	public List<VwCarteiraFormaPagamento> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<VwCarteiraFormaPagamento> listaFiltro) {
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

	public boolean isAgrupamento() {
		return agrupamento;
	}

	public void setAgrupamento(boolean agrupamento) {
		this.agrupamento = agrupamento;
	}

}
