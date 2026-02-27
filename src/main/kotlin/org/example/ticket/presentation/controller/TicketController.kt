package org.example.ticket.presentation.controller

import org.example.ticket.application.dto.TicketCreationDto
import org.example.ticket.application.dto.TicketResponseDto
import org.example.ticket.application.dto.TicketSellingPriceOfferDto
import org.example.ticket.application.service.TicketService.TicketService
import org.springframework.http.ResponseEntity
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
        val ticket = ticketService.createTicket(requestDto)
        return TicketResponseDto(
            barcode = ticket.barcode,
            seller = ticket.sellerName,
            originalPrice = ticket.originalPrice,
            sellingPrice = ticket.sellingPrice,
            expirationDate = ticket.expirationDateTime,
            ticketStatus = ticket.getTicketStatus(),
        )
    }

    @PatchMapping("/price")
    fun applySellerOfferPrice(@RequestBody dto: TicketSellingPriceOfferDto): TicketResponseDto {
        val ticket = ticketService.applySellerOfferPrice(dto)
        return TicketResponseDto(
            barcode = ticket.barcode,
            seller = ticket.sellerName,
            originalPrice = ticket.originalPrice,
            sellingPrice = ticket.sellingPrice,
            expirationDate = ticket.expirationDateTime,
            ticketStatus = ticket.getTicketStatus(),
        )
    }

    @PostMapping("/resale")
    fun reSaleTicket(@RequestBody dto: TicketSellingPriceOfferDto): TicketResponseDto {
        val ticket = ticketService.reSaleTicket(dto)
        return TicketResponseDto(
            barcode = ticket.barcode,
            seller = ticket.sellerName,
            originalPrice = ticket.originalPrice,
            sellingPrice = ticket.sellingPrice,
            expirationDate = ticket.expirationDateTime,
            ticketStatus = ticket.getTicketStatus(),
        )
    }


}