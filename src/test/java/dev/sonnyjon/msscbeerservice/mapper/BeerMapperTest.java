package dev.sonnyjon.msscbeerservice.mapper;

import dev.sonnyjon.msscbeerservice.cases.TestBeer;
import dev.sonnyjon.msscbeerservice.cases.TestBeerDto;
import dev.sonnyjon.msscbeerservice.dto.BeerDto;
import dev.sonnyjon.msscbeerservice.model.beer.Beer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Sonny on 9/16/2022.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {BeerMapperImpl.class, DateMapper.class})
class BeerMapperTest
{
    @Autowired
    private BeerMapper beerMapper;

    @Nested
    @DisplayName("toBeerDto() Method")
    class toDto
    {
        @Test
        @DisplayName("should convert Beer and return as BeerDto")
        void givenBeer_whenToBeerDto_thenConvertToDto()
        {
            // given
            final Beer expected = TestBeer.getBeer();

            // when
            BeerDto actual = beerMapper.toBeerDto( expected );

            // then
            assertNotNull( actual );
            assertEquals( expected.getName(), actual.getName() );
            assertEquals( expected.getStyle(), actual.getStyle().name() );
            assertEquals( expected.getUpc(), actual.getUpc() );
            assertEquals( expected.getPrice(), actual.getPrice() );
        }

        @Test
        @DisplayName("should return null")
        void givenNull_whenToBeerDto_thenNull()
        {
            final Beer expected = null;                         // given
            BeerDto actual = beerMapper.toBeerDto( expected );  // when

            assertNull( actual );                               // then
        }
    }

    @Nested
    @DisplayName("toBeer() Method")
    class toEntity
    {
        @Test
        @DisplayName("should convert BeerDto and return as Beer")
        void givenBeerDto_whenToBeer_thenConvertToEntity()
        {
            // given
            final BeerDto expected = TestBeerDto.getBeerDto();

            // when
            Beer actual = beerMapper.toBeer( expected );

            // then
            assertNotNull( actual );
            assertEquals( expected.getName(), actual.getName() );
            assertEquals( expected.getStyle().name(), actual.getStyle() );
            assertEquals( expected.getUpc(), actual.getUpc() );
            assertEquals( expected.getPrice(), actual.getPrice() );
        }

        @Test
        @DisplayName("should return null")
        void givenNull_whenToBeer_thenNull()
        {
            final BeerDto expected = null;                  // given
            Beer actual = beerMapper.toBeer( expected );    // when

            assertNull( actual );                           // then
        }
    }

    @Disabled
    @Nested
    @DisplayName("toBeerDtoWithInventory() Method")
    class toDtoWithInventory
    {
        @Test
        @DisplayName("should convert Beer and return as Beer Dto with Quantity on Hand")
        void givenBeerDto_andInventory_whenToBeerDtoWithInventory_thenConvertToDtoWithQuantity()
        {
            fail("not implemented");
        }
    }
}