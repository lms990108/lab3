package ce.dankook.lab3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

// RestController : api 제공
@RestController
public class EarthquakeController {
    private final EarthquakeService earthquakeService;

    // EarthquakeService의 의존성 주입
    @Autowired
    public EarthquakeController(EarthquakeService earthquakeService) {
        this.earthquakeService = earthquakeService;
    }

    // 지진 정보를 조회하는 API 엔드포인트 정의 (위도, 경도 추가)
    @GetMapping("/api/earthquakes")
    public Flux<EarthquakeDto> getEarthquakes(
            @RequestParam(name = "year", required = false, defaultValue = "2024") int year,
            @RequestParam(name = "minMagnitude", required = false, defaultValue = "7.0") double minMagnitude,
            @RequestParam(name = "minLatitude", required = false) Double minLatitude, // 위도 필수 입력 아님 (null 허용)
            @RequestParam(name = "maxLatitude", required = false) Double maxLatitude, // 위도 필수 입력 아님 (null 허용)
            @RequestParam(name = "minLongitude", required = false) Double minLongitude, // 경도 필수 입력 아님 (null 허용)
            @RequestParam(name = "maxLongitude", required = false) Double maxLongitude, // 경도 필수 입력 아님 (null 허용)
            @RequestParam(name = "orderBy", required = false, defaultValue = "time") String orderBy) {

        // 위도, 경도 정보가 null 일 경우 기본값 설정 (서비스 계층 로직에서 처리해도 됨)
        double defaultMinLatitude = 5.0;
        double defaultMaxLatitude = 90.0;
        double defaultMinLongitude = -180.0;
        double defaultMaxLongitude = 180.0;

        // minLatitude, maxLatitude, minLongitude, maxLongitude 값이 null이면 기본값 사용
        minLatitude = minLatitude != null ? minLatitude : defaultMinLatitude;
        maxLatitude = maxLatitude != null ? maxLatitude : defaultMaxLatitude;
        minLongitude = minLongitude != null ? minLongitude : defaultMinLongitude;
        maxLongitude = maxLongitude != null ? maxLongitude : defaultMaxLongitude;

        // EarthquakeService를 통해 지진 정보 조회 및 반환
        return earthquakeService.getEarthquakes(year, minMagnitude, minLatitude, maxLatitude, minLongitude, maxLongitude, orderBy);
    }
}
