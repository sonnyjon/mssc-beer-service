package dev.sonnyjon.msscbeerservice.services;

import dev.sonnyjon.msscbeerservice.dto.BeerDto;
import dev.sonnyjon.msscbeerservice.model.beer.BeerPagedList;
import dev.sonnyjon.msscbeerservice.model.beer.BeerStyle;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

/**
 * Created by Sonny on 8/29/2022.
 */
public interface BeerService
{
    BeerPagedList listBeers(String beerName, BeerStyle beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand);

    BeerDto getById(UUID beerId, Boolean showInventoryOnHand);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto);

    BeerDto getByUpc(String upc);
}