package dev.sonnyjon.msscbeerservice.services.order;

import dev.sonnyjon.msscbeerservice.dto.BeerOrderDto;
import dev.sonnyjon.msscbeerservice.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Sonny on 9/9/2022.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderValidator
{
    private final BeerRepository beerRepository;

    public Boolean validateOrder(BeerOrderDto beerOrder)
    {
        AtomicInteger beersNotFound = new AtomicInteger();

        beerOrder.getBeerOrderLines().forEach( orderline -> {
            if(beerRepository.findByUpc(orderline.getUpc()) == null) beersNotFound.incrementAndGet();
        });

        return beersNotFound.get() == 0;
    }

}