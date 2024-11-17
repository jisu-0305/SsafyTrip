package com.trip.board.service;

import java.util.List;

import com.trip.board.dto.Board;

public interface BoardService {

	List<Board> readBoards();

	Board readBoard(int articleNo);

	void createBoard(Board board);

	void udpateBoard(Board board);

	void deleteBoard(Board board);

}
