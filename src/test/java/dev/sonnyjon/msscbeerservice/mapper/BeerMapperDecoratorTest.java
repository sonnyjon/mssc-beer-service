package dev.sonnyjon.msscbeerservice.mapper;

import dev.sonnyjon.msscbeerservice.services.inventory.BeerInventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by Sonny on 9/9/2022.
 */
@Disabled // utility for manual testing
@SpringBootTest
class BeerInventoryServiceRestTemplateImplTest
{
    @Autowired
    BeerInventoryService beerInventoryService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getOnhandInventory() {

        //todo evolve to use UPC
        //  Integer qoh = beerInventoryService.getOnhandInventory(BeerLoader.BEER_1_UUID);

        //System.out.println(qoh);
    }
}