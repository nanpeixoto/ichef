package br.com.ichef.excepticon;

public class NegocioExcepticon extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public NegocioExcepticon(String msg) {
		super(msg);
	}

}
