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
import model.ReplyDTO;

public class ReplyWrite implements Command {

	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession();
		MemberDTO loginMember = (MemberDTO)session.getAttribute("loginMember");
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(loginMember ==null) {
			result.put("success", false);
			result.put("message", "로그인이 필요 합니다");
			sendJson(response,result);
			return false;
		}
		
		int bno = Integer.parseInt(request.getParameter("bno"));
		String content = request.getParameter("content");
		
		ReplyDTO dto = new ReplyDTO();
		dto.setBno(bno);
		dto.setWriter(loginMember.getId());
		dto.setContent(content);
		
		ReplyDAO dao = new ReplyDAO();
		int rno = dao.insert(dto);
		
		result.put("success", true);
		result.put("rno",rno);
		result.put("writerNickname", loginMember.getNickname());
		result.put("content", content);
		
		sendJson(response, result);
		
		return false;
	}
	
	public void sendJson(HttpServletResponse response, Map<String, Object> result) {
		response.setContentType("application/json; charset=utf-8");
		Gson gson = new Gson();
		String json = gson.toJson(result);
		try {
			PrintWriter out = response.getWriter();
			out.print(json);
		}catch(Exception e) {
			e.printStackTrace();
		}
	} //sendJson

}
