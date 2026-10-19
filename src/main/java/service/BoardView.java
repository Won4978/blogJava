package service;
//
import java.io.IOException;
import java.util.List;

//
import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import model.BoardDAO;
//import model.BoardDTO;
//
//public class BoardView implements Command {
//
//	@Override
//	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//		request.setCharacterEncoding("utf-8");
//		
//		int bno = Integer.parseInt(request.getParameter("bno"));
//		System.out.println("1. bno = " + bno);
//
//		
//		BoardDAO dao = new BoardDAO();		
//		dao.increViewCnt(bno);
//		BoardDTO dto = dao.selectOne(bno);
//		
//		request.setAttribute("view", dto);
//		return true;
//	}
//
//}
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BoardDAO;
import model.BoardDTO;
import model.ReplyDAO;
import model.ReplyDTO;

public class BoardView implements Command {

    @Override
    public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("A. BoardView 시작");

        request.setCharacterEncoding("utf-8");

        int bno = Integer.parseInt(request.getParameter("bno"));
        System.out.println("B. bno = " + bno);

        BoardDAO dao = new BoardDAO();
        System.out.println("C. DAO 생성 완료");

         //조회수 증가는 일단 주석 처리
         dao.increViewCnt(bno);

        System.out.println("D. selectOne 시작");
        BoardDTO dto = dao.selectOne(bno);
        System.out.println("E. selectOne 끝");

        System.out.println("F. setAttribute 끝");
        List<ReplyDTO> list = new ReplyDAO().selectList(bno);
        
        request.setAttribute("view", dto);
        request.setAttribute("replyList", list);

        return true;
    }
    
	
}

