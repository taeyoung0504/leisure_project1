/*
 * package com.project.leisure.dogyeom.booking.reserveList;
 * 
 * import java.util.concurrent.atomic.AtomicInteger;
 * 
 * import org.springframework.stereotype.Component;
 * 
 * import lombok.NoArgsConstructor;
 * 
 * @Component
 * 
 * @NoArgsConstructor public class ReserveRoom {
 * 
 * private AtomicInteger reserveRoomCount;
 * 
 * private int product_count;
 * 
 * 
 * 
 * public ReserveRoom(int bookingCount, int product_count) { super();
 * this.reserveRoomCount = new AtomicInteger(bookingCount); this.product_count =
 * product_count; }
 * 
 * public synchronized boolean reserveRoom() { int currentRooms = product_count
 * - reserveRoomCount.get(); if (currentRooms > 0) { // 남은 객실이 있으면 예약 가능
 * reserveRoomCount.decrementAndGet(); return true; } else { // 남은 객실이 없으면 예약
 * 불가능 return false; } }
 * 
 * public synchronized void decrementAndGet() {
 * reserveRoomCount.decrementAndGet(); }
 * 
 * }
 */