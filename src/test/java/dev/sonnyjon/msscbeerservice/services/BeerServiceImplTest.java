package dev.sonnyjon.msscbeerservice.services;

import dev.sonnyjon.msscbeerservice.controllers.NotFoundException;
import dev.sonnyjon.msscbeerservice.dto.BeerDto;
import dev.sonnyjon.msscbeerservice.mapper.BeerMapper;
import dev.sonnyjon.msscbeerservice.model.beer.Beer;
import dev.sonnyjon.msscbeerservice.model.beer.BeerStyle;
import dev.sonnyjon.msscbeerservice.repositories.BeerRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Sonny on 9/11/2022.
 */
@ExtendWith(MockitoExtension.class)
class BeerServiceImplTest
{
    private final static String TEST_UPC = "0631234200036";

    @Mock
    BeerRepository beerRepository;
    @Mock
    BeerMapper beerMapper;

    BeerService beerService;
    AutoCloseable mocks;

    @BeforeEach
    void setUp()
    {
        mocks = MockitoAnnotations.openMocks( this );
        beerService = new BeerServiceImpl( beerRepository, beerMapper );
    }

    @AfterEach
    void tearDown() throws Exception
    {
        mocks.close();
    }

    @Nested
    @DisplayName("getById() Method")
    class GetByIdTest
    {
        @Test
        @DisplayName("should return BeerDto with QoH for given ID")
        void givenValidId_andShowInventory_whenGetById_thenBeerDtoWithQuantityOnHand()
        {
            // given
            final UUID beerId = UUID.randomUUID();
            final Beer testBeer = getTestBeer();
            testBeer.setId( beerId );

            final BeerDto testBeerDto = getBeerDtoWithQuantityOnHand();

            // when
            when(beerRepository.findById(any( UUID.class ))).thenReturn(Optional.of( testBeer ));
            when(beerMapper.toBeerDtoWithInventory(any( Beer.class ))).thenReturn( testBeerDto );

            BeerDto foundBeer = beerService.getById( beerId, true );

            // then
            assertNotNull( foundBeer );
            assertNotNull( foundBeer.getQuantityOnHand() );
            verify(beerRepository, times(1)).findById(any( UUID.class ));
            verify(beerMapper, times(1)).toBeerDtoWithInventory(any( Beer.class ));
            verify(beerMapper, times(0)).toBeerDto(any( Beer.class ));
        }

        @Test
        @DisplayName("should return BeerDto without QoH for given ID")
        void givenValidId_andIgnoreInventory_whenGetById_thenBeerDto()
        {
            // given
            final UUID beerId = UUID.randomUUID();
            final Beer testBeer = getTestBeer();
            testBeer.setId( beerId );

            final BeerDto testBeerDto = getBeerDto();

            // when
            when(beerRepository.findById(any( UUID.class ))).thenReturn(Optional.of( testBeer ));
            when(beerMapper.toBeerDto(any( Beer.class ))).thenReturn( testBeerDto );

            BeerDto foundBeer = beerService.getById( beerId, false );

            // then
            assertNotNull( foundBeer );
            assertNull( foundBeer.getQuantityOnHand() );
            verify(beerRepository, times(1)).findById(any( UUID.class ));
            verify(beerMapper, times(0)).toBeerDtoWithInventory(any( Beer.class ));
            verify(beerMapper, times(1)).toBeerDto(any( Beer.class ));
        }

        @Test
        @DisplayName("should throw NotFoundException for invalid ID")
        void givenBadId_whenGetById_thenThrowException()
        {
            // given
            final UUID beerId = UUID.randomUUID();

            // when
            when(beerRepository.findById(any( UUID.class ))).thenThrow( NotFoundException.class );
            Executable executable = () -> beerService.getById( beerId, true );

            // then
            assertThrows( NotFoundException.class, executable );
            verify(beerRepository, times(1)).findById(any( UUID.class ));
        }
    }

    @Nested
    @DisplayName("getByUpc() Method")
    class GetByUpcTest
    {
        @Test
        @DisplayName("should return BeerDto for given UPC")
        void givenValidUpc_whenGetByUpc_thenBeerDto()
        {
            // given
            final Beer testBeer = getTestBeer();
            final BeerDto testBeerDto = getBeerDto();

            // when
            when(beerRepository.findByUpc( anyString() )).thenReturn( testBeer );
            when(beerMapper.toBeerDto(any( Beer.class ))).thenReturn( testBeerDto );

            BeerDto actualBeer = beerService.getByUpc( TEST_UPC );

            // then
            assertNotNull( actualBeer );
            verify(beerRepository, times(1)).findByUpc( anyString() );
            verify(beerMapper, times(1)).toBeerDto(any( Beer.class ));
        }

        @Test
        @DisplayName("should return null for bad UPC")
        void givenBadUpc_whenGetByUpc_thenNullBeerDto()
        {
            // when
            when(beerRepository.findByUpc( anyString() )).thenReturn( null );
            when(beerMapper.toBeerDto( null )).thenReturn( null );

            BeerDto actualBeer = beerService.getByUpc( TEST_UPC );

            // then
            assertNull( actualBeer );
            verify(beerRepository, times(1)).findByUpc( anyString() );
            verify(beerMapper, times(1)).toBeerDto( any() );
        }
    }

