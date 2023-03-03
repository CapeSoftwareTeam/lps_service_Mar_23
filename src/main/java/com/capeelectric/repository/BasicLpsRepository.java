/**
 * 
 */
package com.capeelectric.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.capeelectric.model.BasicLps;


/**
 * @author CAPE-SOFTWARE
 *
 */
public interface BasicLpsRepository extends CrudRepository<BasicLps, Integer> {

	public List<BasicLps> findByUserNameAndBasicLpsId(String userName, Integer basicLpsId);

	public Optional<BasicLps> findByBasicLpsId(Integer basicLpsId);
	
	public Optional<BasicLps> findByClientNameAndStatus(String clientName,String active);

	public List<BasicLps> findByUserName(String userName);

	public Optional<BasicLps> findByClientName(String clientName);
 
	public Optional<BasicLps>  findByMailIdAndStatus(String userName, String string);

	public Optional<BasicLps> findByClientNameAndStatusAndProjectName(String clientName, String active, String projectName);

	@Query(value="SELECT * FROM lv_safety_verification.basic_lps_table where USER_NAME != 'awstesting@rushforsafety.com'",nativeQuery = true)
	public List<BasicLps> findByUserName(String userName,String name);
}
