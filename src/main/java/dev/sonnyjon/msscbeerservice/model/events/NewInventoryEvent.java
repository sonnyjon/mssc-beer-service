package dev.sonnyjon.msscbeerservice.model.events;

import dev.sonnyjon.msscbeerservice.dto.BeerDto;
import lombok.NoArgsConstructor;

/**
 * Created by Sonny on 9/9/2022.
 */
@NoArgsConstructor
public class NewInventoryEvent extends BeerEvent
{
    public NewInventoryEvent(BeerDto beerDto)
    {
        super(beerDto);
    }
}