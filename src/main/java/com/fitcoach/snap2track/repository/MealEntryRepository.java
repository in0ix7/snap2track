package com.fitcoach.snap2track.repository;

import com.fitcoach.snap2track.entity.MealEntry;
import com.fitcoach.snap2track.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MealEntryRepository extends JpaRepository<MealEntry, Long> {
    List<MealEntry> findByClientOrderByEatenAtDesc(User client);
    List<MealEntry> findByClientAndEatenAtBetween(User client, LocalDateTime start, LocalDateTime end);
}