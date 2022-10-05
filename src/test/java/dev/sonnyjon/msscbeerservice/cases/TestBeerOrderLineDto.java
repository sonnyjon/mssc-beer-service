package dev.sonnyjon.msscbeerservice.cases;


import dev.sonnyjon.msscbeerservice.dto.BeerOrderLineDto;

import java.util.UUID;

/**
 * Created by Sonny on 9/24/2022.
 */
public class TestBeerOrderLineDto
{
    public static BeerOrderLineDto getBeerOrderLineDto()
    {
        return getBeerOrderLineDto( UUID.randomUUID(), TestConstants.BEER_1_UPC, 50 );
    }

    public static BeerOrderLineDto getBeerOrderLineDto(UUID beerId,
                                                       String upc,
                                                       Integer orderQuantity)
    {
        return BeerOrderLineDto.builder()
                .beerId( beerId )
                .upc( upc )
                .orderQuantity( orderQuantity )
                .build();
    }
}
