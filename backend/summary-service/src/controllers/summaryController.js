import { calculateSummary } from "../services/summaryService.js";

export async function getSummary(req, res, next) {
  try {
    const summary = await calculateSummary();

    res.status(200).json({
      success: true,
      data: summary
    });
  } catch (error) {
    next(error);
  }
}