package dev.sonnyjon.msscbeerservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Created by Sonny on 8/21/2022.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto
{
    private UUID id;
    private Integer version;
    private String name;
    private BeerStyle style;
    private Long upc;
    private BigDecimal price;
    private Integer quantityOnHand;

    private OffsetDateTime createdDate;
    private OffsetDateTime lastModifiedDate;

}
