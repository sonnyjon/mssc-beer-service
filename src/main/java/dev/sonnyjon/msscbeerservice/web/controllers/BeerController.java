package dev.sonnyjon.msscbeerservice.web.controllers;

import dev.sonnyjon.msscbeerservice.web.dto.BeerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by Sonny on 8/21/2022.
 */
@RestController
@RequestMapping("/api/v1/beer")
@Slf4j
public class BeerController
{
    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId)
    {
        // TODO: implementation
        return new ResponseEntity<>(BeerDto.builder().build(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity saveNewBeer(@RequestBody BeerDto beerDto)
    {
        // TODO: implementation
        return new ResponseEntity( HttpStatus.CREATED );
    }

    @PutMapping("/{beerId}")
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDto beerDto)
    {
        // TODO: implementation
        return new ResponseEntity( HttpStatus.NO_CONTENT );
    }
}
