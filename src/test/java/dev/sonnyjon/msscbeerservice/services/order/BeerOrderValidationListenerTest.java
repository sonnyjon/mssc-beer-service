package dev.sonnyjon.msscbeerservice.services.order;

import dev.sonnyjon.msscbeerservice.cases.TestBeerOrderDto;
import dev.sonnyjon.msscbeerservice.dto.BeerOrderDto;
import dev.sonnyjon.msscbeerservice.model.events.ValidateOrderRequest;
import dev.sonnyjon.msscbeerservice.model.events.ValidateOrderResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Sonny on 10/5/2022.
 */
@ExtendWith(MockitoExtension.class)
class BeerOrderValidationListenerTest
{
    @Mock
    BeerOrderValidator beerOrderValidator;

    @Mock
    JmsTemplate jmsTemplate;

    @InjectMocks
    BeerOrderValidationListener listener;

    @Test
    @DisplayName("should validate BeerOrder and send results as message")
    void givenValidateOrderRequest_whenListen_thenValidateOrder_andMessageResults()
    {
        // given
        final BeerOrderDto beerOrder = TestBeerOrderDto.getBeerOrderDto();
        final ValidateOrderRequest request = new ValidateOrderRequest( beerOrder );

        // when
        when(beerOrderValidator.validateOrder(any( BeerOrderDto.class ))).thenReturn( true );
        doNothing().when(jmsTemplate).convertAndSend( anyString(), any(ValidateOrderResult.class) );

        listener.listen( request );

        // then
        verify(beerOrderValidator, times(1)).validateOrder(any( BeerOrderDto.class ));
        verify(jmsTemplate, times(1)).convertAndSend( anyString(), any(ValidateOrderResult.class) );
    }

    @Test
    @DisplayName("should do nothing if given a NULL ValidateOrderRequest")
    void givenNull_whenListen_thenDoNothing()
    {
        // given
        final ValidateOrderRequest request = null;

        // when
        listener.listen( request );

        // then
        verify(beerOrderValidator, times(0)).validateOrder(any( BeerOrderDto.class ));
        verify(jmsTemplate, times(0)).convertAndSend( anyString(), any(ValidateOrderResult.class) );
    }
}