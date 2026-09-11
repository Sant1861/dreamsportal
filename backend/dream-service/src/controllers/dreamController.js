import {
  fetchDreams,
  fetchDreamById,
  addDream,
  editDream,
  removeDream
} from "../services/dreamService.js";

export async function getDreams(req, res, next) {
  try {
    const dreams = await fetchDreams();

    res.status(200).json({
      success: true,
      data: dreams
    });
  } catch (error) {
    next(error);
  }
}

export async function getDreamById(req, res, next) {
  try {
    const id = Number(req.params.id);

    if (!Number.isInteger(id) || id <= 0) {
      return res.status(400).json({
        success: false,
        message: "Invalid dream ID"
      });
    }

    const dream = await fetchDreamById(id);

    if (!dream) {
      return res.status(404).json({
        success: false,
        message: "Dream not found"
      });
    }

    res.status(200).json({
      success: true,
      data: dream
    });
  } catch (error) {
    next(error);
  }
}

export async function postDream(req, res, next) {
  try {
    const { name, daysAgo, type } = req.body;

    const dream = await addDream(
      name,
      Number(daysAgo),
      type
    );

    res.status(201).json({
      success: true,
      message: "Dream added successfully",
      data: dream
    });
  } catch (error) {
    next(error);
  }
}

export async function putDream(req, res, next) {
  try {
    const id = Number(req.params.id);

    if (!Number.isInteger(id) || id <= 0) {
      return res.status(400).json({
        success: false,
        message: "Invalid dream ID"
      });
    }

    const { name, daysAgo, type } = req.body;

    const dream = await editDream(
      id,
      name,
      Number(daysAgo),
      type
    );

    res.status(200).json({
      success: true,
      message: "Dream updated successfully",
      data: dream
    });
  } catch (error) {
    next(error);
  }
}

export async function deleteDream(req, res, next) {
  try {
    const id = Number(req.params.id);

    if (!Number.isInteger(id) || id <= 0) {
      return res.status(400).json({
        success: false,
        message: "Invalid dream ID"
      });
    }

    await removeDream(id);

    res.status(200).json({
      success: true,
      message: "Dream deleted successfully"
    });
  } catch (error) {
    next(error);
  }
}