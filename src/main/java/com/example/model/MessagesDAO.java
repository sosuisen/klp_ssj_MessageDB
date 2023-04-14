package com.example.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * DAO for messages table
 */
public class MessagesDAO {
	private DataSource ds;
	
	@Inject
	private Messages messages;

	public MessagesDAO () throws NamingException  {
		var ctx = new InitialContext();
		ds = (DataSource) ctx.lookup("jdbc/__default");	
	}
	
	public void getAll() {
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM messages");) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int myId = rs.getInt("id");
				String name = rs.getString("name");
				String message = rs.getString("message");
				messages.add(new MessageDTO(myId, name, message));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create(MessageDTO mesDTO) {
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn
						.prepareStatement("INSERT INTO messages(name, message) VALUES(?, ?)",
								Statement.RETURN_GENERATED_KEYS);) {
			pstmt.setString(1, mesDTO.getName());
			pstmt.setString(2, mesDTO.getMessage());
			pstmt.executeUpdate();

			// AUTOINCREMENTで生成された id を取得します。
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);
			mesDTO.setId(id);

			messages.add(mesDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteAll() {
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("DELETE from messages");) {
			pstmt.executeUpdate();
			messages.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
