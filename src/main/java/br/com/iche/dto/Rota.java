package br.com.iche.dto;

public class Rota {
  private float codigoLocalidade;
  private float codigoEntregador;
  private String localidade;
  private float ordem;
  private float quantidade;


 // Getter Methods 

  public float getCodigoLocalidade() {
    return codigoLocalidade;
  }

  public float getCodigoEntregador() {
    return codigoEntregador;
  }

  public String getLocalidade() {
    return localidade;
  }

  public float getOrdem() {
    return ordem;
  }

  public float getQuantidade() {
    return quantidade;
  }

 // Setter Methods 

  public void setCodigoLocalidade( float codigoLocalidade ) {
    this.codigoLocalidade = codigoLocalidade;
  }

  public void setCodigoEntregador( float codigoEntregador ) {
    this.codigoEntregador = codigoEntregador;
  }

  public void setLocalidade( String localidade ) {
    this.localidade = localidade;
  }

  public void setOrdem( float ordem ) {
    this.ordem = ordem;
  }

  public void setQuantidade( float quantidade ) {
    this.quantidade = quantidade;
  }
}