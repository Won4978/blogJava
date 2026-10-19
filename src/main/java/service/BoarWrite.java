package service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.BoardDAO;
import model.BoardDTO;
import model.MemberDTO;

public class BoarWrite implements Command {
	
	

	
	private static final String UPLOAD_DIR="C:/upload";
	@Override
	public boolean doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		// 로그인한 사람만 글쓰기 가능
		HttpSession session = request.getSession();
		MemberDTO memberdto = (MemberDTO)session.getAttribute("loginMember");
		if(memberdto==null) {
			response.sendRedirect(request.getContextPath()+"/member/login");
			return false;
		}
		
		// get요청시 글쓰기 화면 보여주기
		if("get".equalsIgnoreCase(request.getMethod())) {
			return true;
		}
		
		BoardDTO dto = new BoardDTO();
		dto.setTitle(request.getParameter("title"));
		dto.setContent(request.getParameter("content"));
		dto.setWriter(memberdto.getId());		
		//이미지 요청 - 첨부했을 경우
		Part filepart = request.getPart("imgFile");
		// jsp/sevlet에서 파일 업로드를 처리할 때 사용하는 servlet 표준 API
		// Part : 업로드된 파일 하나를 표현하는 객체
		// request.getPart("imgFile") : jsp에서 http요청으로 전송된 name="imaFile"에 저장된 이미지 요청
		if(filepart!=null && filepart.getSize() > 0){
			String saveFileName = saveFile(filepart);
			dto.setImgPath(saveFileName);
		}
		
		BoardDAO dao = new BoardDAO();
		dao.insert(dto);
		
		response.sendRedirect(request.getContextPath()+"/blog/list");

		return false;
	} // docommand
	
	public String saveFile(Part filepart) throws IOException {
		// 이미지 업로드 핵심 클래스
		// Path : 파일이나 디렉토리의 경로 정보를 나타내는 객체
		// Files : 파일 생성, 삭제, 읽기, 쓰기, 복사등 실제 동작을 처리하는 정적 메서드 집합
		String oriFileName = filepart.getSubmittedFileName();	// 업로드한 본래의 파일명을 구함
		System.out.println("oriFileName : "+oriFileName);
		
		// 파일 저장이름 방법 1.currentTimeMillis() : 밀리초 단위로 변환 , UUID.randomUUID() : 16진수 숫자
		// String saveFileName = System.currentTimeMillis()+"_"+oriFileName;	
		String saveFileName = UUID.randomUUID()+"_"+oriFileName;	
		System.out.println("UUID : "+saveFileName);
		
		Path uploadDir = Paths.get(UPLOAD_DIR);	// Paths.get : 문자열로 되어있는 경로를 Path객체타입 으로 전환.
		if(!Files.exists(uploadDir)) {		// C:/upload 폴더가 존재하는지 확인		
			Files.createDirectories(uploadDir);	// 폴더가 없으면 상위부터 하위폴더까지 자동 생성
		}
		Path targetPath = uploadDir.resolve(saveFileName); // resolve() : 폴더경로+ 파일이름을 합쳐서 파일경로 생성
		try (InputStream input = filepart.getInputStream()){	// getInputStream : 업로드된 이미지를 전부 읽어옴.
			Files.copy(input, targetPath);	// copy(1,2) : 1에서 읽은 데이터를 2에 복사 
		}	
		return saveFileName;	// dto에 저장 시키기 위해 이름을 리턴함.
	}
}
