package ca.concordia.encs.citydata.core;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class ApplicationHealthController {

	@GetMapping("/ping")
	public String ping() {
		return "Application up and working! ";
	}

//	@GetMapping("/bye")
//	public String sayBye() {
//		return "Good bye";
//	}
//
//	@GetMapping("/welcome")
//	public String sayWelcome() {
//		return "Welcome!";
//	}
//
//	@GetMapping("/{name}")
//	public String name(@PathVariable String name) {
//		return "Hello " + name;
//	}
}