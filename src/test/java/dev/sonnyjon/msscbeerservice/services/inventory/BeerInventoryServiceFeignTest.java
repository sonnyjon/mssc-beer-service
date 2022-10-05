package dev.sonnyjon.msscbeerservice.services.inventory;

import dev.sonnyjon.msscbeerservice.cases.TestBeerInventoryDto;
import dev.sonnyjon.msscbeerservice.dto.BeerInventoryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Sonny on 10/5/2022.
 */
@ExtendWith(MockitoExtension.class)
class BeerInventoryServiceFeignTest
{
    @Mock
    InventoryServiceFeignClient inventoryServiceFeignClient;

    @InjectMocks
    BeerInventoryServiceFeign serviceFeign;

    @Test
    @DisplayName("should return total quantity on hand for the given Beer ID")
    void givenBeerId_whenGetOnHandInventory_thenReturnSumOfQoH()
    {
        // given
        final BeerInventoryDto inventoryDto = TestBeerInventoryDto.getBeerInventoryDto();
        final UUID beerId = inventoryDto.getBeerId();
        final Integer expected = inventoryDto.getQuantityOnHand();

        List<BeerInventoryDto> inventories = List.of( inventoryDto );
        final ResponseEntity<List<BeerInventoryDto>> responseEntity = new ResponseEntity<>( inventories, HttpStatus.OK );

        // when
        when(inventoryServiceFeignClient.getOnHandInventory(any( UUID.class ))).thenReturn( responseEntity );

        Integer actual = serviceFeign.getOnHandInventory( beerId );

        // then
        assertNotNull( actual );
        assertEquals( expected, actual );
        verify(inventoryServiceFeignClient, times(1)).getOnHandInventory(any( UUID.class ));
    }

    // TODO: Need to test for bad Beer ID?
}