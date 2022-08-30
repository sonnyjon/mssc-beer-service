package dev.sonnyjon.msscbeerservice.bootstrap;

import dev.sonnyjon.msscbeerservice.model.Beer;
import dev.sonnyjon.msscbeerservice.repositories.BeerRepository;
import dev.sonnyjon.msscbeerservice.web.dto.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by Sonny on 8/22/2022.
 */
@Slf4j
@Component
public class BeerDataLoader implements CommandLineRunner
{
    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";

    private final BeerRepository beerRepository;

    public BeerDataLoader(BeerRepository beerRepository)
    {
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args)
    {
        if (beerRepository.count() == 0) loadBeerData();
    }

    private void loadBeerData()
    {
        beerRepository.save(
                Beer.builder()
                    .name( "Mango Bobs" )
                    .style( BeerStyle.IPA.name() )
                    .quantityToBrew( 200 )
                    .minOnHand( 12 )
                    .upc( BEER_1_UPC )
                    .price(new BigDecimal( "12.95" ))
                    .build()
        );

        beerRepository.save(
                Beer.builder()
                        .name( "Galaxy Cat" )
                        .style( BeerStyle.IPA.name() )
                        .quantityToBrew( 200 )
                        .minOnHand( 12 )
                        .upc( BEER_2_UPC )
                        .price(new BigDecimal( "11.95" ))
                        .build()
        );

        beerRepository.save(
                Beer.builder()
                        .name( "Pinball Porter" )
                        .style( BeerStyle.PALE_ALE.name() )
                        .quantityToBrew( 200 )
                        .minOnHand( 12 )
                        .upc( BEER_3_UPC )
                        .price(new BigDecimal( "12.95" ))
                        .build()
        );

        String loaded = String.format("Loaded %d beers", beerRepository.count());

        log.info( "-----------------------------------------------------------" );
        log.info( loaded );
        log.info( "-----------------------------------------------------------" );
    }
}
