package com.ayesha.socialsite.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private  String username;
    private String password;
    private  String email;

  @ManyToMany
  @JoinTable(
          name = "user_recipes",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "recipe_id")
  )
  private Set<Recipe> recipes;

}
