package com.example.aistore.config;

import com.example.aistore.entity.*;
import com.example.aistore.repository.*;
import com.example.aistore.service.CustomerFeedbackService;
import com.example.aistore.service.UserPreferenceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataSeeder implements CommandLineRunner {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DataSeeder.class);


    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductSpecificationRepository specRepository;
    private final InventoryRepository inventoryRepository;
    private final ReviewRepository reviewRepository;
    private final CustomerFeedbackService feedbackService;
    private final UserInteractionRepository interactionRepository;
    private final UserPreferenceService preferenceService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final MarketProductRepository marketProductRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    public DataSeeder(UserRepository userRepository, AddressRepository addressRepository, CategoryRepository categoryRepository, BrandRepository brandRepository, ProductRepository productRepository, ProductImageRepository productImageRepository, ProductSpecificationRepository specRepository, InventoryRepository inventoryRepository, ReviewRepository reviewRepository, CustomerFeedbackService feedbackService, UserInteractionRepository interactionRepository, UserPreferenceService preferenceService, OrderRepository orderRepository, OrderItemRepository orderItemRepository, PaymentRepository paymentRepository, MarketProductRepository marketProductRepository, PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.specRepository = specRepository;
        this.inventoryRepository = inventoryRepository;
        this.reviewRepository = reviewRepository;
        this.feedbackService = feedbackService;
        this.interactionRepository = interactionRepository;
        this.preferenceService = preferenceService;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
        this.marketProductRepository = marketProductRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (categoryRepository.count() > 0) {
            log.info("Database already seeded. Skipping initial seeding.");
            return;
        }

        log.info("Starting automated database seeding for OmniMart AI...");

        // 1. Seed Categories
        Map<String, Category> categories = seedCategories();

        // 2. Seed Brands
        Map<String, Brand> brands = seedBrands();

        // 3. Seed Users
        Map<String, User> users = seedUsers();

        // 4. Seed 100 Realistic Products with Specs & Inventory
        List<Product> products = seedProducts(categories, brands);

        // 5. Seed Reviews & Feedback Intelligence
        seedReviews(users, products);

        // 6. Seed Orders and Payments
        seedOrders(users, products);

        // 7. Seed Behavioral Telemetry Interactions (500+ events)
        seedBehavioralInteractions(users, products);

        // 8. Seed Market Benchmark Data
        seedMarketIntelligence(products);

        // Update User Preferences for demo users
        User demoUser = users.get("user@omnimart.com");
        if (demoUser != null) {
            preferenceService.updateUserPreferencesFromBehavior(demoUser.getId());
        }

        log.info("OmniMart AI database successfully seeded with 100 products, 20 users, 200+ reviews, 50+ orders, and 500+ telemetry events!");
    }

    private Map<String, Category> seedCategories() {
        Map<String, Category> map = new HashMap<>();
        String[][] data = {
                {"Smartphones", "smartphones", "Flagship, foldable, and performance mobile smartphones", "fa-mobile-screen", "1"},
                {"Laptops", "laptops", "Ultrabooks, creator workstations, and high-performance gaming laptops", "fa-laptop", "2"},
                {"Headphones", "headphones", "Active noise cancelling wireless headphones, studio monitors & TWS", "fa-headphones", "3"},
                {"Gaming", "gaming", "Next-gen consoles, gaming handhelds, graphics cards, and accessories", "fa-gamepad", "4"},
                {"Smart Home", "smart-home", "Smart assistants, ambient lighting, security cameras & IoT", "fa-house-signal", "5"},
                {"Cameras", "cameras", "Full-frame mirrorless cameras, 4K action cams & cinema lenses", "fa-camera", "6"},
                {"Accessories", "accessories", "Mechanical keyboards, ergonomic mice, GaN chargers & cables", "fa-keyboard", "7"},
                {"Monitors", "monitors", "Ultra-wide OLED gaming monitors & color-accurate 4K displays", "fa-desktop", "8"}
        };

        for (String[] d : data) {
            Category c = Category.builder()
                    .name(d[0])
                    .slug(d[1])
                    .description(d[2])
                    .icon(d[3])
                    .displayOrder(Integer.parseInt(d[4]))
                    .build();
            map.put(d[0], categoryRepository.save(c));
        }
        return map;
    }

    private Map<String, Brand> seedBrands() {
        Map<String, Brand> map = new HashMap<>();
        String[] names = {"Samsung", "Apple", "Sony", "Dell", "Lenovo", "ASUS", "HP", "Bose", "OnePlus", "Logitech", "Canon", "LG"};
        for (String name : names) {
            Brand b = Brand.builder()
                    .name(name)
                    .slug(name.toLowerCase())
                    .website("https://www." + name.toLowerCase() + ".com")
                    .build();
            map.put(name, brandRepository.save(b));
        }
        return map;
    }

    private Map<String, User> seedUsers() {
        Map<String, User> map = new HashMap<>();

        // Demo Admin
        User admin = User.builder()
                .email("admin@omnimart.com")
                .password(passwordEncoder.encode("admin123"))
                .fullName("OmniMart System Admin")
                .phone("+91 98765 43210")
                .active(true)
                .roles(Set.of(UserRole.ROLE_ADMIN, UserRole.ROLE_USER))
                .build();
        admin = userRepository.save(admin);
        map.put(admin.getEmail(), admin);

        // Demo User (Gamer / Tech Enthusiast Profile)
        User demoUser = User.builder()
                .email("user@omnimart.com")
                .password(passwordEncoder.encode("password123"))
                .fullName("Rahul Sharma")
                .phone("+91 98111 22233")
                .active(true)
                .roles(Set.of(UserRole.ROLE_USER))
                .build();
        demoUser = userRepository.save(demoUser);
        map.put(demoUser.getEmail(), demoUser);

        // Add Default Address for Demo User
        Address addr = Address.builder()
                .user(demoUser)
                .fullName("Rahul Sharma")
                .streetAddress("Flat 402, Cyber Heights, Sector 62")
                .city("Gurugram")
                .state("Haryana")
                .postalCode("122001")
                .country("India")
                .phone("+91 98111 22233")
                .addressType("HOME")
                .isDefault(true)
                .build();
        addressRepository.save(addr);

        // 18 Additional Users
        String[] firstNames = {"Amit", "Priya", "Vikram", "Sneha", "Rohan", "Ananya", "Karthik", "Pooja", "Arjun", "Neha", "Deepak", "Divya", "Sanjay", "Kavita", "Manish", "Ritu", "Aditya", "Meera"};
        for (int i = 0; i < firstNames.length; i++) {
            String email = firstNames[i].toLowerCase() + "@example.com";
            User u = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode("password123"))
                    .fullName(firstNames[i] + " Verma")
                    .phone("+91 99000 " + (10000 + i))
                    .active(true)
                    .roles(Set.of(UserRole.ROLE_USER))
                    .build();
            u = userRepository.save(u);
            map.put(email, u);
        }

        return map;
    }

    private List<Product> seedProducts(Map<String, Category> categories, Map<String, Brand> brands) {
        List<Product> products = new ArrayList<>();

        // Helper specs template
        Object[][][] catalog = {
                // Category: Smartphones (15 items)
                {
                        {"Smartphones", "Samsung", "Samsung Galaxy S24 Ultra 5G", "samsung-galaxy-s24-ultra", "Flagship AI Smartphone with 200MP Quad Camera, Snapdragon 8 Gen 3, Titanium Frame, and Built-in S-Pen.", "129999", "134999", "4", "4.8", "142", "45", "camera, android, flagship, AMOLED, 200MP, titanium, snapdragon", true,
                                "Processor:Snapdragon 8 Gen 3|RAM:12GB LPDDR5X|Storage:512GB UFS 4.0|Display:6.8-inch Dynamic AMOLED 2X 120Hz|Battery:5000 mAh 45W Fast Charging|Main Camera:200MP + 50MP + 12MP + 10MP|OS:Android 14 OneUI 6.1"},
                        {"Smartphones", "Apple", "Apple iPhone 16 Pro Max", "apple-iphone-16-pro-max", "A18 Pro chip, Grade 5 Titanium design, 48MP Fusion camera system with 5x Telephoto and Camera Control button.", "144900", "159900", "9", "4.9", "198", "30", "apple, ios, flagship, camera, titanium, 5G, A18 Pro", true,
                                "Processor:Apple A18 Pro|RAM:8GB|Storage:256GB|Display:6.9-inch Super Retina XDR OLED 120Hz|Battery:4685 mAh MagSafe 25W|Main Camera:48MP + 48MP + 12MP|OS:iOS 18"},
                        {"Smartphones", "Samsung", "Samsung Galaxy S24 FE 5G", "samsung-galaxy-s24-fe", "Galaxy AI smartphone with 50MP ProVisual Engine camera, Exynos 2400e, and vibrant 120Hz Dynamic AMOLED display.", "59999", "65999", "9", "4.4", "88", "60", "samsung, android, AI, camera, AMOLED, 120Hz", false,
                                "Processor:Exynos 2400e|RAM:8GB|Storage:256GB|Display:6.7-inch FHD+ Dynamic AMOLED 2X 120Hz|Battery:4700 mAh|Main Camera:50MP + 12MP + 8MP|OS:Android 14"},
                        {"Smartphones", "OnePlus", "OnePlus 12 5G", "oneplus-12-5g", "Snapdragon 8 Gen 3 powerhouse with 4th Gen Hasselblad Camera, 100W SUPERVOOC charging, and 2K 120Hz ProXDR display.", "64999", "69999", "7", "4.6", "115", "50", "oneplus, snapdragon, fast charging, hasselblad, 5G, gaming", true,
                                "Processor:Snapdragon 8 Gen 3|RAM:16GB LPDDR5X|Storage:512GB|Display:6.82-inch 2K 120Hz AMOLED|Battery:5400 mAh 100W Wired|Main Camera:50MP Sony LYT-808 + 64MP + 48MP|OS:OxygenOS 14"},
                        {"Smartphones", "OnePlus", "OnePlus Nord 4 5G", "oneplus-nord-4-5g", "All-metal unibody 5G smartphone powered by Snapdragon 7+ Gen 3 with 5500 mAh battery and 100W fast charging.", "29999", "32999", "9", "4.5", "160", "85", "oneplus, budget, battery, 100W, metal unibody, 5G", false,
                                "Processor:Snapdragon 7+ Gen 3|RAM:8GB LPDDR5X|Storage:128GB|Display:6.74-inch 120Hz Ultra Clear AMOLED|Battery:5500 mAh 100W|Main Camera:50MP Sony LYT-600 OIS + 8MP|OS:OxygenOS 14.1"},
                        {"Smartphones", "Samsung", "Samsung Galaxy A55 5G", "samsung-galaxy-a55-5g", "Premium metal frame, Gorilla Glass Victus+, 50MP OIS Triple camera and Samsung Knox Vault security.", "36999", "42999", "14", "4.3", "74", "40", "samsung, mid-range, waterproof, camera, OIS, AMOLED", false,
                                "Processor:Exynos 1480|RAM:8GB|Storage:128GB|Display:6.6-inch Super AMOLED 120Hz|Battery:5000 mAh 25W|Main Camera:50MP OIS + 12MP + 5MP|OS:Android 14"},
                        {"Smartphones", "Apple", "Apple iPhone 15", "apple-iphone-15", "Dynamic Island, 48MP Main camera with 2x Telephoto, USB-C connectivity and all-day battery life.", "69900", "79900", "13", "4.7", "230", "55", "apple, ios, dynamic island, camera, 48MP, 5G", false,
                                "Processor:Apple A16 Bionic|RAM:6GB|Storage:128GB|Display:6.1-inch Super Retina XDR OLED|Battery:3349 mAh|Main Camera:48MP + 12MP|OS:iOS 17"},
                        {"Smartphones", "Samsung", "Samsung Galaxy Z Fold 6", "samsung-galaxy-z-fold-6", "Ultra-slim foldable with Galaxy AI, Snapdragon 8 Gen 3, Armor Aluminum frame, and dual AMOLED screens.", "164999", "174999", "6", "4.7", "42", "20", "foldable, samsung, galaxy ai, flagship, 120hz, multitasking", true,
                                "Processor:Snapdragon 8 Gen 3|RAM:12GB|Storage:512GB|Display:7.6-inch QXGA+ Dynamic AMOLED 2X Foldable + 6.3-inch Cover|Battery:4400 mAh|Main Camera:50MP + 12MP + 10MP|OS:Android 14"},
                        {"Smartphones", "OnePlus", "OnePlus 12R", "oneplus-12r", "Performance-focused flagship killer with Snapdragon 8 Gen 2, 4th Gen LTPO display and 5500 mAh battery.", "39999", "42999", "7", "4.6", "130", "65", "oneplus, gaming, snapdragon, fast charging, AMOLED", false,
                                "Processor:Snapdragon 8 Gen 2|RAM:16GB LPDDR5X|Storage:256GB|Display:6.78-inch 1.5K 120Hz LTPO4 AMOLED|Battery:5500 mAh 100W|Main Camera:50MP Sony IMX890 OIS + 8MP + 2MP|OS:OxygenOS 14"},
                        {"Smartphones", "Sony", "Sony Xperia 1 VI", "sony-xperia-1-vi", "Creator smartphone with 85-170mm true optical zoom telephoto lens, Bravia AI display processing and 2-day battery.", "119999", "129999", "8", "4.5", "28", "15", "sony, camera, optical zoom, 4K, audio, creator", false,
                                "Processor:Snapdragon 8 Gen 3|RAM:12GB|Storage:256GB|Display:6.5-inch FHD+ 1-120Hz OLED|Battery:5000 mAh|Main Camera:48MP Exmor T + 12MP Telephoto + 12MP Ultrawide|OS:Android 14"},
                        {"Smartphones", "Samsung", "Samsung Galaxy M35 5G", "samsung-galaxy-m35-5g", "Monster 6000 mAh battery smartphone with 120Hz sAMOLED display, Exynos 1380, and 50MP No Shake OIS Camera.", "19999", "24499", "18", "4.2", "95", "90", "budget, battery, 6000mah, samsung, 5g, amoled", false,
                                "Processor:Exynos 1380|RAM:6GB|Storage:128GB|Display:6.6-inch 120Hz Super AMOLED|Battery:6000 mAh 25W|Main Camera:50MP OIS + 8MP + 2MP|OS:Android 14"},
                        {"Smartphones", "OnePlus", "OnePlus Open 5G", "oneplus-open-5g", "Lightweight foldable smartphone with Hasselblad cameras, 2K 120Hz dual displays and Open Canvas multitasking.", "139999", "149999", "7", "4.8", "36", "18", "foldable, oneplus, hasselblad, multitasking, flagship", true,
                                "Processor:Snapdragon 8 Gen 2|RAM:16GB|Storage:512GB|Display:7.82-inch Flexi-fluid AMOLED 120Hz + 6.31-inch Cover|Battery:4805 mAh 67W|Main Camera:48MP Sony LYT-T808 + 64MP + 48MP|OS:OxygenOS 13.2"},
                        {"Smartphones", "Samsung", "Samsung Galaxy S23 Ultra", "samsung-galaxy-s23-ultra", "200MP Nightography flagship with Snapdragon 8 Gen 2 for Galaxy and integrated S-Pen.", "89999", "124999", "28", "4.8", "310", "40", "samsung, camera, 200mp, s-pen, flagship, discounted", false,
                                "Processor:Snapdragon 8 Gen 2 for Galaxy|RAM:12GB|Storage:256GB|Display:6.8-inch QHD+ Dynamic AMOLED 2X 120Hz|Battery:5000 mAh 45W|Main Camera:200MP + 10MP + 10MP + 12MP|OS:Android 14"},
                        {"Smartphones", "Apple", "Apple iPhone 14", "apple-iphone-14", "Reliable dual-camera system, A15 Bionic, Cinematic mode 4K, and Crash Detection safety feature.", "54999", "69900", "21", "4.6", "410", "70", "apple, iphone, budget flagship, ios, reliable", false,
                                "Processor:Apple A15 Bionic|RAM:6GB|Storage:128GB|Display:6.1-inch Super Retina XDR OLED|Battery:3279 mAh|Main Camera:12MP + 12MP|OS:iOS 17"},
                        {"Smartphones", "Samsung", "Samsung Galaxy Z Flip 6", "samsung-galaxy-z-flip-6", "Compact folding pocket phone with FlexCam AI zoom, 50MP camera, and 3.4-inch Flex Window cover screen.", "89999", "109999", "18", "4.5", "52", "25", "flip, foldable, stylish, compact, samsung, galaxy ai", false,
                                "Processor:Snapdragon 8 Gen 3|RAM:12GB|Storage:256GB|Display:6.7-inch Dynamic AMOLED 2X 120Hz + 3.4-inch Super AMOLED|Battery:4000 mAh|Main Camera:50MP + 12MP|OS:Android 14"}
                },

                // Category: Laptops (15 items)
                {
                        {"Laptops", "Lenovo", "Lenovo Legion Pro 7i Gen 9", "lenovo-legion-pro-7i-gen-9", "Top-tier AI-tuned gaming laptop with Intel Core i9-14900HX, NVIDIA GeForce RTX 4090 16GB, and 240Hz PureSight Gaming display.", "289999", "319999", "9", "4.9", "45", "15", "gaming, rtx 4090, i9, 240hz, legion, ai tuning, premium laptop", true,
                                "Processor:Intel Core i9-14900HX (24 cores)|Graphics:NVIDIA GeForce RTX 4090 16GB GDDR6 (175W TGP)|RAM:32GB DDR5 5600MHz|Storage:2TB PCIe Gen4 NVMe SSD|Display:16-inch WQXGA 240Hz 500 nits IPS 100% DCI-P3|Battery:99.99 Wh with 330W GaN Adapter|Weight:2.62 kg|OS:Windows 11 Home"},
                        {"Laptops", "Lenovo", "Lenovo Legion 5i Pro", "lenovo-legion-5i-pro", "High performance gaming laptop with Intel Core i7-14650HX, NVIDIA GeForce RTX 4070 8GB, 165Hz IPS display and Coldfront 5.0 cooling.", "149999", "169999", "12", "4.7", "86", "35", "gaming, rtx 4070, i7, lenovo, 165hz, coding, heavy multitasking", true,
                                "Processor:Intel Core i7-14650HX|Graphics:NVIDIA GeForce RTX 4070 8GB GDDR6 (140W TGP)|RAM:16GB DDR5 5600MHz|Storage:1TB PCIe Gen4 SSD|Display:16-inch WQXGA 165Hz 100% sRGB|Battery:80 Wh|Weight:2.5 kg|OS:Windows 11 Home"},
                        {"Laptops", "Lenovo", "Lenovo LOQ 15 Gaming Laptop", "lenovo-loq-15-gaming", "Best value budget gaming laptop powered by AMD Ryzen 7 7840HS with NVIDIA GeForce RTX 4060 8GB and 144Hz FHD G-SYNC display.", "78999", "89999", "12", "4.5", "145", "50", "gaming, rtx 4060, budget gaming, ryzen 7, 144hz, lenovo, coding", true,
                                "Processor:AMD Ryzen 7 7840HS (8 cores, 16 threads)|Graphics:NVIDIA GeForce RTX 4060 8GB GDDR6 (115W TGP)|RAM:16GB DDR5|Storage:512GB PCIe Gen4 SSD|Display:15.6-inch FHD 144Hz 100% sRGB|Battery:60 Wh|Weight:2.4 kg|OS:Windows 11 Home"},
                        {"Laptops", "ASUS", "ASUS ROG Zephyrus G16 (2024)", "asus-rog-zephyrus-g16-2024", "Ultra-slim 1.49cm aluminum CNC gaming chassis with Intel Core Ultra 9 185H, RTX 4080, and 2.5K 240Hz ROG Nebula OLED display.", "249990", "279990", "11", "4.8", "38", "12", "gaming, asus rog, oled, ultra-slim, rtx 4080, intel core ultra", true,
                                "Processor:Intel Core Ultra 9 185H|Graphics:NVIDIA GeForce RTX 4080 12GB GDDR6|RAM:32GB LPDDR5X|Storage:1TB PCIe 4.0 NVMe SSD|Display:16-inch 2.5K 240Hz 0.2ms OLED ROG Nebula Display|Battery:90 Wh|Weight:1.85 kg|OS:Windows 11 Home"},
                        {"Laptops", "ASUS", "ASUS TUF Gaming A15", "asus-tuf-gaming-a15", "Military-grade durable gaming laptop with AMD Ryzen 7 7735HS, NVIDIA GeForce RTX 4050 6GB and 144Hz Adaptive-Sync display.", "68990", "79990", "14", "4.4", "180", "60", "gaming, asus tuf, rtx 4050, ryzen 7, budget gaming, durable", false,
                                "Processor:AMD Ryzen 7 7735HS|Graphics:NVIDIA GeForce RTX 4050 6GB GDDR6 (140W TGP)|RAM:16GB DDR5 4800MHz|Storage:512GB NVMe SSD|Display:15.6-inch FHD 144Hz IPS|Battery:90 Wh|Weight:2.2 kg|OS:Windows 11 Home"},
                        {"Laptops", "Dell", "Dell XPS 16 (9640)", "dell-xps-16-9640", "Iconic minimalist CNC aluminum and glass design with Intel Core Ultra 7 155H, 4K+ OLED touch screen and NVIDIA RTX 4060 graphics.", "229990", "249990", "8", "4.6", "29", "10", "dell xps, creator, 4k oled, premium ultrabook, coding, design", false,
                                "Processor:Intel Core Ultra 7 155H|Graphics:NVIDIA GeForce RTX 4060 8GB GDDR6|RAM:32GB LPDDR5X|Storage:1TB PCIe SSD|Display:16.3-inch 4K+ OLED InfinityEdge Touch|Battery:99.5 Wh|Weight:2.13 kg|OS:Windows 11 Pro"},
                        {"Laptops", "Dell", "Dell G15 5530 Gaming Laptop", "dell-g15-5530-gaming", "Thermal Alienware-inspired cooling with Intel Core i7-13650HX, NVIDIA GeForce RTX 4060 and 165Hz FHD display.", "86990", "104990", "17", "4.3", "72", "30", "dell, gaming laptop, rtx 4060, i7, alienware cooling", false,
                                "Processor:Intel Core i7-13650HX|Graphics:NVIDIA GeForce RTX 4060 8GB GDDR6 (140W)|RAM:16GB DDR5|Storage:1TB NVMe SSD|Display:15.6-inch FHD 165Hz 100% sRGB|Battery:86 Wh|Weight:2.65 kg|OS:Windows 11 Home"},
                        {"Laptops", "Apple", "Apple MacBook Pro 16 (M3 Max)", "apple-macbook-pro-16-m3-max", "Ultimate workstation powerhouse with 16-core CPU M3 Max, 40-core GPU, Liquid Retina XDR display and 22-hour battery life.", "349900", "369900", "5", "4.9", "62", "14", "apple, macbook, m3 max, video editing, software engineering, retina xdr", true,
                                "Processor:Apple M3 Max (16-core CPU, 40-core GPU)|RAM:48GB Unified Memory|Storage:1TB SSD|Display:16.2-inch Liquid Retina XDR 120Hz ProMotion 1600 nits|Battery:100 Wh (Up to 22 hrs)|Weight:2.16 kg|OS:macOS Sonoma"},
                        {"Laptops", "Apple", "Apple MacBook Air 13 (M3)", "apple-macbook-air-13-m3", "Strikingly thin fanless design with Apple M3 chip, Liquid Retina display, MagSafe 3 and all-day 18-hour battery.", "104900", "114900", "9", "4.8", "215", "45", "apple, macbook air, m3, lightweight, fanless, student, coding", false,
                                "Processor:Apple M3 (8-core CPU, 10-core GPU)|RAM:16GB Unified Memory|Storage:512GB SSD|Display:13.6-inch Liquid Retina 500 nits|Battery:52.6 Wh (Up to 18 hrs)|Weight:1.24 kg|OS:macOS Sonoma"},
                        {"Laptops", "HP", "HP Omen 16 Gaming Laptop", "hp-omen-16-gaming", "Tempest Cooling system with AMD Ryzen 7 7840HS, NVIDIA GeForce RTX 4070 8GB and QHD 240Hz display.", "124990", "139990", "11", "4.5", "54", "22", "hp omen, gaming, rtx 4070, 240hz, tempest cooling", false,
                                "Processor:AMD Ryzen 7 7840HS|Graphics:NVIDIA GeForce RTX 4070 8GB GDDR6 (140W TGP)|RAM:16GB DDR5|Storage:1TB PCIe SSD|Display:16.1-inch QHD 240Hz 3ms IPS|Battery:83 Wh|Weight:2.37 kg|OS:Windows 11 Home"},
                        {"Laptops", "HP", "HP Pavilion 15 (13th Gen Intel)", "hp-pavilion-15-intel", "Everyday performance laptop with Intel Core i5-1335U, 16GB RAM, 512GB SSD and FHD IPS micro-edge display.", "54990", "64990", "15", "4.3", "120", "50", "hp pavilion, budget laptop, student, office, i5, 16gb ram", false,
                                "Processor:Intel Core i5-1335U|Graphics:Intel Iris Xe Graphics|RAM:16GB DDR4|Storage:512GB PCIe NVMe SSD|Display:15.6-inch FHD IPS Micro-edge 250 nits|Battery:41 Wh|Weight:1.75 kg|OS:Windows 11 Home"},
                        {"Laptops", "Lenovo", "Lenovo ThinkPad X1 Carbon Gen 12", "lenovo-thinkpad-x1-carbon-gen-12", "Legendary enterprise ultrabook with carbon-fiber weave lid, Intel Core Ultra 7 155U, and TrackPoint keyboard.", "174990", "199990", "13", "4.7", "33", "18", "thinkpad, business, ultrabook, lightweight, enterprise, durable", false,
                                "Processor:Intel Core Ultra 7 155U|Graphics:Intel Graphics|RAM:32GB LPDDR5X|Storage:1TB PCIe 4.0 SSD|Display:14-inch 2.8K OLED 120Hz HDR 500 nits|Battery:57 Wh|Weight:1.09 kg|OS:Windows 11 Pro"},
                        {"Laptops", "ASUS", "ASUS Zenbook 14 OLED", "asus-zenbook-14-oled", "Intel Evo certified ultraportable with Intel Core Ultra 7 155H, 3K 120Hz OLED HDR display and 75Wh battery.", "96990", "109990", "12", "4.6", "68", "25", "asus zenbook, oled, intel evo, thin, lightweight, coding, portable", false,
                                "Processor:Intel Core Ultra 7 155H|Graphics:Intel Arc Graphics|RAM:16GB LPDDR5X|Storage:1TB NVMe SSD|Display:14-inch 3K 120Hz OLED 16:10 600 nits|Battery:75 Wh|Weight:1.2 kg|OS:Windows 11 Home"},
                        {"Laptops", "Dell", "Dell Inspiron 14 Plus", "dell-inspiron-14-plus", "Compact power laptop featuring Intel Core Ultra 7 with Intel Arc graphics, 2.2K anti-glare display and ExpressCharge.", "79990", "89990", "11", "4.4", "44", "30", "dell, inspiron, intel arc, portable, coding, productivity", false,
                                "Processor:Intel Core Ultra 7 155H|Graphics:Intel Arc Graphics|RAM:16GB LPDDR5X|Storage:1TB SSD|Display:14-inch 2.2K IPS Anti-Glare 300 nits|Battery:64 Wh|Weight:1.6 kg|OS:Windows 11 Home"},
                        {"Laptops", "Samsung", "Samsung Galaxy Book4 Pro 360", "samsung-galaxy-book4-pro-360", "2-in-1 touchscreen convertible laptop with S-Pen, Intel Core Ultra 7, and Dynamic AMOLED 2X 120Hz display.", "154990", "174990", "11", "4.6", "24", "15", "samsung, 2-in-1, touchscreen, amoled, s-pen, convertible", false,
                                "Processor:Intel Core Ultra 7 155H|Graphics:Intel Arc Graphics|RAM:16GB LPDDR5X|Storage:512GB NVMe SSD|Display:16-inch 3K Dynamic AMOLED 2X Touch 120Hz|Battery:76 Wh|Weight:1.66 kg|OS:Windows 11 Home"}
                },

                // Category: Headphones (12 items)
                {
                        {"Headphones", "Sony", "Sony WH-1000XM5 Wireless ANC Headphones", "sony-wh-1000xm5", "Industry-leading noise canceling headphones with Auto NC Optimizer, 8 microphones, 30-hour battery life and LDAC Hi-Res Audio.", "28990", "34990", "17", "4.8", "320", "50", "sony, anc, noise cancelling, audiophile, wireless, ldac, 30hr battery", true,
                                "Driver Size:30mm Carbon Fiber composite|Noise Cancellation:Dual Processor V1 + HD QN1 with 8 Mics|Battery Life:30 hours with ANC (40 hrs without)|Fast Charging:3 min charge for 3 hours playback|Bluetooth Version:5.2 with LDAC, AAC, SBC|Weight:250 grams"},
                        {"Headphones", "Bose", "Bose QuietComfort Ultra Headphones", "bose-quietcomfort-ultra", "World-class spatial audio with Bose Immersive Audio, CustomTune sound calibration and ultra-plush comfort.", "35900", "39900", "10", "4.7", "110", "30", "bose, noise cancelling, spatial audio, premium comfort, bluetooth", true,
                                "Driver:Custom Bose Drivers|Noise Cancellation:Active with Quiet & Aware modes|Battery Life:24 hours (18 hrs Immersive Audio)|Fast Charging:15 min charge for 2.5 hrs playback|Bluetooth Version:5.3 with Snapdragon Sound aptX Adaptive|Weight:253 grams"},
                        {"Headphones", "Apple", "Apple AirPods Max (USB-C)", "apple-airpods-max-usbc", "High-fidelity audio with computational audio powered by Apple H1 chip in each ear cup, Digital Crown, and Smart Case.", "59900", "59900", "0", "4.6", "95", "20", "apple, airpods max, spatial audio, hifi, aluminum earcups, usb-c", false,
                                "Driver Size:40mm Apple-designed dynamic driver|Noise Cancellation:Pro-level Active Noise Cancellation with Transparency Mode|Battery Life:20 hours with ANC enabled|Charging Port:USB-C|Weight:386.2 grams|Audio:Personalized Spatial Audio with dynamic head tracking"},
                        {"Headphones", "Sony", "Sony WF-1000XM5 TWS Earbuds", "sony-wf-1000xm5-tws", "Best noise canceling true wireless earbuds with Integrated Processor V2, Dynamic Driver X and bone conduction sensors.", "21990", "24990", "12", "4.5", "140", "45", "sony, tws, earbuds, anc, ldac, wireless charging, compact", false,
                                "Driver Size:8.4mm Dynamic Driver X|Noise Cancellation:Integrated Processor V2 + HD Noise Cancelling Processor QN2e|Battery Life:8 hours in earbuds + 16 hrs in case|Water Resistance:IPX4|Weight:5.9 grams per earbud"},
                        {"Headphones", "Apple", "Apple AirPods Pro 2 (USB-C)", "apple-airpods-pro-2-usbc", "H2 chip with up to 2x more Active Noise Cancellation, Adaptive Audio, Conversation Awareness and USB-C MagSafe case.", "22900", "24900", "8", "4.8", "450", "80", "apple, airpods pro, anc, transparency, h2 chip, spatial audio, magsafe", true,
                                "Driver:Custom high-excursion Apple driver|Chip:Apple H2 in earbuds, Apple U1/H2 in case|Battery Life:6 hours with ANC (30 hrs with case)|Water Resistance:IP54 sweat and dust resistant|Audio:Adaptive Audio & Personalized Spatial Audio"},
                        {"Headphones", "Bose", "Bose QuietComfort Ultra Earbuds", "bose-qc-ultra-earbuds", "Breakthrough spatialized audio with CustomTune sound calibration and noise cancellation.", "24900", "29900", "17", "4.5", "85", "30", "bose, earbuds, anc, spatial audio, customtune, premium audio", false,
                                "Noise Cancellation:CustomTune technology|Battery Life:6 hours (24 hrs with charging case)|Fast Charging:20 mins for 2 hours playback|Bluetooth Version:5.3|Water Resistance:IPX4"},
                        {"Headphones", "OnePlus", "OnePlus Buds Pro 3", "oneplus-buds-pro-3", "Dual Driver acoustic system co-created with Dynaudio, 50dB adaptive noise cancellation and 43 hours battery life.", "11999", "13999", "14", "4.6", "130", "60", "oneplus, dynaudio, dual driver, 50db anc, fast charging, high bass", false,
                                "Drivers:11mm woofer + 6mm tweeter dual driver|Noise Cancellation:Up to 50dB Real-time Adaptive ANC|Battery Life:Up to 43 hours total|Bluetooth Codec:LHDC 5.0, AAC, SBC|Water Resistance:IP55"},
                        {"Headphones", "Sony", "Sony WH-CH720N Wireless ANC Headphones", "sony-wh-ch720n", "Lightweight over-ear headphones with Dual Noise Sensor technology and Integrated Processor V1.", "9990", "14990", "33", "4.4", "220", "75", "sony, budget anc, wireless headphones, 35hr battery, lightweight", false,
                                "Driver Size:30mm|Noise Cancellation:Integrated Processor V1|Battery Life:35 hours with ANC (50 hrs without)|Weight:192 grams (lightest Sony ANC)|Bluetooth:5.2 multipoint connection"},
                        {"Headphones", "Bose", "Bose QuietComfort Headphones", "bose-quietcomfort-headphones", "Legendary noise cancellation with plush ear cushions, adjustable EQ and 24 hours of playback.", "24900", "29900", "17", "4.6", "90", "25", "bose, comfort, noise cancelling, over-ear, travel", false,
                                "Noise Cancellation:Adjustable Quiet and Aware modes|Battery Life:Up to 24 hours|Charging:USB-C fast charging (15 mins for 2.5 hrs)|Audio Cable:Included 3.5mm with inline mic"},
                        {"Headphones", "Sony", "Sony WH-1000XM4 Wireless Headphones", "sony-wh-1000xm4", "Legendary ANC headphones with Speak-to-Chat, multi-point connection, and 30-hour battery life.", "22990", "29990", "23", "4.8", "520", "40", "sony, xm4, best value anc, 30hr battery, folding design", false,
                                "Driver:40mm dome type|Noise Cancellation:HD Noise Cancelling Processor QN1|Battery Life:30 hours with ANC|Bluetooth:5.0 with LDAC, AAC, SBC|Weight:254 grams"},
                        {"Headphones", "OnePlus", "OnePlus Nord Buds 3 Pro", "oneplus-nord-buds-3-pro", "Budget killer TWS with 49dB Hybrid Active Noise Cancellation and 12.4mm Titanized diaphragm drivers.", "2999", "3999", "25", "4.3", "340", "120", "oneplus, budget tws, 49db anc, bass, cheap earbuds", false,
                                "Drivers:12.4mm Titanized dynamic driver|Noise Cancellation:49dB Hybrid Active Noise Cancellation|Battery Life:12 hours earbuds (44 hrs total)|Bluetooth:5.4 with Google Fast Pair"},
                        {"Headphones", "Logitech", "Logitech G PRO X 2 LIGHTSPEED Gaming Headset", "logitech-g-pro-x-2", "Pro-grade wireless gaming headset with 50mm Graphene drivers, BLUE VO!CE mic and 50-hour battery.", "24995", "27995", "11", "4.7", "50", "20", "logitech, gaming headset, graphene drivers, lightspeed wireless, esports", false,
                                "Drivers:50mm Graphene|Wireless:LIGHTSPEED 2.4GHz + Bluetooth + 3.5mm|Battery Life:Up to 50 hours|Microphone:6mm detachable cardioid with BLUE VO!CE|Weight:345 grams"}
                },

                // Category: Gaming (12 items)
                {
                        {"Gaming", "Sony", "Sony PlayStation 5 Slim Console (Disk Edition)", "sony-playstation-5-slim", "Next-gen gaming console with 1TB SSD, Ray Tracing, 4K 120Hz HDR gaming and DualSense wireless controller.", "54990", "54990", "0", "4.9", "480", "40", "ps5, sony, console, 4k 120fps, ray tracing, dualsense, gaming", true,
                                "Storage:1TB Custom NVMe SSD|Processor:AMD Zen 2 8-core 3.5GHz|GPU:AMD RDNA 2 10.3 TFLOPs|Resolution:Up to 4K 120Hz & 8K output|Audio:Tempest 3D AudioTech|Controller:DualSense with Haptic Feedback & Adaptive Triggers"},
                        {"Gaming", "ASUS", "ASUS ROG Ally X Handheld Gaming Console", "asus-rog-ally-x", "Ultimate Windows handheld console powered by AMD Ryzen Z1 Extreme, 24GB LPDDR5X RAM, 80Wh battery and 120Hz VRR display.", "89990", "99990", "10", "4.8", "65", "25", "handheld, asus rog, z1 extreme, 80wh battery, portable gaming, windows 11", true,
                                "Processor:AMD Ryzen Z1 Extreme (8 cores, 16 threads, up to 5.1GHz)|Graphics:AMD Radeon Graphics (RDNA 3, 12 CUs)|RAM:24GB LPDDR5X 7500MHz|Storage:1TB M.2 2280 NVMe SSD|Display:7-inch FHD 120Hz 100% sRGB 500 nits FreeSync Premium|Battery:80 Wh|Weight:678 grams"},
                        {"Gaming", "Sony", "Sony DualSense Wireless Controller (Midnight Black)", "sony-dualsense-midnight-black", "Immersive wireless gaming controller with haptic feedback, dynamic adaptive triggers and built-in mic.", "5990", "6390", "6", "4.8", "310", "100", "ps5 controller, dualsense, haptic feedback, wireless controller, pc gaming", false,
                                "Compatibility:PS5, PC (Windows), Mac, iOS, Android|Battery:1560 mAh rechargeable|Connectivity:Bluetooth 5.1 & USB-C|Features:Adaptive Triggers, Haptic Feedback, Integrated Speaker"},
                        {"Gaming", "ASUS", "ASUS ROG Strix GeForce RTX 4080 Super OC Edition", "asus-rog-strix-rtx-4080-super", "Flagship graphics card with 16GB GDDR6X, Axial-tech fans, 3.5-slot design and patented vapor chamber.", "129999", "144999", "10", "4.9", "25", "10", "rtx 4080 super, gpu, asus rog, graphics card, 4k gaming, ray tracing", false,
                                "CUDA Cores:10240|Memory:16GB GDDR6X 256-bit 23 Gbps|Boost Clock:2670 MHz (OC mode)|Power Connectors:1x 16-pin (12VHPWR)|Recommended PSU:850W|Outputs:2x HDMI 2.1a, 3x DisplayPort 1.4a"},
                        {"Gaming", "Logitech", "Logitech G502 X PLUS Wireless RGB Gaming Mouse", "logitech-g502-x-plus", "LIGHTFORCE hybrid optical-mechanical switches, HERO 25K gaming sensor and LIGHTSYNC RGB lighting.", "13995", "15995", "13", "4.7", "190", "50", "gaming mouse, logitech, g502, hero 25k sensor, lightforce switches, rgb", false,
                                "Sensor:HERO 25K (100 - 25600 DPI)|Switches:LIGHTFORCE Hybrid Optical-Mechanical|Buttons:13 programmable controls|Battery Life:Up to 120 hours (37 hrs with RGB)|Weight:106 grams"},
                        {"Gaming", "Logitech", "Logitech G915 LIGHTSPEED Wireless Mechanical Gaming Keyboard", "logitech-g915-lightspeed", "Ultra-thin low profile GL mechanical switches, aircraft-grade 5052 aluminum alloy and 30-hour battery.", "19995", "22995", "13", "4.6", "110", "20", "gaming keyboard, mechanical keyboard, logitech, low profile, rgb", false,
                                "Switches:GL Tactile Low Profile Mechanical|Key Roll-over:Full key with anti-ghosting|Battery Life:30 hours at 100% brightness|Lighting:LIGHTSYNC RGB per-key|Dedicated Media Controls:Yes with volume roller"},
                        {"Gaming", "ASUS", "ASUS ROG Swift OLED PG32UCDM 4K 240Hz", "asus-rog-swift-oled-pg32ucdm", "32-inch 4K QD-OLED gaming monitor with blistering 240Hz refresh rate, 0.03ms response time and custom heatsink.", "124990", "139990", "11", "4.9", "20", "8", "monitor, oled, 4k 240hz, qd-oled, asus rog, hdr, esports", true,
                                "Panel:32-inch 4K UHD (3840x2160) QD-OLED|Refresh Rate:240Hz|Response Time:0.03ms (GTG)|Color Gamut:99% DCI-P3|HDR:DisplayHDR True Black 400|Ports:DisplayPort 1.4 DSC, 2x HDMI 2.1, USB-C 90W PD"},
                        {"Gaming", "Lenovo", "Lenovo Legion Gaming Mechanical Keyboard K500", "lenovo-legion-k500", "Minimalist mechanical gaming keyboard with red linear switches, 16.8M RGB per-key and detachable wrist rest.", "5499", "6999", "21", "4.4", "85", "40", "lenovo, mechanical keyboard, red switches, budget gaming keyboard, rgb", false,
                                "Switch Type:Red Linear Mechanical (50M clicks)|Layout:104 keys full size|Anti-Ghosting:100% anti-ghosting with full N-key rollover|Lighting:16.8M colors RGB per key|Cable:1.8m braided USB"},
                        {"Gaming", "Samsung", "Samsung 990 PRO 2TB PCIe 4.0 NVMe SSD", "samsung-990-pro-2tb", "Blazing fast read/write speeds up to 7450/6900 MB/s for PS5 console and high-end PC gaming workstations.", "17499", "21999", "20", "4.9", "320", "60", "ssd, samsung 990 pro, nvme, ps5 ssd, 7450mbps, 2tb storage", false,
                                "Capacity:2TB|Interface:PCIe Gen 4.0 x4, NVMe 2.0|Sequential Read:Up to 7450 MB/s|Sequential Write:Up to 6900 MB/s|Compatibility:PS5 console & Desktop PCs|Warranty:5 Years Limited"},
                        {"Gaming", "Logitech", "Logitech G29 Driving Force Racing Wheel & Pedals", "logitech-g29-racing-wheel", "Dual-motor force feedback racing wheel with helical gearing, stainless steel pedals and 900-degree rotation.", "27995", "33995", "18", "4.7", "140", "15", "racing wheel, sim racing, logitech g29, force feedback, ps5, pc", false,
                                "Rotation:900 degrees lock-to-lock|Feedback:Dual-motor force feedback|Pedals:Nonlinear brake pedal with stainless steel construction|Compatibility:PS5, PS4, PC"},
                        {"Gaming", "Sony", "Sony PlayStation VR2 Headset", "sony-playstation-vr2", "Next-gen virtual reality headset with 4K HDR OLED displays, eye tracking, headset feedback and Tempest 3D audio.", "52990", "57990", "9", "4.6", "45", "12", "ps vr2, virtual reality, 4k hdr oled, eye tracking, ps5 vr", false,
                                "Display:2000 x 2048 per eye OLED 90Hz/120Hz|Field of View:Approx. 110 degrees|Sensors:Six-axis motion sensor, 4 cameras for tracking, IR camera for eye tracking|Feedback:Vibration on headset"},
                        {"Gaming", "ASUS", "ASUS ROG Cetra True Wireless SpeedNova Earbuds", "asus-rog-cetra-speednova", "Ultra-low latency 2.4GHz wireless gaming earbuds with Dirac Opteo sound, ANC and bone conduction AI mic.", "17999", "19999", "10", "4.5", "35", "25", "gaming earbuds, low latency, asus rog, 2.4ghz, anc, tws", false,
                                "Wireless:Dual-mode 2.4GHz ROG SpeedNova + Bluetooth 5.3|Audio:24-bit 96 kHz high-resolution audio with Dirac Opteo|Noise Cancellation:Adaptive ANC|Battery Life:Up to 46 hours"}
                },

                // Category: Smart Home (12 items)
                {
                        {"Smart Home", "Samsung", "Samsung SmartThings Station & Wireless Charger", "samsung-smartthings-station", "Matter-compatible smart home hub with integrated 15W fast wireless charging pad and one-touch routine button.", "6999", "7999", "13", "4.4", "60", "30", "smart home, samsung, matter, smartthings, zigbee, wireless charger", false,
                                "Hub Standards:Matter, Zigbee, Thread, BLE|Charging:15W Fast Wireless Charging|Control:Smart button for 3 routines|App:Samsung SmartThings"},
                        {"Smart Home", "Apple", "Apple HomePod (2nd Generation)", "apple-homepod-2", "Immersive, high-fidelity room-filling audio with Room sensing, spatial audio with Dolby Atmos, and built-in Siri smart assistant.", "32900", "32900", "0", "4.7", "80", "15", "smart speaker, apple homepod, spatial audio, siri, smart home hub, dolby atmos", false,
                                "Audio:High-excursion 4-inch woofer + 5 horn-loaded tweeters|Smart Assistant:Siri|Sensors:Temperature and humidity sensor, Sound Recognition|Connectivity:Thread, Matter, Wi-Fi 802.11n"},
                        {"Smart Home", "Apple", "Apple HomePod mini", "apple-homepod-mini", "Compact smart speaker with 360-degree sound, Siri intelligence, intercom feature and smart home hub capabilities.", "10900", "10900", "0", "4.6", "230", "45", "smart speaker, apple, siri, compact speaker, matter, thread", false,
                                "Audio:Full-range driver and dual passive radiators|Microphone:Four-microphone design for far-field Siri|Smart Home Hub:Thread and Matter controller|Weight:345 grams"},
                        {"Smart Home", "Bose", "Bose Smart Soundbar 600 with Dolby Atmos", "bose-smart-soundbar-600", "Compact soundbar featuring Dolby Atmos and proprietary Bose TrueSpace technology for immersive home theater audio.", "49900", "55900", "11", "4.7", "65", "20", "soundbar, bose, dolby atmos, truespace, hdmi earc, home theater", true,
                                "Audio Channels:Dolby Atmos with 2 upward-firing transducers|Connectivity:HDMI eARC, Optical, Wi-Fi, Bluetooth, Apple AirPlay 2, Spotify Connect|Voice Assistant:Built-in Amazon Alexa & Works with Google"},
                        {"Smart Home", "Sony", "Sony BRAVIA Theatre Bar 8 Dolby Atmos Soundbar", "sony-bravia-theatre-bar-8", "11-unit speaker soundbar with 360 Spatial Sound Mapping, Dolby Atmos, DTS:X and HDMI 2.1 pass-through.", "89990", "104990", "14", "4.8", "30", "10", "sony, soundbar, dolby atmos, 360 spatial sound, home theater, dts x", false,
                                "Speaker Units:11-speaker acoustic architecture|Surround Formats:Dolby Atmos, DTS:X, 360 Spatial Sound Mapping|Pass-through:8K HDR, 4K 120Hz, VRR, ALLM|Connectivity:HDMI eARC, Bluetooth 5.2, Wi-Fi"},
                        {"Smart Home", "Samsung", "Samsung The Freestyle 2nd Gen Smart Projector", "samsung-the-freestyle-2nd-gen", "Point and play 180-degree portable smart projector with Auto Keystone, 100-inch display and 360 sound.", "59990", "69990", "14", "4.5", "40", "15", "projector, portable, 100 inch, samsung freestyle, smart tv, 360 sound", false,
                                "Resolution:1080p Full HD (1920 x 1080)|Screen Size:30 to 100 inches|Auto Setup:Auto Keystone, Auto Focus, Auto Leveling|Audio:5W 360-degree speaker with Dolby Digital Plus|Smart Platform:Tizen OS with Netflix, Prime, YouTube"},
                        {"Smart Home", "LG", "LG PuriCare 360 Smart Air Purifier", "lg-puricare-360", "Aerodynamic 360-degree purification with Clean Booster, HEPA H13 filter, PM1.0 sensor and ThinQ smart app control.", "29990", "39990", "25", "4.6", "75", "25", "air purifier, lg puricare, hepa filter, smart home, pm1.0 sensor", false,
                                "Filtration:HEPA H13 filter + Deodorization filter (99.97% particles)|Coverage Area:659 sq. ft.|Clean Booster:Directs clean air up to 7.5 meters|Smart Control:LG ThinQ App with Wi-Fi"},
                        {"Smart Home", "Samsung", "Samsung 55-inch The Frame QLED 4K Smart TV", "samsung-the-frame-55-inch", "Art Mode lifestyle TV with Matte Display, 100% Color Volume with Quantum Dot and customizable bezels.", "84990", "119990", "29", "4.7", "110", "20", "qled tv, 4k, art mode, matte display, samsung the frame, lifestyle tv", true,
                                "Display:55-inch 4K QLED (3840 x 2160) 100Hz/120Hz|Screen Coating:Matte Anti-Reflection Display|Processor:Quantum Processor 4K|Art Mode:Displays curated artworks with motion sensor|Sound:40W 2.0.2CH with Object Tracking Sound"},
                        {"Smart Home", "LG", "LG C3 55-inch 4K OLED evo Smart TV", "lg-c3-55-inch-oled", "Self-lit OLED evo pixels with α9 AI Processor Gen6, Dolby Vision, Dolby Atmos and 4x HDMI 2.1 for gaming.", "114990", "149990", "23", "4.9", "180", "15", "lg oled, oled evo, 4k tv, 120hz, dolby vision, best gaming tv", true,
                                "Panel:55-inch 4K Self-Lighting OLED evo|Processor:α9 AI Processor 4K Gen6|Gaming Features:0.1ms response time, G-Sync, FreeSync, 4x HDMI 2.1 48Gbps 120Hz|Audio:40W 2.2 Channel Dolby Atmos|OS:webOS 23"},
                        {"Smart Home", "Sony", "Sony BRAVIA 65-inch 4K Google TV (XR-65X90L)", "sony-bravia-65-x90l", "Full Array LED with Cognitive Processor XR, Acoustic Multi-Audio, XR Contrast Booster 10 and Google TV.", "129990", "159990", "19", "4.8", "95", "18", "sony bravia, 65 inch, full array led, cognitive processor xr, google tv", false,
                                "Display:65-inch 4K Full Array LED (3840 x 2160) 120Hz|Processor:Cognitive Processor XR|HDR:Dolby Vision, HDR10, HLG|Audio:Acoustic Multi-Audio with Sound Positioning Tweeters|Gaming:Auto HDR Tone Mapping for PS5"},
                        {"Smart Home", "Logitech", "Logitech Brio 4K Ultra HD Webcam", "logitech-brio-4k-webcam", "Ultra 4K webcam with RightLight 3 with HDR, dual omnidirectional mics and Windows Hello facial recognition.", "18995", "22995", "17", "4.6", "130", "40", "webcam, 4k webcam, logitech, windows hello, hdr, streaming, zoom meetings", false,
                                "Resolution:4K Ultra HD at 30 fps, 1080p at 60 fps|Field of View:Adjustable 90, 78, or 65 degrees|Lighting:RightLight 3 with HDR|Security:Infrared sensor for Windows Hello|Microphone:Dual integrated omnidirectional with noise cancelling"},
                        {"Smart Home", "Samsung", "Samsung Smart Door Lock with Fingerprint", "samsung-smart-door-lock", "Keyless biometric digital door lock with optical fingerprint scanner, PIN code, RFID card and mechanical key override.", "24999", "29999", "17", "4.5", "50", "20", "smart lock, digital lock, fingerprint lock, samsung, home security", false,
                                "Authentication:Fingerprint (Up to 100), RFID Card, Master PIN, Emergency Key|Material:Zinc Alloy Die-casting|Power:8x AA Alkaline Batteries (10 months life)|Safety:Anti-tamper alarm & Random security code"}
                },

                // Category: Cameras (12 items)
                {
                        {"Cameras", "Sony", "Sony Alpha 7 IV Full-Frame Mirrorless Camera", "sony-alpha-7-iv", "33MP Exmor R CMOS sensor, BIONZ XR processing, Real-time Eye AF, 4K 60p 10-bit 4:2:2 video and 5-axis IBIS.", "209990", "229990", "9", "4.9", "78", "12", "camera, full frame, sony a7 iv, 33mp, 4k 60p, mirrorless, creator", true,
                                "Sensor:33.0MP Full-Frame Exmor R BSI CMOS Sensor|Processor:BIONZ XR|Video:4K 60p 10-Bit, S-Cinetone, S-Log3|Autofocus:759 Phase-Detection Points with Real-time Eye AF (Human, Animal, Bird)|Stabilization:5-Axis SteadyShot Inside (5.5 stops)|Viewfinder:3.68m-Dot OLED EVF"},
                        {"Cameras", "Canon", "Canon EOS R6 Mark II Mirrorless Camera", "canon-eos-r6-mark-ii", "24.2MP full-frame CMOS sensor, 40 fps electronic shutter, 6K oversampled uncropped 4K 60p video and up to 8 stops IBIS.", "215995", "239995", "10", "4.8", "60", "10", "canon, mirrorless, eos r6, 40fps, dual pixel af, 4k 60p, full frame", true,
                                "Sensor:24.2MP Full-Frame CMOS Sensor|Processor:DIGIC X|Continuous Shooting:Up to 40 fps Electronic Shutter, 12 fps Mechanical|Video:Uncropped 4K 60p (6K Oversampled), 6K RAW via HDMI|Autofocus:Dual Pixel CMOS AF II with Deep Learning AI|Stabilization:In-Body Image Stabilizer up to 8 stops"},
                        {"Cameras", "Sony", "Sony Alpha 7C II Compact Full-Frame Camera", "sony-alpha-7c-ii", "Compact lightweight rangefinder style body with 33MP sensor, dedicated AI processing unit and 4K 60p recording.", "184990", "199990", "8", "4.8", "42", "14", "sony a7c ii, compact mirrorless, travel camera, 33mp, ai autofocus", false,
                                "Sensor:33MP Full-Frame Exmor R CMOS|AI Processing:Dedicated AI Processing Unit for subject recognition|Stabilization:7.0 stops 5-axis In-body Optical Stabilization|Weight:514 grams with battery and card|Video:4K 60p (Super 35) & 4K 30p 7K oversampled"},
                        {"Cameras", "Canon", "Canon EOS R8 Full-Frame Mirrorless (Body Only)", "canon-eos-r8", "Lightest full-frame EOS R system camera with 24.2MP sensor, 40 fps burst and 4K 60p 10-bit HDR video.", "129995", "144995", "10", "4.6", "55", "18", "canon r8, lightweight full frame, budget full frame, dual pixel af", false,
                                "Sensor:24.2MP Full-Frame CMOS|Processor:DIGIC X|Autofocus:Dual Pixel CMOS AF II with subject tracking|Weight:461 grams|Video:4K 60p (6K oversampled) 10-bit Canon Log 3"},
                        {"Cameras", "Sony", "Sony FE 24-70mm F2.8 GM II Lens", "sony-fe-24-70-f28-gm-ii", "The world's lightest standard zoom G Master lens with constant F2.8 aperture, 4 XD linear motors and optical perfection.", "199990", "219990", "9", "4.9", "35", "8", "sony lens, g master, 24-70 f2.8, professional lens, bokeh, sharp", false,
                                "Focal Length:24-70mm|Aperture:F2.8 constant, 11-blade circular aperture|Filter Diameter:82mm|Weight:695 grams (22% lighter than Gen 1)|Autofocus:Four XD (extreme dynamic) Linear Motors"},
                        {"Cameras", "Canon", "Canon RF 24-70mm F2.8 L IS USM Lens", "canon-rf-24-70-f28-l", "Professional L-series standard zoom lens with 5 stops of Image Stabilization, Nano USM and Control Ring.", "204995", "224995", "9", "4.9", "28", "8", "canon lens, rf mount, 24-70 f2.8, l series, image stabilization", false,
                                "Focal Length:24-70mm|Aperture:F2.8 constant|Image Stabilization:5 stops optical (up to 8 stops with IBIS)|Motor:Nano USM for fast and quiet AF|Filter Size:82mm"},
                        {"Cameras", "Sony", "Sony ZV-E10 II Vlogging Camera with 16-50mm Lens", "sony-zv-e10-ii-kit", "26MP APS-C vlogging camera with 4K 60p, cinematic vlog setting, 3-capsule directional mic and vertical UI.", "84990", "94990", "11", "4.6", "95", "30", "vlogging camera, sony zv-e10, 4k 60p, creator, compact camera, youtube", false,
                                "Sensor:26.0MP APS-C Exmor R CMOS|Video:4K 60p 10-bit 4:2:2 (5.6K oversampled)|Audio:Directional 3-Capsule Mic with Windscreen|Screen:Vari-angle Touchscreen LCD with vertical video UI|Battery:Sony NP-FZ100 (Up to 130 mins video)"},
                        {"Cameras", "Canon", "Canon EOS R50 Vlogger Kit", "canon-eos-r50-kit", "Compact mirrorless camera with 24.2MP APS-C sensor, Movie for Close-up Demos mode and 4K 30p uncropped video.", "62995", "74995", "16", "4.5", "110", "35", "canon r50, beginner mirrorless, vlogging, budget camera, 4k video", false,
                                "Sensor:24.2MP APS-C CMOS|Autofocus:Dual Pixel CMOS AF II with vehicle/animal detection|Video:4K 30p (6K oversampled) uncropped|Weight:375 grams body|Screen:3.0-inch 1.62m-dot Vari-Angle Touchscreen"},
                        {"Cameras", "Sony", "Sony FE 50mm F1.4 GM Prime Lens", "sony-fe-50mm-f14-gm", "Standard prime G Master lens delivering extraordinary resolution, creamy bokeh and fast, precise AF in a compact body.", "124990", "139990", "11", "4.9", "22", "10", "sony prime lens, 50mm f1.4, g master, portrait lens, bokeh", false,
                                "Focal Length:50mm|Max Aperture:F1.4, 11 circular aperture blades|Weight:516 grams|Filter Thread:67mm|Minimum Focus Distance:0.38 meters"},
                        {"Cameras", "Canon", "Canon RF 50mm F1.8 STM Lens", "canon-rf-50mm-f18-stm", "Affordable nifty-fifty prime lens offering bright F1.8 aperture for beautiful background blur in a pocketable design.", "17995", "19995", "10", "4.7", "180", "50", "canon lens, nifty fifty, 50mm f1.8, affordable lens, portrait", false,
                                "Focal Length:50mm|Aperture:F1.8 with 7-blade circular design|Motor:STM stepping motor for smooth video AF|Weight:160 grams|Filter Size:43mm"},
                        {"Cameras", "Sony", "Sony Alpha 6700 APS-C Mirrorless Camera", "sony-alpha-6700", "Top APS-C camera with 26MP BSI sensor, AI recognition AF, 4K 120p high-frame-rate video and 5-axis IBIS.", "136990", "149990", "9", "4.7", "50", "15", "sony a6700, aps-c camera, 4k 120p, ai autofocus, wildlife, creator", false,
                                "Sensor:26.0MP APS-C Exmor R CMOS|Processor:BIONZ XR with dedicated AI Processing Unit|Video:4K 120p, 4K 60p 6K oversampled, 10-bit 4:2:2 S-Log3|Autofocus:759 AF points with Human/Animal/Bird/Car/Train/Airplane recognition|Stabilization:5.0 stops 5-axis IBIS"},
                        {"Cameras", "Canon", "Canon EOS R10 Mirrorless Camera with 18-45mm Lens", "canon-eos-r10-kit", "High-speed hybrid camera with 24.2MP APS-C sensor, up to 23 fps continuous shooting and 4K 60p video.", "88995", "98995", "10", "4.5", "65", "25", "canon r10, mirrorless kit, 23fps, fast autofocus, wildlife, sports", false,
                                "Sensor:24.2MP APS-C CMOS|Continuous Shooting:23 fps electronic shutter, 15 fps mechanical|Video:4K 60p (crop) & 4K 30p (6K oversampled)|Autofocus:Dual Pixel CMOS AF II with vehicle priority"}
                },

                // Category: Monitors (12 items)
                {
                        {"Monitors", "Samsung", "Samsung 49-inch Odyssey OLED G9 Curved Gaming Monitor", "samsung-odyssey-oled-g9", "Super Ultra-Wide 32:9 Dual QHD (5120x1440) QD-OLED display, 240Hz refresh rate, 0.03ms response time and Neo Quantum Processor Pro.", "139999", "179999", "22", "4.9", "40", "8", "monitor, 49 inch, oled, 240hz, ultrawide, curved, 32:9, samsung odyssey", true,
                                "Screen Size:49-inch Curved (1800R)|Resolution:Dual QHD (5120 x 1440) 32:9|Panel:OLED with Neo Quantum Processor Pro|Refresh Rate:240Hz|Response Time:0.03ms (GTG)|HDR:DisplayHDR True Black 400|Ports:DisplayPort 1.4, HDMI 2.1, Micro HDMI, USB Hub"},
                        {"Monitors", "LG", "LG UltraGear 27-inch OLED QHD 240Hz Gaming Monitor (27GR95QE)", "lg-ultragear-27-oled-240hz", "27-inch QHD (2560x1440) Anti-glare OLED panel with lightning-fast 240Hz, 0.03ms response time and 98.5% DCI-P3 color gamut.", "69999", "89999", "22", "4.8", "90", "15", "monitor, 27 inch oled, 240hz, qhd, lg ultragear, 0.03ms, esports", true,
                                "Screen Size:27-inch Flat OLED|Resolution:QHD (2560 x 1440) 16:9|Refresh Rate:240Hz|Response Time:0.03ms (GTG)|Color Gamut:DCI-P3 98.5%|Sync:NVIDIA G-SYNC Compatible & AMD FreeSync Premium Pro|Ports:2x HDMI 2.1, 1x DisplayPort 1.4, USB 3.0 Hub"},
                        {"Monitors", "Dell", "Dell UltraSharp 32 4K USB-C Hub Monitor (U3223QE)", "dell-ultrasharp-32-4k", "IPS Black technology with 2000:1 contrast ratio, 4K UHD resolution, 90W USB-C Power Delivery and built-in RJ45 Ethernet hub.", "68990", "79990", "14", "4.8", "120", "20", "monitor, 4k, ips black, dell ultrasharp, color accurate, usb-c 90w, productivity", true,
                                "Screen Size:31.5-inch 4K UHD (3840 x 2160)|Panel Type:IPS Black Technology|Contrast Ratio:2000:1|Color Coverage:100% sRGB, 100% Rec 709, 98% DCI-P3|Connectivity:USB-C with 90W PD, DisplayPort 1.4, HDMI 2.0, RJ45 Ethernet, 5x USB-A"},
                        {"Monitors", "Samsung", "Samsung 32-inch Odyssey Neo G8 4K 240Hz Curved Monitor", "samsung-odyssey-neo-g8", "World's first 4K 240Hz 1000R curved gaming monitor with Quantum Mini-LED, Quantum HDR 2000 and 1ms response time.", "84999", "109999", "23", "4.7", "55", "12", "monitor, 4k 240hz, mini led, curved 1000r, samsung neo g8, quantum hdr", false,
                                "Screen Size:32-inch Curved (1000R)|Resolution:4K UHD (3840 x 2160)|Panel:Quantum Mini-LED VA|Refresh Rate:240Hz|Peak Brightness:2000 nits (Quantum HDR 2000)|Response Time:1ms (GTG)|Ports:2x HDMI 2.1, 1x DisplayPort 1.4"},
                        {"Monitors", "Dell", "Dell Alienware 34 Curved QD-OLED Gaming Monitor (AW3423DWF)", "dell-alienware-34-qd-oled", "34-inch WQHD (3440x1440) 1800R curved QD-OLED with 165Hz, DisplayHDR True Black 400 and AMD FreeSync Premium Pro.", "89990", "104990", "14", "4.9", "70", "10", "monitor, 34 inch ultrawide, qd-oled, alienware, 165hz, curved", true,
                                "Screen Size:34.18-inch Curved (1800R)|Resolution:WQHD (3440 x 1440) 21:9|Panel:Quantum Dot OLED|Refresh Rate:165Hz|Response Time:0.1ms (GTG)|Color Accuracy:Delta E < 2, 99.3% DCI-P3|Warranty:3-Year Premium Warranty including OLED Burn-in"},
                        {"Monitors", "LG", "LG UltraWide 34-inch WQHD Curved IPS Monitor (34WN80C)", "lg-ultrawide-34-ips", "21:9 WQHD sRGB 99% color gamut, HDR10 support, USB Type-C 60W connectivity and height/tilt adjustable stand.", "38999", "49999", "22", "4.6", "210", "30", "monitor, 34 inch ultrawide, lg, usb-c, ips, productivity, coding", false,
                                "Screen Size:34-inch Curved (1900R) 21:9|Resolution:WQHD (3440 x 1440)|Panel:IPS with 99% sRGB|Connectivity:USB-C (60W PD), 2x HDMI, DisplayPort|Features:HDR10, Reader Mode, Dual Controller (KVM)"},
                        {"Monitors", "ASUS", "ASUS TUF Gaming 27-inch 2K 180Hz Monitor (VG27AQML1A)", "asus-tuf-27-2k-180hz", "Fast IPS panel with overclocked 180Hz, 1ms (GTG), ELMB Sync, Extreme Low Motion Blur and DisplayHDR 400.", "24999", "32999", "24", "4.6", "190", "45", "monitor, budget 2k, 180hz, fast ips, asus tuf, gaming monitor", false,
                                "Screen Size:27-inch QHD (2560 x 1440)|Panel:Fast IPS|Refresh Rate:180Hz (OC)|Response Time:1ms (GTG)|Sync:FreeSync Premium, G-Sync Compatible, ELMB Sync|Color:100% sRGB, DisplayHDR 400"},
                        {"Monitors", "Samsung", "Samsung 27-inch Odyssey G5 QHD 165Hz Gaming Monitor", "samsung-odyssey-g5-27", "1000R curved gaming display with 165Hz refresh rate, 1ms response time and AMD FreeSync Premium.", "19999", "27999", "29", "4.4", "260", "50", "monitor, budget 144hz, 165hz, samsung odyssey, 1000r curved, 27 inch", false,
                                "Screen Size:27-inch Curved (1000R)|Resolution:QHD (2560 x 1440)|Panel:VA|Refresh Rate:165Hz|Response Time:1ms (MPRT)|Sync:AMD FreeSync Premium|Ports:1x HDMI 2.0, 1x DisplayPort 1.2"},
                        {"Monitors", "Dell", "Dell 27-inch 4K UHD USB-C Monitor (S2722QC)", "dell-27-4k-usbc", "4K UHD display with integrated 65W USB-C connectivity, dual 3W built-in speakers and 99% sRGB coverage.", "28990", "36990", "22", "4.7", "140", "35", "monitor, 27 inch 4k, dell, usb-c 65w, speakers, office, coding", false,
                                "Screen Size:27-inch 4K UHD (3840 x 2160)|Panel:IPS with 99% sRGB|Connectivity:USB-C with 65W PD, 2x HDMI 2.0, 2x USB 3.2|Audio:Dual 3W integrated speakers|Stand:Height, pivot, tilt, and swivel adjustable"},
                        {"Monitors", "LG", "LG 27-inch 4K UHD Ergo IPS Monitor (27UN880)", "lg-27-4k-ergo", "Ergonomic C-clamp arm stand offering extensive swivel, pivot, height, extend and retract adjustments with USB-C 60W.", "32999", "42999", "23", "4.7", "85", "20", "monitor, 4k, lg ergo arm, ergonomic stand, usb-c, ips, designer", false,
                                "Screen Size:27-inch 4K UHD (3840 x 2160)|Panel:IPS with DCI-P3 95% (Typ.)|Stand:LG Ergo Arm (Extend/Retract, Swivel, Height, Pivot, Tilt)|Connectivity:USB-C 60W, 2x HDMI, DisplayPort, 2x USB 3.0"},
                        {"Monitors", "ASUS", "ASUS ProArt Display 27-inch 4K Professional Monitor (PA279CV)", "asus-proart-27-4k", "Factory pre-calibrated Calman Verified display with 100% sRGB/Rec. 709 and Delta E < 2 color accuracy.", "39999", "49999", "20", "4.8", "60", "15", "monitor, color accurate, asus proart, calman verified, delta e < 2, photo editing, video", false,
                                "Screen Size:27-inch 4K UHD (3840 x 2160) IPS|Color Accuracy:Delta E < 2, 100% sRGB, 100% Rec. 709|Certification:Calman Verified with factory report|Connectivity:USB-C 65W PD, DisplayPort, 2x HDMI, 4x USB 3.1 Hub"},
                        {"Monitors", "Samsung", "Samsung 32-inch Smart Monitor M8 (4K UHD)", "samsung-smart-monitor-m8", "All-in-one Smart TV monitor with SlimFit magnetic camera, SmartThings IoT hub, streaming apps and AirPlay.", "39999", "54999", "27", "4.5", "110", "25", "smart monitor, 4k, tv apps, samsung m8, slimfit camera, airplay", false,
                                "Screen Size:32-inch 4K UHD (3840 x 2160)|Smart Platform:Tizen OS with Netflix, Prime, YouTube, Office 365|Camera:Detachable SlimFit Camera (FHD)|Connectivity:USB-C 65W PD, Micro HDMI, Wi-Fi, Bluetooth"}
                },

                // Category: Accessories (12 items)
                {
                        {"Accessories", "Logitech", "Logitech MX Master 3S Wireless Performance Mouse", "logitech-mx-master-3s", "Quiet Clicks, 8000 DPI Darkfield tracking on glass, MagSpeed electromagnetic scroll wheel and USB-C.", "8995", "10995", "18", "4.9", "450", "80", "mouse, ergonomic, logitech mx master 3s, 8000 dpi, productivity, mac, windows", true,
                                "Sensor:Darkfield high precision (200 - 8000 DPI)|Scroll Wheel:MagSpeed electromagnetic with Smartshift|Clicks:90% quieter Quiet Click technology|Battery:500 mAh rechargeable (Up to 70 days)|Connectivity:Bluetooth Low Energy & Logi Bolt USB Receiver"},
                        {"Accessories", "Logitech", "Logitech MX Mechanical Wireless Keyboard", "logitech-mx-mechanical", "Low profile tactile quiet mechanical switches with smart illumination, multi-device Easy-Switch and 15-day battery.", "14995", "17495", "14", "4.7", "180", "40", "keyboard, mechanical, logitech mx, low profile, multi-device, coding, quiet", true,
                                "Switches:Tactile Quiet Low Profile Mechanical|Backlighting:Smart ambient illumination with hand proximity sensors|Multi-device:Connect up to 3 devices via Bluetooth/Bolt|Battery:Up to 15 days (10 months with backlighting off)|Keycaps:Dual color keycaps for Mac and Windows"},
                        {"Accessories", "Apple", "Apple Magic Mouse (Black Multi-Touch)", "apple-magic-mouse-black", "Wireless and rechargeable mouse with Multi-Touch surface for gestures like swiping between web pages and scrolling.", "8900", "9500", "6", "4.4", "220", "50", "apple, magic mouse, multi-touch, wireless mouse, macbook accessory", false,
                                "Surface:Multi-Touch surface for swipe and scroll gestures|Battery:Built-in rechargeable lithium-ion|Connectivity:Bluetooth, Lightning/USB-C charging|Weight:99 grams"},
                        {"Accessories", "Apple", "Apple Magic Keyboard with Touch ID and Numeric Keypad", "apple-magic-keyboard-touch-id", "Slim aluminum keyboard featuring built-in Touch ID for fast, secure authentication and purchase verification.", "18500", "19500", "5", "4.7", "130", "30", "apple, magic keyboard, touch id, wireless keyboard, scissor mechanism", false,
                                "Security:Built-in Touch ID sensor for Mac with Apple Silicon|Layout:Full size with numeric keypad and document navigation controls|Connectivity:Bluetooth & USB-C to Lightning cable|Battery:Rechargeable, over a month per charge"},
                        {"Accessories", "Samsung", "Samsung T9 Portable SSD 2TB (USB 3.2 Gen 2x2)", "samsung-t9-portable-ssd-2tb", "Rugged 2000 MB/s portable solid state drive with drop protection up to 3 meters and thermal control.", "19999", "24999", "20", "4.8", "160", "40", "portable ssd, samsung t9, 2000mbps, usb 3.2 gen 2x2, external storage", false,
                                "Capacity:2TB|Interface:USB 3.2 Gen 2x2 (20 Gbps)|Speed:Sequential Read/Write up to 2000 MB/s|Durability:Drop resistant up to 3 meters, rubberized casing|Security:AES 256-bit hardware encryption|Weight:122 grams"},
                        {"Accessories", "Samsung", "Samsung 65W Trio Power Adapter (GaN)", "samsung-65w-trio-gan-adapter", "Compact GaN 3-port wall charger with 65W USB-C PD, 25W USB-C and 15W USB-A for charging laptops, tablets and phones.", "3499", "4499", "22", "4.6", "290", "90", "charger, 65w gan charger, samsung fast charge, usb-c pd, 3-port charger", false,
                                "Total Output:65W Max|Ports:USB-C1 (Max 65W), USB-C2 (Max 25W), USB-A (Max 15W)|Technology:Gallium Nitride (GaN)|Compatibility:Laptops, MacBooks, Galaxy phones, iPhones, iPads"},
                        {"Accessories", "Dell", "Dell Premier Multi-Device Wireless Keyboard and Mouse (KM7321W)", "dell-premier-km7321w", "Seamlessly switch between 3 devices across 2.4GHz and Bluetooth 5.0 with industry-leading 36-month battery life.", "7499", "9999", "25", "4.5", "85", "35", "keyboard mouse combo, dell premier, multi-device, 36 month battery, office", false,
                                "Connectivity:2.4GHz wireless + 2x Bluetooth 5.0 channels|Battery Life:Up to 36 months keyboard and mouse|Mouse DPI:Up to 4000 DPI adjustable in Dell Peripheral Manager|Keyboard:Full size with programmable shortcut keys"},
                        {"Accessories", "Logitech", "Logitech Desk Mat Studio Series (Mid Gray)", "logitech-desk-mat-gray", "Spill-resistant microfiber desk mat with anti-slip rubber base and anti-fraying stitched edges.", "1895", "2295", "17", "4.7", "340", "120", "desk mat, mouse pad, logitech, spill resistant, aesthetic desk setup", false,
                                "Dimensions:300 mm x 700 mm x 2 mm|Material:100% recycled polyester surface, 72% natural rubber base|Features:Spill-resistant coating, anti-fray stitched edges"},
                        {"Accessories", "Sony", "Sony TOUGH 128GB SDXC UHS-II Memory Card (SF-G128T)", "sony-tough-128gb-sd-card", "World's toughest SD card with IP68 waterproof/dustproof rating, 300 MB/s read and 299 MB/s write speed for 8K video.", "16490", "18990", "13", "4.9", "45", "20", "sd card, sony tough, uhs-ii, 300mbps, 8k video, camera accessory", false,
                                "Speed Class:UHS-II, V90, Class 10, U3|Read Speed:Up to 300 MB/s|Write Speed:Up to 299 MB/s|Durability:18x standard bend resistance (180N), IP68 waterproof and dustproof"},
                        {"Accessories", "Dell", "Dell USB-C Dual Charge Dock (HD22Q)", "dell-dual-charge-dock-hd22q", "Laptop docking station with built-in Qi wireless charging stand for smartphone, 90W laptop power and 4K display support.", "14999", "18999", "21", "4.6", "30", "15", "docking station, dell, usb-c dock, wireless charger, 4k display, dual monitor", false,
                                "Laptop Power:Up to 90W Power Delivery|Phone Charger:Up to 12W Qi Wireless charging stand|Display Outputs:1x HDMI 2.1, 1x DisplayPort 1.4 (Dual 4K support)|Ports:4x USB-A 3.2 Gen 1, 1x USB-C 3.2 Gen 2, RJ45 Ethernet"},
                        {"Accessories", "Apple", "Apple 140W USB-C Power Adapter", "apple-140w-usbc-power-adapter", "Fast, efficient charging with GaN architecture, capable of charging a 16-inch MacBook Pro to 50% in 30 minutes.", "9500", "9500", "0", "4.8", "120", "40", "apple charger, 140w, macbook pro fast charger, usb-c pd 3.1", false,
                                "Power Output:140W USB-C Power Delivery 3.1|Fast Charge:50% in 30 mins with USB-C to MagSafe 3 Cable|Compatibility:MacBook Pro 16-inch and USB-C devices"},
                        {"Accessories", "Logitech", "Logitech Litra Beam Premium Streaming Key Light", "logitech-litra-beam", "Desktop LED key light with TrueSoft technology, 3-way adjustable stand and customizable color temperature.", "9995", "11995", "17", "4.6", "75", "25", "streaming light, key light, logitech litra, youtube, twitch, webcam lighting", false,
                                "Color Temperature:2700K - 6500K|Brightness:Up to 400 Lumens Max Output|CRI:93 for natural, radiant skin tones|Controls:Hardware buttons & Logitech G HUB desktop software"}
                }
        };

        for (Object[][] catGroup : catalog) {
            for (Object[] row : catGroup) {
                String catName = (String) row[0];
                String brandName = (String) row[1];
                String name = (String) row[2];
                String slug = (String) row[3];
                String desc = (String) row[4];
                BigDecimal price = new BigDecimal((String) row[5]);
                BigDecimal origPrice = new BigDecimal((String) row[6]);
                int discount = Integer.parseInt((String) row[7]);
                double rating = Double.parseDouble((String) row[8]);
                int reviewCount = Integer.parseInt((String) row[9]);
                int stock = Integer.parseInt((String) row[10]);
                String tags = (String) row[11];
                boolean featured = (Boolean) row[12];
                String rawSpecs = (String) row[13];

                Category cat = categories.get(catName);
                Brand brand = brands.get(brandName);

                Product p = Product.builder()
                        .name(name)
                        .slug(slug)
                        .description(desc)
                        .category(cat)
                        .brand(brand)
                        .price(price)
                        .originalPrice(origPrice)
                        .discountPercent(discount)
                        .rating(rating)
                        .reviewCount(reviewCount)
                        .stock(stock)
                        .active(true)
                        .featured(featured)
                        .tags(tags)
                        .keywords(name.toLowerCase() + ", " + brandName.toLowerCase() + ", " + catName.toLowerCase() + ", " + tags)
                        .primaryImageUrl(resolveProductImage(catName, brandName, slug))
                        .build();

                p = productRepository.save(p);

                // Add Primary & Gallery Product Images
                ProductImage primaryImg = ProductImage.builder()
                        .product(p)
                        .imageUrl(p.getPrimaryImageUrl())
                        .isPrimary(true)
                        .altText(p.getName())
                        .build();
                productImageRepository.save(primaryImg);

                // Add Specifications
                if (rawSpecs != null && !rawSpecs.isEmpty()) {
                    String[] specPairs = rawSpecs.split("\\|");
                    for (String pair : specPairs) {
                        String[] kv = pair.split(":");
                        if (kv.length == 2) {
                            ProductSpecification spec = ProductSpecification.builder()
                                    .product(p)
                                    .specGroup(determineSpecGroup(kv[0]))
                                    .specKey(kv[0].trim())
                                    .specValue(kv[1].trim())
                                    .build();
                            specRepository.save(spec);
                        }
                    }
                }

                // Add Inventory
                Inventory inv = Inventory.builder()
                        .product(p)
                        .stockQuantity(stock)
                        .reservedQuantity(0)
                        .warehouseLocation("WH-DEL-0" + (1 + (products.size() % 4)))
                        .build();
                inventoryRepository.save(inv);

                products.add(p);
            }
        }

        return products;
    }

    private String resolveProductImage(String category, String brand, String slug) {
        String s = slug.toLowerCase();
        // Smartphones
        if (s.contains("iphone-16") || s.contains("iphone-15")) return "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600&auto=format&fit=crop&q=80";
        if (s.contains("iphone-14")) return "https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=600&auto=format&fit=crop&q=80";
        if (s.contains("s24-ultra") || s.contains("s23-ultra")) return "https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&auto=format&fit=crop&q=80";
        if (s.contains("fold") || s.contains("flip") || s.contains("open")) return "https://images.unsplash.com/photo-1580910051074-3eb694886505?w=600&auto=format&fit=crop&q=80";
        if (s.contains("oneplus-12") || s.contains("oneplus-nord")) return "https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=600&auto=format&fit=crop&q=80";
        if (s.contains("samsung-galaxy-a55") || s.contains("samsung-galaxy-m35") || s.contains("s24-fe")) return "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600&auto=format&fit=crop&q=80";
        if (s.contains("xperia")) return "https://images.unsplash.com/photo-1585060544812-6b45742d762f?w=600&auto=format&fit=crop&q=80";

        // Laptops
        if (s.contains("macbook-pro") || s.contains("macbook-air")) return "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600&auto=format&fit=crop&q=80";
        if (s.contains("legion") || s.contains("tuf") || s.contains("alienware") || s.contains("predator") || s.contains("zephyrus") || s.contains("loq") || s.contains("rog")) return "https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=600&auto=format&fit=crop&q=80";
        if (s.contains("xps") || s.contains("yoga") || s.contains("thinkpad") || s.contains("zenbook") || s.contains("spectre") || s.contains("envy") || s.contains("gram")) return "https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=600&auto=format&fit=crop&q=80";

        // Headphones & Audio
        if (s.contains("wh-1000xm5") || s.contains("quietcomfort") || s.contains("airpods-max") || s.contains("momentum")) return "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80";
        if (s.contains("airpods-pro") || s.contains("buds") || s.contains("earbuds") || s.contains("tws") || s.contains("wf-1000xm5") || s.contains("freebuds")) return "https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=600&auto=format&fit=crop&q=80";
        if (s.contains("speaker") || s.contains("soundlink") || s.contains("soundbar")) return "https://images.unsplash.com/photo-1545454675-3531b543be5d?w=600&auto=format&fit=crop&q=80";

        // Gaming
        if (s.contains("playstation") || s.contains("ps5")) return "https://images.unsplash.com/photo-1606813907291-d86efa9b94db?w=600&auto=format&fit=crop&q=80";
        if (s.contains("xbox")) return "https://images.unsplash.com/photo-1621259182978-fbf93132d53d?w=600&auto=format&fit=crop&q=80";
        if (s.contains("switch") || s.contains("deck") || s.contains("ally") || s.contains("claw") || s.contains("handheld")) return "https://images.unsplash.com/photo-1578303512597-81e6cc155b3e?w=600&auto=format&fit=crop&q=80";
        if (s.contains("rtx") || s.contains("graphics-card") || s.contains("gpu")) return "https://images.unsplash.com/photo-1587202372775-e229f172b9d7?w=600&auto=format&fit=crop&q=80";
        if (s.contains("controller") || s.contains("dualsense")) return "https://images.unsplash.com/photo-1600080972464-8e5f35f63d08?w=600&auto=format&fit=crop&q=80";

        // Smart Home & TVs
        if (s.contains("tv") || s.contains("oled") || s.contains("bravia") || s.contains("qled") || s.contains("frame")) return "https://images.unsplash.com/photo-1593359677879-a4bb92f829d1?w=600&auto=format&fit=crop&q=80";
        if (s.contains("echo") || s.contains("alexa") || s.contains("homepod") || s.contains("nest")) return "https://images.unsplash.com/photo-1543512214-318c7553f230?w=600&auto=format&fit=crop&q=80";
        if (s.contains("webcam") || s.contains("brio") || s.contains("camera")) return "https://images.unsplash.com/photo-1557597774-9d273605dfa9?w=600&auto=format&fit=crop&q=80";

        // Cameras
        if (s.contains("lens") || s.contains("50mm") || s.contains("24-70mm") || s.contains("70-200mm")) return "https://images.unsplash.com/photo-1617005082133-548c4dd27f35?w=600&auto=format&fit=crop&q=80";
        if (s.contains("camera") || s.contains("alpha") || s.contains("eos") || s.contains("fx3") || s.contains("z8") || s.contains("r6")) return "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=600&auto=format&fit=crop&q=80";

        // Monitors
        if (s.contains("odyssey") || s.contains("ultragear") || s.contains("ultrasharp") || s.contains("proart") || s.contains("alienware") || s.contains("monitor") || s.contains("curved")) return "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600&auto=format&fit=crop&q=80";

        // Accessories
        if (s.contains("mouse") || s.contains("mx-master") || s.contains("trackball")) return "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=600&auto=format&fit=crop&q=80";
        if (s.contains("keyboard") || s.contains("mx-mechanical") || s.contains("magic-keyboard")) return "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop&q=80";
        if (s.contains("ssd") || s.contains("charger") || s.contains("adapter") || s.contains("dock") || s.contains("hub") || s.contains("light")) return "https://images.unsplash.com/photo-1609081219094-a31f24d8820f?w=600&auto=format&fit=crop&q=80";

        // Fallbacks by Category
        if ("Smartphones".equalsIgnoreCase(category)) return "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600&auto=format&fit=crop&q=80";
        if ("Laptops".equalsIgnoreCase(category)) return "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600&auto=format&fit=crop&q=80";
        if ("Headphones".equalsIgnoreCase(category)) return "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80";
        if ("Gaming".equalsIgnoreCase(category)) return "https://images.unsplash.com/photo-1606813907291-d86efa9b94db?w=600&auto=format&fit=crop&q=80";
        if ("Smart Home".equalsIgnoreCase(category)) return "https://images.unsplash.com/photo-1593359677879-a4bb92f829d1?w=600&auto=format&fit=crop&q=80";
        if ("Cameras".equalsIgnoreCase(category)) return "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=600&auto=format&fit=crop&q=80";
        if ("Monitors".equalsIgnoreCase(category)) return "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600&auto=format&fit=crop&q=80";
        if ("Accessories".equalsIgnoreCase(category)) return "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=600&auto=format&fit=crop&q=80";

        return "https://images.unsplash.com/photo-1526738549149-8e07eca6c147?w=600&auto=format&fit=crop&q=80";
    }

    private String determineSpecGroup(String key) {
        String k = key.toLowerCase();
        if (k.contains("processor") || k.contains("graphics") || k.contains("ram") || k.contains("storage") || k.contains("chip")) return "Performance";
        if (k.contains("display") || k.contains("screen") || k.contains("resolution") || k.contains("panel")) return "Display";
        if (k.contains("camera") || k.contains("sensor") || k.contains("lens") || k.contains("aperture")) return "Camera";
        if (k.contains("battery") || k.contains("charging") || k.contains("fast charge")) return "Battery & Power";
        if (k.contains("connectivity") || k.contains("bluetooth") || k.contains("wireless") || k.contains("ports")) return "Connectivity";
        return "General";
    }

    private void seedReviews(Map<String, User> users, List<Product> products) {
        List<User> userList = new ArrayList<>(users.values());
        Random rand = new Random(42);

        String[][] reviewTemplates = {
                {"5", "Absolute Masterpiece! Exceeded all expectations", "The build quality and performance on this product are second to none. Super fast, flawless display and the battery easily lasts all day without heating up."},
                {"5", "Best purchase I made this year", "Everything works as advertised. The speed and camera clarity are incredible. Totally worth every rupee."},
                {"4", "Great performance but battery could be better", "The speed, screen and responsiveness are top tier. However, under heavy load the battery life is slightly less than claimed. Still highly recommended!"},
                {"4", "Solid value for money", "Really satisfied with the daily performance. Fast delivery and neat packaging."},
                {"3", "Decent features, but minor thermal issues", "The display and features are fantastic, but the device tends to get noticeably warm during extended multitasking. Average battery backup."},
                {"2", "Disappointed with battery drain", "Battery life is terrible under continuous gaming load. It discharges much faster than my previous model. Display is good though."},
                {"1", "Damaged packaging and delayed delivery", "The courier box arrived crushed and shipping was delayed by 3 days. Expected better logistics for a premium item."},
                {"5", "Phenomenal display and crisp audio", "The OLED screen colors pop vividly and the deep bass from the speakers makes media consumption a joy."},
                {"4", "Premium build quality", "Feels sturdy in hand. The aluminum casing and matte finish give it a luxurious look and feel."},
                {"2", "Subpar low light camera performance", "Daylight photos are crisp, but in low light the images turn out grainy and blurry. Needs a software update."}
        };

        for (Product product : products) {
            // Add 2 to 4 reviews per product to reach ~250 total reviews
            int reviewCount = 2 + rand.nextInt(3);
            for (int i = 0; i < reviewCount; i++) {
                User reviewer = userList.get(rand.nextInt(userList.size()));
                String[] template = reviewTemplates[rand.nextInt(reviewTemplates.length)];
                int rating = Integer.parseInt(template[0]);
                String title = template[1];
                String comment = template[2];

                Review review = Review.builder()
                        .user(reviewer)
                        .product(product)
                        .rating(rating)
                        .title(title)
                        .comment(comment)
                        .verifiedPurchase(true)
                        .sentiment(rating >= 4 ? "Positive" : (rating <= 2 ? "Negative" : "Mixed"))
                        .helpfulCount(rand.nextInt(25))
                        .build();

                review = reviewRepository.save(review);
                feedbackService.processReviewFeedback(review);
            }
        }
    }

    private void seedOrders(Map<String, User> users, List<Product> products) {
        List<User> userList = new ArrayList<>(users.values());
        Random rand = new Random(101);

        for (int i = 1; i <= 50; i++) {
            User customer = userList.get(rand.nextInt(userList.size()));
            Product product = products.get(rand.nextInt(products.size()));
            int qty = 1 + rand.nextInt(2);
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(qty));
            BigDecimal tax = itemTotal.multiply(BigDecimal.valueOf(0.18));
            BigDecimal finalAmount = itemTotal.add(tax);

            Order.OrderStatus status = (i % 6 == 0) ? Order.OrderStatus.DELIVERED
                    : ((i % 6 == 1) ? Order.OrderStatus.SHIPPED
                    : ((i % 6 == 2) ? Order.OrderStatus.PROCESSING
                    : ((i % 6 == 3) ? Order.OrderStatus.CONFIRMED
                    : ((i % 6 == 4) ? Order.OrderStatus.DELIVERED : Order.OrderStatus.RETURN_REQUESTED))));

            Order order = Order.builder()
                    .orderNumber("ORD-2026-" + String.format("%05d", i))
                    .user(customer)
                    .totalAmount(itemTotal)
                    .discountAmount(BigDecimal.ZERO)
                    .taxAmount(tax)
                    .finalAmount(finalAmount)
                    .status(status)
                    .carrier("OmniExpress Logistics")
                    .trackingNumber("TRK-EXP-" + String.format("%08d", 10000000 + i))
                    .deliveredAt(status == Order.OrderStatus.DELIVERED ? LocalDateTime.now().minusDays(rand.nextInt(10)) : null)
                    .returnReason(status == Order.OrderStatus.RETURN_REQUESTED ? "Battery drains too fast during high load" : null)
                    .returnStatus(status == Order.OrderStatus.RETURN_REQUESTED ? "REQUESTED" : null)
                    .build();

            order = orderRepository.save(order);

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .productName(product.getName())
                    .productImageUrl(product.getPrimaryImageUrl())
                    .quantity(qty)
                    .unitPrice(product.getPrice())
                    .totalPrice(itemTotal)
                    .build();
            orderItemRepository.save(item);

            Payment payment = Payment.builder()
                    .order(order)
                    .transactionId("TXN-INIT-" + String.format("%07d", 2000000 + i))
                    .paymentMethod(i % 2 == 0 ? "CREDIT_CARD" : "UPI")
                    .paymentStatus(Payment.PaymentStatus.COMPLETED)
                    .amount(finalAmount)
                    .build();
            paymentRepository.save(payment);
        }
    }

    private void seedBehavioralInteractions(Map<String, User> users, List<Product> products) {
        User demoUser = users.get("user@omnimart.com");
        Random rand = new Random(88);

        // Seed 150 targeted events for demoUser specifically focused on Lenovo & Gaming Laptops
        List<Product> gamingAndLaptops = products.stream()
                .filter(p -> (p.getCategory() != null && (p.getCategory().getName().equals("Laptops") || p.getCategory().getName().equals("Gaming"))))
                .toList();

        for (int i = 0; i < 120; i++) {
            Product p = gamingAndLaptops.get(rand.nextInt(gamingAndLaptops.size()));
            UserInteraction.InteractionType type = (i % 5 == 0) ? UserInteraction.InteractionType.CART_ADD
                    : ((i % 5 == 1) ? UserInteraction.InteractionType.PRODUCT_CLICK
                    : ((i % 5 == 2) ? UserInteraction.InteractionType.WISHLIST_ADD
                    : UserInteraction.InteractionType.PRODUCT_VIEW));

            UserInteraction ui = UserInteraction.builder()
                    .user(demoUser)
                    .eventType(type)
                    .product(p)
                    .categoryName(p.getCategory().getName())
                    .brandName(p.getBrand().getName())
                    .priceAtEvent(p.getPrice())
                    .dwellTimeSeconds(15 + rand.nextInt(180))
                    .build();
            interactionRepository.save(ui);
        }

        // Seed 400 distributed events across other users
        List<User> userList = new ArrayList<>(users.values());
        for (int i = 0; i < 400; i++) {
            User u = userList.get(rand.nextInt(userList.size()));
            Product p = products.get(rand.nextInt(products.size()));
            UserInteraction.InteractionType type = UserInteraction.InteractionType.values()[rand.nextInt(UserInteraction.InteractionType.values().length)];

            UserInteraction ui = UserInteraction.builder()
                    .user(u)
                    .eventType(type)
                    .product(p)
                    .categoryName(p.getCategory() != null ? p.getCategory().getName() : null)
                    .brandName(p.getBrand() != null ? p.getBrand().getName() : null)
                    .priceAtEvent(p.getPrice())
                    .dwellTimeSeconds(10 + rand.nextInt(120))
                    .build();
            interactionRepository.save(ui);
        }
    }

    private void seedMarketIntelligence(List<Product> products) {
        Random rand = new Random(77);
        String[] stores = {"AlphaRetail Global", "TechWorld Benchmark", "ElectroHub Direct"};

        for (int i = 0; i < Math.min(40, products.size()); i++) {
            Product p = products.get(i);
            String store = stores[i % stores.length];

            // Market price varies by +/- 5% to 10%
            double multiplier = 0.95 + (rand.nextDouble() * 0.15);
            BigDecimal extPrice = p.getPrice().multiply(BigDecimal.valueOf(multiplier)).setScale(2, java.math.RoundingMode.HALF_UP);

            MarketProduct mp = MarketProduct.builder()
                    .storeName(store)
                    .productName(p.getName())
                    .brand(p.getBrand() != null ? p.getBrand().getName() : "")
                    .category(p.getCategory() != null ? p.getCategory().getName() : "")
                    .externalPrice(extPrice)
                    .externalUrl("https://www." + store.toLowerCase().replaceAll(" ", "") + ".com/item/" + p.getSlug())
                    .observedDate(LocalDate.now().minusDays(rand.nextInt(5)))
                    .benchmarkRating(Math.round((4.0 + rand.nextDouble() * 0.9) * 10.0) / 10.0)
                    .matchedProduct(p)
                    .build();
            marketProductRepository.save(mp);
        }
    }
}
