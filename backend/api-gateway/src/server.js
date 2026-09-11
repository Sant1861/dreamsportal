import express from "express";
import cors from "cors";
import dotenv from "dotenv";

import gatewayRoutes from "./routes/gatewayRoutes.js";
import { PORT } from "./config/config.js";
import { errorHandler } from "./middleware/errorHandler.js";

dotenv.config();

const app = express();

app.use(cors());
app.use(express.json());

// Gateway health check
app.get("/health", (req, res) => {
  res.status(200).json({
    success: true,
    service: "api-gateway",
    status: "running"
  });
});

// Gateway routes
app.use("/api", gatewayRoutes);

// Error handler
app.use(errorHandler);

app.listen(PORT, () => {
  console.log(
    `API Gateway running on http://localhost:${PORT}`
  );
});