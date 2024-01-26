package org.tenten.tentenbe.domain.member.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tenten.tentenbe.global.converter.JsonConverter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Survey {
    @Schema(description = "계획성", defaultValue = "철저하게")
    private String planning;
    @Schema(description = "활동성", defaultValue = "아침형")
    private String activeHours;
    @Schema(description = "숙소", defaultValue = "분위기")
    private String accommodation;
    @Schema(description = "음식", defaultValue = "노포 맛집")
    private String food;
    @Schema(description = "여행 스타일", defaultValue = "액티비티")
    private String tripStyle;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Survey survey = (Survey) o;
        return Objects.equals(planning, survey.planning) && Objects.equals(activeHours, survey.activeHours) &&
            Objects.equals(accommodation, survey.accommodation) &&
            Objects.equals(food, survey.food) && Objects.equals(tripStyle, survey.tripStyle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planning, activeHours, accommodation, food, tripStyle);
    }

    public static class SurveyConverter extends JsonConverter<Survey> {}
}
