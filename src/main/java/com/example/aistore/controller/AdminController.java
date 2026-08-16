package com.example.aistore.controller;

import com.example.aistore.entity.CustomerFeedback;
import com.example.aistore.entity.Order;
import com.example.aistore.entity.Product;
import com.example.aistore.repository.*;
import com.example.aistore.service.CustomerFeedbackService;
import com.example.aistore.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final CustomerFeedbackRepository feedbackRepository;
    private final CustomerFeedbackService feedbackService;
    private final UserInteractionRepository interactionRepository;
    private final ProductService productService;
    public AdminController(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, ReviewRepository reviewRepository, CustomerFeedbackRepository feedbackRepository, CustomerFeedbackService feedbackService, UserInteractionRepository interactionRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.feedbackRepository = feedbackRepository;
        this.feedbackService = feedbackService;
        this.interactionRepository = interactionRepository;
        this.productService = productService;
    }


    @GetMapping
    public String adminDashboard(Model model) {
        BigDecimal totalRevenue = orderRepository.sumTotalRevenue();
        long totalOrders = orderRepository.countTotalOrders();
        long totalProducts = productRepository.count();
        long totalUsers = userRepository.count();

        Map<String, Long> sentiments = feedbackService.getSentimentDistribution();
        long positive = sentiments.getOrDefault("Positive", 0L);
        long totalFeedback = sentiments.values().stream().mapToLong(Long::longValue).sum();
        double positivePct = totalFeedback > 0 ? ((double) positive / totalFeedback) * 100.0 : 0.0;

        List<Order> recentOrders = orderRepository.findTop10ByOrderByCreatedAtDesc();
        List<Object[]> topNegativeProducts = feedbackService.getProductsWithMostNegativeFeedback(5);

        model.addAttribute("totalRevenue", totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("positivePct", Math.round(positivePct * 10.0) / 10.0);
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("topNegativeProducts", topNegativeProducts);

        return "admin/dashboard";
    }

    @GetMapping("/analytics")
    public String analyticsDashboard(Model model) {
        List<Object[]> mostViewed = interactionRepository.findMostViewedProducts(PageRequest.of(0, 8));
        List<Object[]> mostCartAdded = interactionRepository.findMostCartAddedProducts(PageRequest.of(0, 8));
        List<Object[]> topSelling = orderRepository.countOrdersByStatus();

        model.addAttribute("mostViewed", mostViewed);
        model.addAttribute("mostCartAdded", mostCartAdded);
        model.addAttribute("orderStatuses", topSelling);

        return "admin/analytics";
    }

    @GetMapping("/feedback")
    public String feedbackIntelligence(Model model) {
        Map<String, Long> sentiments = feedbackService.getSentimentDistribution();
        Map<String, Long> negativeTopics = feedbackService.getTopNegativeComplaintTopics();
        List<Object[]> negativeProducts = feedbackService.getProductsWithMostNegativeFeedback(10);
        List<CustomerFeedback> recentFeedbacks = feedbackService.getRecentFeedbacks(20);

        model.addAttribute("sentiments", sentiments);
        model.addAttribute("negativeTopics", negativeTopics);
        model.addAttribute("negativeProducts", negativeProducts);
        model.addAttribute("recentFeedbacks", recentFeedbacks);

        return "admin/feedback";
    }
}
