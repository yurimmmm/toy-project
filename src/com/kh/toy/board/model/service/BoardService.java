package com.kh.toy.board.model.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kh.toy.board.model.dao.BoardDao;
import com.kh.toy.board.model.dto.Board;
import com.kh.toy.common.db.JDBCTemplate;
import com.kh.toy.common.exception.DataAccessException;
import com.kh.toy.common.file.FileDTO;

public class BoardService {
	
	private JDBCTemplate template = JDBCTemplate.getInstance();
	private BoardDao boardDao = new BoardDao();
	
	public void insertBoard(Board board, List<FileDTO> fileDTOs) {
		
		Connection conn = template.getConnection();
		
		try {
			boardDao.insertBoard(board,conn);
			for (FileDTO fileDTO : fileDTOs) { 
				boardDao.insertFile(fileDTO,conn);
			}
			template.commit(conn); 
		}catch (DataAccessException e) {
			template.rollback(conn);
			throw e;
		}finally {
			template.close(conn); 
		}
	}

	public Map<String, Object> selectBoardDetail(String bdIdx) {
		
		Connection conn = template.getConnection();
		Map<String, Object> res = new HashMap<String, Object>();
		
		try {
			
			Board board = boardDao.selectBoardDetail(bdIdx,conn);
			List<FileDTO> files = boardDao.selectFileDTOs(bdIdx,conn);
			res.put("board", board);
			res.put("files", files);
			
		} finally {
			template.close(conn);
		}
		
		return res;
	}
	
	

}
