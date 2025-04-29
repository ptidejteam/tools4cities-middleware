package ca.concordia.encs.citydata.core;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * This is the running checkpoint of the Spring Boot Application.
 * 
 * @author Minette Zongo
 * @date 2025-04-22
 */

@RestController
@RequestMapping("/health")
public class ApplicationHealthController {

	@GetMapping("/ping")
	public String ping() {
		return "Application up and working! ";
	}
}