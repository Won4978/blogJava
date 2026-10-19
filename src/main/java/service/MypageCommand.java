package service;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.BoardDAO;
import model.BoardDTO;
import model.MemberDTO;
import model.ReplyDAO;
import model.ReplyDTO;

public class MypageCommand implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		// 세션객채 생성
		// loginMember 속성을 MemberDTO객체로 변환
		// loginMember 가 null과 같으면 로그인 페이지로 이동
		// loginMember에 있는 id를 변수에 저장
		// id를 ReplyDAO, BoardDAo 객체를 생성 후 메서드를 호출하면서 id를 보낸다.
		// 메서드르 처리하고 리턴된 가변 배열의 값을 속성에 담는다.
		// request.setAttribute("myPosts", myPosts);
		// request.setAttribute("myReplies", myReplies);
		// request.setAttribute("myLikes", myLikes);
		
        // 세션객체 생성
        HttpSession session = request.getSession();

        // loginMember 속성을 MemberDTO 객체로 변환
        MemberDTO dto = (MemberDTO)session.getAttribute("loginMember");

        // loginMember가 null과 같으면 로그인 페이지로 이동
        if(dto == null) {
            response.sendRedirect(request.getContextPath() + "/member/login");
            return false;
        }

        // loginMember에 있는 id를 변수에 저장
        String id = dto.getId();

        // ReplyDAO, BoardDAO 객체 생성
        ReplyDAO replyDao = new ReplyDAO();
        BoardDAO boardDao = new BoardDAO();

        // 메서드 호출하면서 id를 전달하고 결과를 List에 저장
        List<BoardDTO> myPosts = boardDao.selectListByWriter(id);
        List<ReplyDTO> myReplies = replyDao.selectListByWriter(id);
        List<BoardDTO> myLikes = boardDao.selectLikedList(id);

        // 조회한 데이터를 request 속성에 저장
        request.setAttribute("myPosts", myPosts);
        request.setAttribute("myReplies", myReplies);
        request.setAttribute("myLikes", myLikes);

		
		
		
		
		
		return true;
	}

}
