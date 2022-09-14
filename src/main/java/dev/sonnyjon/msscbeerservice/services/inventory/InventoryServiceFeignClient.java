package dev.sonnyjon.msscbeerservice.services.inventory;

import dev.sonnyjon.msscbeerservice.config.FeignClientConfig;
import dev.sonnyjon.msscbeerservice.dto.BeerInventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

/**
 * Created by Sonny on 9/8/2022.
 */
@FeignClient(
        name = "inventory-service",
        fallback = InventoryServiceFeignClientFailover.class,
        configuration = FeignClientConfig.class
)
public interface InventoryServiceFeignClient
{
    @RequestMapping(method = RequestMethod.GET, value = BeerInventoryServiceRestTemplateImpl.INVENTORY_PATH)
    ResponseEntity<List<BeerInventoryDto>> getOnhandInventory(@PathVariable UUID beerId);
}