/**
 * 
 */
package org.roadking;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.roadking.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author roadking
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EkplirosApplication.class)
@WebAppConfiguration
public class RepositoryTest {

    @Autowired
    private UserRoleRepository userRole;
    
    @Before
    public void before_test(){
    	System.out.println("Before test");
    }
    
	@Test
	public void test() {
		assertNotNull(userRole.findAll());
	}

}
