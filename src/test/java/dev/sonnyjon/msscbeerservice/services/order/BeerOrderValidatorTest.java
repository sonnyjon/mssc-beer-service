package dev.sonnyjon.msscbeerservice.services.order;

import dev.sonnyjon.msscbeerservice.cases.TestBeer;
import dev.sonnyjon.msscbeerservice.cases.TestBeerOrderDto;
import dev.sonnyjon.msscbeerservice.cases.TestBeerOrderLineDto;
import dev.sonnyjon.msscbeerservice.dto.BeerOrderDto;
import dev.sonnyjon.msscbeerservice.dto.BeerOrderLineDto;
import dev.sonnyjon.msscbeerservice.exceptions.NotFoundException;
import dev.sonnyjon.msscbeerservice.model.beer.Beer;
import dev.sonnyjon.msscbeerservice.repositories.BeerRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by Sonny on 10/3/2022.
 */
@ExtendWith(MockitoExtension.class)
class BeerOrderValidatorTest
{
    @Mock
    BeerRepository beerRepository;

    BeerOrderValidator beerOrderValidator;
    AutoCloseable mocks;

    @BeforeEach
    void setUp()
    {
        mocks = MockitoAnnotations.openMocks( this );
        beerOrderValidator = new BeerOrderValidator( beerRepository );
    }

    @AfterEach
    void tearDown() throws Exception
    {
        mocks.close();
    }

    @Nested
    @DisplayName("validateOrder() Method")
    class ValidateOrderTest
    {
        @Test
        @DisplayName("should return true if all beers in the given BeerOrderDto were found")
        void givenBeerOrderDto_whenValidateOrder_andAllBeersFound_thenReturnTrue()
        {
            // given
            final BeerOrderDto testBeerOrder = getTestBeerOrderDtoWithGoodOrderLine();
            final Beer beer = TestBeer.getBeer();

            // when
            when(beerRepository.findByUpc( anyString() )).thenReturn( beer );

            Boolean isValid = beerOrderValidator.validateOrder( testBeerOrder );

            // then
            assertTrue( isValid );
        }

        @Test
        @DisplayName("should return false if one or more beers in the given BeerOrderDto were not found")
        void givenBeerOrderDto_whenValidateOrder_andOneOrMoreBeersNotFound_thenReturnFalse()
        {
            // given
            final BeerOrderDto testBeerOrder = getTestBeerOrderDtoWithBadOrderLine();

            // when
            when(beerRepository.findByUpc( anyString() )).thenReturn( null );

            Boolean isValid = beerOrderValidator.validateOrder( testBeerOrder );

            // then
            assertFalse( isValid );
        }

        @Test
        @DisplayName("should throw NotFoundException: Null BeerOrderDto")
        void givenNull_whenValidateOrder_thenNotFoundException()
        {
            // given
            final BeerOrderDto testBeerOrder = null;

            // when
            Executable executable = () -> beerOrderValidator.validateOrder( testBeerOrder );

            // then
            assertThrows( NotFoundException.class, executable );
        }
    }

    private BeerOrderDto getTestBeerOrderDtoWithGoodOrderLine()
    {
        BeerOrderLineDto orderLine = TestBeerOrderLineDto.getBeerOrderLineDto();

        return embedOrderLine( orderLine );
    }

    private BeerOrderDto getTestBeerOrderDtoWithBadOrderLine()
    {
        BeerOrderLineDto orderLine = TestBeerOrderLineDto.getBeerOrderLineDto();
        orderLine.setUpc( "XXX" );

        return embedOrderLine( orderLine );
    }

    private BeerOrderDto embedOrderLine( BeerOrderLineDto orderLine )
    {
        List<BeerOrderLineDto> lines = List.of( orderLine );

        BeerOrderDto beerOrder = TestBeerOrderDto.getBeerOrderDto();
        beerOrder.setBeerOrderLines( lines );

        return beerOrder;
    }
}