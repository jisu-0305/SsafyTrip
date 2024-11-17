package com.trip.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trip.board.mapper.BoardMapper;
import com.trip.board.dto.Board;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	BoardMapper mapper;
	
	@Override
	public List<Board> readBoards() {
		return mapper.readBoards();
	}

	@Override
	public Board readBoard(int articleNo) {
		return mapper.readBoard(articleNo);
	}

	@Override
	public void createBoard(Board board) {
		mapper.createBoard(board);
	}

	@Override
	public void udpateBoard(Board board) {
		mapper.updateBoard(board);
	}

	@Override
	public void deleteBoard(Board board) {
		mapper.deleteBoard(board);
	}

}
