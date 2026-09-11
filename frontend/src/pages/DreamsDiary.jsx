import { useEffect } from "react";
import "../styles/DreamsDiary.css";

function DreamsDiary({ navigate, dreams, removeDream, onEdit }) {
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

    localStorage.setItem(
      "dreamStats",
      JSON.stringify({
        good,
        bad,
        total: dreams.length,
        days: uniqueDays.size
      })
    );
  }, [dreams]);

  const handleRemoveDream = () => {
    if (dreams.length === 0) {
      alert("No dreams available.");
      return;
    }

    const dreamToRemove = dreams[dreams.length - 1];

    const confirmed = window.confirm(
      `Are you sure you want to remove "${dreamToRemove.name}"?`
    );

    if (!confirmed) {
      return;
    }

    removeDream(dreamToRemove.id);
  };

  return (
    <div className="diary-page">
      <div className="diary-container">

        <button
          className="back-button"
          onClick={() => navigate("home")}
        >
          ⬅ Back to Home
        </button>

        <h1>Dreams Diary</h1>

        <div className="diary-buttons">

          <button onClick={handleRemoveDream}>
            Remove Dream
          </button>

          <button
            onClick={() => navigate("add")}
            disabled={dreams.length >= 10}
          >
            Add Dream
          </button>

        </div>

        <table>

          <thead>
            <tr>
              <th>Dream Name</th>
              <th>Days Ago</th>
              <th>Dream Type</th>
              <th>Actions</th>
            </tr>
          </thead>

          <tbody>
            {dreams.map((dream) => (
              <tr key={dream.id}>

                <td>{dream.name}</td>

                <td>{dream.daysAgo}</td>

                <td>{dream.type}</td>

                <td>
                  <button
                    className="edit-button"
                    onClick={() => onEdit(dream)}
                  >
                    Edit
                  </button>
                </td>

              </tr>
            ))}
          </tbody>

        </table>

        <div className="dream-count">
          Total Dreams: {dreams.length} / 10
        </div>

      </div>
    </div>
  );
}

export default DreamsDiary;