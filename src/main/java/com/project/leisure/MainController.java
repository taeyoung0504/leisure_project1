package com.project.leisure;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.leisure.taeyoung.statistics.Visit;
import com.project.leisure.taeyoung.statistics.VisitService;
import com.project.leisure.yuri.product.Accommodation;
import com.project.leisure.yuri.product.AccommodationService;



@Controller
public class MainController {

    private final VisitService visitService;
    private final AccommodationService accommodationService;
    @Autowired
    public MainController(VisitService visitService, AccommodationService accommodationService) {
        this.visitService = visitService;
        this.accommodationService = accommodationService;
    }

    @GetMapping("/")
    public String main(Model model) {
    	
        // 방문자 데이터 수집을 위한 처리(일정량 이상 데이터 수집 후 동일 ip당 1일 1회로 제한 예정)
        Visit visit = new Visit();
        visit.setVisit_count(1L);
        visitService.save(visit);

        // 숙박업소 리스트 가져오기
        List<Accommodation> allAccommodations = this.accommodationService.my_acc_list();

        // 숙박업소의 평점에 대한 역순 정렬 
        allAccommodations.sort(Comparator.comparingInt(Accommodation::getAcc_rating).reversed());

        //평점 상위 8개의 업소 추출
        List<Accommodation> topAccommodations = allAccommodations.stream().limit(8).collect(Collectors.toList());

       
        model.addAttribute("acc", topAccommodations);
        
        
        return "main";
    }
    
   
   
}