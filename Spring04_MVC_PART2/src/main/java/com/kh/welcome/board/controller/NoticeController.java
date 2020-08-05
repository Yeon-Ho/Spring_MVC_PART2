package com.kh.welcome.board.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kh.welcome.board.model.service.NoticeService;
import com.kh.welcome.board.model.vo.Notice;
import com.kh.welcome.member.model.vo.Member;

@Controller
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	
	@RequestMapping("board/boardform.do")
	public String boardForm() {
		
		return "board/boardForm";
	}
	
	//------------------------------------------------------------------------------------------------------------
//	//추후에 수정 될 메소드
//	@RequestMapping("notice/noticeupload.do")
//	public ModelAndView noticeUpload(
//			//다중파일 업로드임으로
//			//여러개의 MultipartFile을 담기 위한 List 생성
//			@RequestParam List<MultipartFile> files,HttpSession session
//			) {
//		
//		ModelAndView mav = new ModelAndView();
//		
//		//파일 업로드를 위한 절대경로 반환
//		String root = session.getServletContext().getRealPath("/");
//		
//		//MultipartFile 예외 처리
//		//	사용자가 게시글만 작성하고 파일업로드는 하지 않았을 경우
//		//	사용자가 첨부한 파일이 없어도 List<MultiparFile>의 size()가 1로 잡힌다
//		//	이때 , 첨부한 파일의 이름은 공백이다.
//		
//		if(!(files.size() == 1 && files.get(0).getOriginalFilename().equals(""))) {
//			
//			for (MultipartFile mf : files) {
//				//파일을 저장할 경로
//				String savePath = root + "resources/upload";
//				System.out.println(savePath);
//				
//				//transferTo 메서드의 매개변수에 넣어 줄
//				//빈 파일 객체를 저장할 경로 + 사용자가 등록한 이름으로 생성
//				File fileData = new File(savePath+"/"+mf.getOriginalFilename());
//				
//				try {
//					//빈파일 객체에 사용자가 등록한 파일을 덮어쓴다.
//					mf.transferTo(fileData);
//				} catch (IllegalStateException | IOException e) {
//					e.printStackTrace();
//				}
//				
//				
//			}
//		}
//		return mav;
//	}
	//------------------------------------------------------------------------------------------------------------
	
	//---------------------------------------------------------------------------------------
	@RequestMapping("notice/noticeupload.do")
	public ModelAndView noticeUpload(@RequestParam List<MultipartFile> files , HttpSession session , Notice notice) {
		ModelAndView mav = new ModelAndView();
		
		
		String root = session.getServletContext().getRealPath("/");
		Member sessionMember = (Member) session.getAttribute("logInInfo");
		
		//로그인한 회원이라면
		if(sessionMember != null) {
			notice.setUserId(sessionMember.getUserId());
		}else {
			notice.setUserId("비회원");
		}
		
		noticeService.insertNotice(notice, files, root);
		// notice/noticelist.do 로 다시 요청
		mav.setViewName("redirect:noticelist.do");
		
		return mav;
	}
	
	
	//게시글 목록 
	@RequestMapping("notice/noticelist.do")
	public ModelAndView noticeList(
			
			//@RequestParam 에서 required : 필수 파라미터 여부 지정( default 값은 true )
			//defaultValue : 파라미터가 없을 때 기본값으로 지정할 값
			@RequestParam(required=false, defaultValue="1")int cPage
			) {
		
		ModelAndView mav = new ModelAndView();
		int curPerPage = 10;
		Map<String , Object> commandMap = noticeService.selectNoticeList(cPage, curPerPage);
		
		mav.addObject("paging" ,  commandMap.get("paging"));
		mav.addObject("noticeData" , commandMap);
		mav.setViewName("board/boardList");
		
		return mav;
	}
	
	@RequestMapping("/notice/noticedetail.do")
	public ModelAndView noticeDetail(int nIdx) {
		ModelAndView mav = new ModelAndView();

		Map<String , Object> commandMap = noticeService.selectNoticeDetail(nIdx);
	
		System.out.println("이게 둘다 가져온거"+ commandMap);
		//해당 게시글이 존재하는 지 여부 판단
		//반환되는 Map은 null일수 없다.
		//Map안의 notice객체가 null인지 여부로 판단.
		if(commandMap.get("notice") !=null) {
			mav.setViewName("board/boardView");
			mav.addObject("data",commandMap);
		}else {
			mav.addObject("alertMsg","해당 게시물이 존재하지 않습니다.");
			mav.addObject("url", "board/boardList");
			mav.setViewName("common/result");
		}
		
		return mav;
	}
	
	//------------------------------------------------------------------------------------------------------------
