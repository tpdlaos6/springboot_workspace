package com.exam.insu.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    private String id;
    private String title;
    private String content;
}
