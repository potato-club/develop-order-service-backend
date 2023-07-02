package com.server.dos.controller;

import com.server.dos.dto.*;
import com.server.dos.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "발주 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "모든 발주 리스트 반환")
    @GetMapping("")
    public ResponseEntity<Page<OrderListResponseDto>> getAllOrder(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(required = false, defaultValue = "1", value = "page") int page) {
        Page<OrderListResponseDto> allOrder = orderService.getAllOrder(token, page);
        return ResponseEntity.ok(allOrder);
    }

    @Operation(summary = "단일 발주 정보 반환")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable(name = "orderId") Long orderId) {
        OrderResponseDto orderDto = orderService.getOrder(token, orderId);
        return ResponseEntity.ok(orderDto);
    }

    @Operation(summary = "메인 페이지 발주 리스트 반환")
    @GetMapping("/main")
    public ResponseEntity<List<OrderMainDto>> getMainOrderDetailList() {
        List<OrderMainDto> mainOrders = orderService.getMainOrders();
        return ResponseEntity.ok(mainOrders);
    }

    @Operation(summary = "모든 미팅 시간 반환")
    @GetMapping("/meeting")
    public ResponseEntity<List<MeetingDateDto>> getOrdersMeetingList() {
        List<MeetingDateDto> allMeeting = orderService.getAllOrderMeeting();
        return ResponseEntity.ok(allMeeting);
    }

    @Operation(summary = "발주 디테일 리스트 반환")
    @GetMapping("/detail")
    public ResponseEntity<Page<OrderDetailListDto>> getOrderDetailList(
            @RequestParam(required = false, defaultValue = "working", value = "state") String state,
            @RequestParam(required = false, defaultValue = "latest", value = "sort") String sort,
            @RequestParam(required = false, defaultValue = "1", value = "page") int page) {
        Page<OrderDetailListDto> orderDetailList = orderService.getOrderDetailList(state, sort, page);
        return ResponseEntity.ok(orderDetailList);
    }

    @Operation(summary = "발주 디테일 반환")
    @GetMapping("/detail/{orderId}")
    public ResponseEntity<OrderDetailDto> getOrderDetail(@PathVariable(name = "orderId") Long orderId) {
        OrderDetailDto orderDetail = orderService.getOrderDetail(orderId);
        return ResponseEntity.ok(orderDetail);
    }

    @Operation(summary = "사이트 제목 중복 체크")
    @GetMapping("/{siteName}/exists")
    public ResponseEntity<Boolean> checkSiteNameDuplicate(@PathVariable String siteName) {
        return ResponseEntity.ok(orderService.checkSiteNameDuplicate(siteName));
    }

    @Operation(summary = "발주 신청")
    @PostMapping("")
    public ResponseEntity<String> createOrder(@RequestHeader(value = "Authorization") String token,
                                              @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                              @RequestPart(value = "orderDto") OrderRequestDto orderDto) {
        orderService.createOrder(token, files, orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("발주 성공");
    }

    @Operation(summary = "게시글 추천/취소")
    @PostMapping("/detail/{orderId}/likes")
    public ResponseEntity<String> likeOrderDetail(@RequestHeader(value = "Authorization") String token,
                                                  @PathVariable(name = "orderId") Long orderId) {
        String res = orderService.orderLike(token, orderId);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "발주 디테일 수정")
    @PutMapping("/detail/{orderId}")
    public ResponseEntity<String> updateOrderDetail(@RequestHeader(value = "Authorization") String token,
                                                    @PathVariable(name = "orderId") Long orderId,
                                                    @RequestPart(value = "images",
                                                            required = false) List<MultipartFile> images,
                                                    @RequestPart(value = "orderDetail",
                                                            required = false) OrderDetailRequestDto requestDto) {
        orderService.updateOrderDetail(token, orderId, images, requestDto);
        return ResponseEntity.ok("업데이트 완료");
    }

    @Operation(summary = "완료된 발주 별점 추가")
    @PutMapping("/detail/{orderId}/rating")
    public ResponseEntity<String> addRating(@RequestHeader(value = "Authorization") String token,
                                            @PathVariable(name = "orderId") Long orderId,
                                            @RequestBody OrderRatingDto rating) {
        orderService.addRating(token, orderId, rating);
        return ResponseEntity.ok("별점 추가 완료");
    }

    @Operation(summary = "발주 취소")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> removeOrder(@RequestHeader(value = "Authorization") String token,
                                         @PathVariable(name = "orderId") Long orderId) {
        orderService.removeOrder(token, orderId);
        return ResponseEntity.ok("취소 완료");
    }
}