//	@RequestMapping("notice/noticedownload.do")
//	public void noticeDownload(
//			//response header 지정을 위한 response
//			HttpServletResponse resp,
//			//파일경로 지정을 위한 session
//			HttpSession session,
//			//사용자가 올린 파일 이름
//			String ofname,
//			//서버에 저장된 파일 이름
//			String rfname
//			) {
//		
//		String readFolder = session.getServletContext().getRealPath("/resources/upload");
//		
//		//file 객체 생성
//		File downFile = new File(readFolder + "/"+rfname);
//		
//		OutputStream downOut = null;
//		BufferedInputStream bis = null;
//		
//		
//		try {
//			//response header
//			//Content-Disposition : 브라우저화면에 출력할지 , 다운받을 지 결정
//			//attachment : download 받게끔 지정
//			//filename : download 받을 때 파일명
//			resp.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(ofname,"UTF-8"));
//
//			//응답해야하는 대상과 연경되는 OutputStream
//			downOut = resp.getOutputStream();
//			
//			//파일 읽어오기
//			bis = new BufferedInputStream(new FileInputStream(downFile));
//		
//			int read = 0;
//			while((read = bis.read()) != -1) {
//				downOut.write(read);
//				downOut.flush();
//			}
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
	//------------------------------------------------------------------------------------------------------------
	
	//스프링으로 파일다운로드--------------------------------------------------------------------------------------
	@RequestMapping("notice/noticedownload.do")
	@ResponseBody
	public FileSystemResource noticeDownload(
			HttpServletResponse resp,
			HttpSession session,
			String ofname,
			String rfname
			) {
		
		String readFolder = session.getServletContext().getRealPath("/resources/upload");
		
		//FileSystemResource
		FileSystemResource downFile = new FileSystemResource(readFolder+"/"+rfname);
		
		try {
			resp.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(ofname,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return downFile;
	}
	
	@RequestMapping("notice/noticemodify.do")
	public ModelAndView noticeModify(int nIdx , String userId , HttpSession session ) {
		ModelAndView mav = new ModelAndView();
		
		//로그인정보가져오기
		Member sessionMember = (Member) session.getAttribute("logInInfo");
		
		//해당 게시글 작성자 아이디와 session에 담겨있는 회원 아이디가 일치하는 경우에만 수정을 허용
		if(sessionMember != null && userId.equals(sessionMember.getUserId())) {
			Map<String , Object> commandMap = noticeService.selectNoticeDetail(nIdx);
			
			//게시물이 존재하는지
			if(commandMap.get("notice")!=null) {
				mav.addObject("data",commandMap);
				mav.setViewName("board/boardModify");
			}else {
				mav.addObject("alertMsg" , "게시물이 존재하지 않습니다.");
				mav.addObject("url" , "noticelist.do");
				mav.setViewName("common/result");
			}
			
		}else {
			mav.addObject("alertMsg" , "해당 게시물에 접근할 권한이 없습니다.");
			mav.addObject("url" , "noticelist.do");
			mav.setViewName("common/result");
		}
		
		return mav;
	}
	
	@RequestMapping("notice/noticefiledelete.do")
	@ResponseBody
	public String noticeFileDelete(int fIdx) {
		noticeService.deleteFile(fIdx);
		return "";
	}
	
	
	@RequestMapping("notice/noticeupdate.do")
	public ModelAndView noticeUpdate(@RequestParam List<MultipartFile> files , HttpSession session , Notice notice) {
		ModelAndView mav = new ModelAndView();
		
		
		String root = session.getServletContext().getRealPath("/");
		int res = noticeService.updateNotice(notice, files , root);
		
		
		
		mav.setViewName("redirect:noticelist.do");
		return mav;
	}
	
	
	
}
