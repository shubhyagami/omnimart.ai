package com.example.aistore.repository;

import com.example.aistore.entity.Order;
import com.example.aistore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    Optional<Order> findByOrderNumber(String orderNumber);
    Optional<Order> findByIdAndUser(Long id, User user);
    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<Order> findTop10ByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(o) FROM Order o")
    long countTotalOrders();

    @Query("SELECT COALESCE(SUM(o.finalAmount), 0) FROM Order o WHERE o.status <> 'CANCELLED'")
    BigDecimal sumTotalRevenue();

    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    List<Object[]> countOrdersByStatus();
}
