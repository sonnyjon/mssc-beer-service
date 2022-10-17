package dev.sonnyjon.msscbeerservice.bootstrap;

import dev.sonnyjon.msscbeerservice.model.beer.Beer;
import dev.sonnyjon.msscbeerservice.model.beer.BeerStyle;
import dev.sonnyjon.msscbeerservice.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by Sonny on 10/17/2022.
 */
@RequiredArgsConstructor
@Component
public class BeerLoader implements CommandLineRunner
{
    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";

    private final BeerRepository beerRepository;

    @Override
    public void run(String... args) throws Exception 
    {
        if(beerRepository.count() == 0 ) loadBeerObjects();
    }

    private void loadBeerObjects() 
    {
        Beer b1 = Beer.builder()
                .name("Mango Bobs")
                .style(BeerStyle.IPA.name())
                .minOnHand(12)
                .quantityToBrew(200)
                .price(new BigDecimal("12.95"))
                .upc(BEER_1_UPC)
                .build();

        Beer b2 = Beer.builder()
                .name("Galaxy Cat")
                .style(BeerStyle.PALE_ALE.name())
                .minOnHand(12)
                .quantityToBrew(200)
                .price(new BigDecimal("12.95"))
                .upc(BEER_2_UPC)
                .build();

        Beer b3 = Beer.builder()
                .name("Pinball Porter")
                .style(BeerStyle.PALE_ALE.name())
                .minOnHand(12)
                .quantityToBrew(200)
                .price(new BigDecimal("12.95"))
                .upc(BEER_3_UPC)
                .build();

        beerRepository.save( b1 );
        beerRepository.save( b2 );
        beerRepository.save( b3 );
    }}
