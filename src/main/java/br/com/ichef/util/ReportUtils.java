package br.com.ichef.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

@SuppressWarnings("deprecation")
public class ReportUtils {
    public static final String REL_TIPO_PDF = "PDF";

    /**
     * Indicativo para geração no formato XLS.
     */
    public static final String REL_TIPO_XLS = "XLS";

    public static final String REL_TIPO_DOCX = "DOCX";

    /**
     * 
     */
    private JExcelApiExporter xlsExporter;
    private JRDocxExporter docxExporter;

    public ReportUtils() {
    }

    /**
     * @return the xlsExporter
     */

	private JExcelApiExporter getXlsExporter() {
        if (xlsExporter == null) {
            xlsExporter = new JExcelApiExporter();
        }
        return xlsExporter;
    }

    private JRDocxExporter getDocxExporter() {
        if (docxExporter == null) {
            docxExporter = new JRDocxExporter();
        }
        return docxExporter;
    }


	public URL getJarURLFromURLEntry(URL url, String entry) throws Exception {
        String urlEntry = entry; 
        URL jarUrl;
        String file = url.getFile();
        if (!urlEntry.startsWith("/")) {
            urlEntry = "/" + urlEntry;
        }
        file = file.substring(0, file.length() - urlEntry.length());
        if (file.endsWith("!")) {
            file = file.substring(0, file.length() - 1);
        } else if (file.endsWith("!/")) {
            file = file.substring(0, file.length() - 2);
        }
        if ("jar".equals(url.getProtocol())) {
            jarUrl = new URL(file);
        } else if ("code-source".equals(url.getProtocol())) {
            // patch starts
            jarUrl = new File(file).toURL();
        } else {
            // patch ends
            jarUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(),
                    file);
        }
        return jarUrl;
    }

    /**
     * Converte o <code>stream</code> informado para um array de bytes de um arquivo 
     * do tipo XLS. 
     * 
     * @param stream Stream do relatório jasper
     * @param parametros Parâmetros a ser enviado para o relatório
     * @param dataSource Dados utilizados no preenchimento do relatório
     * @return
     * @throws JRException Caso haja algum erro no preenchimento do relatório
     */
    private byte[] toXLS(InputStream stream, Map<String, Object> parametros,
            Object dataSource) throws JRException {
        ByteArrayOutputStream streamArray = new ByteArrayOutputStream();
        JasperPrint print = null;

        if (dataSource instanceof JRDataSource) {
            print = JasperFillManager.fillReport(stream, parametros, (JRDataSource) dataSource);
        } else {
            print = JasperFillManager.fillReport(stream, parametros, (Connection) dataSource);
        }

        getXlsExporter().setParameter(JRExporterParameter.JASPER_PRINT, print);
        getXlsExporter().setParameter(JRExporterParameter.OUTPUT_STREAM, streamArray);
        getXlsExporter().exportReport();

        return streamArray.toByteArray();
    }

   
	private byte[] toDocx(InputStream stream, Map<String, Object> parametros,
            Object dataSource) throws JRException {
        ByteArrayOutputStream streamArray = new ByteArrayOutputStream();
        JasperPrint print = null;

        if (dataSource instanceof JRDataSource) {
            print = JasperFillManager.fillReport(stream, parametros, (JRDataSource) dataSource);
        } else {
            print = JasperFillManager.fillReport(stream, parametros, (Connection) dataSource);
        }

        getDocxExporter().setParameter(JRExporterParameter.JASPER_PRINT, print);
        getDocxExporter().setParameter(JRExporterParameter.OUTPUT_STREAM, streamArray);
        getDocxExporter().exportReport();

        return streamArray.toByteArray();
    }

    private byte[] obterRelatorio(String caminhoJasper, String arquivoJasper, HashMap<String, Object> parametros, List<?> colecaoDataSource, String tipo) throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource(caminhoJasper);
        URL resource = getJarURLFromURLEntry(url, caminhoJasper);

        @SuppressWarnings("resource")
		InputStream is = new JarFile(new File(resource.getFile())).getInputStream(new ZipEntry(caminhoJasper.concat("/").concat(arquivoJasper)));

        byte[] relatorio = null;

        JRDataSource ds = null;
        if (colecaoDataSource != null) {
            ds = new JRBeanCollectionDataSource(colecaoDataSource);
        } else {
            ds = new JREmptyDataSource();
        }

        if (tipo.equals(REL_TIPO_PDF)) {
            relatorio = JasperRunManager.runReportToPdf(is, parametros, ds);

        } else if (tipo.equalsIgnoreCase(REL_TIPO_XLS)) {
            relatorio = toXLS(is, parametros, ds);
        } else if (tipo.equalsIgnoreCase(REL_TIPO_DOCX)) {
            relatorio = toDocx(is, parametros, ds);
        }

        return relatorio;
    }

    public byte[] obterRelatorio(InputStream is, HashMap<String, Object> parametros, List<?> colecaoDataSource, String tipo) throws Exception {

        if (is == null) {
            throw new Exception("ReportUtils.obterRelatorio(InputStream, HashMap, List, String) : InputStream enviado é nulo");
        }

        byte[] relatorio = null;

        JRDataSource ds = null;
        if (colecaoDataSource != null) {
            ds = new JRBeanCollectionDataSource(colecaoDataSource);
        } else {
            ds = new JREmptyDataSource();
        }

        if (tipo.equals(REL_TIPO_PDF)) {
            relatorio = JasperRunManager.runReportToPdf(
                    is, parametros, ds instanceof JRBeanCollectionDataSource ? (JRBeanCollectionDataSource) ds : (JREmptyDataSource) ds);

        } else if (tipo.equalsIgnoreCase(REL_TIPO_XLS)) {
            relatorio = toXLS(is, parametros, ds);
        } else if (tipo.equalsIgnoreCase(REL_TIPO_DOCX)) {
            relatorio = toDocx(is, parametros, ds);
        }

        return relatorio;
    }

    public byte[] obterRelatorio(InputStream is, HashMap<String, Object> parametros, List<?> colecaoDataSource, String tipo, Connection conexao) throws Exception {
        if (is == null) {
            throw new Exception("ReportUtils.obterRelatorio(InputStream, HashMap, List, String) : InputStream enviado é nulo");
        }

        byte[] relatorio = null;

        if (tipo.equals(REL_TIPO_PDF)) {
            relatorio = JasperRunManager
                    .runReportToPdf(is, parametros, conexao);

        } else if (tipo.equalsIgnoreCase(REL_TIPO_XLS)) {
            relatorio = toXLS(is, parametros, conexao);
        } else if (tipo.equalsIgnoreCase(REL_TIPO_DOCX)) {
            relatorio = toDocx(is, parametros, conexao);
        }

        return relatorio;
    }

    public byte[] obterRelatorioPDF(String caminhoJasper, String arquivoJasper, HashMap<String, Object> parametros, List<?> colecaoDataSource) throws Exception {
        return obterRelatorio(caminhoJasper, arquivoJasper, parametros, colecaoDataSource, REL_TIPO_PDF);
    }

    public byte[] obterRelatorioPDF(String caminhoJasper, String arquivoJasper, HashMap<String, Object> parametros) throws Exception {
        return obterRelatorio(caminhoJasper, arquivoJasper, parametros, null, REL_TIPO_PDF);
    }

    /**
     * Retorna um array de bytes de um arquivo do tipo XLS correspondente ao jasper informado
     * através do <code>arquivoJasper</code>.
     * 
     * @param caminhoJasper
     * @param arquivoJasper
     * @param parametros
     * @param colecaoDataSource
     * @return
     * @throws Exception
     */
    public byte[] obterRelatorioXLS(String caminhoJasper, String arquivoJasper, HashMap<String, Object> parametros, List<?> colecaoDataSource) throws Exception {
        return obterRelatorio(caminhoJasper, arquivoJasper, parametros, colecaoDataSource, REL_TIPO_XLS);
    }

    /**
     * @param relatorio
     * @param nomeRelatorio
     * @param tipo
     * @param httpServletResponse
     * @throws Exception
     * @throws IOException
     */
    private void escreveNoResponse(byte[] relatorio, String nomeRelatorio, String tipo, HttpServletResponse httpServletResponse) throws Exception {

        if (tipo.equals(REL_TIPO_PDF)) {
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + nomeRelatorio + ".PDF\"");
        } else if (tipo.equals(REL_TIPO_XLS)) {
            httpServletResponse.setContentType("application/xls");
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + nomeRelatorio + ".xls\"");
        } else if (tipo.equals(REL_TIPO_DOCX)) {
            httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + nomeRelatorio + ".docx\"");
        }

        httpServletResponse.setContentLength(relatorio.length);
        httpServletResponse.getOutputStream().write(relatorio);
        httpServletResponse.getOutputStream().flush();
    }

    public void escreveNoResponsePDF(byte[] relatorio, String nomeRelatorio, HttpServletResponse httpServletResponse) throws Exception {
        escreveNoResponse(relatorio, nomeRelatorio, REL_TIPO_PDF, httpServletResponse);
    }

    /**
     * Adiciona o array de bytes <code>relatorio</code> como excel na resposta do Servlet.
     * 
     * @param relatorio array de bytes a ser escrito
     * @param nomeRelatorio nome de saída do arquivo no cliente
     * @param httpServletResponse objeto HTTP contendo o stream de resposta. 
     * @throws Exception
     * @throws IOException
     */
    public void escreveNoResponseXLS(byte[] relatorio, String nomeRelatorio, HttpServletResponse httpServletResponse) throws Exception {
        escreveNoResponse(relatorio, nomeRelatorio, REL_TIPO_XLS, httpServletResponse);
    }

    public void escreveNoResponseDOCX(byte[] relatorio, String nomeRelatorio, HttpServletResponse httpServletResponse) throws Exception {
        escreveNoResponse(relatorio, nomeRelatorio, REL_TIPO_DOCX, httpServletResponse);
    }
}
