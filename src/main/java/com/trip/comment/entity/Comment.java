package com.trip.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private int commentId;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(name = "attraction_id", nullable = false)
    private int attractionId;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static Comment create(Long authorId, int attractionId, String content) {
        Comment comment = new Comment();
        comment.authorId = authorId;
        comment.attractionId = attractionId;
        comment.content = content;
        comment.createdAt = LocalDateTime.now();
        return comment;
    }
}