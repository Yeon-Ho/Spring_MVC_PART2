package com.kh.welcome.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.welcome.board.model.dao.NoticeDao;
import com.kh.welcome.board.model.vo.Notice;

import common.util.FileUtil;
import common.util.Paging;

@Service
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private NoticeDao noticeDao;

	//스프링에게 트랜잭션 관리를 위임하는 어노테이션
	//메소드 or class 위에 작성할 수있다.
	//CLass에 지정할 경우 , 해당 클래스의 모든 메소드가
	//스프링에게 트랜잭션 관리를 위임하게 된다.
//	@Transactional
	public int insertNotice(Notice notice , List<MultipartFile> files , String root ) {

		int result = noticeDao.inserNotice(notice);
		
		//에러 발생을 위한 코드
		int errorNumber = 10/0;
		
		if(!(files.size() == 1 && files.get(0).getOriginalFilename().equals(""))) {
			
			//파일업로드를 위해 FileUtil.fileUpload() 호출
			List<Map<String , String>> fileData = new FileUtil().filUpload(files, root);
			
			for (Map<String, String> f : fileData) {
				noticeDao.insertFile(f);
			}
		}
		
		return result;
	}
	
											 // 현재페이지 ,     페이지당 노출할 게시글 수
	public Map<String , Object> selectNoticeList(int currentPage , int cntPerPage){
	
		Map<String , Object> commandMap = new HashMap<String, Object>();
	
		//페이징 처리를 위한 객체 생성
		Paging p = new Paging(noticeDao.selectContentCnt(),currentPage , cntPerPage);
		
		//현재 페이지에 필요한 게시물 목록
		List<Notice> nlist = noticeDao.selectNoticeList(p);
		commandMap.put("nlist", nlist);
		commandMap.put("paging", p);
		
		return commandMap;
	}
	
	//게시글 상세보기
	public Map<String , Object> selectNoticeDetail(int nIdx){
		
		Map<String , Object> commandMap = new HashMap<String, Object>();
		
		//파일의 상세정보
		Notice notice = noticeDao.selectNoticeDetail(nIdx);
		System.out.println("여기가 노티스 "+notice);
		//해당게시글의 파일들
		List<Map<String , String>> flist = noticeDao.selectFileWithNotice(nIdx); 
		System.out.println("여기가 에프리스트 "+flist);
		
		commandMap.put("notice", notice);
		commandMap.put("flist", flist);
		
		
		
		
		return commandMap;
	}

	public int deleteFile(int fIdx) {
	
		String path = noticeDao.selectFileSavePath(fIdx);
		FileUtil fu = new FileUtil();
		fu.deleteFile(path);
		
		return noticeDao.deleteFile(fIdx);
	}


	public int updateNotice(Notice notice, List<MultipartFile> files, String root) {

		int result = noticeDao.updateNotice(notice);
		int nIdx = notice.getnIdx();
				
		if(!(files.size() == 1 && files.get(0).getOriginalFilename().equals(""))) {
			
			//파일업로드를 위해 FileUtil.fileUpload() 호출
			List<Map<String , String>> fileData = new FileUtil().filUpload(files, root);

			for (Map<String, String> f : fileData) {
				noticeDao.updateInsertFile(f ,nIdx);
			}
		}
		return result;
	}

	
	
	
	
}
