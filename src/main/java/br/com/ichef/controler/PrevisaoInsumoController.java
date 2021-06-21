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
import br.com.ichef.model.Cardapio;
import br.com.ichef.model.Empresa;
import br.com.ichef.model.VwPrevisaoInsumo;
import br.com.ichef.model.VwPrevisaoInsumoID;
import br.com.ichef.service.EmpresaService;
import br.com.ichef.service.PrevisaoInsumoService;
import br.com.ichef.util.FacesUtil;

@Named
@ViewScoped
public class PrevisaoInsumoController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private PrevisaoInsumoService service;

	@Inject
	private EmpresaService empresaService;

	private Long id;

	// relatorio
	List<VwPrevisaoInsumo> previsaoInsumo = new ArrayList<>();
	private List<Empresa> empresas = new ArrayList<>();
	private Empresa empresa;
	private Date dataInicial;
	private Date dataFinal;

	public void inicializar() {

	}

	@PostConstruct
	public void init() {
		obterListas();

	}

	private void obterListas() {
		try {

			empresas = empresaService.listAll(true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void imprimir() {
		if (getEmpresa() == null) {
			FacesUtil.addInfoMessage(getRequiredMessage("Empresa"));
			return;
		}
		if (getDataInicial() == null || getDataFinal() == null) {
			FacesUtil.addInfoMessage(getRequiredMessage("Data"));
			return;
		} else {

			VwPrevisaoInsumoID id = new VwPrevisaoInsumoID();
			id.setCodigoEmpresa(getEmpresa().getId());

			VwPrevisaoInsumo filter = new VwPrevisaoInsumo(); 
			filter.setId(id);

			try {
				previsaoInsumo = service.findPrevisao(getEmpresa(), getDataInicial(), getDataFinal());

			} catch (Exception e) {
				FacesUtil.addErroMessage("Erro ao obter os dados do relatório");
			}

			if (previsaoInsumo.size() == 0) {
				FacesUtil.addErroMessage("Nenhum dado encontrado");
			} else {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				try {
					setParametroReport(REPORT_PARAM_LOGO, getImagem(LOGO));
					setParametroReport("pDescricaoEmpresa", getEmpresa().getNomeFantasia());
					setParametroReport("pDataInicio", format.format(getDataInicial()));
					setParametroReport("pDataFinal", format.format( getDataFinal()) );
					
					escreveRelatorioPDF("PrevisaoInsumos", true, previsaoInsumo);
				} catch (Exception e) {
					e.printStackTrace();
					FacesUtil.addErroMessage("Erro ao gerar o relatório");
				}
			}

		}

	}

	public PrevisaoInsumoService getService() {
		return service;
	}

	public void setService(PrevisaoInsumoService service) {
		this.service = service;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

}
