export function errorHandler(err, req, res, next) {
  console.error("Gateway error:", err.message);

  res.status(502).json({
    success: false,
    message: "A backend service is unavailable"
  });
}