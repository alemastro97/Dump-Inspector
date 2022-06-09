package it.polimi.tiw.dump_inspector.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import it.polimi.tiw.dump_inspector.bean.Annotazione;

public class AnnotazioneDAO {
	private Connection connection = null;
	
	public AnnotazioneDAO(Connection connection) 
	{
		this.connection = connection;
	}
	
	public void createAnnotazione(String date1, boolean validita, String fiducia, String note,int idUtente, int idImmagine) 
	{
		String query = "INSERT INTO dbproject.annotazione (DataCreazione,Validita,Fiducia,Note,idUtente,idImmagine) VALUES (?,?,?,?,?,?)";
		PreparedStatement pstatement = null;
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setString(1, date1);
			pstatement.setBoolean(2,validita);
			pstatement.setString(3,fiducia);
			pstatement.setString(4,note);
			pstatement.setInt(5,idUtente);
			pstatement.setInt(6,idImmagine);
			pstatement.executeUpdate();
			pstatement.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> findUserByAnnotazione(int idAnnotazione)
	{
		ArrayList<String> userData = new ArrayList<String>();
		String query = "SELECT Username,Email,Exp,Foto FROM dbproject.utente AS u NATURAL JOIN dbproject.annotazione AS a WHERE idAnnotazione = ?";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1,idAnnotazione);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				userData.add(result.getString("Username"));
				userData.add(result.getString("Email"));			
				userData.add(result.getString("Exp"));
				byte[] imgData = result.getBytes("Foto");
				if(imgData != null) {
				String encodedImg=Base64.getEncoder().encodeToString(imgData);
				userData.add(encodedImg);}else {userData.add("");}
				
			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userData;
	}
	
	
	public List<Annotazione> findAllImmagineAnnotazione(int idImmagine)
	{
		List<Annotazione> annotazioni = new ArrayList<Annotazione>();
		String query = "SELECT idAnnotazione, DataCreazione,Validita,Fiducia,Note FROM dbproject.annotazione WHERE idImmagine = ?";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1,idImmagine);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				Annotazione a = new Annotazione();
				a.setIdAnnotazione(result.getInt("idAnnotazione"));//
				a.setDatacreazione(result.getDate("DataCreazione"));
				a.setValidita(result.getBoolean("Validita"));
				a.setFiducia(result.getString("Fiducia"));
				a.setNote(result.getString("Note"));
				annotazioni.add(a);
			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return annotazioni;
	}
	
	public Annotazione findUserAnnotation(int idImmagine,int idUtente) 
	{
		Annotazione a = null;
		String query = "SELECT DataCreazione,Validita,Fiducia,Note FROM dbproject.annotazione WHERE idImmagine = ? AND idUtente = ?";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1,idImmagine);
			pstatement.setInt(2,idUtente);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				a = new Annotazione();
				a.setDatacreazione(result.getDate("DataCreazione"));
				a.setValidita(result.getBoolean("Validita"));
				a.setFiducia(result.getString("Fiducia"));
				a.setNote(result.getString("Note"));
			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}
	
	public ArrayList<Annotazione> findUserOnAnnotation(int idImmagine) 
	{
		Annotazione a = null;
		String query = "SELECT idAnnotazione,DataCreazione,Validita,Fiducia,Note FROM dbproject.annotazione JOIN dbproject.utente ON dbproject.utente.idUtente = dbproject.annotazione.idUtente WHERE idImmagine = ?";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		ArrayList<Annotazione> annotations = new ArrayList <Annotazione>();
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1,idImmagine);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				a = new Annotazione();
				a.setIdAnnotazione(result.getInt("idAnnotazione"));
				a.setDatacreazione(result.getDate("DataCreazione"));
				a.setValidita(result.getBoolean("Validita"));
				a.setFiducia(result.getString("Fiducia"));
				a.setNote(result.getString("Note"));
				annotations.add(a);			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return annotations;
	}
	
	public int getTotalAnnotazioniByCampagna(int idCampagna)
	{
		int total = 0;
		String query = "SELECT COUNT(*) FROM (dbproject.Localita as L JOIN dbproject.immagine as imm on L.idLocalita = imm.idLocalita) JOIN dbproject.annotazione as ann on imm.idImmagine = ann.idImmagine WHERE L.idCampagna = ? ";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1,idCampagna);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				total = result.getInt(1);
			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return total;
	}
	
	public float getAverageAnnotazioniPerImmagineByCampagna(int idCampagna) 
	{
		int total = 0;
		float average = 0;
		ArrayList<Integer> counts = new ArrayList<Integer>();
		String query = "SELECT COUNT(*) "
				+ "FROM (dbproject.Localita as L JOIN dbproject.immagine as imm on L.idLocalita = imm.idLocalita) "
				+ "	JOIN dbproject.Annotazione as ann on imm.idImmagine = ann.idImmagine"
				+ " WHERE L.idCampagna= ? GROUP BY imm.idImmagine";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1, idCampagna);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				counts.add(result.getInt(1));
				total+=result.getInt(1);
			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(counts.size()!=0)
			average = (float) total/(counts.size());
		else
			average = 0;
		return average;
	}
}
