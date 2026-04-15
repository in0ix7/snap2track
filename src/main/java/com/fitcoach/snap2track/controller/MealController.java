package com.fitcoach.snap2track.controller;

import com.fitcoach.snap2track.dto.MealDto;
import com.fitcoach.snap2track.entity.FoodItem;
import com.fitcoach.snap2track.entity.MealEntry;
import com.fitcoach.snap2track.service.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @PostMapping
    public ResponseEntity<MealEntry> createMeal(@Valid @RequestBody MealDto.CreateMealRequest request) {
        MealEntry meal = mealService.createMeal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(meal);
    }

    @PostMapping("/{mealId}/food-items")
    public ResponseEntity<FoodItem> addFoodItem(
            @PathVariable Long mealId,
            @Valid @RequestBody MealDto.AddFoodItemRequest request) {
        FoodItem foodItem = mealService.addFoodItem(mealId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(foodItem);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<MealEntry>> getClientMeals(@PathVariable Long clientId) {
        List<MealEntry> meals = mealService.getClientMeals(clientId);
        return ResponseEntity.ok(meals);
    }

    @GetMapping("/{mealId}")
    public ResponseEntity<MealDto.MealResponse> getMealWithFoodItems(@PathVariable Long mealId) {
        MealDto.MealResponse meal = mealService.getMealWithFoodItems(mealId);
        return ResponseEntity.ok(meal);
    }
}