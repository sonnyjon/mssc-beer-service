package dev.sonnyjon.msscbeerservice.model.events;

import dev.sonnyjon.msscbeerservice.dto.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Sonny on 9/9/2022.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateOrderRequest
{
    private BeerOrderDto beerOrder;
}