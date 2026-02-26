package org.example.deal.application.service

import org.example.deal.domain.model.Deal
import org.example.ticket.domain.enum.TicketStatus
import org.example.ticket.domain.model.Ticket

class DealService (){

    fun dealStart(ticket: Ticket, buyerName:String): Deal{
        val deal = Deal(ticket.sellerName, buyerName)
        ticket.ticketStatus = TicketStatus.RESERVED
        return deal
    }

}