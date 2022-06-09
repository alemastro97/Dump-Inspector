package it.polimi.tiw.dump_inspector.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;

import it.polimi.tiw.dump_inspector.bean.Localita;

public class LocalitaDAO {
	private Connection connection = null;
	
	public LocalitaDAO(Connection connection) 
	{
		this.connection = connection;
	}
	
	public int createLocalita (double latitudine, double longitudine, String nome, String comune, String regione,int idCampagna) 
	{
		String query = "INSERT INTO dbproject.localita (Latitudine,Longitudine,Nome,Comune,Regione,idCampagna) VALUES (?,?,?,?,?,?)";
		PreparedStatement pstatement = null;
		int key = -1;
		
		try {
			pstatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pstatement.setDouble(1,latitudine);
			pstatement.setDouble(2,longitudine);
			pstatement.setString(3,nome);
			pstatement.setString(4,comune);
			pstatement.setString(5,regione);
			pstatement.setInt(6,idCampagna);
			
			pstatement.executeUpdate();
			
			ResultSet rs = pstatement.getGeneratedKeys();

            if (rs.next()) {
                key = rs.getInt(1);
            }

			
			pstatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  key;
		
	}
	
	public List<Localita> searchLocalitaCampagna(int idCampagna)
	{
		List<Localita> localita = new ArrayList<Localita>();
		String query = "SELECT idLocalita,Latitudine,Longitudine,Nome,Comune,Regione FROM dbproject.Localita WHERE idCampagna = ?";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1,idCampagna);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				Localita l = new Localita ();
				l.setIdLocalita(result.getInt("idLocalita"));
				l.setLatitudine(result.getDouble("Latitudine"));
				l.setLongitudine(result.getDouble("Longitudine"));
				l.setNome(result.getString("Nome"));
				l.setComune(result.getString("Comune"));
				l.setRegione(result.getString("Regione"));
				localita.add(l);
			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return localita;
	}
	
	public int getTotalLocalitaByCampagna(int idCampagna)
	{
		int total = 0;
		String query = "SELECT COUNT(*) FROM dbproject.Localita WHERE idCampagna = ? ";
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
	
	public ArrayList<Integer> getLocalitaSenzaAnnotazioni(int idCampagna) //ritorna gli id delle localita senza annotazioni (icona gialla)
	{
		ArrayList<Integer> ids = new ArrayList<Integer>();
		String query = "SELECT idLocalita "
				+ "FROM dbproject.localita AS L "
				+ "WHERE L.idCampagna = ? AND NOT EXISTS "
					+ "(SELECT * "
					+ "FROM dbproject.annotazione AS an JOIN dbproject.immagine AS im on an.idImmagine=im.idImmagine "
					+ "WHERE idLocalita = L.idLocalita)";
		
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1,idCampagna);
			result = pstatement.executeQuery();
			while(result.next()) 
			{

				ids.add(result.getInt("idLocalita"));

			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ids;
	}
	
	public ArrayList<Integer> getLocalitaConConflitti(int idCampagna) //ritorna gli id delle localita senza annotazioni (icona gialla)
	{
		ArrayList<Integer> ids = new ArrayList<Integer>();
		String query = "SELECT DISTINCT I.idLocalita " + 
				" FROM (((dbproject.annotazione AS A JOIN dbproject.immagine AS I on (A.idImmagine= I.idImmagine)) " + 
													"JOIN dbproject.localita AS L on (I.idLocalita = L.idLocalita)) " + 
													"JOIN dbproject.campagna AS C on (L.idCampagna = C.idCampagna)) " + 
				" WHERE  C.idCampagna = ? AND EXISTS " + 
				"		(SELECT * " + 
				"			FROM dbproject.annotazione AS A2 JOIN dbproject.immagine AS I2 on A2.idImmagine=I2.idImmagine " + 
				"			WHERE I2.idImmagine = I.idImmagine AND A2.Validita NOT LIKE A.Validita)";
		
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1,idCampagna);
			result = pstatement.executeQuery();
			while(result.next()) 
			{

				ids.add(result.getInt("idLocalita"));

			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ids;
	}

	public ArrayList<Localita> getDefaultLocalita() throws SQLException {
		ArrayList<Localita> Default_localita = new ArrayList<Localita>();
		String query = "SELECT * FROM dbproject.default_location";
		PreparedStatement pstatement = connection.prepareStatement(query);
		ResultSet result = null;
		
		try {
			result = pstatement.executeQuery();
			while(result.next()) 
			{

				Localita l = new Localita ();
				l.setLatitudine(result.getDouble("Latitudine"));
				l.setLongitudine(result.getDouble("Longitudine"));
				l.setNome(result.getString("Nome"));
				l.setComune(result.getString("Comune"));
				l.setRegione(result.getString("Regione"));
				Default_localita.add(l);
				
			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Default_localita ;
	}

	
}
