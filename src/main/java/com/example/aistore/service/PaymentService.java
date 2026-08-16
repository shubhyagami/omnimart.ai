package com.example.aistore.service;

import com.example.aistore.entity.Order;
import com.example.aistore.entity.Payment;
import com.example.aistore.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    @Transactional
    public Payment processPayment(Order order, String paymentMethod, BigDecimal amount) {
        String txId = "TXN-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        
        Payment payment = Payment.builder()
                .order(order)
                .transactionId(txId)
                .paymentMethod(paymentMethod)
                .amount(amount)
                .paymentStatus(Payment.PaymentStatus.COMPLETED)
                .build();

        return paymentRepository.save(payment);
    }
}
