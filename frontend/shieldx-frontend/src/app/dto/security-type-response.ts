export interface SecurityTypeResponse {
   stId: number;          // Unique identifier for the security type
  levelName: string;    // Matches SecurityTypeRequest
  description: string;  // Matches SecurityTypeRequest
  isArmed: string;      // Matches SecurityTypeRequest (e.g., "yes" or "no")
  pricePerDay: number;  // Matches SecurityTypeRequest
}
