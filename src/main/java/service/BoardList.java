package service;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BoardDAO;
import model.BoardDTO;

public class BoardList implements Command {
	
	private static final int PAGE_SIZE = 9;		// 한페이지에 보여줄 갯수
	private static final int PAGE_BLOCK = 5;	// 하단에 보여줄 페이지 번호 1 2 3 4 5

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		
		String searchtype = request.getParameter("searchtype");
		String keyword = request.getParameter("keyword");
		
		int currentPage = 1;
		String pageParam = request.getParameter("page");
		if(pageParam != null) {
			try {
				currentPage = Integer.parseInt(pageParam);
			}catch(NumberFormatException e) {
				currentPage = 1;
			}
		}
		
		BoardDAO dao = new BoardDAO();
		
		int totalCount = dao.selectCount(searchtype, keyword);
		int totalPage = (int)Math.ceil((double)totalCount / PAGE_SIZE);
		if(totalPage == 0) totalPage = 1;
		if(currentPage < 1) currentPage = 1;
		if(currentPage > totalPage) currentPage = totalPage;
		
		
		int startRow = (currentPage - 1) * PAGE_SIZE + 1;
		int endRow = currentPage * PAGE_SIZE;
		
		List<BoardDTO> list = new BoardDAO().selectList(searchtype, keyword, startRow, endRow);
		
		int startPage = ((currentPage - 1) / PAGE_BLOCK) * PAGE_BLOCK + 1;
		int endPage = Math.min(startPage + PAGE_BLOCK - 1, totalPage);
			
		request.setAttribute("boardList", list);	
		request.setAttribute("searchtype",searchtype);
		request.setAttribute("keyword", keyword);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);
		
		return true;
	}

}
