import express from "express";
import axios from "axios";

import {
  DREAM_SERVICE_URL,
  SUMMARY_SERVICE_URL
} from "../config/config.js";

const router = express.Router();

// GET all dreams
router.get("/dreams", async (req, res, next) => {
  try {
    const response = await axios.get(
      `${DREAM_SERVICE_URL}/api/dreams`
    );

    res.status(response.status).json(response.data);
  } catch (error) {
    next(error);
  }
});

// GET one dream by ID
router.get("/dreams/:id", async (req, res, next) => {
  try {
    const response = await axios.get(
      `${DREAM_SERVICE_URL}/api/dreams/${req.params.id}`
    );

    res.status(response.status).json(response.data);
  } catch (error) {
    if (error.response) {
      return res
        .status(error.response.status)
        .json(error.response.data);
    }

    next(error);
  }
});

// POST a new dream
router.post("/dreams", async (req, res, next) => {
  try {
    const response = await axios.post(
      `${DREAM_SERVICE_URL}/api/dreams`,
      req.body
    );

    res.status(response.status).json(response.data);
  } catch (error) {
    if (error.response) {
      return res
        .status(error.response.status)
        .json(error.response.data);
    }

    next(error);
  }
});

// PUT - update a dream
router.put("/dreams/:id", async (req, res, next) => {
  try {
    const response = await axios.put(
      `${DREAM_SERVICE_URL}/api/dreams/${req.params.id}`,
      req.body
    );

    res.status(response.status).json(response.data);
  } catch (error) {
    if (error.response) {
      return res
        .status(error.response.status)
        .json(error.response.data);
    }

    next(error);
  }
});

// DELETE a dream
router.delete("/dreams/:id", async (req, res, next) => {
  try {
    const response = await axios.delete(
      `${DREAM_SERVICE_URL}/api/dreams/${req.params.id}`
    );

    res.status(response.status).json(response.data);
  } catch (error) {
    if (error.response) {
      return res
        .status(error.response.status)
        .json(error.response.data);
    }

    next(error);
  }
});

// GET dream summary
router.get("/summary", async (req, res, next) => {
  try {
    const response = await axios.get(
      `${SUMMARY_SERVICE_URL}/api/summary`
    );

    res.status(response.status).json(response.data);
  } catch (error) {
    next(error);
  }
});

export default router;