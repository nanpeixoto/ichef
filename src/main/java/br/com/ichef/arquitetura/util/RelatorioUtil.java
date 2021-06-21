package br.com.ichef.arquitetura.util;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

public class RelatorioUtil {

	private String nomeRelatorio;
	private Object document;

	public RelatorioUtil(String nomeRelatorio, Object document) {
		this.nomeRelatorio = nomeRelatorio;
		this.document = document;
	}

	public void preExportar() {
		HSSFWorkbook wb = (HSSFWorkbook) document;

		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow header = sheet.getRow(0);
		Short df = (Short) null;
		df = wb.createDataFormat().getFormat("#0");
		HSSFCellStyle estiloNumero = getEstiloLinhasTabela(wb, df);

		df = wb.createDataFormat().getFormat("R$ #,##0.00");

		HSSFCellStyle estiloMoeda = getEstiloLinhasTabela(wb, df);

		HSSFCellStyle estiloLetra = getEstiloLinhasTabela(wb, null);

		// HEAD DA COLUNA
		for (int i = 0; i < header.getLastCellNum(); i++) {
			for (int j = 0; j < header.getPhysicalNumberOfCells(); j++) {
				HSSFCell cell = header.getCell(j);
				cell.setCellStyle(getEstilhoTituloColuna(wb));
			}
		}

		int quantidadeColunas = 0;

		// DEFINICAO DO HEAD DA TABELA
		HSSFRow cabecalhoTabela = sheet.getRow(0);
		for (int j = 0; j < cabecalhoTabela.getPhysicalNumberOfCells(); j++) {
			HSSFCell cell = cabecalhoTabela.getCell(j);
			cell.setCellStyle(getEstilhoTituloColuna(wb));
			quantidadeColunas++;
		}

		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			HSSFRow row = sheet.getRow(i);
			for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
				HSSFCell cell = row.getCell(j);

				if (NumberUtils.isNumber(cell.getStringCellValue())) {

					cell.setCellValue(Integer.parseInt(cell.getStringCellValue().replace("R$", "")));
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellStyle(estiloNumero);
				} else if (cell.getStringCellValue().contains("R$")) {
					try {

						cell.setCellValue(Double.parseDouble(cell.getStringCellValue().replace("R$", "")
								.replace(".", "").replace(",", ".").replace(" ", "")));
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellStyle(estiloMoeda);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					if (cell.getStringCellValue().contains("<br>")) {
						cell.setCellValue(cell.getStringCellValue().replace("<br>", " - "));
					}
					cell.setCellStyle(estiloLetra);
				}

			}
		}

		sheet.shiftRows(0, sheet.getLastRowNum(), 2, true, true);

		criarCabecalhoPadraoRelatorio(sheet, wb, quantidadeColunas);

		criarCabecalhoNomeRelatorio(sheet, wb, quantidadeColunas);

