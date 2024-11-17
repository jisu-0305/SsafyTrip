package com.trip.attraction.controller;

import com.trip.attraction.dto.AttractionListDto;
import com.trip.attraction.dto.AttractionDetailDto;
import com.trip.attraction.dto.ContentTypeDto;
import com.trip.attraction.dto.GuGunDto;
import com.trip.attraction.dto.SidoDto;
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
@RequestMapping("/attraction")
@Tag(name = "관광지 컨트롤러", description = "관광지 관련 서비스 제공 (리스트 조회, 지역 및 상세 정보 조회)")
@RequiredArgsConstructor
public class AttractionController {

    private final AttractionService attractionService;

    @GetMapping("/list")
    @Operation(summary = "관광지 정보 리스트 조회", description = "지역, 시/군/구, 관광지 유형, 키워드를 기준으로 관광지 정보를 검색합니다.")
    public ResponseEntity<List<AttractionListDto>> getAttractionList(
            @Parameter(description = "시도 코드", example = "1") @RequestParam(required = false) Integer sido,
            @Parameter(description = "시군구 코드", example = "1") @RequestParam(required = false) Integer sigun,
            @Parameter(description = "관광지 유형 코드", example = "12") @RequestParam(required = false) Integer type,
            @Parameter(description = "검색 키워드", example = "공원") @RequestParam(required = false) String word) {

        List<AttractionListDto> attractions = attractionService.getAttractionList(sido, sigun, type, word);
        return ResponseEntity.ok(attractions);
    }

    @GetMapping("/sido")
    @Operation(summary = "시도 리스트 조회", description = "모든 시도의 정보를 조회합니다.")
    public ResponseEntity<List<SidoDto>> getSidosList() {
        List<SidoDto> sidos = attractionService.getSidosList();
        return ResponseEntity.ok(sidos);
    }

    @GetMapping("/gugun")
    @Operation(summary = "구군 리스트 조회", description = "입력된 시도 코드에 해당하는 구군 리스트를 조회합니다.")
    public ResponseEntity<List<GuGunDto>> getGuGunList(
            @Parameter(description = "시도 코드", required = true, example = "1") @RequestParam("sido") int sidoCode) {
        List<GuGunDto> guguns = attractionService.getGuGunList(sidoCode);
        return ResponseEntity.ok(guguns);
    }

    @GetMapping("/list/{contentId}")
    @Operation(summary = "관광지 상세 정보 조회", description = "관광지 ID를 사용하여 관광지의 상세 정보를 조회합니다.")
    public ResponseEntity<AttractionDetailDto> getAttractionDetail(
            @Parameter(description = "관광지 ID", required = true, example = "1001") @PathVariable("contentId") int contentId) {
        AttractionDetailDto attraction = attractionService.getAttractionDetail(contentId);
        return ResponseEntity.ok(attraction);
    }

    @GetMapping("/content")
    @Operation(summary = "관광지 유형 조회", description = "관광지의 모든 유형(Content Type)을 조회합니다.")
    public ResponseEntity<List<ContentTypeDto>> getContentList() {
        List<ContentTypeDto> contentTypes = attractionService.getContentList();
        return ResponseEntity.ok(contentTypes);
    }
}
