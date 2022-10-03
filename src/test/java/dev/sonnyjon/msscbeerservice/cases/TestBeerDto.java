package dev.sonnyjon.msscbeerservice.cases;

import dev.sonnyjon.msscbeerservice.dto.BeerDto;
import dev.sonnyjon.msscbeerservice.model.beer.Beer;
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

    public static BeerDto getBeerDtoFromBeer(Beer beer)
    {
        BeerDto beerDto = getBeerDto();

        beerDto.setId( beer.getId() );
        beerDto.setName( beer.getName() );
        beerDto.setStyle(BeerStyle.valueOf( beer.getStyle() ));
        beerDto.setUpc( beer.getUpc() );
        beerDto.setPrice( beer.getPrice() );

        return beerDto;
    }

    public static BeerDto getBeerDtoWithQuantity()
    {
        BeerDto beerDto = getBeerDto();
        beerDto.setQuantityOnHand( 100 );
        return beerDto;
    }

    public static BeerDto getBeerDtoWithQuantity(Beer beer)
    {
        BeerDto beerDto = getBeerDtoFromBeer( beer );
        beerDto.setQuantityOnHand( 100 );
        return beerDto;
    }

    public static BeerDto getBeerDtoWithoutQuantity()
    {
        BeerDto testBeerDto = getBeerDto();
        testBeerDto.setQuantityOnHand( null );
        return testBeerDto;
    }

    public static BeerDto getBeerDtoWithoutQuantity(Beer beer)
    {
        BeerDto beerDto = getBeerDtoFromBeer( beer );
        beerDto.setQuantityOnHand( null );
        return beerDto;
    }
}
