package dev.sonnyjon.msscbeerservice.repositories;

import dev.sonnyjon.msscbeerservice.model.beer.Beer;
import dev.sonnyjon.msscbeerservice.model.beer.BeerStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

/**
 * Created by Sonny on 8/22/2022.
 */
public interface BeerRepository extends PagingAndSortingRepository<Beer, UUID>
{
    Page<Beer> findAllByName(String beerName, PageRequest pageRequest);

    Page<Beer> findAllByStyle(BeerStyle beerStyle, PageRequest pageRequest);

    Page<Beer> findAllByNameAndStyle(String beerName, BeerStyle beerStyle, PageRequest pageRequest);

    Beer findByUpc(String upc);
}
