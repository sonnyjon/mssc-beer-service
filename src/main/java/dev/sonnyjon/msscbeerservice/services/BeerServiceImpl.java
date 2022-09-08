package dev.sonnyjon.msscbeerservice.services;

import dev.sonnyjon.msscbeerservice.model.Beer;
import dev.sonnyjon.msscbeerservice.model.BeerDto;
import dev.sonnyjon.msscbeerservice.model.BeerPagedList;
import dev.sonnyjon.msscbeerservice.model.BeerStyle;
import dev.sonnyjon.msscbeerservice.repositories.BeerRepository;
import dev.sonnyjon.msscbeerservice.web.controllers.NotFoundException;
import dev.sonnyjon.msscbeerservice.web.mapper.BeerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.stream.Collectors;

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
    public BeerPagedList listBeers(String beerName, BeerStyle beerStyle, PageRequest pageRequest)
    {
        BeerPagedList beerPagedList;
        Page<Beer> beerPage = getBeerPage( beerName, beerStyle, pageRequest );

        beerPagedList = new BeerPagedList(
                beerPage.getContent()
                    .stream()
                    .map(beerMapper::toBeerDto)
                    .collect(Collectors.toList()),
                PageRequest.of(
                    beerPage.getPageable().getPageNumber(),
                    beerPage.getPageable().getPageSize()
                ),
                beerPage.getTotalElements()
        );

        return beerPagedList;
    }

    @Override
    public BeerDto getById(UUID beerId)
    {
        return beerMapper.toBeerDto(beerRepository.findById( beerId ).orElseThrow( NotFoundException::new ));
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto)
    {
        Beer beer = beerMapper.toBeer( beerDto );
        return beerMapper.toBeerDto(beerRepository.save( beer ));
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto)
    {
        Beer beer = beerRepository.findById( beerId ).orElseThrow( NotFoundException::new );

        beer.setName( beerDto.getName() );
        beer.setStyle( beerDto.getStyle().name() );
        beer.setPrice( beerDto.getPrice() );
        beer.setUpc( beerDto.getUpc() );

        return beerMapper.toBeerDto(beerRepository.save( beer ));
    }

    private Page<Beer> getBeerPage(String beerName, BeerStyle beerStyle, PageRequest pageRequest)
    {
        Page<Beer> beerPage;

        if (StringUtils.hasLength( beerName ))
        {
            beerPage = !ObjectUtils.isEmpty( beerStyle )
                    ? beerRepository.findAllByBeerNameAndBeerStyle( beerName, beerStyle, pageRequest ) // search both
                    : beerRepository.findAllByBeerName( beerName, pageRequest ); // search beer_service name
        }
        else // !StringUtils.hasLength
        {
            beerPage = !ObjectUtils.isEmpty( beerStyle )
                    ? beerRepository.findAllByBeerStyle( beerStyle, pageRequest ) // search beer_service style
                    : beerRepository.findAll( pageRequest );
        }

        return beerPage;
    }
}
