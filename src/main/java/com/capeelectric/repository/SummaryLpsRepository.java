/**
 * 
 */
package com.capeelectric.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.capeelectric.model.SummaryLps;
import com.capeelectric.model.SummaryLpsBuildings;

/**
 * @author CAPE-SOFTWARE
 *
 */
public interface SummaryLpsRepository extends CrudRepository<SummaryLps, Integer>{

	public List<SummaryLps> findByUserNameAndBasicLpsId(String userName, Integer basicLpsId);

	public Optional<SummaryLps> findByBasicLpsIdAndFlag(Integer basicLpsId,String flag);

	public List<SummaryLps> findByBasicLpsId(Integer basicLpsId);

}
