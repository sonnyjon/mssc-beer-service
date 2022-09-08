package dev.sonnyjon.msscbeerservice.web.mapper;

import dev.sonnyjon.msscbeerservice.model.Beer;
import dev.sonnyjon.msscbeerservice.model.BeerDto;
import org.mapstruct.Mapper;

/**
 * Created by Sonny on 8/25/2022.
 */
@Mapper(uses = DateMapper.class)
public interface BeerMapper
{
    BeerDto toBeerDto(Beer beer);
    Beer toBeer(BeerDto beerDto);
}
