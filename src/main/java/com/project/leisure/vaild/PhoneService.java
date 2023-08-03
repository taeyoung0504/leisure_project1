package com.project.leisure.vaild;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhoneService {
	
	private final PhoneRepository phoneRepository;

	public Phone findPhone(Phone entity, String phoneNumber) {
		
		Phone phone = phoneRepository.findByPhoneNumber(phoneNumber);
		
		if(phone != null) {
			return phone;
		}else {
			return phoneRepository.save(entity);
			}
		}

	public int updateCount(PhoneResDto phoneResDto, String phoneNumber) {
		
		Phone phone = phoneRepository.findByPhoneNumber(phoneNumber);
		
		phone.setCount(phoneResDto.getCount());
		
		Phone phone2 = phoneRepository.save(phone);
		
		if(phone2 != null) {
			return 1;
		}else {
			return 0;
		}
		
	}

	public Phone updateEnableZero(int enable, String phoneNumber) {
		
		Phone phone = phoneRepository.findByPhoneNumber(phoneNumber);
		
		phone.setEnable(enable);
		
		Phone phone2 = phoneRepository.save(phone);
		
		
		return phone2;
	}
	
	public void updateEnableOne(String phoneNumber) { // 우선 인증에 성공하면 저장되었던 해당번호의 정보들 리셋
		Phone phone = phoneRepository.findByPhoneNumber(phoneNumber);
		phone.setEnable(1);
		phone.setCount(0);
		phoneRepository.save(phone);
	}
	
	// 개인정보 때문에 시간이 지나면 해당 폰번호도 테이블에서 삭제하는 기능을 추가 해야할 수도 있다. - 임시테이블로 만들면 가장 좋다.(session temp table)
	 @Scheduled(fixedDelay = 180000) // 근데 한 번호당 24시간은 아니고 그냥 여기 루틴에 맞춘거다.
	    public void updatePhoneEnable() {
	        List<Phone> phoneToBeUpdated = phoneRepository.findAll();

	        for (Phone phone : phoneToBeUpdated) {
	        	phone.setEnable(1);
	        	phone.setCount(0);
	        	phoneRepository.save(phone);
	        }
	    }

		
		
	
}
