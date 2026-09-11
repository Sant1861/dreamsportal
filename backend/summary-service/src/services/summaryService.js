import axios from "axios";
import { DREAM_SERVICE_URL } from "../config/config.js";

export async function calculateSummary() {
  const response = await axios.get(
    `${DREAM_SERVICE_URL}/api/dreams`
  );

  const dreams = response.data.data;

  let good = 0;
  let bad = 0;

  const uniqueDays = new Set();

  dreams.forEach((dream) => {
    if (dream.type === "Good") {
      good++;
    }

    if (dream.type === "Bad") {
      bad++;
    }

    uniqueDays.add(dream.days_ago);
  });

  return {
    good,
    bad,
    total: dreams.length,
    days: uniqueDays.size
  };
}