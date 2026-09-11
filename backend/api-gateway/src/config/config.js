import dotenv from "dotenv";

dotenv.config();

export const PORT = process.env.PORT || 3000;

export const DREAM_SERVICE_URL =
  process.env.DREAM_SERVICE_URL ||
  "http://localhost:3001";

export const SUMMARY_SERVICE_URL =
  process.env.SUMMARY_SERVICE_URL ||
  "http://localhost:3002";