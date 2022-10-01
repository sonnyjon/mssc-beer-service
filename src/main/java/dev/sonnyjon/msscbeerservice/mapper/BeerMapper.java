package dev.sonnyjon.msscbeerservice.mapper;

import dev.sonnyjon.msscbeerservice.model.beer.Beer;
import dev.sonnyjon.msscbeerservice.dto.BeerDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

/**
 * Created by Sonny on 8/25/2022.
 */
@Mapper(uses = DateMapper.class)
@DecoratedWith(BeerMapperDecorator.class)
public interface BeerMapper
{
    BeerDto toBeerDto(Beer beer);

    Beer toBeer(BeerDto beerDto);

    BeerDto toBeerDtoWithInventory(Beer beer);
}
