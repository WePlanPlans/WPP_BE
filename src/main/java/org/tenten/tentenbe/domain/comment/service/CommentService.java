package org.tenten.tentenbe.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.comment.dto.request.CommentCreateRequest;
import org.tenten.tentenbe.domain.comment.dto.request.CommentUpdateRequest;
import org.tenten.tentenbe.domain.comment.dto.response.CommentResponse;
import org.tenten.tentenbe.domain.comment.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    @Transactional
    public CommentResponse createComment(CommentCreateRequest commentCreateRequest) {
        return null;
    }
    @Transactional
    public CommentResponse updateComment(Long memberId, Long commentId, CommentUpdateRequest commentUpdateRequest) {
        return null;
    }
    @Transactional
    public void deleteComment(Long memberId, Long commentId) {

    }
}
