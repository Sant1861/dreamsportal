import express from "express";
import cors from "cors";
import dotenv from "dotenv";

import summaryRoutes from "./routes/summaryRoutes.js";

dotenv.config();

const app = express();

const PORT = process.env.PORT || 3002;

app.use(cors());
app.use(express.json());

app.get("/health", (req, res) => {
  res.status(200).json({
    success: true,
    service: "summary-service",
    status: "running"
  });
});

app.use("/api/summary", summaryRoutes);

app.use((err, req, res, next) => {
  console.error(err);

  res.status(500).json({
    success: false,
    message: err.message || "Internal server error"
  });
});

app.listen(PORT, () => {
  console.log(
    `Summary Service running on http://localhost:${PORT}`
  );
});