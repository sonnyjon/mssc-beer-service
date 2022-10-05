package dev.sonnyjon.msscbeerservice.services;

import dev.sonnyjon.msscbeerservice.exceptions.NotFoundException;
import dev.sonnyjon.msscbeerservice.dto.BeerDto;
import dev.sonnyjon.msscbeerservice.mapper.BeerMapper;
import dev.sonnyjon.msscbeerservice.model.beer.Beer;
import dev.sonnyjon.msscbeerservice.model.beer.BeerPagedList;
import dev.sonnyjon.msscbeerservice.model.beer.BeerStyle;
import dev.sonnyjon.msscbeerservice.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
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

    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false ")
    @Override
    public BeerPagedList listBeers(String beerName,
                                   BeerStyle beerStyle,
                                   PageRequest pageRequest,
                                   Boolean showInventoryOnHand)
    {
        showInventoryOnHand = (showInventoryOnHand != null) && showInventoryOnHand;

        Page<Beer> beerPage = getBeerPage( beerName, beerStyle, pageRequest );

        List<BeerDto> content = (showInventoryOnHand)
                ? beerPage.getContent().stream().map(beerMapper::toBeerDtoWithInventory).toList()
                : beerPage.getContent().stream().map(beerMapper::toBeerDto).toList();

        Pageable pageable = PageRequest.of(
                beerPage.getPageable().getPageNumber(),
                beerPage.getPageable().getPageSize()
        );

        return new BeerPagedList( content, pageable, beerPage.getTotalElements() );
    }

    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand == false ")
    @Override
    public BeerDto getById(UUID beerId, Boolean showInventoryOnHand)
    {
        Beer beer = beerRepository.findById( beerId ).orElseThrow( NotFoundException::new );

        return (showInventoryOnHand)
                ? beerMapper.toBeerDtoWithInventory( beer )
                : beerMapper.toBeerDto( beer );
    }

    @Cacheable(cacheNames = "beerUpcCache")
    @Override
    public BeerDto getByUpc(String upc)
    {
        return beerMapper.toBeerDto(beerRepository.findByUpc( upc ));
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
                    ? beerRepository.findAllByNameAndStyle( beerName, beerStyle, pageRequest ) // search both
                    : beerRepository.findAllByName( beerName, pageRequest ); // search beer_service name
        }
        else // !StringUtils.hasLength
        {
            beerPage = !ObjectUtils.isEmpty( beerStyle )
                    ? beerRepository.findAllByStyle( beerStyle, pageRequest ) // search beer_service style
                    : beerRepository.findAll( pageRequest );
        }

        return beerPage;
    }
}
