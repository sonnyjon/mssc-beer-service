package dev.sonnyjon.msscbeerservice.repositories;

import dev.sonnyjon.msscbeerservice.model.Beer;
import dev.sonnyjon.msscbeerservice.model.BeerStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

/**
 * Created by Sonny on 8/22/2022.
 */
public interface BeerRepository extends PagingAndSortingRepository<Beer, UUID>
{
    Page<Beer> findAllByBeerName(String beerName, PageRequest pageRequest);

    Page<Beer> findAllByBeerStyle(BeerStyle beerStyle, PageRequest pageRequest);

    Page<Beer> findAllByBeerNameAndBeerStyle(String beerName, BeerStyle beerStyle, PageRequest pageRequest);

    Beer findByUpc(String upc);
}
