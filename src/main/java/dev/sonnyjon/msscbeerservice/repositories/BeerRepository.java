package dev.sonnyjon.msscbeerservice.repositories;

import dev.sonnyjon.msscbeerservice.model.Beer;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

/**
 * Created by Sonny on 8/22/2022.
 */
public interface BeerRepository extends PagingAndSortingRepository<Beer, UUID>
{
}
