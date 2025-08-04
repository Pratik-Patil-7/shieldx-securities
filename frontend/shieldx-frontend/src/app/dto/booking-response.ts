export interface BookingResponse {
  bookingId: number;
  userId: number;
  vipId: number;
  securityTypeId: number;
  bouncerCount: string;
  startDate: string; // Represents Java LocalDate (e.g., "2025-07-13")
  endDate: string;   // Represents Java LocalDate
  startTime: string; // Represents Java LocalTime (e.g., "14:30:00")
  endTime: string;   // Represents Java LocalTime
  location: string;
  status: string;
  totalPrice: number;
  bouncerIds: number[];
}
