package org.example.deal.repository

import org.example.deal.domain.model.Deal
import org.springframework.data.jpa.repository.JpaRepository

interface DealJpaRepository: JpaRepository<Deal, Long> {
    fun findByBarcode(barcode: String): Deal?
    fun findBySellerName(sellerName: String): Deal?
}
