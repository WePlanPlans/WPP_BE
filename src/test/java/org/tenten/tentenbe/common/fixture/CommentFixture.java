package org.tenten.tentenbe.common.fixture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.tenten.tentenbe.domain.comment.dto.request.CommentCreateRequest;
import org.tenten.tentenbe.domain.comment.dto.request.CommentUpdateRequest;
import org.tenten.tentenbe.domain.comment.dto.response.CommentInfo;
import org.tenten.tentenbe.domain.comment.dto.response.CommentResponse;
import org.tenten.tentenbe.domain.comment.model.Comment;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.review.dto.response.KeywordResponse;
import org.tenten.tentenbe.domain.review.model.Review;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.tenten.tentenbe.common.fixture.AuthFixture.newBasicMember;
import static org.tenten.tentenbe.common.fixture.AuthFixture.updateMember;
import static org.tenten.tentenbe.common.fixture.ReviewFixture.review;

public class CommentFixture {


    public static CommentInfo commentInfo(){
        return new CommentInfo(1L, "comment Nick name", "profile image url",
                "comment test content", LocalDateTime.now(), true);
    }

    public static CommentResponse commentResponse(){
        Pageable pageable = PageRequest.of(0, 10);
        List<CommentInfo> commentInfos = List.of(commentInfo());
        Page<CommentInfo> commentInfoPage = new PageImpl<>(commentInfos, pageable, commentInfos.size());
        return new CommentResponse(commentInfoPage);
    }

   public static CommentCreateRequest commentCreateRequest (){
        return new CommentCreateRequest("success create comment", 1L);
    }

    public static CommentUpdateRequest commentUpdateRequest(){
        return new CommentUpdateRequest("update comment");
    }

    public static Comment comment(){
        return new Comment("comment content", review(), updateMember());
    }

    public static Comment saveComment(String content, Review review, Member member){
        return new Comment(content, review, member);
    }

}
