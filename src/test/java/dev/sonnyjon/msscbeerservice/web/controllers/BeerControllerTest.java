package dev.sonnyjon.msscbeerservice.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sonnyjon.msscbeerservice.cases.TestBeerDto;
import dev.sonnyjon.msscbeerservice.cases.TestConstants;
import dev.sonnyjon.msscbeerservice.controllers.BeerController;
import dev.sonnyjon.msscbeerservice.controllers.NotFoundException;
import dev.sonnyjon.msscbeerservice.dto.BeerDto;
import dev.sonnyjon.msscbeerservice.model.beer.BeerPagedList;
import dev.sonnyjon.msscbeerservice.model.beer.BeerStyle;
import dev.sonnyjon.msscbeerservice.services.BeerService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Sonny on 8/22/2022.
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(BeerController.class)
class BeerControllerTest
{
    public static final String PATH_ROOT = "/api/v1/";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;

    AutoCloseable mocks;

    @BeforeEach
    void setUp()
    {
        mocks = MockitoAnnotations.openMocks( this );
    }

    @AfterEach
    void tearDown() throws Exception
    {
        mocks.close();
    }

    @Nested
    @DisplayName("listBeers() Method")
    class ListBeersTest
    {
        @Test
        @DisplayName("should return BeerPagedList for given beer name and style")
        void givenBeerName_andStyle_andInventoryFlag_whenListBeers_thenBeerPagedList() throws Exception
        {
            // given
            final String PATH = PATH_ROOT + "beer";
            BeerPagedList expected = getTestBeerPagedList();

            // when, then
            when(beerService.listBeers(anyString(), any(BeerStyle.class), any(), anyBoolean()))
                    .thenReturn( expected );

            MvcResult result = mockMvc.perform(get( PATH )
                            .accept( MediaType.APPLICATION_JSON )
                            .param("pageNumber", "1")
                            .param("pageSize", "1")
                            .param("name", "Test Beer")
                            .param("style", "IPA")
                            .param("showInventoryOnHand", "false"))
                    .andDo( print() )
                    .andExpect( status().isOk() )
                    .andReturn();

            String jsonValue = result.getResponse().getContentAsString();
            BeerPagedList actual = objectMapper.readValue( jsonValue, BeerPagedList.class );

            assertNotNull( actual );
            assertEquals( expected.getSize(), actual.getSize() );
            assertEquals( expected.getPageable(), actual.getPageable() );
        }
    }

    @Nested
    @DisplayName("saveNewBeer() Method")
    class SaveNewBeerTest
    {
        @Test
        @DisplayName("should save beer info from BeerDto")
        void givenBeerDto_whenSaveNewBeer_thenSaveBeer() throws Exception
        {
            // given
            final String PATH = PATH_ROOT + "beer";
            final BeerDto expected = TestBeerDto.getBeerDto();
            final String beerContent = objectMapper.writeValueAsString( expected );

            // when, then
            when(beerService.saveNewBeer( any(BeerDto.class) )).thenReturn( expected );

            MvcResult result = mockMvc.perform(post( PATH )
                                                .contentType( MediaType.APPLICATION_JSON )
                                                .content( beerContent ))
                                        .andExpect( status().isCreated() )
                                        .andReturn();

            String jsonValue = result.getResponse().getContentAsString();
            BeerDto actual = objectMapper.readValue( jsonValue, BeerDto.class );

            assertNotNull( actual );
            assertEquals( expected.getName(), actual.getName() );
            assertEquals( expected.getStyle().name(), actual.getStyle().name() );
            assertEquals( expected.getUpc(), actual.getUpc() );

            verify(beerService, times(1)).saveNewBeer( any(BeerDto.class) );
        }
    }

    @Nested
    @DisplayName("getBeerById() Method")
    class GetBeerByIdTest
    {
        @Test
        @DisplayName("should return BeerDto with QoH for the given beer ID")
        void givenBeerId_andInventoryFlag_whenGetBeerById_thenBeerDto() throws Exception
        {
            // given
            final BeerDto expected = TestBeerDto.getBeerDtoWithQuantity();
            final String PATH = PATH_ROOT + "beer/" + UUID.randomUUID();

            // when, then
            when(beerService.getById( any(UUID.class), anyBoolean() )).thenReturn( expected );

            MvcResult result = mockMvc.perform(get( PATH )
                                                .accept( MediaType.APPLICATION_JSON ))
                                        .andExpect( status().isOk() )
                                        .andReturn();

            String jsonValue = result.getResponse().getContentAsString();
            BeerDto actual = objectMapper.readValue( jsonValue, BeerDto.class );

            assertNotNull( actual );
            assertNotNull( actual.getQuantityOnHand() );
            assertEquals( expected, actual );
        }

