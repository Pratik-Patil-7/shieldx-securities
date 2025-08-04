// In your dto/job-application-response.ts
export interface JobApplicationResponse {
  applicationId: number;
  name: string;
  email: string;
  mobile: string;
  userId?: number; // Optional if it might be null
  dob?: string; // Optional date of birth
  gender?: string; // Optional gender
  address?: string; // Optional address
  qualification: string;
  experience: string;
  resumeUrl?: string | null; // Optional and can be null
  photoUrl?: string | null; // Optional and can be null
  status: string; // Optional status, can be null
}
