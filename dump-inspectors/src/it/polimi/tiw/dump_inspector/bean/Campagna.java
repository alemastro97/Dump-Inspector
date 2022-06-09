package it.polimi.tiw.dump_inspector.bean;

public class Campagna {
	private int idCampagna;
	private String committente;
	private String stato;
	private String nome;
	
	public Campagna() {}
	public Campagna(int idCampagna,String nome,String committente,String stato) 
	{
		this.setIdCampagna(idCampagna);
		this.setNome(nome);
		this.setCommittente(committente);
		this.setStato(stato);
	}
	public int getIdCampagna() {return idCampagna;}
	public void setIdCampagna(int idCampagna) {this.idCampagna = idCampagna;}
	/**
	 * @return the committente
	 */
	public String getCommittente() {return committente;}
	/**
	 * @param committente the committente to set
	 */
	public void setCommittente(String committente) {this.committente = committente;}
	/**
	 * @return the stato
	 */
	public String getStato() {return stato;}
	/**
	 * @param stato the stato to set
	 */
	public void setStato(String stato) {this.stato = stato;}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
