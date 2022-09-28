package dev.sonnyjon.msscbeerservice.services.inventory;

import dev.sonnyjon.msscbeerservice.dto.BeerInventoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by Sonny on 9/8/2022.
 */
@Slf4j
@RequiredArgsConstructor
@Profile("local-discovery")
@Service
public class BeerInventoryServiceFeign implements BeerInventoryService
{
    private final InventoryServiceFeignClient inventoryServiceFeignClient;

    @Override
    public Integer getOnhandInventory(UUID beerId)
    {
        log.debug("Calling Inventory Service - BeerId: " + beerId);

        ResponseEntity<List<BeerInventoryDto>> responseEntity = inventoryServiceFeignClient.getOnhandInventory( beerId );

        Integer onHand = Objects.requireNonNull(responseEntity.getBody())
                                .stream()
                                .mapToInt(BeerInventoryDto::getQuantityOnHand)
                                .sum();

        log.debug("BeerId: " + beerId + " On hand is: " + onHand);

        return onHand;
    }
}