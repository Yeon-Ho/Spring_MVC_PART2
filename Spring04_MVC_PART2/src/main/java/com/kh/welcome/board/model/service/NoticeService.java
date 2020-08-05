package com.kh.welcome.board.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.welcome.board.model.vo.Notice;


public interface NoticeService {

	public int insertNotice(Notice notice , List<MultipartFile> files , String root );
	
	// 현재페이지 ,   페이지당 노출할 게시글 수
	public Map<String , Object> selectNoticeList(int currentPage , int cntPerPage);
	
	//게시글 상세보기
	public Map<String , Object> selectNoticeDetail(int nIdx);

	public int deleteFile(int fIdx);

	public int updateNotice(Notice notice, List<MultipartFile> files, String root);

	
}
