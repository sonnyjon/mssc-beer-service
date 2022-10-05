package dev.sonnyjon.msscbeerservice.services.brewing;

import dev.sonnyjon.msscbeerservice.cases.TestBeer;
import dev.sonnyjon.msscbeerservice.mapper.BeerMapper;
import dev.sonnyjon.msscbeerservice.model.beer.Beer;
import dev.sonnyjon.msscbeerservice.model.events.BrewBeerEvent;
import dev.sonnyjon.msscbeerservice.repositories.BeerRepository;
import dev.sonnyjon.msscbeerservice.services.inventory.BeerInventoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Sonny on 9/17/2022.
 */
@ExtendWith(MockitoExtension.class)
class BrewingServiceTest
{
    @Mock
    BeerRepository beerRepository;

    @Mock
    BeerInventoryService beerInventoryService;

    @Mock
    JmsTemplate jmsTemplate;

    @Mock
    BeerMapper beerMapper;

    @InjectMocks
    BrewingService brewingService;

    @Test
    @DisplayName("should find all beer low in quantity and send message to increase inventory.")
    void givenLowQoH_whenCheckForLowInventory_thenSendMessageToBrewMore()
    {
        // given
        final Beer beer = TestBeer.getBeerWithId();   // minOnHand: 12
        List<Beer> beers = List.of( beer );
        final Integer inStock = 10;

        // when
        when(beerRepository.findAll()).thenReturn( beers );
        when(beerInventoryService.getOnHandInventory(any( UUID.class ))).thenReturn( inStock );
        doNothing().when(jmsTemplate).convertAndSend( anyString(), any(BrewBeerEvent.class) );

        brewingService.checkForLowInventory();

        // then
        assertTrue( inStock <= beer.getMinOnHand() );
        verify(beerRepository, times(1)).findAll();
        verify(beerInventoryService, times(1)).getOnHandInventory(any( UUID.class ));
        verify(jmsTemplate, times(1)).convertAndSend( anyString(), any(BrewBeerEvent.class) );
    }
}