package dev.sonnyjon.msscbeerservice.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sonnyjon.msscbeerservice.web.dto.BeerDto;
import dev.sonnyjon.msscbeerservice.web.dto.BeerStyle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Sonny on 8/22/2022.
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(BeerController.class)
class BeerControllerTest
{
    public static final String URL_ROOT = "/api/v1/beer/";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
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
    void givenUUID_whenGetBeerById_thenStatusOk() throws Exception
    {
        // given
        final BeerDto testBeer = getTestBeer();
        testBeer.setId( UUID.randomUUID() );
        final String url = URL_ROOT + testBeer.getId().toString();

        // when, then
        mockMvc.perform(get( url )
                        .accept( MediaType.APPLICATION_JSON ))
                .andExpect( status().isOk() );
    }

    @Test
    void givenBeer_whenSaveNewBeer_thenStatusCreated() throws Exception
    {
        // given
        final BeerDto testBeer = getTestBeer();
        final String beerContent = objectMapper.writeValueAsString( testBeer );

        // when, then
        mockMvc.perform(post( URL_ROOT )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( beerContent ))
                .andExpect( status().isCreated() );
    }

    @Test
    void givenIdAndBeer_whenUpdateBeerById_thenStatusNoContent() throws Exception
    {
        // given
        final BeerDto testBeer = getTestBeer();
        final String url = URL_ROOT + UUID.randomUUID();
        final String beerContent = objectMapper.writeValueAsString( testBeer );

        // when, then
        mockMvc.perform(put( url )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( beerContent ))
                .andExpect( status().isNoContent() );
    }

    private BeerDto getTestBeer()
    {
        return BeerDto.builder()
                .name( "My Beer" )
                .style( BeerStyle.ALE )
                .price(new BigDecimal( "2.99" ))
                .upc( 123456789012L )
                .build();
    }

}