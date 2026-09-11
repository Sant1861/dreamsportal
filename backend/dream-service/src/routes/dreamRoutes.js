import express from "express";

import {
  getDreams,
  getDreamById,
  postDream,
  putDream,
  deleteDream
} from "../controllers/dreamController.js";

const router = express.Router();

router.get("/", getDreams);

router.get("/:id", getDreamById);

router.post("/", postDream);

router.put("/:id", putDream);

router.delete("/:id", deleteDream);

export default router;