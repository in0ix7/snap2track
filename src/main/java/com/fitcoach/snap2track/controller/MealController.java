package com.fitcoach.snap2track.controller;

import com.fitcoach.snap2track.dto.MealDto;
import com.fitcoach.snap2track.entity.FoodItem;
import com.fitcoach.snap2track.entity.MealEntry;
import com.fitcoach.snap2track.entity.User;
import com.fitcoach.snap2track.exception.ResourceNotFoundException;
import com.fitcoach.snap2track.repository.UserRepository;
import com.fitcoach.snap2track.service.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;
    private final UserRepository userRepository;

    // Константа для сообщения об ошибке
    private static final String USER_NOT_FOUND = "User not found";

    @PostMapping
    public ResponseEntity<MealEntry> createMeal(@Valid @RequestBody MealDto.CreateMealRequest request,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", userDetails.getUsername()));

        if (currentUser.getRole() == User.Role.CLIENT && !currentUser.getId().equals(request.getClientId())) {
            throw new AccessDeniedException("CLIENT can only create meals for themselves");
        }

        MealEntry meal = mealService.createMeal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(meal);
    }

    @PostMapping("/{mealId}/food-items")
    public ResponseEntity<FoodItem> addFoodItem(@PathVariable Long mealId,
                                                @Valid @RequestBody MealDto.AddFoodItemRequest request,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        MealDto.MealResponse meal = mealService.getMealWithFoodItems(mealId);

        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", userDetails.getUsername()));

        if (currentUser.getRole() == User.Role.CLIENT && !meal.getClientEmail().equals(currentUser.getEmail())) {
            throw new AccessDeniedException("You can only add food items to your own meals");
        }

        FoodItem foodItem = mealService.addFoodItem(mealId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(foodItem);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<MealEntry>> getClientMeals(@PathVariable Long clientId,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", userDetails.getUsername()));

        if (currentUser.getRole() == User.Role.CLIENT && !currentUser.getId().equals(clientId)) {
            throw new AccessDeniedException("CLIENT can only see their own meals");
        }

        List<MealEntry> meals = mealService.getClientMeals(clientId);
        return ResponseEntity.ok(meals);
    }

    @GetMapping("/{mealId}")
    public ResponseEntity<MealDto.MealResponse> getMealWithFoodItems(@PathVariable Long mealId,
                                                                     @AuthenticationPrincipal UserDetails userDetails) {
        MealDto.MealResponse meal = mealService.getMealWithFoodItems(mealId);

        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", userDetails.getUsername()));

        if (currentUser.getRole() == User.Role.CLIENT && !meal.getClientEmail().equals(currentUser.getEmail())) {
            throw new AccessDeniedException("You don't have permission to access this resource");
        }

        return ResponseEntity.ok(meal);
    }
}