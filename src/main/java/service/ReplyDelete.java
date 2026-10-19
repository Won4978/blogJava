package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.MemberDTO;
import model.ReplyDAO;

public class ReplyDelete implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession();
		MemberDTO dto = (MemberDTO)session.getAttribute("loginMember");
		
		Map<String, Object> result = new HashMap<>();
		if(dto==null) {
			result.put("success", false);
			result.put("message", "로그인 해야합니다");
			sendJson(response, result);
			return false;
		}
		int rno = Integer.parseInt(request.getParameter("rno"));
		int cnt = new ReplyDAO().replyDelete(rno, dto.getId());
		
		if(cnt>0) {
			result.put("success", true);
			result.put("message", "댓글이 삭제되었습니다.");
		}else {
			result.put("success", false);
			result.put("message", "작성자 댓글만 삭제가 가능합니다.");
		}
		sendJson(response, result);
		
		
		return false;
	}
	
	public void sendJson(HttpServletResponse response, Map<String, Object> result) {
		response.setContentType("application/json; charset=utf-8");
		Gson gson = new Gson();
		String json = gson.toJson(result);
		try {
			PrintWriter out = response.getWriter();
			out.print(json);	// 호출한 클라이언트로 json을 보낸다.
		}catch(Exception e) {
			e.printStackTrace();
		}
	} //sendJson

}
