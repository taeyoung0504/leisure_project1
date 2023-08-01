package com.project.leisure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.leisure.taeyoung.statistics.Visit;
import com.project.leisure.taeyoung.statistics.VisitService;



@Controller
public class MainController {

    private final VisitService visitService;

    @Autowired
    public MainController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping("/")
    public String main() {
        // Create a new Visit object and save it to the database
        Visit visit = new Visit();
        visit.setVisit_count(1L);
        visitService.save(visit);
        
        return "main";
    }
}