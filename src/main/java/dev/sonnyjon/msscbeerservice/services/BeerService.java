package dev.sonnyjon.msscbeerservice.services;

import dev.sonnyjon.msscbeerservice.web.dto.BeerDto;

import java.util.UUID;

/**
 * Created by Sonny on 8/29/2022.
 */
public interface BeerService
{
    BeerDto getById(UUID beerId);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto);
}