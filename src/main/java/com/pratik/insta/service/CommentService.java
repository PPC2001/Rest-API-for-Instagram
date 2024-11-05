package com.pratik.insta.service;

import com.pratik.insta.exception.CommentException;
import com.pratik.insta.exception.PostException;
import com.pratik.insta.exception.UserException;
import com.pratik.insta.model.Comment;

public interface CommentService {

	Comment createComment(Comment comment, Integer postId, Integer userId) throws UserException, PostException;

	Comment findCommentById(int commentId) throws CommentException;

	Comment likeComment(int commentId, int userId) throws CommentException, UserException;

	Comment unlikeComment(int commentId, int userId) throws CommentException, UserException;

}
