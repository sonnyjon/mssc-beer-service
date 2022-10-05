package dev.sonnyjon.msscbeerservice.cases;

import dev.sonnyjon.msscbeerservice.dto.BeerInventoryDto;

import java.util.UUID;

/**
 * Created by Sonny on 10/5/2022.
 */
public class TestBeerInventoryDto
{
    public static BeerInventoryDto getBeerInventoryDto()
    {
        return getBeerInventoryDto(UUID.randomUUID(), TestConstants.BEER_1_UUID, 10);
    }

    public static BeerInventoryDto getBeerInventoryDto(UUID id, UUID beerId, Integer quantityOnHand)
    {
        return BeerInventoryDto.builder()
                .id( id )
                .beerId( beerId )
                .quantityOnHand( quantityOnHand )
                .build();
    }

}
