package dev.sonnyjon.msscbeerservice.bootstrap;

import dev.sonnyjon.msscbeerservice.model.Beer;
import dev.sonnyjon.msscbeerservice.repositories.BeerRepository;
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
    private final BeerRepository beerRepository;

    public BeerDataLoader(BeerRepository beerRepository)
    {
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args) throws Exception
    {
        if (beerRepository.count() == 0) loadBeerData();
    }

    private void loadBeerData()
    {
        beerRepository.save(
                Beer.builder()
                    .name( "Mango Bobs" )
                    .style( "IPA" )
                    .quantityToBrew( 200 )
                    .minOnHand( 12 )
                    .upc( 337010000001L )
                    .price(new BigDecimal( "12.95" ))
                    .build()
        );

        beerRepository.save(
                Beer.builder()
                        .name( "Galaxy Cat" )
                        .style( "PALE_ALE" )
                        .quantityToBrew( 200 )
                        .minOnHand( 12 )
                        .upc( 337010000002L )
                        .price(new BigDecimal( "11.95" ))
                        .build()
        );

        String loaded = String.format("Loaded %d beers", beerRepository.count());

        log.info( "-----------------------------------------------------------" );
        log.info( loaded );
        log.info( "-----------------------------------------------------------" );
    }
}
