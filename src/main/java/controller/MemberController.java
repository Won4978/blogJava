package controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.Command;
import service.IdCheckCommand;
import service.LoginCommand;
import service.LogoutCommand;
import service.MemberInsert;
import service.MypageCommand;


@WebServlet("/member/*")	
// @WebServlet : URL을 통해서 요청하는 주소가 /member로 시작하는 모든요청(*)은 이클래스가 자동실행
public class MemberController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       

    public MemberController() {
    	   // ↑ 생성자 (리턴타입이 없고 클래스 명과 같다)
        super();
      
    }
    
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		doAction(request,response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		doAction(request,response);
	}
	
	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		String pathInfo = request.getPathInfo();
		System.out.println("pathInfo: "+pathInfo);
		
		String page=null;
		Command command = null;
		
		
		switch(pathInfo) {
		case "/join":
			command = new MemberInsert();	// 부모타입의 참초변수(Command)로 자식타입의 참조변수로 받을 수 있다.(다형성)
			page="/mem/join.jsp";
			break;
		case "/idCheck": //localhost:8088/blogProject/member/idCheck
			command = new IdCheckCommand();
	        page = null;
	        break;	
		case "/login":
			command = new LoginCommand();
			page="/mem/login.jsp";
			break;
		case "/logout":
			command = new LogoutCommand();
			page= null;
			break;
		case "/mypage":
			command = new MypageCommand();
			page = "/mem/mypage.jsp";
			break;
		}
		
		boolean needForward = command.doCommand(request, response);
		
		if(needForward && page != null) {
			RequestDispatcher rd = request.getRequestDispatcher(page);
			rd.forward(request, response);
		}
	} //doAction

}
