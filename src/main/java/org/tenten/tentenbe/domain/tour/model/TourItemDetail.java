package org.tenten.tentenbe.domain.tour.model;

import jakarta.persistence.*;
import lombok.*;
import org.tenten.tentenbe.domain.tour.model.TourItemDetail.ItemDetail.ItemDetailConverter;
import org.tenten.tentenbe.domain.tour.model.TourItemDetail.RoomOption.RoomOptionConverter;
import org.tenten.tentenbe.global.converter.JsonConverter;

import java.util.List;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourItemDetail {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "tourItemDetailId")
    private Long id;

    @Convert(converter = RoomOptionConverter.class)
    @Column(columnDefinition = "JSON")
    private RoomOption roomOption;

    @Convert(converter = ItemDetailConverter.class)
    @Column(columnDefinition = "JSON")
    private ItemDetail itemDetail;

    @OneToOne
    @JoinColumn(name = "tourItemId")
    private TourItem tourItem;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoomOption {
        List<ApiRoomOption> roomOptions;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RoomOption that = (RoomOption) o;
            return Objects.equals(roomOptions, that.roomOptions);
        }

        @Override
        public int hashCode() {
            return Objects.hash(roomOptions);
        }

        public static class RoomOptionConverter extends JsonConverter<RoomOption> {}
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDetail {
        List<ApiItemDetail> itemDetails;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ItemDetail that = (ItemDetail) o;
            return Objects.equals(itemDetails, that.itemDetails);
        }

        @Override
        public int hashCode() {
            return Objects.hash(itemDetails);
        }

        public static class ItemDetailConverter extends JsonConverter<ItemDetail> {}
    }

}
