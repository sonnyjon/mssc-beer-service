package dev.sonnyjon.msscbeerservice.services.inventory;

import dev.sonnyjon.msscbeerservice.dto.BeerInventoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Created by Sonny on 9/8/2022.
 */
@RequiredArgsConstructor
@Component
public class InventoryServiceFeignClientFailover implements InventoryServiceFeignClient
{
    private final InventoryFailoverFeignClient failoverFeignClient;

    @Override
    public ResponseEntity<List<BeerInventoryDto>> getOnHandInventory(UUID beerId)
    {
        return failoverFeignClient.getOnHandInventory();
    }
}