		sheet = wb.getSheetAt(0);
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);
		sheet.autoSizeColumn(7);
		sheet.autoSizeColumn(8);
		sheet.autoSizeColumn(9);

	}

	private HSSFCellStyle getEstilhoTituloColuna(HSSFWorkbook wb) {
		HSSFCellStyle headerEstilo = wb.createCellStyle();
		HSSFPalette palette = wb.getCustomPalette();
		palette.setColorAtIndex(HSSFColor.GREY_40_PERCENT.index, (byte) 191, // RGB red
				(byte) 191, // RGB green
				(byte) 191 // RGB blue
		);
		headerEstilo.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		headerEstilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerEstilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerEstilo.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
		headerEstilo.setBottomBorderColor(HSSFColor.BLACK.index);
		headerEstilo.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		headerEstilo.setLeftBorderColor(HSSFColor.BLACK.index);
		headerEstilo.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		headerEstilo.setRightBorderColor(HSSFColor.BLACK.index);
		headerEstilo.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		headerEstilo.setTopBorderColor(HSSFColor.BLACK.index);
		headerEstilo.setFont(getFonteCalibriNegritoTamanho11(wb));
		return headerEstilo;
	}

	private HSSFCellStyle getEstiloLinhasTabela(HSSFWorkbook wb, Short df) {
		HSSFCellStyle linhaEstiloBrancoSemNegrito = wb.createCellStyle();
		HSSFPalette palette = wb.getCustomPalette();
		palette.setColorAtIndex(HSSFColor.GREY_25_PERCENT.index, (byte) 242, // RGB red
				(byte) 242, // RGB green
				(byte) 242 // RGB blue
		);
		if (df != null)
			linhaEstiloBrancoSemNegrito.setDataFormat(df);
		/*
		 * linhaEstiloBrancoSemNegrito.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.
		 * index);
		 * linhaEstiloBrancoSemNegrito.setFillPattern(CellStyle.SOLID_FOREGROUND);
		 * linhaEstiloBrancoSemNegrito.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 * linhaEstiloBrancoSemNegrito.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
		 * linhaEstiloBrancoSemNegrito.setBottomBorderColor(HSSFColor.BLACK.index);
		 * linhaEstiloBrancoSemNegrito.setBorderRight(HSSFCellStyle.BORDER_DOTTED);
		 * linhaEstiloBrancoSemNegrito.setRightBorderColor(HSSFColor.BLACK.index);
		 * linhaEstiloBrancoSemNegrito.setVerticalAlignment(HSSFCellStyle.
		 * VERTICAL_JUSTIFY);
		 */
		return linhaEstiloBrancoSemNegrito;
	}

	private HSSFFont getFonteCalibriNegritoTamanho11(HSSFWorkbook wb) {
		HSSFFont fontCalibriNegritoTamanho11 = wb.createFont();
		fontCalibriNegritoTamanho11.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		fontCalibriNegritoTamanho11.setFontHeightInPoints((short) 11);
		fontCalibriNegritoTamanho11.setFontName("Calibri");
		return fontCalibriNegritoTamanho11;
	}

	private void criarCabecalhoNomeRelatorio(HSSFSheet sheet, HSSFWorkbook wb, int quantidadeColunas) {
		HSSFRow row2 = sheet.createRow(1);
		HSSFCell cell2 = row2.createCell(0);
		cell2.setCellValue(new HSSFRichTextString(nomeRelatorio));
		cell2.setCellStyle(getEstiloHeadNomeRelatorio(wb));

		for (int j = 1; j < row2.getPhysicalNumberOfCells(); j++) {
			HSSFCell celula2 = row2.getCell(j);
			celula2.setCellStyle(getEstiloHeadNomeRelatorio(wb));
		}

		CellRangeAddress region2 = new CellRangeAddress(1, 1, 0, (quantidadeColunas - 1));
		cleanBeforeMergeOnValidCells(row2.getSheet(), region2, getEstiloHeadNomeRelatorio(wb));
		row2.getSheet().addMergedRegion(region2);
	}

	private void criarCabecalhoPadraoRelatorio(HSSFSheet sheet, HSSFWorkbook wb, int quantidadeColunas) {

		HSSFRow row = sheet.createRow(0);
		row.setHeight((short) 700);

		HSSFCell cell1 = row.createCell(0);
		cell1.setCellValue(new HSSFRichTextString("iChef"));
		cell1.setCellStyle(getEstiloHeadNomeSistema(wb));

		for (int j = 1; j < row.getPhysicalNumberOfCells(); j++) {
			HSSFCell celula = row.getCell(j);
			celula.setCellStyle(getEstiloHeadNomeSistema(wb));
		}

		/* MESCLA AS COLUNAS */
		CellRangeAddress region = new CellRangeAddress(0, 0, 0, (quantidadeColunas - 1));
		cleanBeforeMergeOnValidCells(row.getSheet(), region, getEstiloHeadNomeSistema(wb));
		row.getSheet().addMergedRegion(region);
	}

	private HSSFCellStyle getEstiloHeadNomeSistema(HSSFWorkbook wb) {
		HSSFCellStyle nomeSistemaEstilo = wb.createCellStyle();
		HSSFPalette palette = wb.getCustomPalette();
		palette.setColorAtIndex(HSSFColor.GREY_80_PERCENT.index, (byte) 166, // RGB red
				(byte) 166, // RGB green
				(byte) 166 // RGB blue
		);
		nomeSistemaEstilo.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
		nomeSistemaEstilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		nomeSistemaEstilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		nomeSistemaEstilo.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		nomeSistemaEstilo.setBottomBorderColor(HSSFColor.BLACK.index);
		nomeSistemaEstilo.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		nomeSistemaEstilo.setLeftBorderColor(HSSFColor.BLACK.index);
		nomeSistemaEstilo.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		nomeSistemaEstilo.setRightBorderColor(HSSFColor.BLACK.index);
		nomeSistemaEstilo.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		nomeSistemaEstilo.setTopBorderColor(HSSFColor.BLACK.index);
		nomeSistemaEstilo.setFont(getFonteCalibriNegritoTamanho11(wb));
		nomeSistemaEstilo.setAlignment(CellStyle.ALIGN_CENTER);
		nomeSistemaEstilo.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return nomeSistemaEstilo;
	}

	private HSSFCellStyle getEstiloHeadNomeRelatorio(HSSFWorkbook wb) {
		HSSFCellStyle nomeSistemaEstilo = wb.createCellStyle();
		HSSFPalette palette = wb.getCustomPalette();
		palette.setColorAtIndex(HSSFColor.GREY_50_PERCENT.index, (byte) 217, // RGB red
				(byte) 217, // RGB green
				(byte) 217 // RGB blue
		);
		nomeSistemaEstilo.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
		nomeSistemaEstilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		nomeSistemaEstilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		nomeSistemaEstilo.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		nomeSistemaEstilo.setBottomBorderColor(HSSFColor.BLACK.index);
		nomeSistemaEstilo.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		nomeSistemaEstilo.setLeftBorderColor(HSSFColor.BLACK.index);
		nomeSistemaEstilo.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		nomeSistemaEstilo.setRightBorderColor(HSSFColor.BLACK.index);
		nomeSistemaEstilo.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		nomeSistemaEstilo.setTopBorderColor(HSSFColor.BLACK.index);
		nomeSistemaEstilo.setFont(getFonteCalibriNegritoTamanho11(wb));
		nomeSistemaEstilo.setAlignment(CellStyle.ALIGN_CENTER);
		nomeSistemaEstilo.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return nomeSistemaEstilo;
	}

	private void cleanBeforeMergeOnValidCells(HSSFSheet sheet, CellRangeAddress region, HSSFCellStyle cellStyle) {
		for (int rowNum = region.getFirstRow(); rowNum <= region.getLastRow(); rowNum++) {
			HSSFRow row = sheet.getRow(rowNum);
			if (row == null) {
				sheet.createRow(rowNum);
			}
			for (int colNum = region.getFirstColumn(); colNum <= region.getLastColumn(); colNum++) {
				HSSFCell currentCell = row.getCell(colNum);
				if (currentCell == null) {
					currentCell = row.createCell(colNum);
				}

				currentCell.setCellStyle(cellStyle);

			}
		}

	}

	public String getNOME_RELATORIO() {
		return nomeRelatorio;
	}

	public void setNOME_RELATORIO(String nOME_RELATORIO) {
		nomeRelatorio = nOME_RELATORIO;
	}

}
