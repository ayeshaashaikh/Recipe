package com.ayesha.socialsite.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private int votes;
    private String description;
    private String imgName;
    private String videoUrl;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}