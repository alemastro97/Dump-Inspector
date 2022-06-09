package it.polimi.tiw.dump_inspector.bean;

import java.sql.Date;

public class Annotazione {
	private int idAnnotazione;
	private Date datacreazione;
	private boolean validita;
	private String fiducia;
	private String note;
	
	public Annotazione() {}
	public Annotazione( int idAnnotazione, Date datacreazione, boolean validita, String fiducia, String note) 
	{
		 this.setIdAnnotazione(idAnnotazione);
		 this.setDatacreazione(datacreazione);
		 this.setValidita(validita);
		 this.setFiducia(fiducia);
		 this.setNote(note);
	}
	public int getIdAnnotazione() {
		return idAnnotazione;
	}
	public void setIdAnnotazione(int idAnnotazione) {
		this.idAnnotazione = idAnnotazione;
	}
	public boolean isValidita() {
		return validita;
	}
	public void setValidita(boolean validita) {
		this.validita = validita;
	}
	public Date getDatacreazione() {
		return datacreazione;
	}
	public void setDatacreazione(Date datacreazione) {
		this.datacreazione = datacreazione;
	}
	public String getFiducia() {
		return fiducia;
	}
	public void setFiducia(String fiducia) {
		this.fiducia = fiducia;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
