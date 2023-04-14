package com.example;

import javax.naming.NamingException;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.glassfish.jersey.server.mvc.Template;

import com.example.model.MessageDTO;
import com.example.model.MessagesDAO;
import com.example.model.UserDTO;

@Path("/")
public class MyResources {
	private String userName = "KCG";
	MessagesDAO messageDAO;
	
	public MyResources() throws NamingException {
		messageDAO = new MessagesDAO();
	}

	@GET
	@Path("")
	@Template(name = "/index")
	public String home() {
		// @Templateを使うときの戻り値はvoidにできないので、代わりに""を返しています。
		return "";
	}

	@GET
	@Path("login")
	@Template(name = "/login")
	public String getLogin() {
		return "";
	}

	@POST
	@Path("login")
	@Template(name = "/login")
	public String postLogin(@BeanParam UserDTO user) {
		if (user.getName().equals("kcg") && user.getPassword().equals("foobar")) {
			// login.jsp の中で条件分岐してlistへリダイレクトします。
			return "success";
		}
		return "ユーザ名またはパスワードが異なります";
	}

	@GET
	@Path("list")
	@Template(name = "/message")
	public String getMessage() {
		/**
		 * まだ厳密な認証をしていないため、ログインフォームを無視して
		 * http://localhost:8080/MessageDB/message/list
		 * を直接開くことができてしまいます。
		 */
		messageDAO.getAll();
		return userName;
	}

	@POST
	@Path("list")
	@Template(name = "/message")
	public String postMessage(@BeanParam MessageDTO mes) {
		// フォーム側にidの値はないので0が入っています。
		messageDAO.create(mes);
		return userName;
	}

	@GET
	@Path("clear")
	@Template(name = "/redirect")
	public String clearMessage() {
		messageDAO.deleteAll();
		return "list";
	}
}
