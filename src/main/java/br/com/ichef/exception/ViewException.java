package br.com.ichef.exception;

import java.util.Date;

import br.com.ichef.util.Util;


public class ViewException extends AppException {

	
	private static final long serialVersionUID = 6498157868306431575L;
	
	//@Inject
	//private Logger logger;	
	
	public ViewException(){
		super();
		
		String mensagemLog = "****************************************************************\n\r"
				+ "ATIVIDADE AGENDADA\n\r"
				+ "DATA: " + Util.formatData(new Date(), "dd/MM/yyyy hh:mm:ss")
				+ "EVENTO: ATUALIZAR TABELA DE [TIPOS DE UNIDADE/UNIDADES/FERIADOS] (SIICO)\n\r"
				+ "EVENTO: CARREGAR TABELA DE MUNICÃ�PIOS (SIICO)\n\r"
				+ "ERRO: FONTE DE DADOS INCONSISTENTE\n\r"
				+ "****************************************************************";
		System.out.println(mensagemLog);
		//logger.info(mensagemLog);
		//logger.error(mensagemLog);
		
	}
	
	/**
     * Exception que carrega todas as mensagens de validaÃ§Ã£o da base de dados.
     * @param e
     */
	public ViewException(String e) {
		super(e);
		//logger.error(e);
		
	}
	
	/**
     * Exception que carrega todas as mensagens de validaÃ§Ã£o da base de dados.
     * @param e
     * @param classname
     */
	public ViewException(Throwable e, String classname) {
		
		super(e);
		
		String mensagemLog = " DATA: " +  Util.formatData(new Date(), "dd/MM/yyyy hh:mm:ss")  + "\n EVENTO: " + "\n ERRO: FALHA AO CONECTAR BANCO DE DADOS. ";
		//logger.error(mensagemLog);
		System.out.println(mensagemLog);
	}
	
	/**
     * Exception que carrega todas as mensagens de validaÃ§Ã£o da base de dados.
     * @param e
     */
	public ViewException(Throwable e) {
		super(e);
		System.out.println("Message: " + e.getMessage() + " Cause: " + getCause());
		//logger.error("Message: " + e.getMessage() + " Cause: " + getCause(), e);
	}
	
	/**
     * Exception que carrega todas as mensagens de validaÃ§Ã£o da base de dados.
     * @param messagem
     * @param e
     */
	public ViewException(String mensagem, Exception e) {		
		super(mensagem);
		System.out.println(mensagem);
		//logger.error(mensagem+" - "+e.getMessage(), e);
	}
	
	
	public ViewException(Exception e) {
		super(e);
		
		String mensagemLog = "****************************************************************\n\r"
				+ "ATIVIDADE AGENDADA\n\r"
				+ "DATA: " + Util.formatData(new Date(), "dd/MM/yyyy hh:mm:ss")
				+ "EVENTO: ATUALIZAR TABELA DE [TIPOS DE UNIDADE/UNIDADES/FERIADOS] (SIICO)\n\r"
				+ "EVENTO: CARREGAR TABELA DE MUNICÃ�PIOS (SIICO)\n\r"
				+ "REGISTROS NAO ATUALIZADOS - VIEW VAZIA\n\r"
				+ "****************************************************************";
		
		System.out.println(mensagemLog);
		//logger.info(mensagemLog);
		//logger.error(mensagemLog);
		//logger.error("Message: " + e.getMessage() + " Cause: " + getCause(), e);
	}
	
}
