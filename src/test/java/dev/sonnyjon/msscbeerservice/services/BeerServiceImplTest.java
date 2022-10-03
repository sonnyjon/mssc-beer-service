package dev.sonnyjon.msscbeerservice.services;

import dev.sonnyjon.msscbeerservice.cases.TestBeer;
import dev.sonnyjon.msscbeerservice.cases.TestBeerDto;
import dev.sonnyjon.msscbeerservice.cases.TestConstants;
import dev.sonnyjon.msscbeerservice.controllers.NotFoundException;
import dev.sonnyjon.msscbeerservice.dto.BeerDto;
import dev.sonnyjon.msscbeerservice.mapper.BeerMapper;
import dev.sonnyjon.msscbeerservice.model.beer.Beer;
import dev.sonnyjon.msscbeerservice.model.beer.BeerPagedList;
import dev.sonnyjon.msscbeerservice.model.beer.BeerStyle;
import dev.sonnyjon.msscbeerservice.repositories.BeerRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
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
            final Beer beer = TestBeer.getBeerWithId();
            final BeerDto expectedDto = TestBeerDto.getBeerDtoWithQuantity();

            // when
            when(beerRepository.findById(any( UUID.class ))).thenReturn(Optional.of( beer ));
            when(beerMapper.toBeerDtoWithInventory(any( Beer.class ))).thenReturn( expectedDto );

            BeerDto foundBeer = beerService.getById( beer.getId(), true );

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
            final Beer beer = TestBeer.getBeerWithId();
            final BeerDto expectedDto = TestBeerDto.getBeerDtoWithoutQuantity();

            // when
            when(beerRepository.findById(any( UUID.class ))).thenReturn(Optional.of( beer ));
            when(beerMapper.toBeerDto(any( Beer.class ))).thenReturn( expectedDto );

            BeerDto foundBeer = beerService.getById( beer.getId(), false );

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
            final Beer beer = TestBeer.getBeerWithId();
            final BeerDto expectedDto = TestBeerDto.getBeerDtoWithoutQuantity();

            // when
            when(beerRepository.findByUpc( anyString() )).thenReturn( beer );
            when(beerMapper.toBeerDto(any( Beer.class ))).thenReturn( expectedDto );

            BeerDto actualDto = beerService.getByUpc( TestConstants.BEER_1_UPC );

            // then
            assertNotNull( actualDto );
            assertEquals( expectedDto.getUpc(), actualDto.getUpc() );
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

            BeerDto actualBeer = beerService.getByUpc( TestConstants.BEER_1_UPC );

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
            final Beer beer = TestBeer.getBeerWithId();
            final BeerDto expectedDto = TestBeerDto.getBeerDtoWithoutQuantity();

            // when
            when(beerMapper.toBeer(any( BeerDto.class ))).thenReturn( beer );
            when(beerRepository.save(any( Beer.class ))).thenReturn( beer );
            when(beerMapper.toBeerDto(any( Beer.class ))).thenReturn( expectedDto );

            BeerDto actualDto = beerService.saveNewBeer( expectedDto );

            // then
            assertNotNull( actualDto );
            assertEquals( expectedDto.getId(), actualDto.getId() );
            assertEquals( expectedDto.getUpc(), actualDto.getUpc() );
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
            final Beer expectedBeer = TestBeer.getBeerWithId();
            final BeerDto expectedDto = TestBeerDto.getBeerDtoWithoutQuantity( expectedBeer );

            // when
            when(beerRepository.findById(any( UUID.class ))).thenReturn(Optional.of( expectedBeer ));
            when(beerRepository.save(any( Beer.class ))).thenReturn( expectedBeer );
            when(beerMapper.toBeerDto(any( Beer.class ))).thenReturn( expectedDto );

            BeerDto actualBeer = beerService.updateBeer( UUID.randomUUID(), expectedDto );

            // then
            assertNotNull( actualBeer );
            assertEquals( expectedBeer.getId(), actualBeer.getId() );
            assertEquals( expectedBeer.getName(), actualBeer.getName() );
            assertEquals( expectedBeer.getStyle(), actualBeer.getStyle().name() );
            assertEquals( expectedBeer.getUpc(), actualBeer.getUpc() );
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
            final BeerDto testBeerDto = TestBeerDto.getBeerDtoWithoutQuantity();

            // when
            when(beerRepository.findById(any( UUID.class ))).thenThrow( NotFoundException.class );
            Executable executable = () -> beerService.updateBeer( beerId, testBeerDto );

            // then
            assertThrows( NotFoundException.class, executable );
            verify(beerRepository, times(1)).findById(any( UUID.class ));
        }
    }

    @Nested
    @DisplayName("listBeers() Method")
    class ListBeersTest
    {
        @Test
        @DisplayName("should return BeerPagedList of all beers with name, style, and QoH")
        void givenBeerName_andStyle_andInventory_whenListBeers_thenBeerList_withNameAndStyleAndQuantity()
        {
            // given
            final PageRequest pageRequest = PageRequest.of( 1, 1 );
            final Beer beer = TestBeer.getBeerWithId();
            final BeerDto beerDto = TestBeerDto.getBeerDtoWithQuantity( beer );

            Page<Beer> beerPage = getTestBeerPage( beer, pageRequest );
            final BeerPagedList expected = getTestBeerPagedList( beerDto, pageRequest );

            // when
            when(beerRepository.findAllByNameAndStyle(anyString(), any(BeerStyle.class), any())).thenReturn( beerPage );
            when(beerMapper.toBeerDtoWithInventory(any( Beer.class ))).thenReturn( beerDto );

            BeerPagedList actual = beerService.listBeers(
                    beer.getName(),
                    BeerStyle.valueOf(beer.getStyle()),
                    pageRequest,
                    true
            );

            // then
            assertNotNull( actual );
            assertEquals( expected.getSize(), actual.getSize() );
            assertEquals( expected.getPageable(), actual.getPageable() );
            assertEquals( expected.getTotalPages(), actual.getTotalPages() );

            verify(beerRepository, times(1)).findAllByNameAndStyle(anyString(), any( BeerStyle.class ), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAllByName(anyString(), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAllByStyle(any( BeerStyle.class ), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAll(any( PageRequest.class ));

            verify(beerMapper, times(1)).toBeerDtoWithInventory(any( Beer.class ));
            verify(beerMapper, times(0)).toBeerDto(any( Beer.class ));
        }

        @Test
        @DisplayName("should return BeerPagedList of all beers with name and style")
        void givenBeerName_andStyle_whenListBeers_thenBeerList_withNameAndStyle()
        {
            // given
            final PageRequest pageRequest = PageRequest.of( 1, 1 );
            final Beer beer = TestBeer.getBeerWithId();
            final BeerDto beerDto = TestBeerDto.getBeerDtoWithoutQuantity( beer );

            Page<Beer> beerPage = getTestBeerPage( beer, pageRequest );
            final BeerPagedList expected = getTestBeerPagedList( beerDto, pageRequest );

            // when
            when(beerRepository.findAllByNameAndStyle(anyString(), any(BeerStyle.class), any())).thenReturn( beerPage );
            when(beerMapper.toBeerDto(any( Beer.class ))).thenReturn( beerDto );

            BeerPagedList actual = beerService.listBeers(
                    beer.getName(),
                    BeerStyle.valueOf(beer.getStyle()),
                    pageRequest,
                    false
            );

            // then
            assertNotNull( actual );
            assertEquals( expected.getSize(), actual.getSize() );
            assertEquals( expected.getPageable(), actual.getPageable() );
            assertEquals( expected.getTotalPages(), actual.getTotalPages() );

            verify(beerRepository, times(1)).findAllByNameAndStyle(anyString(), any( BeerStyle.class ), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAllByName(anyString(), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAllByStyle(any( BeerStyle.class ), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAll(any( PageRequest.class ));

            verify(beerMapper, times(0)).toBeerDtoWithInventory(any( Beer.class ));
            verify(beerMapper, times(1)).toBeerDto(any( Beer.class ));
        }

        @Test
        @DisplayName("should return BeerPagedList of all beers with name and QoH")
        void givenBeerName_andInventory_whenListBeers_thenBeerList_withNameAndQuantity()
        {
            // given
            final PageRequest pageRequest = PageRequest.of( 1, 1 );
            final Beer beer = TestBeer.getBeerWithId();
            final BeerDto beerDto = TestBeerDto.getBeerDtoWithQuantity( beer );

            Page<Beer> beerPage = getTestBeerPage( beer, pageRequest );
            final BeerPagedList expected = getTestBeerPagedList( beerDto, pageRequest );

            // when
            when(beerRepository.findAllByName( anyString(), any(PageRequest.class ))).thenReturn( beerPage );
            when(beerMapper.toBeerDtoWithInventory(any( Beer.class ))).thenReturn( beerDto );

            BeerPagedList actual = beerService.listBeers(
                    beer.getName(),
                    null,
                    pageRequest,
                    true
            );

            // then
            assertNotNull( actual );
            assertEquals( expected.getSize(), actual.getSize() );
            assertEquals( expected.getPageable(), actual.getPageable() );
            assertEquals( expected.getTotalPages(), actual.getTotalPages() );

            verify(beerRepository, times(0)).findAllByNameAndStyle(anyString(), any( BeerStyle.class ), any( PageRequest.class ));
            verify(beerRepository, times(1)).findAllByName(anyString(), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAllByStyle(any( BeerStyle.class ), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAll(any( PageRequest.class ));

            verify(beerMapper, times(1)).toBeerDtoWithInventory(any( Beer.class ));
            verify(beerMapper, times(0)).toBeerDto(any( Beer.class ));
        }

        @Test
        @DisplayName("should return BeerPagedList of all beers with name")
        void givenBeerName_whenListBeers_thenBeerList_withName()
        {
            // given
            final PageRequest pageRequest = PageRequest.of( 1, 1 );
            final Beer beer = TestBeer.getBeerWithId();
            final BeerDto beerDto = TestBeerDto.getBeerDtoWithoutQuantity( beer );

            Page<Beer> beerPage = getTestBeerPage( beer, pageRequest );
            final BeerPagedList expected = getTestBeerPagedList( beerDto, pageRequest );

            // when
            when(beerRepository.findAllByName( anyString(), any(PageRequest.class ))).thenReturn( beerPage );
            when(beerMapper.toBeerDto(any( Beer.class ))).thenReturn( beerDto );

            BeerPagedList actual = beerService.listBeers(
                    beer.getName(),
                    null,
                    pageRequest,
                    false
            );

            // then
            assertNotNull( actual );
            assertEquals( expected.getSize(), actual.getSize() );
            assertEquals( expected.getPageable(), actual.getPageable() );
            assertEquals( expected.getTotalPages(), actual.getTotalPages() );

            verify(beerRepository, times(0)).findAllByNameAndStyle(anyString(), any( BeerStyle.class ), any( PageRequest.class ));
            verify(beerRepository, times(1)).findAllByName(anyString(), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAllByStyle(any( BeerStyle.class ), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAll(any( PageRequest.class ));

            verify(beerMapper, times(0)).toBeerDtoWithInventory(any( Beer.class ));
            verify(beerMapper, times(1)).toBeerDto(any( Beer.class ));
        }

        @Test
        @DisplayName("should return BeerPagedList of all beers with style and QoH")
        void givenBeerStyle_andInventory_whenListBeers_thenBeerList_withStyleAndQuantity()
        {
            // given
            final PageRequest pageRequest = PageRequest.of( 1, 1 );
            final Beer beer = TestBeer.getBeerWithId();
            final BeerDto beerDto = TestBeerDto.getBeerDtoWithQuantity( beer );

            Page<Beer> beerPage = getTestBeerPage( beer, pageRequest );
            final BeerPagedList expected = getTestBeerPagedList( beerDto, pageRequest );

            // when
            when(beerRepository.findAllByStyle(any( BeerStyle.class ), any())).thenReturn( beerPage );
            when(beerMapper.toBeerDtoWithInventory(any( Beer.class ))).thenReturn( beerDto );

            BeerPagedList actual = beerService.listBeers(
                    null,
                    BeerStyle.valueOf(beer.getStyle()),
                    pageRequest,
                    true
            );

            // then
            assertNotNull( actual );
            assertEquals( expected.getSize(), actual.getSize() );
            assertEquals( expected.getPageable(), actual.getPageable() );
            assertEquals( expected.getTotalPages(), actual.getTotalPages() );

            verify(beerRepository, times(0)).findAllByNameAndStyle(anyString(), any( BeerStyle.class ), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAllByName(anyString(), any( PageRequest.class ));
            verify(beerRepository, times(1)).findAllByStyle(any( BeerStyle.class ), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAll(any( PageRequest.class ));

            verify(beerMapper, times(1)).toBeerDtoWithInventory(any( Beer.class ));
            verify(beerMapper, times(0)).toBeerDto(any( Beer.class ));
        }

        @Test
        @DisplayName("should return BeerPagedList of all beers with style")
        void givenBeerStyle_whenListBeers_thenBeerList_withStyle()
        {
            // given
            final PageRequest pageRequest = PageRequest.of( 1, 1 );
            final Beer beer = TestBeer.getBeerWithId();
            final BeerDto beerDto = TestBeerDto.getBeerDtoWithoutQuantity( beer );

            Page<Beer> beerPage = getTestBeerPage( beer, pageRequest );
            final BeerPagedList expected = getTestBeerPagedList( beerDto, pageRequest );

            // when
            when(beerRepository.findAllByStyle(any( BeerStyle.class ), any())).thenReturn( beerPage );
            when(beerMapper.toBeerDto(any( Beer.class ))).thenReturn( beerDto );

            BeerPagedList actual = beerService.listBeers(
                    null,
                    BeerStyle.valueOf(beer.getStyle()),
                    pageRequest,
                    false
            );

            // then
            assertNotNull( actual );
            assertEquals( expected.getSize(), actual.getSize() );
            assertEquals( expected.getPageable(), actual.getPageable() );
            assertEquals( expected.getTotalPages(), actual.getTotalPages() );

            verify(beerRepository, times(0)).findAllByNameAndStyle(anyString(), any( BeerStyle.class ), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAllByName(anyString(), any( PageRequest.class ));
            verify(beerRepository, times(1)).findAllByStyle(any( BeerStyle.class ), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAll(any( PageRequest.class ));

            verify(beerMapper, times(0)).toBeerDtoWithInventory(any( Beer.class ));
            verify(beerMapper, times(1)).toBeerDto(any( Beer.class ));
        }

        @Test
        @DisplayName("should return BeerPagedList of all beers with QoH")
        void givenShowInventory_whenListBeers_thenBeerList_ofAllBeersAndQuantity()
        {
            // given
            final PageRequest pageRequest = PageRequest.of( 1, 1 );
            final Beer beer = TestBeer.getBeerWithId();
            final BeerDto beerDto = TestBeerDto.getBeerDtoWithQuantity( beer );

            Page<Beer> beerPage = getTestBeerPage( beer, pageRequest );
            final BeerPagedList expected = getTestBeerPagedList( beerDto, pageRequest );

            // when
            when(beerRepository.findAll(any( PageRequest.class ))).thenReturn( beerPage );
            when(beerMapper.toBeerDtoWithInventory(any( Beer.class ))).thenReturn( beerDto );

            BeerPagedList actual = beerService.listBeers(
                    null,
                    null,
                    pageRequest,
                    true
            );

            // then
            assertNotNull( actual );
            assertEquals( expected.getSize(), actual.getSize() );
            assertEquals( expected.getPageable(), actual.getPageable() );
            assertEquals( expected.getTotalPages(), actual.getTotalPages() );

            verify(beerRepository, times(0)).findAllByNameAndStyle(anyString(), any( BeerStyle.class ), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAllByName(anyString(), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAllByStyle(any( BeerStyle.class ), any( PageRequest.class ));
            verify(beerRepository, times(1)).findAll(any( PageRequest.class ));

            verify(beerMapper, times(1)).toBeerDtoWithInventory(any( Beer.class ));
            verify(beerMapper, times(0)).toBeerDto(any( Beer.class ));
        }

        @Test
        @DisplayName("should return BeerPagedList of all beers")
        void whenListBeers_thenBeerList_ofAllBeers()
        {
            // given
            final PageRequest pageRequest = PageRequest.of( 1, 1 );
            final Beer beer = TestBeer.getBeerWithId();
            final BeerDto beerDto = TestBeerDto.getBeerDtoWithoutQuantity( beer );

            Page<Beer> beerPage = getTestBeerPage( beer, pageRequest );
            final BeerPagedList expected = getTestBeerPagedList( beerDto, pageRequest );

            // when
            when(beerRepository.findAll(any( PageRequest.class ))).thenReturn( beerPage );
            when(beerMapper.toBeerDto(any( Beer.class ))).thenReturn( beerDto );

            BeerPagedList actual = beerService.listBeers(
                    null,
                    null,
                    pageRequest,
                    false
            );

            // then
            assertNotNull( actual );
            assertEquals( expected.getSize(), actual.getSize() );
            assertEquals( expected.getPageable(), actual.getPageable() );
            assertEquals( expected.getTotalPages(), actual.getTotalPages() );

            verify(beerRepository, times(0)).findAllByNameAndStyle(anyString(), any( BeerStyle.class ), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAllByName(anyString(), any( PageRequest.class ));
            verify(beerRepository, times(0)).findAllByStyle(any( BeerStyle.class ), any( PageRequest.class ));
            verify(beerRepository, times(1)).findAll(any( PageRequest.class ));

            verify(beerMapper, times(0)).toBeerDtoWithInventory(any( Beer.class ));
            verify(beerMapper, times(1)).toBeerDto(any( Beer.class ));
        }

    }

    //==================================================================================================================
    private BeerPagedList getTestBeerPagedList(BeerDto beerDto, PageRequest pageRequest)
    {
        List<BeerDto> content = List.of( beerDto );
        return new BeerPagedList( content, pageRequest, content.size() );
    }

    private Page<Beer> getTestBeerPage(Beer beer, PageRequest pageRequest)
    {
        return new PageImpl<>(List.of( beer ), pageRequest, 1);
    }
}