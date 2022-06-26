package main;
import java.util.List;
import java.util.ArrayList;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class Sample {

	List<Branch> storage = new ArrayList<>();
	
	@GetMapping("/api/branch/list")
	ResponseEntity<List<Branch>> list() {
		return ResponseEntity.status(200).body(storage);
	}
	
	@PutMapping ("/api/branch/create")
	ResponseEntity<String> createByPut(
			HttpServletRequest request, 
			String name, 
			double area) {
		return createByPost(request, name, area);
	}
	
	@PostMapping("/api/branch/create")
	ResponseEntity<String> createByPost(
			HttpServletRequest request, 
			String name, 
			double area) {
		boolean pass = request.getRemoteAddr().equals("127.0.0.1");
		if (pass) {
			Branch b = new Branch(name, area);
			storage.add(b);
			return ResponseEntity.status(201).body("Created");
		} else {
			return ResponseEntity.status(405).body("Method Not Allowed");
		}
	}
}

record Branch(String name, double area) { }
