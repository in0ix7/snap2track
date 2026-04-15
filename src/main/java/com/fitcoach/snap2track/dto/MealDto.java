package com.fitcoach.snap2track.dto;

import com.fitcoach.snap2track.entity.MealEntry;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class MealDto {

    private MealDto() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateMealRequest {
        @NotNull
        private Long clientId;

        @NotNull
        private MealEntry.MealType mealType;

        @NotNull
        private LocalDateTime eatenAt;

        private String photoUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddFoodItemRequest {
        @NotNull
        private String productName;

        @NotNull
        private BigDecimal weightGrams;

        @NotNull
        private Integer calories;

        private BigDecimal proteinG;
        private BigDecimal fatG;
        private BigDecimal carbsG;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealResponse {
        private Long id;
        private Long clientId;
        private String clientEmail;  // НОВОЕ ПОЛЕ
        private String clientName;
        private MealEntry.MealType mealType;
        private LocalDateTime eatenAt;
        private String photoUrl;
        private MealEntry.AnalysisStatus analysisStatus;
        private List<FoodItemResponse> foodItems;
        private Integer totalCalories;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FoodItemResponse {
        private Long id;
        private String productName;
        private BigDecimal weightGrams;
        private Integer calories;
        private BigDecimal proteinG;
        private BigDecimal fatG;
        private BigDecimal carbsG;
        private String source;
    }
}