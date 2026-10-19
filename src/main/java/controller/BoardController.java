package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.BoarWrite;
import service.BoardDelete;
import service.BoardList;
import service.BoardModify;
import service.BoardView;
import service.Command;
import service.LikeCommand;
import service.popularCommand;


@WebServlet("/blog/*")
@MultipartConfig(
		maxFileSize = 10 * 1024 * 1024,
		maxRequestSize = 20 * 1024 * 1024
		)

public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public BoardController() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}
	
	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		System.out.println("pathInfo: "+pathInfo);
		String page = null;
		Command command = null;
		switch(pathInfo){
		case "/list" :
			command = new BoardList();
			page="/board/list.jsp";
			break;
		case "/write":
			command = new BoarWrite();
			page="/board/write.jsp";
			break;
		case "/view":
			command = new BoardView();
			page = "/board/view.jsp";
			break;
		case "/delete":
			command = new BoardDelete();
			page = null;
			break;
		case "/update":
			command = new BoardModify();
			page = "/board/modify.jsp";
			break;
		case "/like":
			command = new LikeCommand();
			page = null;
			break;
		case "/popular":
			command = new popularCommand();
			page = "/board/popular.jsp";
			break;
		}
		boolean needForward = command.doCommand(request, response);

		if(needForward &&page !=null) {
			request.getRequestDispatcher(page).forward(request, response);
		}
	}
} //BoardController
