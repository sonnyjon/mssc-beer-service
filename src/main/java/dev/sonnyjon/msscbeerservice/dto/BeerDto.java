package dev.sonnyjon.msscbeerservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.sonnyjon.msscbeerservice.model.beer.BeerStyle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;
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
public class BeerDto implements Serializable
{
    static final long serialVersionUID = -5815566940065181210L;

    @Null
    private UUID id;

    @Null
    private Integer version;

    @NotBlank
    private String name;

    @NotNull
    private BeerStyle style;

    @NotNull
    private String upc;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Positive
    @NotNull
    private BigDecimal price;

    @PositiveOrZero
    private Integer quantityOnHand;

    @Null
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ", shape=JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate;

    @Null
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ", shape=JsonFormat.Shape.STRING)
    private OffsetDateTime lastModifiedDate;

}
