package com.trip.attraction.controller;

import com.trip.attraction.dto.*;
import com.trip.attraction.service.AttractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/attractions")
@Tag(name = "관광지 컨트롤러", description = "관광지 관련 서비스 제공 (리스트 조회, 지역 및 상세 정보 조회)")
@RequiredArgsConstructor
public class AttractionController {

    private final AttractionService attractionService;

    @GetMapping("/init")
    @Operation(summary = "관광지 초기 데이터 로드", description = "관광지 초기 화면 접근 시 필요한 데이터를 로드합니다.")
    public ResponseEntity<AttractionInitDataResponseDto> getInitialData() {

        AttractionInitDataResponseDto response = attractionService.getAttractionInitialData(1, 5);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "관광지 조건 검색", description = "지역, 시군구, 관광지 유형, 키워드, 페이지, 정렬 조건을 통해 관광지를 검색합니다.")
    public ResponseEntity<PagedAttractionResponseDto> searchAttractions(
            @Parameter(description = "시도 코드", example = "1") @RequestParam(required = false) Integer sidoCode,
            @Parameter(description = "시군구 코드", example = "1") @RequestParam(required = false) Integer gugunCode,
            @Parameter(description = "관광지 유형 코드", example = "12") @RequestParam(required = false) Integer type,
            @Parameter(description = "검색 키워드", example = "공원") @RequestParam(required = false) String word,
            @Parameter(description = "페이지 번호", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "페이지 크기", example = "5") @RequestParam(defaultValue = "5") int size,
            @Parameter(description = "정렬 기준 (예: name, likes, views)", example = "name") @RequestParam(defaultValue = "name") String sortBy) {

        // Service 호출 및 결과 반환
        PagedAttractionResponseDto response = attractionService.searchAttractions(sidoCode, gugunCode, type, word, page, size, sortBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/guguns/{sidoId}")
    @Operation(summary = "구군 리스트 조회", description = "입력된 시도 코드에 해당하는 구군 리스트를 조회합니다.")
    public ResponseEntity<List<GuGunDto>> getGuGunList(
            @Parameter(description = "시도 코드", required = true, example = "1") @PathVariable("sidoId") int sidoCode) {
        List<GuGunDto> gugunList = attractionService.getGuGunList(sidoCode);
        return ResponseEntity.ok(gugunList);
    }

    @GetMapping("/detail/{attractionId}")
    @Operation(summary = "관광지 상세 정보 조회", description = "관광지 ID를 사용하여 관광지의 상세 정보를 조회합니다.")
    public ResponseEntity<AttractionDetailDto> getAttractionDetail(
            @Parameter(description = "관광지 ID", required = true, example = "3820") @PathVariable("attractionId") int attractionId) {
        AttractionDetailDto attraction = attractionService.getAttractionDetail(attractionId);
        return ResponseEntity.ok(attraction);
    }
}
