import express from "express";
import cors from "cors";
import dotenv from "dotenv";

import dreamRoutes from "./routes/dreamRoutes.js";
import { testDatabaseConnection } from "./config/db.js";

dotenv.config();

const app = express();

const PORT = process.env.PORT || 3001;

app.use(cors());
app.use(express.json());

app.get("/health", (req, res) => {
  res.status(200).json({
    success: true,
    service: "dream-service",
    status: "running"
  });
});

app.use("/api/dreams", dreamRoutes);

app.use((err, req, res, next) => {
  console.error(err);

  res.status(500).json({
    success: false,
    message: err.message || "Internal server error"
  });
});

async function startServer() {
  try {
    await testDatabaseConnection();

    app.listen(PORT, () => {
      console.log(
        `Dream Service running on http://localhost:${PORT}`
      );
    });
  } catch (error) {
    console.error("Failed to start Dream Service");
    console.error(error.message);

    process.exit(1);
  }
}

startServer();