DROP TABLE if EXISTS test;

DROP TABLE IF EXISTS Comment cascade;

DROP TABLE IF EXISTS LikedItem cascade;

DROP TABLE IF EXISTS ReviewKeyword cascade;

DROP TABLE IF EXISTS Keyword cascade;

DROP TABLE IF EXISTS Review cascade;

DROP TABLE IF EXISTS TourItemDetail cascade;

DROP TABLE IF EXISTS TourItemImage cascade;

DROP TABLE IF EXISTS TripItem cascade;

DROP TABLE IF EXISTS TourItem cascade;

DROP TABLE IF EXISTS TripMember cascade;

DROP TABLE IF EXISTS Trip cascade;

DROP TABLE IF EXISTS Member cascade;



create table Keyword
(
    keywordId bigint auto_increment
        primary key,
    content   varchar(255)                                                           null,
    type      enum ('DINING_KEYWORD', 'ACCOMMODATION_KEYWORD', 'ATTRACTION_KEYWORD') null
);

create table Member
(
    createdTime     datetime(6)             null,
    memberId        bigint auto_increment
        primary key,
    modifiedTime    datetime(6)             null,
    email           varchar(255)            null,
    name            varchar(255)            null,
    nickname        varchar(255)            null,
    password        varchar(255)            null,
    profileImageUrl varchar(255)            null,
    survey          json                    null,
    loginType       enum ('EMAIL', 'KAKAO') null,
    userAuthority   enum ('ROLE_USER')      null,
    ageType         enum ('TEENAGER', 'TWENTIES', 'THIRTIES', 'FOURTIES', 'ABOVE_FIFTIES', 'DEFAULT') null,
    genderType      enum ('MALE', 'FEMALE', 'NON_BINARY', 'DEFAULT')      null
);


create table TourItem
(
    areaCode             bigint       null,
    contentId            bigint       null,
    contentTypeId        bigint       null,
    subAreaCode          bigint       null,
    tourItemId           bigint auto_increment
        primary key,
    address              varchar(255) null,
    detailedAddress      varchar(255) null,
    longitude            varchar(255) null,
    latitude             varchar(255) null,
    originalThumbnailUrl varchar(255) null,
    smallThumbnailUrl    varchar(255) null,
    tel                  varchar(255) null,
    title                varchar(255) null,
    zipcode              varchar(255) null
);

create table LikedItem
(
    likedItemId bigint auto_increment
        primary key,
    memberId    bigint null,
    tourItemId  bigint null,
    constraint FKhlcf4xrx53ft7qcvh0k20xmxq
        foreign key (tourItemId) references TourItem (tourItemId),
    constraint FKtfahd69vv3e4wj1uhkpxs3vlq
        foreign key (memberId) references Member (memberId)
);

create table Review
(
    createdTime  datetime(6) null,
    memberId     bigint      null,
    modifiedTime datetime(6) null,
    rating       bigint      null,
    reviewId     bigint auto_increment
        primary key,
    tourItemId   bigint      null,
    content      text        null,
    constraint FK3ip6vbitby5kgkos0sfo172r0
        foreign key (tourItemId) references TourItem (tourItemId),
    constraint FKrlpd519txx779gau0g12498jg
        foreign key (memberId) references Member (memberId)
);

create table Comment
(
    commentId    bigint auto_increment
        primary key,
    createdTime  datetime(6) null,
    memberId     bigint      null,
    modifiedTime datetime(6) null,
    reviewId     bigint      null,
    content      text        null,
    constraint FK4uunsyafmesugo6g82lsvnjte
        foreign key (reviewId) references Review (reviewId),
    constraint FKt1hm8idyj3hvvb76u4gb1owhu
        foreign key (memberId) references Member (memberId)
);

create table ReviewKeyword
(
    keywordId   bigint null,
    reviewId    bigint null,
    reviewKeywordId bigint auto_increment
        primary key,
    constraint FKa4v4wydktt3nkb8k3yq2fj9d2
        foreign key (keywordId) references Keyword (keywordId),
    constraint FKip2fmgieccwcqg5lw59ds0l80
        foreign key (reviewId) references Review (reviewId)
);

