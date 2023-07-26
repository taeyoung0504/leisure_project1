package com.project.leisure.dogyeom.booking;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.leisure.dogyeom.kakao.DataNotFoundException;
import com.project.leisure.taeyoung.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {
	
	private RoomRepository roomRepository;
	
	@Autowired
	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}
	
	public Room getRoom(int id) {
		
		Optional<Room> room = this.roomRepository.findById(id);
        if (room.isPresent()) {
            return room.get();
        } else {
            throw new DataNotFoundException("room not found");
        }
	}
	
	
	
}
