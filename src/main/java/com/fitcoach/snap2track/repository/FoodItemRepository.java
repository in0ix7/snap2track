package com.fitcoach.snap2track.repository;

import com.fitcoach.snap2track.entity.FoodItem;
import com.fitcoach.snap2track.entity.MealEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    List<FoodItem> findByMealEntry(MealEntry mealEntry);
}