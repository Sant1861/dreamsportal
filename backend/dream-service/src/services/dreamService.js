import {
  getAllDreams,
  getDreamById,
  createDream,
  updateDream,
  deleteDream
} from "../models/dreamModel.js";

export async function fetchDreams() {
  return await getAllDreams();
}

export async function fetchDreamById(id) {
  return await getDreamById(id);
}

export async function addDream(name, daysAgo, type) {
  if (!name || !name.trim()) {
    throw new Error("Dream name is required");
  }

  if (!Number.isInteger(daysAgo) || daysAgo < 1 || daysAgo > 7) {
    throw new Error("Days ago must be between 1 and 7");
  }

  if (type !== "Good" && type !== "Bad") {
    throw new Error("Dream type must be Good or Bad");
  }

  const dreams = await getAllDreams();

  if (dreams.length >= 10) {
    throw new Error("Maximum of 10 dreams allowed");
  }

  return await createDream(
    name.trim(),
    daysAgo,
    type
  );
}

export async function editDream(id, name, daysAgo, type) {
  if (!name || !name.trim()) {
    throw new Error("Dream name is required");
  }

  if (!Number.isInteger(daysAgo) || daysAgo < 1 || daysAgo > 7) {
    throw new Error("Days ago must be between 1 and 7");
  }

  if (type !== "Good" && type !== "Bad") {
    throw new Error("Dream type must be Good or Bad");
  }

  const existingDream = await getDreamById(id);

  if (!existingDream) {
    throw new Error("Dream not found");
  }

  await updateDream(
    id,
    name.trim(),
    daysAgo,
    type
  );

  return await getDreamById(id);
}

export async function removeDream(id) {
  const affectedRows = await deleteDream(id);

  if (affectedRows === 0) {
    throw new Error("Dream not found");
  }

  return true;
}