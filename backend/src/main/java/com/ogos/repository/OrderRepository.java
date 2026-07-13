package com.ogos.repository;

import com.ogos.entity.Order;
import com.ogos.entity.OrderStatus;
import com.ogos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserOrderByCreatedAtDesc(User user);

    List<Order> findByStatus(OrderStatus status);

    @Query("SELECT o FROM Order o JOIN o.items oi WHERE oi.product.vendor = :vendor ORDER BY o.createdAt DESC")
    List<Order> findOrdersByVendor(@Param("vendor") User vendor);

    @Query("SELECT COUNT(o) FROM Order o JOIN o.items oi WHERE oi.product.vendor = :vendor")
    long countOrdersByVendor(@Param("vendor") User vendor);

    @Query("SELECT COUNT(o) FROM Order o JOIN o.items oi WHERE oi.product.vendor = :vendor AND o.status = :status")
    long countOrdersByVendorAndStatus(@Param("vendor") User vendor, @Param("status") OrderStatus status);

    @Query("SELECT COALESCE(SUM(oi.totalPrice), 0) FROM OrderItem oi WHERE oi.product.vendor = :vendor AND oi.order.status = :status")
    Double calculateRevenueByVendor(@Param("vendor") User vendor, @Param("status") OrderStatus status);
}
