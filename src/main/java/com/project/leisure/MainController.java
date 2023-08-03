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
        // Create a new Visit object and save it to the database
        Visit visit = new Visit();
        visit.setVisit_count(1L);
        visitService.save(visit);

        List<Accommodation> allAccommodations = this.accommodationService.my_acc_list();

        // Sort the accommodations by acc_rating in descending order
        allAccommodations.sort(Comparator.comparingInt(Accommodation::getAcc_rating).reversed());

        // Get the top 6 accommodations based on acc_rating
        List<Accommodation> topAccommodations = allAccommodations.stream().limit(6).collect(Collectors.toList());

        // Add the top 6 accommodations to the model
        model.addAttribute("acc", topAccommodations);
        
        
        return "main";
    }
}