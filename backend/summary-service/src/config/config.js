import dotenv from "dotenv";

dotenv.config();

export const PORT = process.env.PORT || 3002;

export const DREAM_SERVICE_URL =
  process.env.DREAM_SERVICE_URL ||
  "http://localhost:3001";