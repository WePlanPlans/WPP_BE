package org.tenten.tentenbe.domain.trip.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.tenten.tentenbe.domain.trip.dto.request.TripInfoUpdateRequest;
import org.tenten.tentenbe.domain.trip.dto.response.TripInfoUpdateResponse;
import org.tenten.tentenbe.global.common.BaseTimeEntity;
import org.tenten.tentenbe.global.converter.MapConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = JOINED)
public class Trip extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "tripId")
    private Long id;
    private Long numberOfPeople; // 인원수
    private LocalDate startDate;
    private LocalDate endDate;
    private String area;
    private String subarea;
    private Boolean isDeleted;
    private String tripName;
    private Long budget;
    @ColumnDefault("0")
    private Long tripItemPriceSum;
    @ColumnDefault("0")
    private Long transportationPriceSum;
    @Convert(converter = MapConverter.class)
    @Column(columnDefinition = "JSON")
    private Map<String, Integer> tripPathPriceMap;
    @Convert(converter = MapConverter.class)
    @Column(columnDefinition = "JSON")
    private Map<String, String> tripTransportationMap;

    @OneToMany(mappedBy = "trip", fetch = LAZY, cascade = REMOVE)
    private final List<TripMember> tripMembers = new ArrayList<>();

    @OneToMany(mappedBy = "trip", fetch = LAZY, cascade = REMOVE)
    private final List<TripItem> tripItems = new ArrayList<>();

    @OneToMany(mappedBy = "trip", fetch = LAZY, cascade = REMOVE)
    private final List<TripLikedItem> tripLikedItems = new ArrayList<>();

    public TripInfoUpdateResponse updateTripInfo(TripInfoUpdateRequest request) {
        this.tripName = request.tripName();
        this.numberOfPeople = request.numberOfPeople();
        this.startDate = request.startDate();
        this.endDate = request.endDate();
        this.area = request.area();
        this.subarea = request.subarea();
        return new TripInfoUpdateResponse(
            request.tripName(),
            request.numberOfPeople(),
            request.startDate(),
            request.endDate(),
            request.area(),
            request.subarea()
        );
    }

}
