package dev.sonnyjon.msscbeerservice.services.brewing;

import dev.sonnyjon.msscbeerservice.config.JmsConfig;
import dev.sonnyjon.msscbeerservice.mapper.BeerMapper;
import dev.sonnyjon.msscbeerservice.model.beer.Beer;
import dev.sonnyjon.msscbeerservice.model.events.BrewBeerEvent;
import dev.sonnyjon.msscbeerservice.repositories.BeerRepository;
import dev.sonnyjon.msscbeerservice.services.inventory.BeerInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Sonny on 9/9/2022.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BrewingService
{
    private final BeerRepository beerRepository;
    private final BeerInventoryService beerInventoryService;
    private final JmsTemplate jmsTemplate;
    private final BeerMapper beerMapper;

    @Scheduled(fixedRate = 5000) //every 5 seconds
    public void checkForLowInventory()
    {
        List<Beer> beers = (List<Beer>) beerRepository.findAll();

        beers.forEach( beer -> {
            Integer invQOH = beerInventoryService.getOnHandInventory(beer.getId());
            log.debug("Checking Inventory for: " + beer.getName() + " / " + beer.getId());
            log.debug("Min Onhand is: " + beer.getMinOnHand());
            log.debug("Inventory is: "  + invQOH);

            if (beer.getMinOnHand() >= invQOH)
            {
                jmsTemplate.convertAndSend(
                        JmsConfig.BREWING_REQUEST_QUEUE,
                        new BrewBeerEvent(beerMapper.toBeerDto( beer ))
                );
            }
        });

    }
}