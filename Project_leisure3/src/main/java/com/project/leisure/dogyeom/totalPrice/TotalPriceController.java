package com.project.leisure.dogyeom.totalPrice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/price/*")
@Slf4j
public class TotalPriceController {
	
	private final TotalPriceRepository totalPriceRepository;
	
	@Autowired
    public TotalPriceController(TotalPriceRepository totalPriceRepository) {
        this.totalPriceRepository = totalPriceRepository;
    }
	
	@PostMapping("/savePrice")
    public ResponseEntity<String> saveData(@RequestBody List<String> reservePriceArray) {
        // 받은 배열 데이터를 객체에 하나씩 집어넣고, 해당 객체들을 서비스나 리포지토리를 호출하여 테이블에 저장하는 로직을 작성
		
        List<TotalPrice> totalPriceArray = new ArrayList<>();
        for (String price : reservePriceArray) {
        	TotalPrice totalPrice = new TotalPrice();
        	totalPrice.setTotalPrice(price); // 예시로 받은 배열의 요소를 DataEntity 객체에 넣는다.
        	totalPriceArray.add(totalPrice);
        }

        totalPriceRepository.saveAll(totalPriceArray); // 테이블에 저장하는 리포지토리 메서드를 호출.

//        return ResponseEntity.ok("Data saved successfully");
     // JSON 형식으로 응답 데이터 생성 - JSON형식으로 응답 데이터를 줘야하기 때문에 객체 선언함.
        JSONObject responseJson = new JSONObject();
        responseJson.put("message", "Data saved successfully");

        // ResponseEntity로 JSON 형식의 응답 반환
        return ResponseEntity.ok(responseJson.toString());
    }
	
	
}