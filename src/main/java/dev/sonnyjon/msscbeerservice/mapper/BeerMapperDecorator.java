package dev.sonnyjon.msscbeerservice.mapper;

import dev.sonnyjon.msscbeerservice.dto.BeerDto;
import dev.sonnyjon.msscbeerservice.model.beer.Beer;
import dev.sonnyjon.msscbeerservice.services.inventory.BeerInventoryService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Sonny on 9/9/2022.
 */
public abstract class BeerMapperDecorator implements BeerMapper
{
    private BeerInventoryService beerInventoryService;
    private BeerMapper mapper;

    @Override
    public BeerDto toBeerDto(Beer beer)
    {
        return mapper.toBeerDto( beer );
    }

    @Override
    public Beer toBeer(BeerDto beerDto)
    {
        return mapper.toBeer( beerDto );
    }

    @Override
    public BeerDto toBeerDtoWithInventory(Beer beer)
    {
        BeerDto dto = mapper.toBeerDto( beer );
        dto.setQuantityOnHand(beerInventoryService.getOnhandInventory( beer.getId() ));
        return dto;
    }

    @Autowired
    public void setBeerInventoryService(BeerInventoryService beerInventoryService)
    {
        this.beerInventoryService = beerInventoryService;
    }

    @Autowired
    public void setMapper(BeerMapper mapper)
    {
        this.mapper = mapper;
    }

}