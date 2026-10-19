package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.MemberDTO;
import model.ReplyDAO;

public class ReplyModify implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession();
		MemberDTO dto = (MemberDTO)session.getAttribute("loginMember");
		
		Map<String, Object> reslut = new HashMap<String, Object>();
		if(dto==null) {
			reslut.put("success", false);
			reslut.put("message","로그인 후 이용가능합니다.");
			sendJson(response, reslut);
			return false;
		}
		int rno = Integer.parseInt(request.getParameter("rno"));
		String content = request.getParameter("content");
		int updateCount = new ReplyDAO().replyUpdate(rno, content, dto.getId());
		if(updateCount > 0) {
			reslut.put("success", true);
			reslut.put("content",content);
			reslut.put("message","수정이 완료 되었습니다.");
		}else {
			reslut.put("success", false);
			reslut.put("message","본인 댓글만 수정할 수 있습니다.");
		}
		
		sendJson(response,reslut);
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
