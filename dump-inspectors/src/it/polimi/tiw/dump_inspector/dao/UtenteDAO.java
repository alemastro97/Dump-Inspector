package it.polimi.tiw.dump_inspector.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import it.polimi.tiw.dump_inspector.bean.Utente;

public class UtenteDAO {
private Connection connection = null;
	public UtenteDAO(Connection connection) 
	{
		this.connection = connection;
	}
	/**Metodo di creazione dell'utente nel database**/
	public void createUser(String username,String password,String email,boolean manager,String esperienza, InputStream foto) 
	{
		String query;
		if(manager) { query = "INSERT INTO dbproject.utente (Username,Password,Email,manager) VALUES (?,?,?,?)";}
		else {query = "INSERT INTO dbproject.utente (Username,Password,Email,manager,Exp,Foto) VALUES (?,?,?,?,?,?)";}
		PreparedStatement pstatement = null;
		try {
			pstatement = connection.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			pstatement.setString(1,username);
			pstatement.setString(2,password);
			pstatement.setString(3,email);
			pstatement.setBoolean(4,manager);
			if(!manager) {
			pstatement.setString(5,esperienza);
			pstatement.setBlob(6,foto);}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			pstatement.executeUpdate();
			pstatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	/**Metodo che accede al database per controllare che le credeziali inserite nel form di login 
	 * siano di un utente registrato, accesso effettuato con username o in alternativa email**/
	public Utente checkUser(String username, String password) 
	{
		Utente u = null;
		String query = "SELECT * FROM dbproject.utente WHERE (Username = ? OR Email = ?) AND Password = ?";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			pstatement.setString(1,username);
			pstatement.setString(2,username);
			pstatement.setString(3,password);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			result = pstatement.executeQuery();
			while(result.next()) 
			{
			u = new Utente();
				u.setIdUtente(result.getInt("idUtente"));
				u.setUsername(result.getString("Username"));
				u.setPassword(result.getString("Password"));
				u.setEmail(result.getString("Email"));
				u.setManager(result.getBoolean("Manager"));
				u.setEsperienza(result.getString("Exp"));
				byte[] imgData = result.getBytes("Foto");
				System.out.println(imgData);
				if(imgData != null) {
				String encodedImg=Base64.getEncoder().encodeToString(imgData);
				u.setFoto(encodedImg);}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return u;
	}
	
	public void setCredenzialiUtente(int idUtente, String username,String password,String email,boolean manager,String esperienza, InputStream foto) 
	{
		String query = "UPDATE dbproject.utente SET Username = ?, Password = ?, Email = ?, Manager = ?, Exp = ?, Foto = ? WHERE idUtente = ?";
		PreparedStatement pstatement = null;
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setString(1,username);
			pstatement.setString(2,password);
			pstatement.setString(3,email);
			pstatement.setBoolean(4,manager);
			pstatement.setString(5,esperienza);
			pstatement.setBlob(6,foto);
			pstatement.setInt(7,idUtente);
			pstatement.executeUpdate();
			pstatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void joinCampagna(int idCampagna, int idUtente) 
	{
		String query = "INSERT INTO dbproject.partecipazione (idCampagna,idUtente) VALUES (?,?)";
		PreparedStatement pstatement = null;
		
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setInt(1,idCampagna);
			pstatement.setInt(2,idUtente);
			pstatement.executeUpdate();
			pstatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean usernamePresente(String username) 
	{
		boolean b = false;
		String query = "SELECT * FROM dbproject.Utente WHERE Username = ?";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setString(1,username); 
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				b = true;
			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return b;
	}
	
	public Utente lastUser() 
	{
		String query = "SELECT * FROM dbproject.Utente ORDER BY idUtente DESC LIMIT 1";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		Utente u = null;
		try {
			pstatement = connection.prepareStatement(query);
			result = pstatement.executeQuery();
			
			while(result.next()) 
			{
				u = new Utente();
				u.setIdUtente(result.getInt("idUtente"));
				u.setUsername(result.getString("Username"));
				u.setPassword(result.getString("Password"));
				u.setEmail(result.getString("Email"));
				u.setManager(result.getBoolean("Manager"));
				u.setEsperienza(result.getString("Exp"));
				byte[] imgData = result.getBytes("Foto");
				String encodedImg=Base64.getEncoder().encodeToString(imgData);
				u.setFoto(encodedImg);
			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return u;
	}
	public boolean emailPresente(String email) 
	{
		boolean b = false;
		String query = "SELECT * FROM dbproject.Utente WHERE Email = ?";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setString(1,email);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				b = true;
			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return b;
	}
	public int getIdByUsername(String username) 
	{
		int id = -1;
		String query = "SELECT idUtente FROM dbproject.Utente WHERE Username = ?";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setString(1,username);
			result = pstatement.executeQuery();
			while(result.next()) 
			{
				id = result.getInt("idUtente");
			}
			pstatement.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return id;
	}
	
	public void updateManagerData(String username, String email, int userid)
	{
		String query = "UPDATE dbproject.utente SET Username = ?,  Email = ? WHERE idUtente = ?";
		PreparedStatement pstatement = null;
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setString(1,username);
			pstatement.setString(2,email);
			pstatement.setInt(3,userid);
			pstatement.executeUpdate();
			pstatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateManagerPwd(String pwd, int userid)
	{
		String query = "UPDATE dbproject.utente SET Password = ? WHERE idUtente = ?";
		PreparedStatement pstatement = null;
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setString(1,pwd);
			pstatement.setInt(2,userid);
			pstatement.executeUpdate();
			pstatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Utente getUserByUsername(String username)
	{
		Utente u = null;
		String query = "SELECT * FROM dbproject.utente WHERE (Username = ? OR Email = ?)";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		
		try {
			pstatement = connection.prepareStatement(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			pstatement.setString(1,username);
			pstatement.setString(2,username);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			result = pstatement.executeQuery();
			while(result.next()) 
			{
			u = new Utente();
				u.setIdUtente(result.getInt("idUtente"));
				u.setUsername(result.getString("Username"));
				u.setPassword(result.getString("Password"));
				u.setEmail(result.getString("Email"));
				u.setManager(result.getBoolean("Manager"));
				u.setEsperienza(result.getString("Exp"));
				byte[] imgData = result.getBytes("Foto");
				
				if(imgData != null) {
	                String encodedImg=Base64.getEncoder().encodeToString(imgData);
	                u.setFoto(encodedImg);}
				
//				String encodedImg=Base64.getEncoder().encodeToString(imgData);
//				u.setFoto(encodedImg);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return u;
	}
	public void setCredenzialiUtenteWithoutImage(int idUtente, String username,String password,String email,boolean manager,String esperienza) {
		
			String query = "UPDATE dbproject.utente SET Username = ?, Password = ?, Email = ?, Manager = ?, Exp = ? WHERE idUtente = ?";
			PreparedStatement pstatement = null;
			try {
				pstatement = connection.prepareStatement(query);
				pstatement.setString(1,username);
				pstatement.setString(2,password);
				pstatement.setString(3,email);
				pstatement.setBoolean(4,manager);
				pstatement.setString(5,esperienza);
				pstatement.setInt(6,idUtente);
				pstatement.executeUpdate();
				pstatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
}
