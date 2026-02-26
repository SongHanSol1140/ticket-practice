package org.example.deal.application.service

import org.example.deal.domain.model.Deal
import org.example.ticket.domain.enum.TicketStatus
import org.example.ticket.domain.model.Ticket
import org.example.user.repository.UserRepository

class DealService (
    private val userRepository: UserRepository,
){

    fun dealStart(ticket: Ticket, buyerName:String): Deal{
        val sellingPrice = requireNotNull(ticket.sellingPrice) { "판매 가격이 설정되지 않은 티켓은 거래할 수 없습니다." }
        val deal = Deal(ticket.barcode, ticket.sellerName, buyerName, sellingPrice);
        ticket.ticketReserve();
        return deal
    }

    fun dealComplete(deal: Deal, ticket: Ticket){
        val seller = userRepository.findByName(deal.sellerName)
        val buyer = userRepository.findByName(deal.buyerName)
        requireNotNull(seller){"판매자 정보를 찾을 수 없습니다."}
        requireNotNull(buyer){"구매자 정보를 찾을 수 없습니다."}
        buyer.withdraw(deal.sellingPrice)
        seller.deposit(deal.sellingPrice)
        ticket.ticketSold()
    }

}