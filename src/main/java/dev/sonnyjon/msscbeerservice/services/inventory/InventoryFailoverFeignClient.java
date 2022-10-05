package dev.sonnyjon.msscbeerservice.services.inventory;

import dev.sonnyjon.msscbeerservice.dto.BeerInventoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.cloud.openfeign.FeignClient;


import java.util.List;

/**
 * Created by Sonny on 9/8/2022.
 */
@FeignClient(
        name = "inventory-failover",
        url = "/inventory-failover"
)
public interface InventoryFailoverFeignClient
{
    @RequestMapping(method = RequestMethod.GET, value = "/inventory-failover")
    ResponseEntity<List<BeerInventoryDto>> getOnHandInventory();
}