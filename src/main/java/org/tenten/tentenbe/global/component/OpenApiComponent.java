package org.tenten.tentenbe.global.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.tenten.tentenbe.domain.admin.exception.AdminException;
import org.tenten.tentenbe.domain.region.dto.response.RegionResponse.RegionInfo;
import org.tenten.tentenbe.domain.tour.model.TourItem;
import org.tenten.tentenbe.global.component.dto.response.AreaOpenApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.CONFLICT;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenApiComponent {
    @Value("${open-api.url}")
    private String apiUrl;
    @Value("${open-api.key}")
    private String apiKey;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private static final String AREA = "/areaCode1";
    private static final String INFO = "/areaBasedList1";


    public List<RegionInfo> getSubRegion(String areaCode) {
        UriComponents uri = UriComponentsBuilder
            .fromUriString(apiUrl + AREA)
            .queryParam("serviceKey", apiKey)
            .queryParam("numOfRows", "100")
            .queryParam("pageNo", "1")
            .queryParam("_type", "json")
            .queryParam("MobileOS", "ETC")
            .queryParam("MobileApp", "TestApp")
            .queryParam("areaCode", areaCode)
            .build();
        log.info(uri.toUriString());
        ResponseEntity<AreaOpenApiResponse> apiResponseEntity =
            restTemplate.getForEntity(uri.toUriString(), AreaOpenApiResponse.class);

        AreaOpenApiResponse apiResponse = apiResponseEntity.getBody();
        return apiResponse.getResponse().getBody().getItems().getItem().stream()
            .map(areaResponse -> new RegionInfo(
                Long.parseLong(areaCode),
                Long.parseLong(areaResponse.getCode()),
                areaResponse.getName())
            ).toList();
    }


    public List<TourItem> getTourItems(Long contentTypeId, Long page, Long size) {
        UriComponents uriComponents = UriComponentsBuilder
            .fromUriString(apiUrl + INFO)
            .queryParam("serviceKey", apiKey)
            .queryParam("_type", "json")
            .queryParam("MobileOS", "ETC")
            .queryParam("MobileApp", "TestApp")
            .queryParam("listYN", "Y")
            .queryParam("contentTypeId", Long.toString(contentTypeId))
            .queryParam("numOfRows", Long.toString(size))
            .queryParam("pageNo", Long.toString(page))
            .build();
        HttpHeaders header = new HttpHeaders();
        HttpEntity request = new HttpEntity(header);
        ResponseEntity<String> responseEntity = restTemplate.exchange(uriComponents.toUriString(), GET, request, String.class);
        try {
            Map<String, Object> objectMap = objectMapper.readValue(responseEntity.getBody(), new TypeReference<Map<String, Object>>() {
            });
            Map<String, Object> responseMap = (Map<String, Object>) objectMap.get("response");
            Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
            Map<String, Object> itemMap = (Map<String, Object>) bodyMap.get("items");
            List<Map<String, String>> items = (List<Map<String, String>>) itemMap.get("item");
            List<TourItem> tourItems = new ArrayList<>();
            for (Map<String, String> item : items) {
                Long areaCode = null;
                Long subAreaCode = null;
                if (!(item.get("areaCode") == null) && !item.get("areaCode").isEmpty()) {
                    areaCode = Long.parseLong(item.get("areaCode"));
                }
                if (!(item.get("sigunguCode") == null) && !item.get("sigunguCode").isEmpty()) {
                    subAreaCode = Long.parseLong(item.get("sigunguCode"));
                }
                tourItems.add(
                    TourItem.builder()
                        .address(item.get("addr1"))
                        .detailedAddress(item.get("addr2"))
                        .tel(item.get("tel"))
                        .title(item.get("title"))
                        .contentId(Long.parseLong(item.get("contentId")))
                        .contentTypeId(Long.parseLong(item.get("contentTypeId")))
                        .areaCode(areaCode)
                        .subAreaCode(subAreaCode)
                        .latitude(item.get("mapy"))
                        .longitude(item.get("mapx"))
                        .zipcode(item.get("zipcode"))
                        .originalThumbnailUrl(item.get("firstimage"))
                        .smallThumbnailUrl(item.get("firstimage2"))
                        .likedTotalCount(0L)
                        .reviewTotalCount(0L)
                        .build()
                );
            }

            return tourItems;
        } catch (JsonProcessingException jsonProcessingException) {
            log.info("open api 응답을 파싱하는 과정에서 오류가 발생했습니다.");
            throw new AdminException(jsonProcessingException.getMessage(), CONFLICT);
        } catch (Exception e) {
            throw new AdminException(e.getMessage(), CONFLICT);
        }
    }
}
