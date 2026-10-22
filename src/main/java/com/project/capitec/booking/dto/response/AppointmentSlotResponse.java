package com.project.capitec.booking.dto.response;

import java.time.LocalDateTime;

public class AppointmentSlotResponse {
    private Long slotId;
    private BranchResponse branch;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long capacity;
    private Long bookedCount;

    public AppointmentSlotResponse() {}

    public AppointmentSlotResponse(Long slotId, BranchResponse branch, LocalDateTime startTime,
                                   LocalDateTime endTime, Long capacity, Long bookedCount) {
        this.slotId = slotId;
        this.branch = branch;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.bookedCount = bookedCount;
    }

    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }
    public BranchResponse getBranch() { return branch; }
    public void setBranch(BranchResponse branch) { this.branch = branch; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Long getCapacity() { return capacity; }
    public void setCapacity(Long capacity) { this.capacity = capacity; }
    public Long getBookedCount() { return bookedCount; }
    public void setBookedCount(Long bookedCount) { this.bookedCount = bookedCount; }

    public Long getAvailableCapacity() {
        return capacity - bookedCount;
    }
}
