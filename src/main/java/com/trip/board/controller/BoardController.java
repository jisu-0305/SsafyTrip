package com.trip.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trip.board.service.BoardService;
import com.trip.global.ErrorRes;
import com.trip.global.GeneralRes;
import com.trip.global.SuccessRes;
import com.trip.board.dto.Board;
import com.trip.member.dto.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/board")
@Tag(name = "게시판 컨트롤러", description = "게시글 생성, 조회, 수정, 삭제와 관련된 서비스를 제공하는 클래스.")
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/")
	@Operation(summary = "게시글 목록 조회", description = "모든 게시글을 조회하여 목록을 반환합니다.")
	public ResponseEntity<List<Board>> readBoards() {
		List<Board> boards = boardService.readBoards();
		return ResponseEntity.ok(boards);
	}
	
	@GetMapping("/{articleNo}")
	@Operation(summary = "게시글 단건 조회", description = "게시글 번호를 사용하여 특정 게시글을 조회합니다.")
	public ResponseEntity<Board> readBoard(
			@Parameter(description = "조회할 게시글 번호", required = true) 
			@PathVariable("articleNo") int articleNo) {
		Board board = boardService.readBoard(articleNo);
		return ResponseEntity.ok(board);
	}
	
	@PostMapping("/")
	@Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
	public ResponseEntity<Board> createBoard(
			@Parameter(description = "생성할 게시글 정보", required = true) 
			@RequestBody Board board, HttpSession session) {
		Member member = (Member) session.getAttribute("memberInfo");
		board.setUserId(member.getUserId());
		boardService.createBoard(board);
		return ResponseEntity.status(HttpStatus.CREATED).body(board);
	}
	
	@PutMapping("/")
	@Operation(summary = "게시글 수정", description = "기존 게시글의 내용을 수정합니다.")
	public ResponseEntity<GeneralRes> updateBoard(
			@Parameter(description = "수정할 게시글 정보", required = true) 
			@RequestBody Board board, HttpSession session) {
		Member member = (Member) session.getAttribute("memberInfo");
		
		if(member.getUserId().equals(board.getUserId())) {
			boardService.udpateBoard(board);
			return ResponseEntity.ok(new SuccessRes("게시글 정보가 성공적으로 수정되었습니다."));
		} else {
			return ResponseEntity.ok(new ErrorRes("게시글 정보 수정에 실패했습니다."));
		}
	}
	
	@DeleteMapping("/")
	@Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
	public ResponseEntity<GeneralRes> deleteBoard(
			@Parameter(description = "삭제할 게시글 정보", required = true) 
			@RequestBody Board board, HttpSession session) {

		Member member = (Member) session.getAttribute("memberInfo");

		if(member.getUserId().equals(board.getUserId())) {
			boardService.deleteBoard(board);
			return ResponseEntity.ok(new SuccessRes("게시글이 삭제되었습니다."));
		} else {
			return ResponseEntity.ok(new ErrorRes("게시글이 삭제에 실패했습니다."));
		}
	}
}
