package dev.sonnyjon.msscbeerservice.cases;

import dev.sonnyjon.msscbeerservice.dto.BeerDto;
import dev.sonnyjon.msscbeerservice.model.beer.BeerStyle;

import java.math.BigDecimal;

/**
 * Created by Sonny on 9/17/2022.
 */
public class TestBeerDto
{
    public static BeerDto getBeerDto()
    {
        return getBeerDto( "Test Beer DTO", BeerStyle.IPA );
    }

    public static BeerDto getBeerDto(String name, BeerStyle beerStyle)
    {
        return BeerDto.builder()
                .name( name )
                .style( beerStyle )
                .upc( TestUpc.BEER_1_UPC )
                .price(new BigDecimal( "12.95" ))
                .build();
    }

    public static BeerDto getBeerDtoWithQuantity()
    {
        BeerDto dto = getBeerDto();
        dto.setQuantityOnHand( 50 );
        return dto;
    }

    public static BeerDto getBeerDtoWithQuantity(String name, BeerStyle beerStyle, Integer quantity)
    {
        BeerDto dto = getBeerDto( name, beerStyle );
        dto.setQuantityOnHand( quantity );
        return dto;
    }
}
