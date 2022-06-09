package it.polimi.tiw.dump_inspector.bean;

public class Utente {
	private int idUtente;
	private String username;
	private String password;
	private String email;
	private boolean manager;
	private String esperienza;
	private String foto;
	
	public Utente() {}
	public Utente(int idUtente,String username,String password,String email,boolean manager,String esperienza,String foto) 
	{
		this.setIdUtente(idUtente);
		this.setUsername(username);
		this.setPassword(password);
		this.setEmail(email);
		this.setManager(manager);
		this.setEsperienza(esperienza);
		this.setFoto(foto);
	}
	public int getIdUtente() {
		return idUtente;
	}
	/**
	 * @param idManager the idManager to set
	 */
	public void setIdUtente(int idUtente) {
		this.idUtente = idUtente;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the esperienza
	 */
	public String getEsperienza() {
		return esperienza;
	}
	/**
	 * @param esperienza the esperienza to set
	 */
	public void setEsperienza(String esperienza) {
		this.esperienza = esperienza;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public boolean isManager() {
		return manager;
	}
	public void setManager(boolean manager) {
		this.manager = manager;
	}

}
