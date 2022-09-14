package dev.sonnyjon.msscbeerservice.services.inventory;

import java.util.UUID;

/**
 * Created by Sonny on 9/8/2022.
 */
public interface BeerInventoryService
{
    Integer getOnhandInventory(UUID beerId);
}
