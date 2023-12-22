package org.tenten.tentenbe.domain.tour.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiItemDetail {
    String serialNumber;
    String infoName;
    String infoText;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiItemDetail that = (ApiItemDetail) o;
        return Objects.equals(serialNumber, that.serialNumber) && Objects.equals(infoName, that.infoName) && Objects.equals(infoText, that.infoText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber, infoName, infoText);
    }
}
