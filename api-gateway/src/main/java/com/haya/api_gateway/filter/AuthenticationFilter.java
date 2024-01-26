package com.haya.api_gateway.filter;

import com.haya.api_gateway.model.dtos.AutenticationRequest;
import com.haya.api_gateway.model.dtos.ResponseValidate;
import com.haya.api_gateway.util.CustomResponseErrorHandler;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;
    public AuthenticationFilter() {
        super(Config.class);
    }

    @Autowired
    private RestTemplate restTemplate;


    @Bean
    public CustomResponseErrorHandler getErrorHandler() {
        return new CustomResponseErrorHandler();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    JSONObject request = new JSONObject();
                    request.put("token", authHeader);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
                    ResponseEntity<String> loginResponse = null;

                    loginResponse = restTemplate.exchange("http://localhost:62190/api/validate",
                            HttpMethod.POST, entity, String.class);

                    if (loginResponse.getStatusCode() == HttpStatus.OK && loginResponse.hasBody()) {
                        JSONObject userJson = new JSONObject(loginResponse.getBody());
                        System.out.println(userJson);
                    } else if (loginResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                        throw new RuntimeException("El acceso es inauthorized, por favor crear un token valido");
                    }else{
                        throw new RuntimeException("Algo salio mal ");
                    }

                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new RuntimeException("unauthorized access to application" + e);
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
