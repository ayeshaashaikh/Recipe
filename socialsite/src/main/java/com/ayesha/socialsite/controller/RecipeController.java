package com.ayesha.socialsite.controller;

import com.ayesha.socialsite.model.Recipe;
import com.ayesha.socialsite.model.User;
import com.ayesha.socialsite.repository.RecipeRepository;
import com.ayesha.socialsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RecipeController {
    private static String uploadDir = "C:/Users/Acer/Desktop/Java Projects/socialsite/src/main/resources/static/uploads";
    @Autowired
    UserRepository userRepository;
    @Autowired
    RecipeRepository recipeRepository;

    @GetMapping("/submit-recipe")
    public String showRecipeForm(Model model){
        model.addAttribute("recipe", new Recipe());
        return "submit-recipe";
    }

//    @PostMapping("/submit-recipe")
//    public String submitRecipe(@ModelAttribute("recipe") Recipe recipe){
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findByUsername(username);
//        recipe.setUser(user);
//        recipeRepository.save(recipe);
//        return "redirect:/recipes";
//    }

@PostMapping("/submit-recipe")
public String submitRecipe(@RequestParam("title") String title,
                           @RequestParam("description") String description,
                           @RequestParam("recipeImage") MultipartFile file,
                           @RequestParam(value = "videoUrl", required = false) String videoUrl,
                           RedirectAttributes redirectAttributes) throws IOException {

    // Get current authenticated user
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findByUsername(username);

    // Save the file if it's not empty
    String imageUUID;
    if (!file.isEmpty()) {
        imageUUID = file.getOriginalFilename();
        Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
        Files.write(fileNameAndPath, file.getBytes());
    } else {
        imageUUID = "default-image.jpg"; // Provide a default image name if no file is uploaded
    }

    // Create and save the recipe
    Recipe recipe = new Recipe();
    recipe.setTitle(title);
    recipe.setDescription(description);
    recipe.setImgName(imageUUID);
    recipe.setVideoUrl(videoUrl);
    recipe.setUser(user); // Associate the recipe with the current user
    recipeRepository.save(recipe);

    redirectAttributes.addFlashAttribute("message", "Recipe submitted successfully!");
    return "redirect:/recipes";
}




    @GetMapping("/recipes")
    public String showRecipes(Model model){
        model.addAttribute("recipes", recipeRepository.findAll());
        return "recipes";
    }

    @PostMapping("/vote")
    public String voteForRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();
        recipe.setVotes(recipe.getVotes() + 1);
        recipeRepository.save(recipe);
        return "redirect:/recipes";
    }


    @GetMapping("/leaderboard")
    public String showLeaderboard(Model model){
        List<Recipe> topRecipes = recipeRepository.findAll()
                .stream()
                .sorted((r1, r2) -> r2.getVotes() - r1.getVotes())
                .limit(10)
                .collect(Collectors.toList());
        model.addAttribute("recipes", topRecipes);
        return "leaderboard";
    }
}