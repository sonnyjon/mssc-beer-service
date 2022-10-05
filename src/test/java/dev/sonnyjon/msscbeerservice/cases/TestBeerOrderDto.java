package dev.sonnyjon.msscbeerservice.cases;


import dev.sonnyjon.msscbeerservice.dto.BeerOrderDto;
import dev.sonnyjon.msscbeerservice.dto.BeerOrderLineDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Sonny on 9/24/2022.
 */
public class TestBeerOrderDto
{
    public static BeerOrderDto getBeerOrderDto()
    {
        return getBeerOrderDto(
                    UUID.randomUUID(),
                    UUID.randomUUID().toString(),
                    new ArrayList<>(),
                    "NEW",
                    "callback-url"
        );
    }

    public static BeerOrderDto getBeerOrderDto(UUID customerId,
                                               String customerRef,
                                               List<BeerOrderLineDto> beerOrderLines,
                                               String orderStatus,
                                               String orderStatusCallbackUrl)
    {
        return BeerOrderDto.builder()
                .customerId( customerId )
                .customerRef( customerRef )
                .beerOrderLines( beerOrderLines )
                .orderStatus( orderStatus )
                .orderStatusCallbackUrl( orderStatusCallbackUrl )
                .build();
    }

}
