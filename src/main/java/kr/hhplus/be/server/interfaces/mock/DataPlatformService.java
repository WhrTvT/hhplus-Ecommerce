package kr.hhplus.be.server.interfaces.mock;

import kr.hhplus.be.server.application.out.PaymentInfo;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DataPlatformService {
    private final RestTemplate restTemplate;

    public DataPlatformService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String sendPaymentToMockPlatform(PaymentInfo paymentInfo) {
        String mockApiUrl = "http://localhost:8080/mock/dataplatform/payments";
        ResponseEntity<String> response = restTemplate.postForEntity(mockApiUrl, paymentInfo, String.class);

        return response.getBody();
    }
}
