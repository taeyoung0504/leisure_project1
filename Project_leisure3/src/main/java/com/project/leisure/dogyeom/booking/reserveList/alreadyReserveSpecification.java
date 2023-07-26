//package com.project.leisure.dogyeom.booking.reserveList;
//
//import java.time.LocalDate;
//
//import org.springframework.data.jpa.domain.Specification;
//
//import com.project.leisure.dogyeom.booking.BookingVO;
//
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Predicate;
//import jakarta.persistence.criteria.Root;
//
//public class alreadyReserveSpecification {
//	public static Specification<BookingVO> searchByAddressAndCategories(Long accomid, LocalDate currentDate,
//			LocalDate nextDate) {
//	    return new Specification<BookingVO>() {
//	    	
//	    	@Override
//	    	public Predicate toPredicate(Root<BookingVO> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//	    		
//	    		return null;
//	    	}
//	    	public static Specification<BookingVO> aaa() {
//	    		
//	    		return null;
//	    	}
//	    	
//	    	
//
//	    };
//	}
//}