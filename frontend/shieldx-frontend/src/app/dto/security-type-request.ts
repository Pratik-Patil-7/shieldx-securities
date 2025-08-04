export interface SecurityTypeRequest {
  levelName: string;
  description: string;
  isArmed: string; // Represents Java String (e.g., "yes" or "no")
  pricePerDay: number;
}
