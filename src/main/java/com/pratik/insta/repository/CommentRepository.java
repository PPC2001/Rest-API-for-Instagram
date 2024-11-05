package com.pratik.insta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pratik.insta.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
