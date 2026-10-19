package service;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MemberDAO;
import model.MemberDTO;

public class MemberInsert implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// get 방식으로 요청이 들어오면 true 리턴
		if("get".equalsIgnoreCase(request.getMethod())) {	// getMethod() : 요청방식 호출
			return true;
		}
		
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());	// pwd 암호화(jbcrypt-0.4.jar)
		String nickname = request.getParameter("nickname");
		String email = request.getParameter("email");
		
		MemberDTO dto = new MemberDTO();
		dto.setId(id);
		dto.setPwd(hashedPwd);
		dto.setNickname(nickname);
		dto.setEmail(email);
		
		MemberDAO dao = new MemberDAO();
		dao.memberInsert(dto);
		
		response.sendRedirect(request.getContextPath()+"/member/login");
		
		return false;		// sendRedirect(보내버림)가 있으면 false 포워딩이 필요하면 true
	}

}
