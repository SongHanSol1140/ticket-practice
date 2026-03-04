package org.example.ticket.presentation.controller

import org.example.ticket.application.dto.TicketCreationDto
import org.example.ticket.application.dto.TicketResponseDto
import org.example.ticket.application.dto.TicketSellingPriceOfferDto
import org.example.ticket.application.service.TicketService
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tickets")
class TicketController(
    private val ticketService: TicketService
) {
    @PostMapping("/create")
    fun createTicket(@RequestBody requestDto: TicketCreationDto): TicketResponseDto {
        return ticketService.createTicket(requestDto)
    }

    @PatchMapping("/price")
    fun applySellerOfferPrice(@RequestBody dto: TicketSellingPriceOfferDto): TicketResponseDto {
        return ticketService.applySellerOfferPrice(dto)
    }

    @PostMapping("/resale")
    fun reSaleTicket(@RequestBody dto: TicketSellingPriceOfferDto): TicketResponseDto {
        return ticketService.reSaleTicket(dto)
    }
}