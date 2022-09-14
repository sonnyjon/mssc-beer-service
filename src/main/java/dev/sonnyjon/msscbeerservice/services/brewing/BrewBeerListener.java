package dev.sonnyjon.msscbeerservice.services.brewing;

import dev.sonnyjon.msscbeerservice.config.JmsConfig;
import dev.sonnyjon.msscbeerservice.controllers.NotFoundException;
import dev.sonnyjon.msscbeerservice.dto.BeerDto;
import dev.sonnyjon.msscbeerservice.model.beer.Beer;
import dev.sonnyjon.msscbeerservice.model.events.BrewBeerEvent;
import dev.sonnyjon.msscbeerservice.model.events.NewInventoryEvent;
import dev.sonnyjon.msscbeerservice.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Sonny on 9/9/2022.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BrewBeerListener
{
    private final BeerRepository beerRepository;
    private final JmsTemplate jmsTemplate;

    @Transactional
    @JmsListener(destination = JmsConfig.BREWING_REQUEST_QUEUE)
    public void listen(BrewBeerEvent event)
    {
        BeerDto beerDto = event.getBeerDto();

        Beer beer = beerRepository.findById( beerDto.getId() ).orElseThrow( NotFoundException::new );

        beerDto.setQuantityOnHand(beer.getQuantityToBrew());

        NewInventoryEvent newInventoryEvent = new NewInventoryEvent(beerDto);

        log.debug("Brewed beer " + beer.getMinOnHand() + " : QOH: " + beerDto.getQuantityOnHand());

        jmsTemplate.convertAndSend(JmsConfig.NEW_INVENTORY_QUEUE, newInventoryEvent);
    }
}