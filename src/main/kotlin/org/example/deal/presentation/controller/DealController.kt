package org.example.deal.presentation.controller

import org.example.deal.application.dto.DealEndDto
import org.example.deal.application.dto.DealResponseDto
import org.example.deal.application.dto.DealStartDto
import org.example.deal.application.service.DealService
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/deals"])
class DealController(
    private val dealService: DealService
) {
    @PostMapping("")
    fun dealStart(@RequestBody dealStartDto: DealStartDto): DealResponseDto {
        return dealService.dealStart(dealStartDto)
    }

    @PatchMapping("/{barcode}")
    fun dealEnd(
        @PathVariable("barcode") barcode: String,
        @RequestBody dealEndDto: DealEndDto
    ): DealResponseDto {
        return dealService.dealEnd(barcode, dealEndDto)
    }
}