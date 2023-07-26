package com.project.leisure.dogyeom.totalPrice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TotalPriceRepository extends JpaRepository<TotalPrice, Long> {
	// JPQL 쿼리를 @Query 어노테이션을 이용하여 작성
	// 사용 후엔 명시적으로 삭제 해줘야함 자동 삭제가 되지만 혹시 모르기 때문.
	// 근데 내가 테이블을 생성안하려고 임시테이블을 만들었는데 이러면 쓰는 이유가 없는 것 같은데
	// 임시테이블 쓰는 방법을 알아봐야함.
    @Query(value = "CREATE TEMPORARY TABLE temp_total_price AS SELECT * FROM total_price", nativeQuery = true)
    void createTempTable();
    
    List<TotalPrice> findByTotalPrice(String totalPrice);
}