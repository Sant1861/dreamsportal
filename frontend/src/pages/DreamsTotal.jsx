import { useEffect, useState } from "react";
import "../styles/DreamsTotal.css";

function DreamsTotal({ navigate, dreams }) {
  const [stats, setStats] = useState({
    good: 0,
    bad: 0,
    total: 0,
    days: 0
  });

  useEffect(() => {
    let good = 0;
    let bad = 0;

    const uniqueDays = new Set();

    dreams.forEach((dream) => {
      uniqueDays.add(dream.daysAgo);

      if (dream.type === "Good") {
        good++;
      }

      if (dream.type === "Bad") {
        bad++;
      }
    });

    setStats({
      good,
      bad,
      total: dreams.length,
      days: uniqueDays.size
    });
  }, [dreams]);

  return (
    <div className="summary-page">

      <div className="summary-container">

        <button
          className="back-button"
          onClick={() => navigate("home")}
        >
          ⬅ Back to Home
        </button>

        <h1>Dreams Summary</h1>

        <table>

          <thead>
            <tr>
              <th>Dream Type</th>
              <th>Total Count</th>
            </tr>
          </thead>

          <tbody>

            <tr>
              <td>Good Dreams</td>
              <td>{stats.good}</td>
            </tr>

            <tr>
              <td>Bad Dreams</td>
              <td>{stats.bad}</td>
            </tr>

            <tr>
              <td>Total Dreams</td>
              <td>{stats.total}</td>
            </tr>

            <tr>
              <td>Total Days</td>
              <td>{stats.days}</td>
            </tr>

          </tbody>

        </table>

      </div>

    </div>
  );
}

export default DreamsTotal;