package service;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BoardDAO;
import model.BoardDTO;

public class HomeCommand implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		List<BoardDTO> reclist = new BoardDAO().selectRecentHomeList();
		List<BoardDTO> poplist = new BoardDAO().selectPopularHomeList();

		request.setAttribute("reclist", reclist);
		request.setAttribute("poplist", poplist);
		
		
		
		return true;
	}

}
