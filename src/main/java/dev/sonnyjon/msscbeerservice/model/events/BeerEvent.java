package dev.sonnyjon.msscbeerservice.model.events;

import dev.sonnyjon.msscbeerservice.dto.BeerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.io.Serializable;

/**
 * Created by Sonny on 9/9/2022.
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BeerEvent implements Serializable
{
    static final long serialVersionUID = -5781515597148163111L;

    private BeerDto beerDto;
}