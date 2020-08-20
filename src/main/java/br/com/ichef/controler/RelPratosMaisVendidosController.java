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
import br.com.ichef.service.EmpresaService;
import br.com.ichef.util.FacesUtil;
import br.com.ichef.util.Util;

@Named
@ViewScoped
public class RelPratosMaisVendidosController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Inject
	private EmpresaService empresaService;

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
				setParametroReport(REPORT_PARAM_LOGO, getImagem(LOGO));
				setParametroReport("pDataInicio", Util.formataData(getDataInicial()));
				setParametroReport("pDataFinal", Util.formataData(getDataFinal()));
				setParametroReport("pCdEmpresa", new Integer(getEmpresa().getId().toString()));
				if (excel) {
					geraRelatorioXLS(empresaService.getConnection(), "PratosMaisVendidos");
				} else {
					escreveRelatorioPDF("PratosMaisVendidos", true, empresaService.getConnection());
				}
			} catch (Exception e) {
				e.printStackTrace();
				FacesUtil.addErroMessage("Erro ao gerar o relatório");
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
