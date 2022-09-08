package dev.sonnyjon.msscbeerservice.services;

import dev.sonnyjon.msscbeerservice.model.BeerDto;
import dev.sonnyjon.msscbeerservice.model.BeerPagedList;
import dev.sonnyjon.msscbeerservice.model.BeerStyle;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

/**
 * Created by Sonny on 8/29/2022.
 */
public interface BeerService
{
    BeerPagedList listBeers(String beerName, BeerStyle beerStyle, PageRequest pageRequest);

    BeerDto getById(UUID beerId);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto);
}