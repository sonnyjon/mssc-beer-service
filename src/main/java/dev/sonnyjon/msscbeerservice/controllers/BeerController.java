package dev.sonnyjon.msscbeerservice.controllers;

import dev.sonnyjon.msscbeerservice.dto.BeerDto;
import dev.sonnyjon.msscbeerservice.model.beer.BeerPagedList;
import dev.sonnyjon.msscbeerservice.model.beer.BeerStyle;
import dev.sonnyjon.msscbeerservice.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by Sonny on 8/21/2022.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class BeerController
{
    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final BeerService beerService;

    @GetMapping(produces = { "application/json" }, path = "beer")
    public ResponseEntity<BeerPagedList> listBeers(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "name", required = false) String beerName,
            @RequestParam(value = "style", required = false) BeerStyle beerStyle,
            @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand)
    {
        pageNumber = (pageNumber == null || pageNumber < 0) ? DEFAULT_PAGE_NUMBER : pageNumber;
        pageSize = (pageSize == null || pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;

        BeerPagedList beerList = beerService.listBeers(
                beerName,
                beerStyle,
                PageRequest.of(pageNumber, pageSize),
                showInventoryOnHand
        );

        return new ResponseEntity<>(beerList, HttpStatus.OK);
    }

    @PostMapping(path = "beer")
    public ResponseEntity saveNewBeer(@Validated @RequestBody BeerDto beerDto)
    {
        return new ResponseEntity<>(  beerService.saveNewBeer( beerDto ), HttpStatus.CREATED );
    }

    @GetMapping("beer/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(
            @PathVariable("beerId") UUID beerId,
            @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand)
    {
        showInventoryOnHand = showInventoryOnHand != null && showInventoryOnHand;

        return new ResponseEntity<>( beerService.getById(beerId, showInventoryOnHand), HttpStatus.OK );
    }

    @GetMapping("beerUpc/{upc}")
    public ResponseEntity<BeerDto> getBeerByUpc(@PathVariable("upc") String upc)
    {
        return new ResponseEntity<>( beerService.getByUpc(upc), HttpStatus.OK );
    }

    @PutMapping("beer/{beerId}")
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDto beerDto)
    {
        return new ResponseEntity<>( beerService.updateBeer(beerId, beerDto), HttpStatus.NO_CONTENT );
    }
}
