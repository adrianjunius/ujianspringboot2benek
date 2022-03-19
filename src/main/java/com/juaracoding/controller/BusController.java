package com.juaracoding.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.juaracoding.config.JwtTokenUtil;
import com.juaracoding.model.BusModel;
import com.juaracoding.model.KeberangkatanCustomModel;
import com.juaracoding.model.KeberangkatanModel;
import com.juaracoding.model.PenumpangModel;
import com.juaracoding.repository.BookingRepository;
import com.juaracoding.repository.BusRepository;
import com.juaracoding.repository.KeberangkatanRepository;
import com.juaracoding.repository.PenumpangRepository;
import com.juaracoding.service.JwtPenumpangDetailService;

@RestController
@RequestMapping("/busbookingsystem")
public class BusController {
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired	
	BusRepository busRepo;
	
	@Autowired
	PenumpangRepository penumpangRepo;
	
	@Autowired
	KeberangkatanRepository keberangkatanRepo;
	
	@Autowired
	BookingRepository bookingRepo;
	
	@Autowired
	JwtPenumpangDetailService jwtPenumpangDetailService;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	PasswordEncoder pEncoder;
	
	@GetMapping("/")
	private List<BusModel>getAll(){
		return busRepo.findAll();
	}
	
	@GetMapping("/findkeberangkatan")
	private List<KeberangkatanCustomModel> getDataKeberangkatan(
		@RequestParam(name="terminal")String terminal,
		@RequestParam(name="tanggal")String tanggal){
		return keberangkatanRepo.getDataKeberangkatanByTerminalOrTanggal(terminal, tanggal);
	}
	
	@PostMapping("/booking")
	private String saveDataSeat(@RequestBody PenumpangModel penumpang) {
		penumpangRepo.save(penumpang);
		return "pemesanan Bus Berhasil";
	}
	@PostMapping("/cancel")
	private String deleteByBookingId(@RequestParam(name="id")long id) {
		bookingRepo.deleteById(id);
		return "pemesanan dengan id " +id+ " telah dibatalkan";
	}
	
	@PostMapping("/login")
	private ResponseEntity<?> login(@RequestBody PenumpangModel penumpangModel) throws Exception {
		authenticate(penumpangModel.getUsername(),penumpangModel.getPassword());
		
		final UserDetails userDetails = jwtPenumpangDetailService
				.loadUserByUsername(penumpangModel.getUsername());
		
		final String  token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(token);
	}
	
	private void authenticate(String username,String password) throws Exception  {
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException error) {
			//User disabled
			throw new Exception("USER_DISABLED",error);
		} catch (BadCredentialsException error) {
			//invalid credentials
			throw new Exception("INVALID_CREDENTIALS",error);
			}
	}
	@PostMapping("/register")
	private ResponseEntity<String> saveCustomer(@RequestBody PenumpangModel penumpang){
		penumpang.setPassword(pEncoder.encode(penumpang.getPassword()));
		penumpangRepo.save(penumpang);
		return ResponseEntity.status(HttpStatus.CREATED).body("Akun berhasil dibuat");
	}
	

}
