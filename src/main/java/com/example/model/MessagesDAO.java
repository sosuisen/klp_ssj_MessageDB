package com.example.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * DAO for messages table
 */
public class MessagesDAO {
	DataSource ds;

	public MessagesDAO() throws NamingException {
		InitialContext ctx = new InitialContext();
		ds = (DataSource) ctx.lookup("jdbc/__default");
	}

	public MessageDTO get(int id) {
		MessageDTO mesDTO = null;
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM messages where id=?");) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int myId = rs.getInt("id");
				String name = rs.getString("name");
				String message = rs.getString("message");
				mesDTO = new MessageDTO(myId, name, message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mesDTO;
	}

	public ArrayList<MessageDTO> getAll() {
		var messageList = new ArrayList<MessageDTO>();
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM messages");) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int myId = rs.getInt("id");
				String name = rs.getString("name");
				String message = rs.getString("message");
				messageList.add(new MessageDTO(myId, name, message));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return messageList;
	}

	public MessageDTO create(String name, String message) {
		MessageDTO mesDTO = null;
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn
						.prepareStatement("INSERT INTO messages(name, message) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
			) {
			pstmt.setString(1, name);
			pstmt.setString(2, message);
			pstmt.executeUpdate();

			// AUTOINCREMENTで生成された id を取得します。
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);

			mesDTO = new MessageDTO(id, name, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mesDTO;
	}

	public void updateName(int id, String name) {
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("UPDATE message SET title=(?) WHERE id=?")) {
			pstmt.setString(1, name);
			pstmt.setInt(2, id);
			int num = pstmt.executeUpdate();
			if (num <= 0) {
				System.out.println("id " + id + " の行はありません。");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateMessage(int id, String message) {
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("UPDATE messages SET message=(?) WHERE id=?")) {
			pstmt.setString(1, message);
			pstmt.setInt(2, id);
			int num = pstmt.executeUpdate();
			if (num <= 0) {
				System.out.println("id " + id + " の行はありません。");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(int id) {
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("DELETE from messages WHERE id=?");) {
			pstmt.setInt(1, id);
			int num = pstmt.executeUpdate();
			if (num <= 0) {
				System.out.println("id " + id + " の行はありません。");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteAll() {
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("DELETE from messages");) {
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
