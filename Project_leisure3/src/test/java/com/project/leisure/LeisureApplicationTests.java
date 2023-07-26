package com.project.leisure;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.security.Principal;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.project.leisure.taeyoung.review.Declaration;
import com.project.leisure.taeyoung.review.ReviewController;
import com.project.leisure.taeyoung.review.ReviewService;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class LeisureApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @Test
    void testSubmitDeclaration() throws Exception {
        Long reviewId = 36L;
        String declaration = "Reason for declaration";
        String declarationDetail = "Declaration detail";
        String reporter = "test_user"; // Set the reporter value as needed

        Declaration newDeclaration = new Declaration();
        // Set up the mock behavior for reviewService.addDeclarationToReview
        when(reviewService.addDeclarationToReview(any(), any(), any(), any())).thenReturn(newDeclaration);

        // Mock the Principal object with the desired username
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn(reporter);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/decl")
                .param("reviewId", String.valueOf(reviewId))
                .param("reason", declaration)
                .param("decl_detail", declarationDetail)
                .with(request -> {
                    request.setRemoteUser(reporter); // Use setRemoteUser instead of setUserPrincipal
                    return request;
                })
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/review"));

        // Add more assertions as needed based on your use case
    }
}