package com.example.aistore.ai;

import com.example.aistore.dto.FeedbackAnalysisDto;
import com.example.aistore.dto.ProductCardDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MockAIProvider implements AIProvider {

    @Override
    public String getProviderName() {
        return "MockAIProvider (Deterministic Spec Engine)";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String generateChatResponse(String systemPrompt, String userMessage, List<Map<String, String>> conversationHistory) {
        String msgLower = userMessage != null ? userMessage.split("\n\n\\[")[0].toLowerCase() : "";
        if (msgLower.contains("review") || msgLower.contains("feedback") || msgLower.contains("battery") || msgLower.contains("complaint") || msgLower.contains("customer say")) {
            if (msgLower.contains("battery")) {
                return "Based on verified customer feedback and review telemetry for this product, users report solid battery endurance for everyday browsing and productivity, with fast-charging support to quickly top up before intensive tasks.";
            }
            return "Based on verified customer reviews in our database, users report high overall satisfaction with build quality and display performance, with minimal reported issues.";
        }

        if (msgLower.contains("compare") || msgLower.contains("difference")) {
            return "I've analyzed the specifications between the candidate models. Here is a direct comparison highlighting their differences in display quality, chipset performance, camera capabilities, and value for money.";
        }

        if (msgLower.contains("camera") || msgLower.contains("photo") || msgLower.contains("ois")) {
            return "For photography and videography enthusiasts, here are the top models offering high megapixel sensors, optical image stabilization (OIS), and 4K recording capabilities within your budget:";
        }
        
        if (msgLower.contains("gaming") || msgLower.contains("fps") || msgLower.contains("rtx")) {
            return "Based on your interest in gaming, I've filtered our catalog for high-refresh displays and powerful dedicated GPUs. Here are the top rated gaming systems currently in stock:";
        }

        if (msgLower.contains("budget") || msgLower.contains("under") || msgLower.contains("cheap") || msgLower.contains("price")) {
            return "I have filtered our inventory to find the highest-value products matching your exact price constraints. Here are the recommended models:";
        }

        return "I've analyzed your request and queried our current inventory. Here are the best matching products along with their key specifications:";
    }

    @Override
    public FeedbackAnalysisDto analyzeCustomerFeedback(String reviewTitle, String reviewComment, int rating) {
        String fullText = (reviewTitle + " " + reviewComment).toLowerCase();
        
        String sentiment = "Neutral";
        String emotion = "Neutral";
        if (rating >= 4) {
            sentiment = "Positive";
            emotion = rating == 5 ? "Delighted" : "Satisfied";
        } else if (rating <= 2) {
            sentiment = "Negative";
            emotion = "Disappointed";
        } else {
            sentiment = "Mixed";
            emotion = "Frustrated";
        }

        String primaryTopic = "General";
        List<String> issues = new ArrayList<>();
        List<String> positives = new ArrayList<>();

        // Topic detection
        if (fullText.contains("battery") || fullText.contains("drain") || fullText.contains("charging")) {
            primaryTopic = "Battery";
            if (fullText.contains("drain") || fullText.contains("poor") || fullText.contains("slow") || fullText.contains("heating") || rating <= 3) {
                issues.add("Battery life or charging performance");
            } else {
                positives.add("Long battery backup");
            }
        }
        if (fullText.contains("display") || fullText.contains("screen") || fullText.contains("amoled") || fullText.contains("brightness")) {
            if (primaryTopic.equals("General")) primaryTopic = "Display";
            if (fullText.contains("flicker") || fullText.contains("dim") || fullText.contains("broken")) {
                issues.add("Screen brightness or display artifact");
            } else {
                positives.add("Vibrant AMOLED display");
            }
        }
        if (fullText.contains("camera") || fullText.contains("photo") || fullText.contains("lens")) {
            if (primaryTopic.equals("General")) primaryTopic = "Camera";
            if (fullText.contains("grainy") || fullText.contains("blurry") || fullText.contains("night")) {
                issues.add("Low light camera quality");
            } else {
                positives.add("Sharp camera and portrait mode");
            }
        }
        if (fullText.contains("lag") || fullText.contains("heat") || fullText.contains("slow") || fullText.contains("fps") || fullText.contains("processor")) {
            if (primaryTopic.equals("General")) primaryTopic = "Performance";
            if (rating <= 3) {
                issues.add("Thermal throttling or UI stutter");
            } else {
                positives.add("Fast application loading");
            }
        }
        if (fullText.contains("delivery") || fullText.contains("shipping") || fullText.contains("box") || fullText.contains("package")) {
            if (primaryTopic.equals("General")) primaryTopic = "Delivery";
            if (rating <= 3) {
                issues.add("Delayed shipping or damaged box packaging");
            } else {
                positives.add("Super fast 1-day delivery");
            }
        }
        if (fullText.contains("sound") || fullText.contains("speaker") || fullText.contains("bass") || fullText.contains("audio")) {
            if (primaryTopic.equals("General")) primaryTopic = "Audio";
            if (rating <= 3) {
                issues.add("Low bass or muffled speaker volume");
            } else {
                positives.add("Crystal clear audio and deep bass");
            }
        }

        if (positives.isEmpty() && rating >= 4) {
            positives.add("Excellent value for price and premium feel");
        }
        if (issues.isEmpty() && rating <= 2) {
            issues.add("Product did not meet expectations");
        }

        return FeedbackAnalysisDto.builder()
                .sentiment(sentiment)
                .emotion(emotion)
                .primaryTopic(primaryTopic)
                .specificIssues(issues)
                .positiveAspects(positives)
                .confidenceScore(0.92)
                .build();
    }

    @Override
    public String generateProductComparisonSummary(List<ProductCardDto> products, Map<String, Map<Long, String>> specMatrix) {
        if (products.isEmpty()) {
            return "No products available for comparison.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Comprehensive AI Spec Comparison for ").append(products.size()).append(" selected products:\n\n");
        for (ProductCardDto p : products) {
            sb.append("• **").append(p.getName()).append("** (₹").append(p.getPrice()).append("): ")
              .append("Rated ").append(p.getRating()).append("★ with ").append(p.getReviewCount()).append(" verified reviews. ");
            if (p.getTags() != null) {
                sb.append("Key strengths include: ").append(p.getTags()).append(". ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public Map<String, Object> parseNaturalLanguageSearch(String searchQuery) {
        Map<String, Object> params = new HashMap<>();
        String lower = searchQuery.toLowerCase();

        // Budget extraction: "under 40000", "under 40k", "below ₹30,000", "<50000"
        Pattern budgetPattern = Pattern.compile("(?:under|below|less than|within|budget of|<|upto|up to)\\s*₹?\\s*(\\d+)(?:k|000)?");
        Matcher budgetMatcher = budgetPattern.matcher(lower);
        if (budgetMatcher.find()) {
            String numStr = budgetMatcher.group(1);
            long budget = Long.parseLong(numStr);
            if (lower.contains("k") && budget < 1000) {
                budget = budget * 1000;
            } else if (budget < 1000) {
                budget = budget * 1000; // treat 40 as 40k
            }
            params.put("maxPrice", BigDecimal.valueOf(budget));
        }

        // Min budget extraction: "above 20000", "from 15k"
        Pattern minBudgetPattern = Pattern.compile("(?:above|more than|from|>)\\s*₹?\\s*(\\d+)(?:k|000)?");
        Matcher minMatcher = minBudgetPattern.matcher(lower);
        if (minMatcher.find()) {
            String numStr = minMatcher.group(1);
            long budget = Long.parseLong(numStr);
            if (lower.contains("k") && budget < 1000) budget = budget * 1000;
            params.put("minPrice", BigDecimal.valueOf(budget));
        }

        // Category keywords - check specific compound terms first
        if (lower.contains("gaming laptop") || lower.contains("gaming notebook") || lower.contains("laptop for gaming")) {
            params.put("category", "Laptops");
            if (!params.containsKey("features")) params.put("features", new ArrayList<>(List.of("gaming")));
        } else if (lower.contains("coding laptop") || lower.contains("programming laptop")) {
            params.put("category", "Laptops");
            if (!params.containsKey("features")) params.put("features", new ArrayList<>(List.of("coding", "programming")));
        } else if (lower.contains("camera phone") || lower.contains("phone with best camera") || lower.contains("best camera phone")) {
            params.put("category", "Smartphones");
            if (!params.containsKey("features")) params.put("features", new ArrayList<>(List.of("camera")));
        } else if (lower.contains("phone") || lower.contains("smartphone") || lower.contains("mobile") || lower.contains("iphone") || lower.contains("galaxy")) {
            params.put("category", "Smartphones");
        } else if (lower.contains("laptop") || lower.contains("notebook") || lower.contains("macbook") || lower.contains("ultrabook") || lower.contains("thinkpad") || lower.contains("legion")) {
            params.put("category", "Laptops");
        } else if (lower.contains("headphone") || lower.contains("earphone") || lower.contains("earbud") || lower.contains("audio") || lower.contains("tws") || lower.contains("airpods")) {
            params.put("category", "Headphones");
        } else if (lower.contains("console") || lower.contains("playstation") || lower.contains("ps5") || lower.contains("xbox") || lower.contains("nintendo")) {
            params.put("category", "Gaming");
        } else if (lower.contains("gaming") || lower.contains("rtx")) {
            // "gaming" by itself could be gaming laptops or gaming gear
            if (lower.contains("phone")) {
                params.put("category", "Smartphones");
            } else {
                params.put("category", "Laptops"); // Default high-intent gaming to Laptops
            }
        } else if (lower.contains("watch") || lower.contains("smart home") || lower.contains("speaker") || lower.contains("alexa")) {
            params.put("category", "Smart Home");
        } else if (lower.contains("camera") || lower.contains("dslr") || lower.contains("lens")) {
            params.put("category", "Cameras");
        } else if (lower.contains("monitor") || lower.contains("display") || lower.contains("screen")) {
            params.put("category", "Monitors");
        } else if (lower.contains("mouse") || lower.contains("keyboard") || lower.contains("accessory") || lower.contains("cable") || lower.contains("ssd")) {
            params.put("category", "Accessories");
        }

        // Brand extraction
        String[] knownBrands = {"samsung", "apple", "sony", "dell", "lenovo", "asus", "hp", "oneplus", "xiaomi", "bose", "logitech", "canon", "lg"};
        for (String brand : knownBrands) {
            if (lower.contains(brand)) {
                params.put("brand", brand.substring(0, 1).toUpperCase() + brand.substring(1));
                break;
            }
        }

        // Rating extraction: "4 star", "top rated", "best rated", "top rating", "highly rated"
        if (lower.contains("top rated") || lower.contains("top rating") || lower.contains("highly rated") || lower.contains("best rated") || lower.contains("4 star") || lower.contains("4+")) {
            params.put("minRating", 4.0);
        }

        // Features/Preferences extraction
        @SuppressWarnings("unchecked")
        List<String> features = (List<String>) params.getOrDefault("features", new ArrayList<>());
        if (lower.contains("camera") || lower.contains("photo") || lower.contains("ois") || lower.contains("lens")) {
            if (!features.contains("camera")) features.add("camera");
        }
        if (lower.contains("battery") || lower.contains("backup") || lower.contains("charging") || lower.contains("mah")) {
            if (!features.contains("battery")) features.add("battery");
        }
        if (lower.contains("gaming") || lower.contains("gpu") || lower.contains("rtx") || lower.contains("fps")) {
            if (!features.contains("gaming")) features.add("gaming");
        }
        if (lower.contains("oled") || lower.contains("amoled") || lower.contains("120hz") || lower.contains("144hz") || lower.contains("display") || lower.contains("screen")) {
            if (!features.contains("display")) features.add("display");
        }
        if (lower.contains("lightweight") || lower.contains("portable") || lower.contains("slim") || lower.contains("thin")) {
            if (!features.contains("portability")) features.add("portability");
        }
        if (lower.contains("coding") || lower.contains("programming") || lower.contains("developer")) {
            if (!features.contains("coding")) features.add("coding");
        }
        if (!features.isEmpty()) {
            params.put("features", features);
        }

        // Query keywords cleanup - strip numbers, budget phrases, categories, and stop words
        String cleaned = searchQuery
                .replaceAll("(?i)(?:under|below|less than|within|budget of|<|>|upto|up to|above|more than|from)\\s*₹?\\s*\\d+(?:k|000|lakh)?", "")
                .replaceAll("(?i)\\b(with|the|a|an|in|for|and|or|of|under|below|less|than|budget|upto|up|to|above|best|top|good|great|show|me|find|i|want|need|give|suggest|recommend|phone|phones|smartphone|smartphones|mobile|mobiles|laptop|laptops|headphone|headphones|earphones|camera|cameras|monitor|monitors|accessories|accessory|rated|rating)\\b", "")
                .replaceAll("[₹,]", "")
                .replaceAll("\\s+", " ")
                .trim();
        if (!cleaned.isEmpty() && !cleaned.matches("^[0-9\\s]+$")) {
            params.put("query", cleaned);
        }

        return params;
    }

    @Override
    public String answerAdminQuery(String adminQuestion, String analyticsContext) {
        String q = adminQuestion.toLowerCase();
        if (q.contains("negative") || q.contains("complaint") || q.contains("issue")) {
            return "Based on active customer review sentiment analysis, the top complaint topics are: **Battery Life (34% of negative reviews)** and **Packaging/Delivery (22%)**. Products with recurring thermal or battery feedback have been flagged for supplier quality review.";
        }
        if (q.contains("high view") || q.contains("low purchase") || q.contains("conversion") || q.contains("cart")) {
            return "Our behavioral telemetry identifies 3 flagship models with high view traffic (>500 weekly views) but lower-than-average checkout conversions (<4.2%). Customers drop off predominantly at the payment stage, indicating sensitivity to shipping charges or lack of bundled warranty.";
        }
        if (q.contains("feature") || q.contains("request")) {
            return "Customers frequently request: (1) Higher wattage fast chargers in-box, (2) Extended 2-year warranty options for high-tier gaming laptops, and (3) Matte screen finishes on professional monitors.";
        }
        if (q.contains("improve") || q.contains("recommendation")) {
            return "Priority Improvement Plan:\n1. Update supplier specifications for battery endurance under gaming loads.\n2. Add bundle discounts for accessories (Keyboards & Mice) with Gaming Laptops to increase basket size.\n3. Implement 1-day express delivery for top-tier electronics to reduce delivery complaint churn.";
        }
        return "Factual Store Summary: " + analyticsContext;
    }
}
