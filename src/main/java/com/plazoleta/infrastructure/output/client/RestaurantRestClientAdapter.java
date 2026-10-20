package com.plazoleta.infrastructure.output.client;

import com.plazoleta.application.port.output.RestaurantServicePort;
import com.plazoleta.domain.exception.RestaurantServiceUnavailableException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Optional;

/** Llama al microservicio de Plazoleta reenviando el mismo token JWT que recibió Usuarios. */
public class RestaurantRestClientAdapter implements RestaurantServicePort {

    private final RestClient restClient;

    public RestaurantRestClientAdapter(String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<Long> findOwnerIdByRestaurantId(Long restaurantId) {
        try {
            Map<String, Object> body = restClient.get()
                    .uri("/api/v1/restaurants/{id}/owner", restaurantId)
                    .header(HttpHeaders.AUTHORIZATION, currentAuthorizationHeader())
                    .retrieve()
                    .body(Map.class);
            return Optional.ofNullable(body)
                    .map(b -> b.get("idPropietario"))
                    .map(v -> ((Number) v).longValue());
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (RestClientException e) {
            throw new RestaurantServiceUnavailableException();
        }
    }

    private String currentAuthorizationHeader() {
        var attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String header = attrs.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
        return header == null ? "" : header;
    }
}
