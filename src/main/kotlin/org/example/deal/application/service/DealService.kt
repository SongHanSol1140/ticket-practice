package org.example.deal.application.service

import org.example.deal.application.dto.DealEndDto
import org.example.deal.application.dto.DealStartDto
import org.example.deal.domain.model.Deal
import org.example.deal.infrastructure.KakaoPaymentGateway
import org.example.deal.infrastructure.PaymentGatewayResolver
import org.example.deal.repository.DealJpaRepository
import org.example.ticket.domain.model.Ticket
import org.example.ticket.infra.repository.TicketJpaRepository
import org.example.user.repository.UserJpaRepository
import org.springframework.stereotype.Service

@Service
class DealService (
    private val dealRepository: DealJpaRepository,
    private val ticketRepository: TicketJpaRepository,
    private val paymentGatewayResolver: PaymentGatewayResolver
){

    fun dealStart(dealStartDto: DealStartDto): Deal{
        val ticket = requireNotNull(ticketRepository.findByBarcode(dealStartDto.barcode)){"존재하지 않는 티켓입니다."}
        val sellingPrice = requireNotNull(ticket.sellingPrice){"판매가가 설정되지 않았습니다."}

        val deal = Deal(
            barcode = ticket.barcode,
            sellerName = ticket.sellerName,
            buyerName = dealStartDto.buyerName,
            sellingPrice = sellingPrice
        )

        ticket.ticketReserve();
        ticketRepository.save(ticket)
        return dealRepository.save(deal)
    }

    fun dealEnd(dealEndDto: DealEndDto): Deal{
        val deal = requireNotNull(dealRepository.findByBarcode(dealEndDto.barcode)){"존재하지 않는 거래입니다."}
        val ticket = requireNotNull(ticketRepository.findByBarcode(dealEndDto.barcode)){"존재하지 않는 티켓입니다."}
        val dealExpiredCheck = deal.reservedTimeExpiredCheck();
        if(dealExpiredCheck){
            deal.dealCancel()
            ticket.ticketOnSale()
            throw IllegalArgumentException("10분 이내 입금이 되지않아 결제가 취소되었습니다.")
        }
        val paymentGateway = paymentGatewayResolver.resolve(dealEndDto.payementType)
        paymentGateway.pay(deal.buyerName, deal.sellerName, deal.sellingPrice)
        deal.dealComplete()
        ticket.ticketSold()
        ticketRepository.save(ticket)
        return dealRepository.save(deal)
    }

}