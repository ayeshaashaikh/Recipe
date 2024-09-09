package com.ayesha.socialsite.repository;

import com.ayesha.socialsite.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
