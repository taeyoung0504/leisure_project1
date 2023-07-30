package com.project.leisure.taeyoung.statistics;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/api")
@Getter
@Setter
public class StatisticsAPI {
	 private final JdbcTemplate jdbcTemplate;

	    @Autowired
	    public StatisticsAPI(JdbcTemplate jdbcTemplate) {
	        this.jdbcTemplate = jdbcTemplate;
	    }

	    @GetMapping("/roles")
	    public String getUserRolesCount() throws JsonProcessingException {
	    	String sql = "SELECT " +
	                "CASE " +
	                "    WHEN role = 'ADMIN' THEN '관리자' " +
	                "    WHEN role = 'PARTNER' THEN '파트너' " +
	                "    WHEN role = 'USER' THEN '일반회원' " +
	                "    WHEN role = 'SNS_USER' THEN '소셜회원' " +
	                "    ELSE role " +
	                "END AS role, " +
	                "COUNT(*) AS count " +
	                "FROM users " +
	                "GROUP BY role";
	        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);

	        ObjectMapper objectMapper = new ObjectMapper();
	        return objectMapper.writeValueAsString(result);
	    }
	    
	    /* 업종 통계*/
	    
	    @GetMapping("/sector")
	    public String getSectorsCount() throws JsonProcessingException {
	        String sql = "SELECT acc_sectors, COUNT(*) AS sector_count FROM accommodation GROUP BY acc_sectors";
	        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);

	        ObjectMapper objectMapper = new ObjectMapper();
	        return objectMapper.writeValueAsString(result);
	    }
	    
	    /* 금일 예약 통계 */
	    @GetMapping("/today_booking")
	    public String getTodayBookCount() throws JsonProcessingException {
	        String sql = "SELECT COUNT(*) AS count_today_bookings FROM bookingvo WHERE DATE(payment_date) = CURDATE();";
	        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);

	        // Extracting the count_today_bookings value from the result
	        int countTodayBookings = 0;
	        if (!result.isEmpty()) {
	            Map<String, Object> row = result.get(0);
	            countTodayBookings = ((Number) row.get("count_today_bookings")).intValue();
	        }

	        return String.valueOf(countTodayBookings);
	    }
	    
	    /* 예약매출 통계 */
	    @GetMapping("/today_benefit_total")
	    public String getTodaybenefit_total() throws JsonProcessingException {
	        String sql = "SELECT FORMAT(SUM(REPLACE(total_price, ',', '')), 0) AS count_today_bookings FROM bookingvo WHERE DATE(payment_date) = CURDATE()";
	        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);

	        // Extracting the count_today_bookings value from the result
	        String countTodayBookings = "0";
	        if (!result.isEmpty()) {
	            Map<String, Object> row = result.get(0);
	            countTodayBookings = (String) row.get("count_today_bookings");
	        }

	        return countTodayBookings;
	    }
	    
	    
	    
	    /* 정산  건 수 통계 
	    @GetMapping("/today_cal")
	    public String getTodaycal_total() throws JsonProcessingException {
	        String sql = "SELECT COUNT(*) AS count_today_bookings " +
	                     "FROM bookingvo " +
	                     "WHERE book_status = '이용완료' " +
	                     "AND DATE(payment_date) = DATE_SUB(CURDATE(), INTERVAL 15 DAY)";

	        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);

	        // Extracting the count_today_bookings value from the result
	        String countTodayBookings = "0";
	        if (!result.isEmpty()) {
	            Map<String, Object> row = result.get(0);
	            countTodayBookings = String.valueOf(row.get("count_today_bookings"));
	        }

	        return countTodayBookings;
	    }
	    */
	    
	    /* 정산 금액 통계 
	    @GetMapping("/today_cal_price")
	    public String getTodaycal_total2() throws JsonProcessingException {
	        String sql = "SELECT FORMAT(SUM(REPLACE(total_price, ',', '')), 0) AS total_sum " +
	                     "FROM bookingvo " +
	                     "WHERE book_status = '이용완료' " +
	                     "AND payment_date = DATE_SUB(CURDATE(), INTERVAL 15 DAY)";

	        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);

	        String totalSum = "0";
	        if (!result.isEmpty()) {
	            Map<String, Object> row = result.get(0);
	            totalSum = String.valueOf(row.get("total_sum"));
	        }

	        return totalSum;
	    }
	    */
}