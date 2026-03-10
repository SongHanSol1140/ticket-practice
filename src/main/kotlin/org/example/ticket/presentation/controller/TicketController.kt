package org.example.ticket.presentation.controller

import org.example.ticket.application.dto.TicketCreationDto
import org.example.ticket.application.dto.TicketResponseDto
import org.example.ticket.application.dto.TicketSellingPriceOfferDto
import org.example.ticket.application.service.TicketService
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tickets")
class TicketController(
    private val ticketService: TicketService
) {
    @PostMapping("")
    fun createTicket(@RequestBody requestDto: TicketCreationDto): TicketResponseDto {
        return ticketService.createTicket(requestDto)
    }

    @PatchMapping("/{barcode}/price")
    fun applySellerOfferPrice(
        @PathVariable("barcode") barcode: String,
        @RequestBody dto: TicketSellingPriceOfferDto
    ): TicketResponseDto {
        return ticketService.applySellerOfferPrice(barcode, dto)
    }
}