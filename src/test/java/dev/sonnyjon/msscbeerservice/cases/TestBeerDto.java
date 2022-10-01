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
        return getBeerDto(
                "Test Beer",
                BeerStyle.IPA,
                TestConstants.BEER_1_UPC,
                12,
                new BigDecimal( "19.95" )
        );
    }

    public static BeerDto getBeerDto(String name,
                                     BeerStyle style,
                                     String upc,
                                     Integer quantityOnHand,
                                     BigDecimal price)
    {
        return BeerDto.builder()
                .name( name )
                .style( style )
                .upc( upc )
                .quantityOnHand( quantityOnHand )
                .price( price )
                .build();
    }
}
