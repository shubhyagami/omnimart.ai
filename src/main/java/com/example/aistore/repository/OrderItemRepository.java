package com.example.aistore.repository;

import com.example.aistore.entity.Order;
import com.example.aistore.entity.OrderItem;
import com.example.aistore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);

    @Query("SELECT oi.product.id, oi.product.name, SUM(oi.quantity), SUM(oi.totalPrice) " +
           "FROM OrderItem oi GROUP BY oi.product.id, oi.product.name ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findTopSellingProducts();

    @Query("SELECT COUNT(oi) > 0 FROM OrderItem oi WHERE oi.order.user.id = :userId AND oi.product.id = :productId")
    boolean existsByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}
