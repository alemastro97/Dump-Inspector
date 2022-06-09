package it.polimi.tiw.dump_inspector.bean;

import java.sql.Date;

public class Immagine {
	private int idImmagine;
	private String provenienza;
	private String image;
	private Date datarecupero;
	private String risoluzione;
	
	public Immagine() {}
	
	public Immagine(int idImmagine, String provenienza, String image, Date datarecupero, String risoluzione)
	{
		this.setIdImmagine(idImmagine);
		this.setProvenienza(provenienza);
		this.setDatarecupero(datarecupero);
		this.setRisoluzione(risoluzione);
		this.setImage(image);
	}
	public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIdImmagine() {
		return idImmagine;
	}
	public void setIdImmagine(int idImmagine) {
		this.idImmagine = idImmagine;
	}
	public String getProvenienza() {
		return provenienza;
	}
	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}
	public Date getDatarecupero() {
		return datarecupero;
	}
	public void setDatarecupero(Date datarecupero) {
		this.datarecupero = datarecupero;
	}
	public String getRisoluzione() {
		return risoluzione;
	}
	public void setRisoluzione(String risoluzione) {
		this.risoluzione = risoluzione;
	}
}
