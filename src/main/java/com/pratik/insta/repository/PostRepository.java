package com.pratik.insta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pratik.insta.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

	@Query("select p from Post p where p.user.id=?1")
	List<Post> findByUserId(int userId);

	@Query("select p from Post p where p.user.id IN :users ORDER BY p.createdAt DESC")
	List<Post> findAllPostsByUserIds(@Param("users") List<Integer> userIds);

}