create table TourItemDetail
(
    tourItemDetailId bigint auto_increment
        primary key,
    tourItemId       bigint null,
    itemDetail       json   null,
    roomOption       json   null,
    constraint UK_kuc31jv07x3i0yto2lae2molf
        unique (tourItemId),
    constraint FKqooyir77i8mjvvcb1idt8m7t2
        foreign key (tourItemId) references TourItem (tourItemId)
);

create table TourItemImage
(
    tourItemId      bigint null,
    tourItemImageId bigint auto_increment
        primary key,
    itemImage       json   null,
    constraint UK_emcjjwfo22jbixav9gwjtgsp6
        unique (tourItemId),
    constraint FK49pgfofh6aqykphqvjics3547
        foreign key (tourItemId) references TourItem (tourItemId)
);

create table Trip
(
    endDate        date         null,
    startDate      date         null,
    createdTime    datetime(6)  null,
    modifiedTime   datetime(6)  null,
    numberOfPeople bigint       null,
    tripId         bigint auto_increment
        primary key,
    departure      varchar(255)                    null,
    destination      varchar(255)                    null,
    tripName       varchar(255)                    null,
    isDeleted      boolean                         null,
    tripStatus     enum ('BEFORE', 'AFTER', 'ING') null,
    area           varchar(255)                    null,
    subarea        varchar(255)                    null,
    budget         bigint                          null
);

create table TripItem
(
    visitDate      date                                  null,
    createdTime    datetime(6)                           null,
    memberId       bigint                                null,
    modifiedTime   datetime(6)                           null,
    price          bigint                                null,
    seqNum         bigint                                null,
    tourItemId     bigint                                null,
    tripId         bigint                                null,
    tripItemId     bigint auto_increment
        primary key,
    transportation enum ('CAR', 'PUBLIC_TRANSPORTATION') null,
    constraint FK2usvl9nqv9wyhyxo6n4wt215k
        foreign key (tourItemId) references TourItem (tourItemId),
    constraint FKjhahhv3w4r4ijrcdsyhbwsdos
        foreign key (memberId) references Member (memberId),
    constraint FKs1xb61ylvu77xldywtdj9bnxs
        foreign key (tripId) references Trip (tripId)
);

create table TripMember
(
    createdTime   datetime(6)                 null,
    memberId      bigint                      null,
    modifiedTime  datetime(6)                 null,
    tripId        bigint                      null,
    tripMemberId  bigint auto_increment
        primary key,
    tripAuthority enum ('READ_ONLY', 'WRITE') null,
    constraint FKl30rei713mxy9yt35fscmp2ov
        foreign key (tripId) references Trip (tripId),
    constraint FKqvcr8k1koo12vdoirnakirv13
        foreign key (memberId) references Member (memberId)
);

create table TripLikedItem
(
    tripLikedItemId bigint not null
        primary key,
    tripId          bigint null,
    tourItemId      bigint null,
    constraint FK_TRIPLIKEDITEM_ON_TOURITEMID
        foreign key (tourItemId) references TourItem (tourItemId),
    constraint FK_TRIPLIKEDITEM_ON_TRIPID
        foreign key (tripId) references Trip (tripId)
);

create table TripLikedItemPreference
(
    tripLikedItemPreferenceId bigint not null
        primary key,
    liked                     bit    null,
    disliked                  bit    null,
    tripMemberId              bigint null,
    tripLikedItemId           bigint null,
    constraint FK_TRIPLIKEDITEMPREFERENCE_ON_TRIPLIKEDITEMID
        foreign key (tripLikedItemId) references TripLikedItem (tripLikedItemId),
    constraint FK_TRIPLIKEDITEMPREFERENCE_ON_TRIPMEMBERID
        foreign key (tripMemberId) references TripMember (tripMemberId)
);



