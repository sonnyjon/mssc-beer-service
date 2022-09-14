package dev.sonnyjon.msscbeerservice.model.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Created by Sonny on 9/9/2022.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateOrderResult
{
    private UUID orderId;
    private Boolean isValid;
}