package org.example.deal.presentation.controller

import org.example.deal.application.dto.DealEndDto
import org.example.deal.application.dto.DealResponseDto
import org.example.deal.application.dto.DealStartDto
import org.example.deal.application.service.DealService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/deal"])
class DealController(
    private val dealService: DealService
) {
    @PostMapping("/start")
    fun dealStart(@RequestBody dealStartDto: DealStartDto): DealResponseDto {
        return dealService.dealStart(dealStartDto)
    }

    @PostMapping("/end")
    fun dealEnd(@RequestBody dealEndDto: DealEndDto): DealResponseDto {
        return dealService.dealEnd(dealEndDto)
    }
}