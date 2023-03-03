/**
 * 
 */
package com.capeelectric.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.capeelectric.model.SummaryLpsBuildings;

/**
 * @author CAPE-SOFTWARE
 *
 */
public interface SummaryBuildingRepository extends CrudRepository<SummaryLpsBuildings, Integer>{

	Optional<SummaryLpsBuildings> findByBuildingCount(Integer buildingCount);

}
