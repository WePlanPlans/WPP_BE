package org.tenten.tentenbe.domain.tour.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiItemImage {
    String imageName;
    String originalImageUrl;
    String smallImageUrl;
    String serialNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiItemImage that = (ApiItemImage) o;
        return Objects.equals(imageName, that.imageName)
            && Objects.equals(originalImageUrl, that.originalImageUrl)
            && Objects.equals(smallImageUrl, that.smallImageUrl)
            && Objects.equals(serialNumber, that.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageName, originalImageUrl, smallImageUrl, serialNumber);
    }
}