        @Test
        @DisplayName("should return BeerDto without QoH for the given beer ID")
        void givenBeerId_whenGetBeerById_thenBeerDto() throws Exception
        {
            // given
            final BeerDto expected = TestBeerDto.getBeerDtoWithoutQuantity();
            final String PATH = PATH_ROOT + "beer/" + UUID.randomUUID();

            // when, then
            when(beerService.getById( any(UUID.class), anyBoolean() )).thenReturn( expected );

            MvcResult result = mockMvc.perform(get( PATH )
                            .accept( MediaType.APPLICATION_JSON ))
                    .andExpect( status().isOk() )
                    .andReturn();

            String jsonValue = result.getResponse().getContentAsString();
            BeerDto actual = objectMapper.readValue( jsonValue, BeerDto.class );

            assertNotNull( actual );
            assertNull( actual.getQuantityOnHand() );
            assertEquals( expected, actual );
        }

        @Test
        @DisplayName("should throw NotFoundException: Bad beer ID")
        void givenBadBeerId_whenGetBeerById_thenNotFoundException() throws Exception
        {
            // given
            final String PATH = PATH_ROOT + "beer/" + UUID.randomUUID();

            // when, then
            when(beerService.getById( any(UUID.class), anyBoolean() )).thenThrow( NotFoundException.class );

            mockMvc.perform(get( PATH ))
                    .andExpect( status().isNotFound() )
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

            verify(beerService, times(1)).getById( any(UUID.class), anyBoolean() );
        }
    }

    @Nested
    @DisplayName("getBeerByUpc() Method")
    class GetBeerByUpcTest
    {
        @Test
        @DisplayName("should return BeerDto for the given UPC")
        void givenUpc_whenGetBeerByUpc_thenBeerDto() throws Exception
        {
            // given
            final String PATH = PATH_ROOT + "beerUpc/" + TestConstants.BEER_1_UPC;
            BeerDto expected = TestBeerDto.getBeerDto();

            // when, then
            when(beerService.getByUpc( anyString() )).thenReturn( expected );

            MvcResult result = mockMvc.perform(get( PATH )
                                            .accept( MediaType.APPLICATION_JSON ))
                                    .andExpect( status().isOk() )
                                    .andReturn();

            String jsonValue = result.getResponse().getContentAsString();
            BeerDto actual =  objectMapper.readValue( jsonValue, BeerDto.class );

            assertNotNull( actual );
            assertEquals( TestConstants.BEER_1_UPC, actual.getUpc() );

            verify(beerService, times(1)).getByUpc( anyString() );
        }
    }

    @Nested
    @DisplayName("updateBeerById() Method")
    class UpdateBeerById
    {
        @Test
        @DisplayName("should find Beer with the given ID and update Beer from data in DTO")
        void givenBeerId_andBeerDto_whenUpdateBeerById_thenUpdateBeerFromDto() throws Exception
        {
            // given
            final BeerDto expected = TestBeerDto.getBeerDto();
            final String beerContent = objectMapper.writeValueAsString( expected );
            final String PATH = PATH_ROOT + "beer/" + UUID.randomUUID();

            // when, then
            when(beerService.updateBeer( any(UUID.class), any(BeerDto.class) )).thenReturn( expected );

            MvcResult result = mockMvc.perform(put( PATH )
                                                .contentType( MediaType.APPLICATION_JSON )
                                                .content( beerContent ))
                                        .andExpect( status().isNoContent() )
                                        .andReturn();

            String jsonValue =  result.getResponse().getContentAsString();
            BeerDto actual = objectMapper.readValue( jsonValue, BeerDto.class );

            assertNotNull( actual );
            assertEquals( expected.getName(), actual.getName() );
            assertEquals( expected.getStyle().name(), actual.getStyle().name() );
            assertEquals( expected.getUpc(), actual.getUpc() );

            verify(beerService, times(1)).updateBeer( any(UUID.class), any(BeerDto.class) );
        }

        @Test
        @DisplayName("should throw NotFoundException: Bad customer ID")
        void givenBadBeerId_whenUpdateBeerBeerById_thenNotFoundException() throws Exception
        {
            // given
            final String beerContent = objectMapper.writeValueAsString( TestBeerDto.getBeerDto() );
            final String PATH = PATH_ROOT + "beer/" + UUID.randomUUID();

            // when, then
            when(beerService.updateBeer( any(UUID.class), any(BeerDto.class) )).thenThrow( NotFoundException.class );

            mockMvc.perform(put( PATH )
                            .contentType( MediaType.APPLICATION_JSON )
                            .content( beerContent ))
                    .andExpect( status().isNotFound() )
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

            verify(beerService, times(1)).updateBeer( any(UUID.class), any(BeerDto.class) );
        }
    }

    //==================================================================================================================
    private BeerPagedList getTestBeerPagedList()
    {
        List<BeerDto> content = List.of( getTestBeerDto() );
        PageRequest pageRequest = PageRequest.of( 1, 1 );

        return new BeerPagedList(content, pageRequest, 1);
    }

    private BeerDto getTestBeerDto()
    {
        return TestBeerDto.getBeerDto();
    }

}