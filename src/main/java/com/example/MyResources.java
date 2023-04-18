package com.example;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
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

	/**
	 * MessageDAOクラスは中でインジェクション（@Inject）を用いています。
	 * この場合、messageDAO = new MessageDAO()のようにコンストラクタを呼び出して
	 * インスタンスを作ると、上記@Injectが動作しません。
	 * （自作クラス内における@Inject動作の仕様がそうなので）
	 * messageDAOも下記のようにインジェクションで作成する必要があります。
	 */
	@Inject
	private MessagesDAO messageDAO;

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
	@Template(name = "/redirect")
	public String postMessage(@BeanParam MessageDTO mes) {
		// フォーム側にidの値はないので0が入っています。
		messageDAO.create(mes);
		// ここで messageDAO.getAll()を呼んでもよいが、
		// 表示処理は GET list で集中管理したいので、リダイレクト。
		return "list";
	}

	@GET
	@Path("clear")
	@Template(name = "/redirect")
	public String clearMessage() {
		messageDAO.deleteAll();
		return "list";
	}
	

	@POST
	@Path("search")
	@Template(name = "/message")
	public String postSearch(@FormParam("keyword") String keyword) {
		messageDAO.search(keyword);
		return userName;
	}

}
