/**
 * 
 */
package com.capeelectric.repository;

import org.springframework.data.repository.CrudRepository;

import com.capeelectric.model.SummaryLpsBuildings;
import com.capeelectric.model.SummaryLpsObservation;

/**
 * @author CAPE-SOFTWARE
 *
 */
public interface SummaryLpsListRepository extends CrudRepository<SummaryLpsBuildings, Integer>{

	public SummaryLpsBuildings findByBuildingCount(Integer buildingCount);
}
