package org.example.deal.application.service

import jakarta.transaction.Transactional
import org.example.deal.application.dto.DealEndDto
import org.example.deal.application.dto.DealResponseDto
import org.example.deal.application.dto.DealStartDto
import org.example.deal.domain.model.Deal
import org.example.deal.infrastructure.api.PaymentGatewayResolver
import org.example.deal.infrastructure.repository.DealJpaRepository
import org.example.ticket.infrastructure.repository.TicketJpaRepository
import org.springframework.stereotype.Service

@Service
class DealService(
    private val dealRepository: DealJpaRepository,
    private val ticketRepository: TicketJpaRepository,
    private val paymentGatewayResolver: PaymentGatewayResolver
) {
    @Transactional
    fun dealStart(dealStartDto: DealStartDto): DealResponseDto {
        val ticket = requireNotNull(ticketRepository.findByBarcode(dealStartDto.barcode)) { "존재하지 않는 티켓입니다." }
        val sellingPrice = requireNotNull(ticket.sellingPrice) { "판매가가 설정되지 않았습니다." }

        val deal = Deal(
            barcode = ticket.barcode,
            sellerName = ticket.sellerName,
            buyerName = dealStartDto.buyerName,
            sellingPrice = sellingPrice
        )

        ticket.ticketReserve()
        val savedDeal = dealRepository.save(deal)
        return DealResponseDto(
            barcode = savedDeal.barcode,
            sellerName = savedDeal.sellerName,
            buyerName = savedDeal.buyerName,
            sellingPrice = savedDeal.sellingPrice,
            reservedDateTime = savedDeal.reservedDateTime,
            dealStatus = savedDeal.dealStatus
        )
    }
    @Transactional
    fun dealEnd(barcode:String, dealEndDto: DealEndDto): DealResponseDto {
        val deal = requireNotNull(dealRepository.findByBarcode(barcode)) { "존재하지 않는 거래입니다." }
        val ticket = requireNotNull(ticketRepository.findByBarcode(barcode)) { "존재하지 않는 티켓입니다." }
        val dealExpiredCheck = deal.reservedTimeExpiredCheck()
        if (dealExpiredCheck) {
            deal.dealCancel()
            ticket.ticketOnSale()
            return DealResponseDto(
                barcode = deal.barcode,
                sellerName = deal.sellerName,
                buyerName = deal.buyerName,
                sellingPrice = deal.sellingPrice,
                reservedDateTime = deal.reservedDateTime,
                dealStatus = deal.dealStatus
            )
        }
        val paymentGateway = paymentGatewayResolver.resolve(dealEndDto.paymentType)
        paymentGateway.pay(deal.buyerName, deal.sellerName, deal.sellingPrice)
        deal.dealComplete()
        ticket.ticketSold()
        return DealResponseDto(
            barcode = deal.barcode,
            sellerName = deal.sellerName,
            buyerName = deal.buyerName,
            sellingPrice = deal.sellingPrice,
            reservedDateTime = deal.reservedDateTime,
            dealStatus = deal.dealStatus
        )
    }

}