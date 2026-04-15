package com.fitcoach.snap2track.service;

import com.fitcoach.snap2track.dto.MealDto;
import com.fitcoach.snap2track.entity.FoodItem;
import com.fitcoach.snap2track.entity.MealEntry;
import com.fitcoach.snap2track.entity.User;
import com.fitcoach.snap2track.exception.ResourceNotFoundException;
import com.fitcoach.snap2track.repository.FoodItemRepository;
import com.fitcoach.snap2track.repository.MealEntryRepository;
import com.fitcoach.snap2track.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MealService {

    private final MealEntryRepository mealEntryRepository;
    private final FoodItemRepository foodItemRepository;
    private final UserRepository userRepository;

    @Transactional
    public MealEntry createMeal(MealDto.CreateMealRequest request) {
        User client = userRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", request.getClientId()));

        if (client.getRole() != User.Role.CLIENT) {
            throw new IllegalArgumentException("User with id " + request.getClientId() + " is not a CLIENT");
        }

        MealEntry meal = MealEntry.builder()
                .client(client)
                .mealType(request.getMealType())
                .eatenAt(request.getEatenAt())
                .photoUrl(request.getPhotoUrl())
                .analysisStatus(MealEntry.AnalysisStatus.PENDING)
                .build();

        MealEntry saved = mealEntryRepository.save(meal);
        log.info("Created meal entry for client: {}", client.getEmail());
        return saved;
    }

    @Transactional
    public FoodItem addFoodItem(Long mealId, MealDto.AddFoodItemRequest request) {
        MealEntry meal = mealEntryRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal", mealId));

        FoodItem foodItem = FoodItem.builder()
                .mealEntry(meal)
                .productName(request.getProductName())
                .weightGrams(request.getWeightGrams())
                .calories(request.getCalories())
                .proteinG(request.getProteinG())
                .fatG(request.getFatG())
                .carbsG(request.getCarbsG())
                .source(FoodItem.FoodSource.MANUAL)
                .build();

        FoodItem saved = foodItemRepository.save(foodItem);
        log.info("Added food item '{}' to meal {}", request.getProductName(), mealId);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<MealEntry> getClientMeals(Long clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", clientId));
        return mealEntryRepository.findByClientOrderByEatenAtDesc(client);
    }

    @Transactional(readOnly = true)
    public MealDto.MealResponse getMealWithFoodItems(Long mealId) {
        MealEntry meal = mealEntryRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal", mealId));

        List<FoodItem> foodItems = foodItemRepository.findByMealEntry(meal);

        int totalCalories = foodItems.stream()
                .mapToInt(FoodItem::getCalories)
                .sum();

        return MealDto.MealResponse.builder()
                .id(meal.getId())
                .clientId(meal.getClient().getId())
                .clientName(meal.getClient().getName())
                .mealType(meal.getMealType())
                .eatenAt(meal.getEatenAt())
                .photoUrl(meal.getPhotoUrl())
                .analysisStatus(meal.getAnalysisStatus())
                .foodItems(foodItems.stream().map(this::toFoodItemResponse).collect(Collectors.toList()))
                .totalCalories(totalCalories)
                .build();
    }

    private MealDto.FoodItemResponse toFoodItemResponse(FoodItem item) {
        return MealDto.FoodItemResponse.builder()
                .id(item.getId())
                .productName(item.getProductName())
                .weightGrams(item.getWeightGrams())
                .calories(item.getCalories())
                .proteinG(item.getProteinG())
                .fatG(item.getFatG())
                .carbsG(item.getCarbsG())
                .source(item.getSource().name())
                .build();
    }
}