    @Nested
    @DisplayName("saveNewBeer() Method")
    class SaveNewBeerTest
    {
        @Test
        @DisplayName("should save Beer and return BeerDto")
        void givenBeerDto_whenSaveNewBeer_thenSaveBeer()
        {
            // given
            final Beer testBeer = getTestBeer();
            final BeerDto testBeerDto = getBeerDto();

            // when
            when(beerMapper.toBeer(any( BeerDto.class ))).thenReturn( testBeer );
            when(beerRepository.save(any( Beer.class ))).thenReturn( testBeer );
            when(beerMapper.toBeerDto(any( Beer.class ))).thenReturn( testBeerDto );

            BeerDto actualBeer = beerService.saveNewBeer( testBeerDto );

            // then
            assertNotNull( actualBeer );
            verify(beerMapper, times(1)).toBeer(any( BeerDto.class ));
            verify(beerRepository, times(1)).save(any( Beer.class ));
            verify(beerMapper, times(1)).toBeerDto(any( Beer.class ));
        }
    }

    @Nested
    @DisplayName("updateBeer() Method")
    class UpdateBeerTest
    {
        @Test
        @DisplayName("should update mutable attributes and return BeerDto")
        void givenValidId_andBeerDto_whenUpdate_thenSaveBeer()
        {
            // given
            final Beer testBeer = getTestBeer();
            final BeerDto testBeerDto = getBeerDto();

            final BeerDto desiredBeerDto = getBeerDto();
            desiredBeerDto.setName( testBeer.getName() );
            desiredBeerDto.setStyle(BeerStyle.valueOf( testBeer.getStyle() ));
            desiredBeerDto.setPrice( testBeer.getPrice() );
            desiredBeerDto.setUpc( testBeer.getUpc() );

            // when
            when(beerRepository.findById(any( UUID.class ))).thenReturn(Optional.of( testBeer ));
            when(beerRepository.save(any( Beer.class ))).thenReturn( testBeer );
            when(beerMapper.toBeerDto(any( Beer.class ))).thenReturn( desiredBeerDto );

            BeerDto actualBeer = beerService.updateBeer( UUID.randomUUID(), testBeerDto );

            // then
            assertNotNull( actualBeer );
            verify(beerRepository, times(1)).findById(any( UUID.class ));
            verify(beerRepository, times(1)).save(any( Beer.class ));
            verify(beerMapper, times(1)).toBeerDto(any( Beer.class ));
        }

        @Test
        @DisplayName("should throw NotFoundException")
        void givenBadId_whenUpdate_thenNotFoundException()
        {
            // given
            final UUID beerId = UUID.randomUUID();
            final BeerDto testBeerDto = getBeerDto();

            // when
            when(beerRepository.findById(any( UUID.class ))).thenThrow( NotFoundException.class );
            Executable executable = () -> beerService.updateBeer( beerId, testBeerDto );

            // then
            assertThrows( NotFoundException.class, executable );
            verify(beerRepository, times(1)).findById(any( UUID.class ));
        }
    }

    // TODO: Write tests for ListBeers after instructor lectures on this part.

    @Disabled
    @Nested
    @DisplayName("listBeers() Method")
    class ListBeersTest
    {
        @Test
        @DisplayName("should return BeerPagedList of all beers with name, style, and QoH")
        void givenBeerName_andStyle_andInventory_whenListBeers_thenBeerList_withNameAndStyleAndQuantity()
        {
            // given

            // when

            // then
        }

        @Test
        @DisplayName("should return BeerPagedList of all beers with name and style")
        void givenBeerName_andStyle_whenListBeers_thenBeerList_withNameAndStyle()
        {
            fail("not implemented");
        }

        @Test
        @DisplayName("should return BeerPagedList of all beers with name and QoH")
        void givenBeerName_andInventory_whenListBeers_thenBeerList_withNameAndQuantity()
        {
            fail("not implemented");
        }

        @Test
        @DisplayName("should return BeerPagedList of all beers with name")
        void givenBeerName_whenListBeers_thenBeerList_withName()
        {
            fail("not implemented");
        }

        @Test
        @DisplayName("should return BeerPagedList of all beers with style and QoH")
        void givenBeerStyle_andInventory_whenListBeers_thenBeerList_withStyleAndQuantity()
        {
            fail("not implemented");
        }

        @Test
        @DisplayName("should return BeerPagedList of all beers with style")
        void givenBeerStyle_whenListBeers_thenBeerList_withStyle()
        {
            fail("not implemented");
        }

        @Test
        @DisplayName("should return BeerPagedList of all beers with QoH")
        void givenShowInventory_whenListBeers_thenBeerList_ofAllBeersAndQuantity()
        {
            fail("not implemented");
        }

        @Test
        @DisplayName("should return BeerPagedList of all beers")
        void whenListBeers_thenBeerList_ofAllBeers()
        {
            fail("not implemented");
        }

    }

    //==================================================================================================================
    private Beer getTestBeer()
    {
        return getTestBeer( "Test Beer", BeerStyle.IPA );
    }

    private Beer getTestBeer(String name, BeerStyle beerStyle)
    {
        return Beer.builder()
                .name( name )
                .style( beerStyle.name() )
                .upc( TEST_UPC )
                .price(new BigDecimal( "12.95" ))
                .minOnHand( 12 )
                .quantityToBrew( 200 )
                .build();
    }

    private BeerDto getBeerDto()
    {
        return getBeerDto( "Test Beer DTO", BeerStyle.IPA );
    }

    private BeerDto getBeerDto(String name, BeerStyle beerStyle)
    {
        return BeerDto.builder()
                .name( name )
                .style( beerStyle )
                .upc( TEST_UPC )
                .price(new BigDecimal( "12.95" ))
                .build();
    }

    private BeerDto getBeerDtoWithQuantityOnHand()
    {
        BeerDto dto = getBeerDto();
        dto.setQuantityOnHand( 50 );
        return dto;
    }

    private BeerDto getBeerDtoWithQuantityOnHand(String name, BeerStyle beerStyle, Integer quantity)
    {
        BeerDto dto = getBeerDto( name, beerStyle );
        dto.setQuantityOnHand( quantity );
        return dto;
    }
}