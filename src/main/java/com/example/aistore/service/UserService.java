package com.example.aistore.service;

import com.example.aistore.dto.AuthDtos;
import com.example.aistore.entity.Address;
import com.example.aistore.entity.User;
import com.example.aistore.entity.UserPreference;
import com.example.aistore.entity.UserRole;
import com.example.aistore.repository.AddressRepository;
import com.example.aistore.repository.UserPreferenceRepository;
import com.example.aistore.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, AddressRepository addressRepository, UserPreferenceRepository userPreferenceRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.userPreferenceRepository = userPreferenceRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public User registerUser(AuthDtos.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered: " + request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName().trim())
                .phone(request.getPhone())
                .active(true)
                .roles(Set.of(UserRole.ROLE_USER))
                .build();

        user = userRepository.save(user);

        // Initialize User Preference record
        UserPreference preference = UserPreference.builder()
                .user(user)
                .recommendationsEnabled(true)
                .behaviorTrackingEnabled(true)
                .aiChatHistoryEnabled(true)
                .build();
        userPreferenceRepository.save(preference);

        return user;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase().trim());
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Address> getUserAddresses(User user) {
        return addressRepository.findByUser(user);
    }

    @Transactional
    public Address addAddress(User user, Address address) {
        address.setUser(user);
        if (address.isDefault()) {
            addressRepository.findByUser(user).forEach(a -> {
                a.setDefault(false);
                addressRepository.save(a);
            });
        }
        return addressRepository.save(address);
    }

    @Transactional
    public Address updateAddress(User user, Long addressId, Address updated) {
        Address existing = addressRepository.findById(addressId)
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Address not found or unauthorized"));

        existing.setFullName(updated.getFullName());
        existing.setPhone(updated.getPhone());
        existing.setStreetAddress(updated.getStreetAddress());
        existing.setCity(updated.getCity());
        existing.setState(updated.getState());
        existing.setPostalCode(updated.getPostalCode());
        existing.setCountry(updated.getCountry());
        
        if (updated.isDefault()) {
            addressRepository.findByUser(user).forEach(a -> {
                if (!a.getId().equals(addressId)) {
                    a.setDefault(false);
                    addressRepository.save(a);
                }
            });
            existing.setDefault(true);
        }
        return addressRepository.save(existing);
    }

    @Transactional
    public void deleteAddress(User user, Long addressId) {
        Address existing = addressRepository.findById(addressId)
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Address not found or unauthorized"));
        addressRepository.delete(existing);
    }

    @Transactional
    public void setDefaultAddress(User user, Long addressId) {
        List<Address> addresses = addressRepository.findByUser(user);
        for (Address a : addresses) {
            a.setDefault(a.getId().equals(addressId));
            addressRepository.save(a);
        }
    }

    @Transactional
    public void updatePersonalInfo(User user, String fullName, String phone) {
        if (fullName != null && !fullName.trim().isEmpty()) {
            user.setFullName(fullName.trim());
        }
        if (phone != null) {
            user.setPhone(phone.trim());
        }
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(User user, String currentPassword, String newPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password does not match.");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void updateEmail(User user, String newEmail) {
        String cleanEmail = newEmail.toLowerCase().trim();
        if (userRepository.existsByEmail(cleanEmail) && !user.getEmail().equalsIgnoreCase(cleanEmail)) {
            throw new IllegalArgumentException("Email is already taken by another account.");
        }
        user.setEmail(cleanEmail);
        userRepository.save(user);
    }
}
