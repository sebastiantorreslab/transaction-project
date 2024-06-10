package com.api.ms_transaction.client;

import com.api.ms_transaction.configuration.feign.FeignInterceptor;
import com.api.ms_transaction.configuration.feign.LoadBalancerConfig;
import org.keycloak.representations.account.UserRepresentation;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-user", url = "http://localhost:8082",configuration = FeignInterceptor.class)
@LoadBalancerClient(name = "ms-user", configuration = LoadBalancerConfig.class)
public interface UserClient {

    @GetMapping("user/search/internal")
    ResponseEntity<List<UserRepresentation>> getUserByUserName(@RequestParam String username);




}
