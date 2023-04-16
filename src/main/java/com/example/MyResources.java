package com.example;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.mvc.Template;

import com.example.model.MessageDTO;
import com.example.model.MessagesDAO;
import com.example.model.User;
import com.example.model.UserDTO;

@Path("/")
public class MyResources {
	/**
	 * MessageDAOクラスは中でインジェクション（@Inject）を用いています。
	 * この場合、messageDAO = new MessageDAO()のようにコンストラクタを呼び出して
	 * インスタンスを作ると、上記@Injectが動作しません。
	 * （自作クラス内における@Inject動作の仕様がそうなので）
	 * messageDAOも下記のようにインジェクションで作成する必要があります。
	 */
	@Inject
	private MessagesDAO messageDAO;

	@Inject
	private User user;

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
		user.setName("");
		return "";
	}

	@POST
	@Path("login")
	@Template(name = "/login")
	public String postLogin(@BeanParam UserDTO userDTO) {
		if (userDTO.getName().equals("kcg") && userDTO.getPassword().equals("foo")) {
			// login.jsp の中で条件分岐してlistへリダイレクトします。
			user.setName(userDTO.getName());
			throw new RedirectException("list");
		}
		return "ユーザ名またはパスワードが異なります";
	}

	@GET
	@Path("list")
	@Template(name = "/message")
	public String getMessage() {
		if (user.getName().equals("")) {
			// 認証に成功していない場合は、loginへリダイレクト
			throw new RedirectException("login");
		}
		messageDAO.getAll();
		return "";
	}

	@POST
	@Path("list")
	@Template(name = "/message")
	public String postMessage(@BeanParam MessageDTO mes) {
		// フォーム側にidの値はないので0が入っています。
		messageDAO.create(mes);
		messageDAO.getAll();
		return "";
	}

	@GET
	@Path("clear")
	public void clearMessage() {
		messageDAO.deleteAll();
		throw new RedirectException("list");
	}

	@lombok.Getter
	@lombok.Setter
	@lombok.AllArgsConstructor
	public static class RedirectException extends RuntimeException {
		private String redirectTo;
	}

	@Provider
	public static class RedirectExceptionMapper implements ExceptionMapper<RedirectException> {
		@Override
		public Response toResponse(RedirectException exception) {
			return Response.seeOther(URI.create(exception.redirectTo)).build();
		}
	}
}
