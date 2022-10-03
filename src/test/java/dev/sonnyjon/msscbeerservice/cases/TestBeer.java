package dev.sonnyjon.msscbeerservice.cases;

import dev.sonnyjon.msscbeerservice.model.beer.Beer;
import dev.sonnyjon.msscbeerservice.model.beer.BeerStyle;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by Sonny on 9/17/2022.
 */
public class TestBeer
{
    public static Beer getBeer()
    {
        return getBeer( "Test Beer", BeerStyle.IPA );
    }

    public static Beer getBeer(String name, BeerStyle beerStyle)
    {
        return Beer.builder()
                .name( name )
                .style( beerStyle.name() )
                .upc( TestUpc.BEER_1_UPC )
                .price(new BigDecimal( "12.95" ))
                .minOnHand( 12 )
                .quantityToBrew( 200 )
                .build();
    }

    public static Beer getBeerWithId()
    {
        Beer beer = getBeer();
        beer.setId( UUID.randomUUID() );
        return beer;
    }
}
