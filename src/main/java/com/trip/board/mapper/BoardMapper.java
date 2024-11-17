package com.trip.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.trip.board.dto.Board;

@Mapper
public interface BoardMapper {

	List<Board> readBoards();

	Board readBoard(int articleNo);

	void createBoard(Board board);

	void updateBoard(Board board);

	void deleteBoard(Board board);

}
