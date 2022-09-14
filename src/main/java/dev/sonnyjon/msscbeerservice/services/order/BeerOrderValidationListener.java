package dev.sonnyjon.msscbeerservice.services.order;

import dev.sonnyjon.msscbeerservice.config.JmsConfig;
import dev.sonnyjon.msscbeerservice.model.events.ValidateOrderRequest;
import dev.sonnyjon.msscbeerservice.model.events.ValidateOrderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Sonny on 9/9/2022.
 */
@RequiredArgsConstructor
@Component
public class BeerOrderValidationListener
{
    private final BeerOrderValidator validator;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_QUEUE)
    public void listen(ValidateOrderRequest validateOrderRequest)
    {
        Boolean isValid = validator.validateOrder( validateOrderRequest.getBeerOrder() );

        jmsTemplate.convertAndSend(
                JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE,
                ValidateOrderResult.builder()
                        .isValid( isValid )
                        .orderId( validateOrderRequest.getBeerOrder().getId() )
                        .build()
        );
    }
}