package it.polimi.tiw.dump_inspector.bean;

public class Localita {
		private int idLocalita;
		private double latitudine;
		private double longitudine;
		private String nome;
		private String comune;
		private String regione;
		
		public Localita() {}
		public Localita(int idLocalita,double latitudine,double longitudine,String nome,String comune,String regione) 
		{
			this.setIdLocalita(idLocalita);
			this.setLatitudine(latitudine);
			this.setLongitudine(longitudine);
			this.setNome(nome);
			this.setComune(comune);
			this.setRegione(regione);
		}
		public int getIdLocalita() {
			return idLocalita;
		}
		public void setIdLocalita(int idLocalita) {
			this.idLocalita = idLocalita;
		}
		public double getLatitudine() {
			return latitudine;
		}
		public void setLatitudine(double latitudine) {
			this.latitudine = latitudine;
		}
		public double getLongitudine() {
			return longitudine;
		}
		public void setLongitudine(double longitudine) {
			this.longitudine = longitudine;
		}
		public String getNome() {
			return nome;
		}
		public void setNome(String nome) {
			this.nome = nome;
		}
		public String getComune() {
			return comune;
		}
		public void setComune(String comune) {
			this.comune = comune;
		}
		public String getRegione() {
			return regione;
		}
		public void setRegione(String regione) {
			this.regione = regione;
		}
		
		
}
