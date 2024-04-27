package ce.dankook.lab3;

import ce.dankook.lab3.EarthquakeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class EarthquakeService {

    private final WebClient webClient;

    @Autowired
    public EarthquakeService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://earthquake.usgs.gov").build();
    }

    /**
     * 지진 데이터를 조회합니다.
     *
     * @param year 조회할 연도
     * @param minMagnitude 조회할 최소 규모
     * @param minLatitude 조회할 최소 위도 - 추가됨
     * @param maxLatitude 조회할 최대 위도 - 추가됨
     * @param minLongitude 조회할 최소 경도 - 추가됨
     * @param maxLongitude 조회할 최대 경도 - 추가됨
     * @param orderBy 정렬 기준 (예: time, magnitude) - 추가됨
     * @return 조회된 지진 데이터 목록 (Flux<EarthquakeDto>) - 반환 타입 수정
     */
    public Flux<EarthquakeDto> getEarthquakes(int year, double minMagnitude,
                                              double minLatitude, double maxLatitude,
                                              double minLongitude, double maxLongitude,
                                              String orderBy) {
        String uriTemplate = String.format(
                "/fdsnws/event/1/query?format=geojson&starttime=%d-01-01&endtime=%d-12-31" +
                        "&minmagnitude=%s" +
                        "&minlatitude=%s&maxlatitude=%s" +
                        "&minlongitude=%s&maxlongitude=%s" +
                        "&orderby=%s",
                year, year, minMagnitude, minLatitude, maxLatitude, minLongitude, maxLongitude, orderBy);

        return webClient.get()
                .uri(uriTemplate)
                .retrieve()
                .bodyToMono(String.class)
                .flatMapMany(body -> Flux.fromIterable(EarthquakeDto.parseEarthquakeData(body)));
    }
}
