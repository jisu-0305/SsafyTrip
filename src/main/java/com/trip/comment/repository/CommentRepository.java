package com.trip.comment.repository;

import com.trip.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    boolean existsBycommentIdAndAuthorId(int commentId, long authorId);
}
