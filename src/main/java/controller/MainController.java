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
import service.HomeCommand;
import service.LikeCommand;
import service.popularCommand;


@WebServlet("/home/*")
@MultipartConfig(
		maxFileSize = 10 * 1024 * 1024,
		maxRequestSize = 20 * 1024 * 1024
		)

public class MainController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public MainController() {
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
		case "/main":
		    command = new HomeCommand();
		    page = "/index.jsp";
		    break;

		}
		boolean needForward = command.doCommand(request, response);

		if(needForward &&page !=null) {
			request.getRequestDispatcher(page).forward(request, response);
		}
	}
} //MainController
