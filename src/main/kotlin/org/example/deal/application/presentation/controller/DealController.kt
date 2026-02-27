package org.example.deal.application.presentation.controller

import org.example.deal.application.dto.DealEndDto
import org.example.deal.application.dto.DealResponseDto
import org.example.deal.application.dto.DealStartDto
import org.example.deal.application.service.DealService
import org.example.deal.domain.enum.DealStatus
import org.example.deal.domain.model.Deal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.String

@RestController
@RequestMapping(value = ["/deal"])
class DealController (
    private val dealService: DealService
){
    @PostMapping("/start")
    fun dealStart(@RequestBody dealStartDto: DealStartDto ): DealResponseDto {
        val deal = dealService.dealStart(dealStartDto)
        return DealResponseDto(
            barcode = deal.barcode,
            sellerName = deal.sellerName,
            buyerName = deal.buyerName,
            sellingPrice = deal.sellingPrice,
            reservedDateTime = deal.reservedDateTime,
            dealStatus = deal.getDealStatus()
        )
    }
    @PostMapping("/end")
    fun dealEnd(@RequestBody dealEndDto: DealEndDto): DealResponseDto {
        val deal = dealService.dealEnd(dealEndDto)
        return DealResponseDto(
            barcode = deal.barcode,
            sellerName = deal.sellerName,
            buyerName = deal.buyerName,
            sellingPrice = deal.sellingPrice,
            reservedDateTime = deal.reservedDateTime,
            dealStatus = deal.getDealStatus()
        )
    }
}