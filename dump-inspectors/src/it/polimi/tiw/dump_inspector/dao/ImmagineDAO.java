package it.polimi.tiw.dump_inspector.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import it.polimi.tiw.dump_inspector.bean.Immagine;

public class ImmagineDAO {
	private Connection connection = null;
	
	public ImmagineDAO(Connection connection) 
	{
		this.connection = connection;
	}
	
	public Immagine getImmagineByID(int id)
	{
		Immagine imm = null;
		String query = "SELECT * FROM dbproject.Immagine WHERE idImmagine = ? ";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1,id);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				imm = new Immagine();
				imm.setIdImmagine(result.getInt("IdImmagine"));
				byte[] imgData = result.getBytes("image");
				String encodedImg=Base64.getEncoder().encodeToString(imgData);
				imm.setImage(encodedImg);
				imm.setProvenienza(result.getString("Provenienza"));
				imm.setRisoluzione(result.getString("Risoluzione"));
				imm.setDatarecupero(result.getDate("DataRecupero"));
			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return imm;
	}
	
	
	public int insertImmagine(String provenienza, String datarecupero, InputStream imageStream, String risoluzione, int idLocalita) 
	{
		String query = "INSERT INTO dbproject.immagine (Provenienza,DataRecupero,Image,Risoluzione,idLocalita) VALUES(?,?,?,?,?)";
		PreparedStatement pstatement = null;
		int key = 0;
		try {
			 pstatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pstatement.setString(1,provenienza);
			pstatement.setString(2,datarecupero);
			pstatement.setBlob(3, imageStream);
			pstatement.setString(4,risoluzione);
			pstatement.setInt(5,idLocalita);
            ResultSet rs = pstatement.getGeneratedKeys();

            if (rs.next()) {
                key = rs.getInt(1);
            }
			pstatement.executeUpdate();
			pstatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;
	}
	
	public int getTotalImmaginiByCampagna(int idCampagna)
	{
		int total = 0;
		String query = "SELECT COUNT(*) FROM dbproject.Localita as L JOIN dbproject.immagine as imm on L.idLocalita = imm.idLocalita WHERE idCampagna = ? ";
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
	
	public float getAverageImmaginiPerLocalitaByCampagna(int idCampagna) 
	{
		int total = 0;
		float average = 0;
		ArrayList<Integer> counts = new ArrayList<Integer>();
		String query = "SELECT COUNT(*) FROM dbproject.Localita as L JOIN dbproject.immagine as imm on L.idLocalita = imm.idLocalita WHERE L.idCampagna= ? GROUP BY L.idLocalita";
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
		average = (float) total/(counts.size());
		return average;
	}
	
	public List<Immagine> findAllImmagineLocalita(int idLocalita)
	{
		List<Immagine> immagini = new ArrayList<Immagine>();
		String query = "SELECT idImmagine,Provenienza,DataRecupero,Risoluzione,image FROM dbproject.immagine WHERE idLocalita = ?";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1,idLocalita);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				Immagine i = new Immagine();
				i.setIdImmagine(result.getInt("idImmagine"));
				i.setProvenienza(result.getString("Provenienza"));
				i.setDatarecupero(result.getDate("DataRecupero"));
				i.setRisoluzione(result.getString("Risoluzione"));
				
				byte[] imgData = result.getBytes("image");
				String encodedImg=Base64.getEncoder().encodeToString(imgData);
				i.setImage(encodedImg);
				immagini.add(i);
			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return immagini;
	}
	public int findLastImage()
	{

		String query = "SELECT idImmagine FROM dbproject.immagine ORDER BY idLocalita DESC LIMIT 1";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		int i = -1;
		try {
			pstatement = connection.prepareStatement(query);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				i = result.getInt("idImmagine");
			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}
	
	public ArrayList<Integer> getImmaginiConConflitti(int idCampagna) //ritorna gli id delle localita senza annotazioni (icona gialla)
	{
		ArrayList<Integer> ids = new ArrayList<Integer>();
		String query = "SELECT DISTINCT I.idImmagine " + 
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

				ids.add(result.getInt("idImmagine"));

			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ids;
	}
}
