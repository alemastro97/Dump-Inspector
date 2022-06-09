package it.polimi.tiw.dump_inspector.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.dump_inspector.bean.Campagna;

public class CampagnaDAO {
	private Connection connection = null;

	public CampagnaDAO(Connection connection) 
	{
		this.connection = connection;
	}

	public void createCampagna(String nome, String committente,int idUtente) 
	{
		String query = "INSERT INTO dbproject.campagna (Nome,Committente,Stato,idUtente) VALUES (?,?,?,?)";
		PreparedStatement pstatement = null;

		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setString(1,nome);
			pstatement.setString(2,committente);
			pstatement.setString(3,"creato");
			pstatement.setInt(4,idUtente);
			pstatement.executeUpdate();
			pstatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startCampagna(int idCampagna) 
	{
		String query = "UPDATE dbproject.campagna SET Stato = ? WHERE idCampagna = ?";
		PreparedStatement pstatement = null;
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setString(1,"avviato");
			pstatement.setInt(2,idCampagna);
			pstatement.executeUpdate();
			pstatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Campagna findCampagna(int idCampagna) 
	{
		Campagna c = new Campagna();
		String query = "SELECT idCampagna,Nome,Committente,Stato FROM dbproject.campagna WHERE idCampagna = ? ";
		PreparedStatement pstatement = null;
		ResultSet result = null;

		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1,idCampagna);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				c.setIdCampagna(result.getInt("idCampagna"));
				c.setNome(result.getString("Nome"));
				c.setCommittente(result.getString("Committente"));
				c.setStato(result.getString("Stato"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	
	public List<Campagna> findManagerCampagna(int idUtente)
	{
		List<Campagna> c = new ArrayList<Campagna>();
		String query = "SELECT idCampagna,Nome,Committente,Stato FROM dbproject.campagna WHERE idUtente = ? ORDER BY idCampagna DESC";
		PreparedStatement pstatement = null;
		ResultSet result = null;

		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1,idUtente);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				Campagna campagna = new Campagna();
				campagna.setIdCampagna(result.getInt("idCampagna"));
				campagna.setNome(result.getString("Nome"));
				campagna.setCommittente(result.getString("Committente"));
				campagna.setStato(result.getString("Stato"));
				c.add(campagna);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return c;
	}

	public void closeCampagna(int idCampagna) 
	{
		String query = "UPDATE dbproject.campagna SET Stato = ? WHERE idCampagna = ?";
		PreparedStatement pstatement = null;

		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setString(1,"chiuso");
			pstatement.setInt(2,idCampagna);
			pstatement.executeUpdate();
			pstatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public List<Campagna> findWorkerCampagna(int idUtente) 
	{
		List<Campagna> c = new ArrayList<Campagna>();
		String query = "SELECT c.idCampagna,c.Nome,c.Committente,c.Stato  FROM dbproject.campagna as c LEFT JOIN dbproject.partecipazione as p ON c.idCampagna = p.idCampagna WHERE p.idUtente = ? AND c.Stato = 'avviato'" ;
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1,idUtente);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				Campagna campagna = new Campagna();
				campagna.setIdCampagna(result.getInt("idCampagna"));
				campagna.setNome(result.getString("Nome"));
				campagna.setCommittente(result.getString("Committente"));
				campagna.setStato(result.getString("Stato"));
				c.add(campagna);	
			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	
	public List<Campagna> findCampagnaNotJoin(int idUtente)
	{
		List<Campagna> c = new ArrayList<Campagna>();
		String query="SELECT c1.idCampagna,c1.Nome,c1.Committente,c1.Stato FROM dbproject.campagna as c1 WHERE c1.Stato = 'avviato' AND c1.idCampagna NOT IN(SELECT c2.idCampagna FROM dbproject.partecipazione as c2 WHERE c2.idUtente = ?)";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1,idUtente);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				Campagna campagna = new Campagna();
				campagna.setIdCampagna(result.getInt("idCampagna"));
				System.out.println(campagna.getIdCampagna());
				campagna.setNome(result.getString("Nome"));
				campagna.setCommittente(result.getString("Committente"));
				campagna.setStato(result.getString("Stato"));
				c.add(campagna);	
			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	
	public void updateCampagna(String nome, String committente, int idCampagna)
	{
		String query = "UPDATE dbproject.campagna SET Nome = ?, Committente = ? WHERE idCampagna = ?";
		PreparedStatement pstatement = null;
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setString(1,nome);
			pstatement.setString(2,committente);
			pstatement.setInt(3, idCampagna);
			pstatement.executeUpdate();
			pstatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


















