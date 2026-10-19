package service;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.MemberDAO;
import model.MemberDTO;

public class LoginCommand implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if("get".equalsIgnoreCase(request.getMethod())) {
	         return true;
	    }
		
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = dao.login(id);
		// setAttribute : 처리된 값을 jsp로 출력을 하려면 request,session 속성에 저장해서 포워딩한다.
		// setAttribute(1,2) : 2의 자료를 1의 저장
		// request : 저장된 자료는 다른 페이지로 이동시 삭제 됨 ,session : 저장된 자료를 다른 페이지로 이동하여도 사라지지 않음
		if(dto != null && BCrypt.checkpw(pwd, dto.getPwd())) {		// checkpw(pwd, dto.getPwd()) : 암호화된 pwd와 비교하기
			HttpSession session = request.getSession();	//session 만들기
			session.setAttribute("loginMember", dto);
			response.sendRedirect(request.getContextPath()+"/");
			return false;	// return false : forward를 하지 않기위해 
		}else {
			request.setAttribute("loginFail", "아이디 또는 비밀번호를 확인하세요");
			return true;	// return true : forward를 하기 위해 
		}
	}
}
