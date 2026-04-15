package com.fitcoach.snap2track.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "food_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meal_entry_id", nullable = false)
    private MealEntry mealEntry;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer calories;

    @Column(name = "weight_grams", nullable = false, precision = 8, scale = 2)
    private BigDecimal weightGrams;

    @Column(name = "protein_g", precision = 8, scale = 2)
    private BigDecimal proteinG;

    @Column(name = "fat_g", precision = 8, scale = 2)
    private BigDecimal fatG;

    @Column(name = "carbs_g", precision = 8, scale = 2)
    private BigDecimal carbsG;
    @Column(name = "source", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private FoodSource source = FoodSource.MANUAL;

    public enum FoodSource {
        MANUAL, AI_DETECTED, TRAINER_CORRECTED
    }
}