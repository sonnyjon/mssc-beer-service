package dev.sonnyjon.msscbeerservice.web.controllers;

import dev.sonnyjon.msscbeerservice.services.BeerService;
import dev.sonnyjon.msscbeerservice.web.dto.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/v1/beer")
@Slf4j
public class BeerController
{
    private final BeerService beerService;

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId)
    {
        return new ResponseEntity<>( beerService.getById(beerId), HttpStatus.OK );
    }

    @PostMapping
    public ResponseEntity saveNewBeer(@Validated @RequestBody BeerDto beerDto)
    {
        return new ResponseEntity<>(  beerService.saveNewBeer( beerDto ), HttpStatus.CREATED );
    }

    @PutMapping("/{beerId}")
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDto beerDto)
    {
        return new ResponseEntity<>( beerService.updateBeer(beerId, beerDto), HttpStatus.NO_CONTENT );
    }
}
