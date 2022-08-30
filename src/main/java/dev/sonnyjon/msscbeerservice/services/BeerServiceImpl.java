package dev.sonnyjon.msscbeerservice.services;

import dev.sonnyjon.msscbeerservice.model.Beer;
import dev.sonnyjon.msscbeerservice.repositories.BeerRepository;
import dev.sonnyjon.msscbeerservice.web.controllers.NotFoundException;
import dev.sonnyjon.msscbeerservice.web.dto.BeerDto;
import dev.sonnyjon.msscbeerservice.web.mapper.BeerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Sonny on 8/29/2022.
 */
@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService
{
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public BeerDto getById(UUID beerId)
    {
        return beerMapper.convert(beerRepository.findById( beerId ).orElseThrow( NotFoundException::new ));
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto)
    {
        Beer beer = beerMapper.convert( beerDto );
        return beerMapper.convert(beerRepository.save( beer ));
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto)
    {
        Beer beer = beerRepository.findById( beerId ).orElseThrow( NotFoundException::new );

        beer.setName( beerDto.getName() );
        beer.setStyle( beerDto.getStyle().name() );
        beer.setPrice( beerDto.getPrice() );
        beer.setUpc( beerDto.getUpc() );

        return beerMapper.convert(beerRepository.save( beer ));
    }
}
