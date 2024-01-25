package org.tenten.tentenbe.common.fixture;

import org.tenten.tentenbe.domain.liked.model.LikedItem;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.model.Survey;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.tour.model.TourItem;

import static org.tenten.tentenbe.common.fixture.AuthFixture.*;

public class MemberFixture {

    public static TourItem tourItem(){
        return TourItem.builder()
                .id(1L)
                .contentId(1L)
                .contentTypeId(1L)
                .areaCode(1L)
                .subAreaCode(1L)
                .address("test address")
                .detailedAddress("test detail address")
                .originalThumbnailUrl("origin thumnail url")
                .smallThumbnailUrl("small thumnail url")
                .title("test title")
                .zipcode("test zip code")
                .tel("test telephone")
                .longitude("longitude")
                .latitude("latitude")
                .reviewTotalCount(10L)
                .likedTotalCount(20L)
                .build();
    }

    public static TourItem tourItemSecond(){
        return TourItem.builder()
                .id(1L)
                .contentId(1L)
                .contentTypeId(12L)
                .areaCode(1L)
                .subAreaCode(1L)
                .address("search address")
                .detailedAddress("search address")
                .originalThumbnailUrl("origin thumnail url")
                .smallThumbnailUrl("small thumnail url")
                .title("test title")
                .zipcode("test zip code")
                .tel("test telephone")
                .longitude("longitude")
                .latitude("latitude")
                .reviewTotalCount(10L)
                .likedTotalCount(20L)
                .build();
    }


    public static LikedItem likedItem(){
        return LikedItem.builder()
                .id(1L)
                .member(newMember())
                .tourItem(tourItem())
                .build();
    }

    public static LikedItem likedSaveItem(Member member, TourItem tourItem){
        return LikedItem.builder()
                .member(member)
                .tourItem(tourItem)
                .build();
    }


    public static LikedItem serviceLikedItem(){
        return LikedItem.builder()
                .id(1L)
                .member(newBasicMember())
                .tourItem(tourItem())
                .build();
    }


    public static Survey survey(){
        return new Survey("계획성", "활동성", "숙소", "음식", "여행 스타일");
    }

    public static Survey updateSurvey(){
        return new Survey("철저하게", "아침형", "분위기", "음식", "액티비티");
    }




}
