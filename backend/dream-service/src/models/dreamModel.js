import pool from "../config/db.js";

export async function getAllDreams() {
  const [rows] = await pool.query(
    `
    SELECT
      id,
      name,
      days_ago,
      type,
      created_at
    FROM dreams
    ORDER BY days_ago ASC, id ASC
    `
  );

  return rows;
}

export async function getDreamById(id) {
  const [rows] = await pool.query(
    `
    SELECT
      id,
      name,
      days_ago,
      type,
      created_at
    FROM dreams
    WHERE id = ?
    `,
    [id]
  );

  return rows[0];
}

export async function createDream(name, daysAgo, type) {
  const [result] = await pool.query(
    `
    INSERT INTO dreams (name, days_ago, type)
    VALUES (?, ?, ?)
    `,
    [name, daysAgo, type]
  );

  return getDreamById(result.insertId);
}

export async function updateDream(id, name, daysAgo, type) {
  const [result] = await pool.query(
    `
    UPDATE dreams
    SET
      name = ?,
      days_ago = ?,
      type = ?
    WHERE id = ?
    `,
    [name, daysAgo, type, id]
  );

  return result.affectedRows;
}

export async function deleteDream(id) {
  const [result] = await pool.query(
    `
    DELETE FROM dreams
    WHERE id = ?
    `,
    [id]
  );

  return result.affectedRows;
}