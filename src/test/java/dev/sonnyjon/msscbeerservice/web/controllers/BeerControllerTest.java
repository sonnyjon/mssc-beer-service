package dev.sonnyjon.msscbeerservice.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sonnyjon.msscbeerservice.services.BeerService;
import dev.sonnyjon.msscbeerservice.model.BeerDto;
import dev.sonnyjon.msscbeerservice.model.BeerStyle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Sonny on 8/22/2022.
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(BeerController.class)
class BeerControllerTest
{
    public static final String PATH_ROOT = "/api/v1/beer/";

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

    @Test
    void givenValid_UUID_whenGetBeerById_thenBeerDto() throws Exception
    {
        // given
        final BeerDto beerDto = getTestBeerDto();
        final String PATH = PATH_ROOT + UUID.randomUUID();

        // when, then
        when(beerService.getById( any(UUID.class) )).thenReturn( beerDto );

        mockMvc.perform(get( PATH )
                        .accept( MediaType.APPLICATION_JSON ))
                .andExpect( status().isOk() );
    }

    @Test
    void givenInvalid_UUID_whenGetBeerById_thenThrowException() throws Exception
    {
        // given
        final String PATH = PATH_ROOT + UUID.randomUUID();

        // when, then
        when(beerService.getById( any(UUID.class) )).thenThrow( NotFoundException.class );

        mockMvc.perform(get( PATH ))
                .andExpect( status().isNotFound() )
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

        verify(beerService, times(1)).getById( any(UUID.class) );
    }

    @Test
    void givenBeerDto_whenSaveNewBeer_thenSaveBeer() throws Exception
    {
        // given
        final BeerDto testBeer = getTestBeerDto();
        final String beerContent = objectMapper.writeValueAsString( testBeer );

        // when, then
        when(beerService.saveNewBeer( any(BeerDto.class) )).thenReturn( testBeer );

        mockMvc.perform(post(PATH_ROOT)
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( beerContent ))
                .andExpect( status().isCreated() );

        verify(beerService, times(1)).saveNewBeer( any(BeerDto.class) );
    }


    @Test
    void givenValid_UUIDandDto_whenUpdateBeerById_thenSaveBeer() throws Exception
    {
        // given
        final BeerDto testBeer = getTestBeerDto();
        final String beerContent = objectMapper.writeValueAsString( testBeer );
        final String PATH = PATH_ROOT + UUID.randomUUID();

        // when, then
        when(beerService.updateBeer( any(UUID.class), any(BeerDto.class) )).thenReturn( testBeer );

        mockMvc.perform(put( PATH )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( beerContent ))
                .andExpect( status().isNoContent() );

        verify(beerService, times(1)).updateBeer( any(UUID.class), any(BeerDto.class) );
    }

    @Test
    void givenInvalid_UUID_whenUpdateBeerById_thenThrowException() throws Exception
    {
        // given
        final String beerContent = objectMapper.writeValueAsString( getTestBeerDto() );
        final String PATH = PATH_ROOT + UUID.randomUUID();

        // when, then
        when(beerService.updateBeer( any(UUID.class), any(BeerDto.class) )).thenThrow( NotFoundException.class );

        mockMvc.perform(put( PATH )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( beerContent ))
                .andExpect( status().isNotFound() )
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

        verify(beerService, times(1)).updateBeer( any(UUID.class), any(BeerDto.class) );
    }

    private BeerDto getTestBeerDto()
    {
        return BeerDto.builder()
                .name( "My Beer" )
                .style( BeerStyle.ALE )
                .price(new BigDecimal( "2.99" ))
//                .upc( BeerDataLoader.BEER_1_UPC )
                .build();
    }
}