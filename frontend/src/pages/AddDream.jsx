import { useEffect, useState } from "react";
import "../styles/AddDream.css";

function AddDream({
  navigate,
  addDream,
  editingDream,
  updateDream
}) {
  const [dreamName, setDreamName] = useState("");
  const [daysAgo, setDaysAgo] = useState("1 day ago");
  const [dreamType, setDreamType] = useState("Good");

  const isEditing =
    editingDream !== undefined &&
    editingDream !== null;

  useEffect(() => {
    if (editingDream) {
      setDreamName(editingDream.name);
      setDaysAgo(editingDream.daysAgo);
      setDreamType(editingDream.type);
    }
  }, [editingDream]);

  const handleSubmit = (event) => {
    event.preventDefault();

    // Validate dream name
    if (dreamName.trim() === "") {
      alert("Please enter a dream name.");
      return;
    }

    // Get the number from "1 day ago", "2 days ago", etc.
    const daysNumber = Number(
      daysAgo.match(/\d+/)?.[0]
    );

    // Validate days ago
    if (
      !Number.isInteger(daysNumber) ||
      daysNumber < 1 ||
      daysNumber > 7
    ) {
      alert("Days ago must be between 1 and 7.");
      return;
    }

    // Validate dream type
    if (
      dreamType !== "Good" &&
      dreamType !== "Bad"
    ) {
      alert("Dream type must be Good or Bad.");
      return;
    }

    const dreamData = {
      name: dreamName.trim(),
      daysAgo: daysAgo,
      type: dreamType
    };

    // Update existing dream
    if (isEditing) {
      updateDream({
        ...editingDream,
        ...dreamData
      });
    }

    // Add new dream
    else {
      addDream(dreamData);
    }
  };

  return (
    <div className="add-dream-page">

      <div className="add-dream-container">

        <button
          className="back-button"
          onClick={() => navigate("diary")}
        >
          ⬅ Back to Dreams Diary
        </button>

        <h1>
          {isEditing ? "Edit Dream" : "Add Dream"}
        </h1>

        <form onSubmit={handleSubmit}>

          <label>
            Dream Name
          </label>

          <input
            type="text"
            placeholder="Enter your dream"
            value={dreamName}
            onChange={(event) =>
              setDreamName(event.target.value)
            }
          />

          <label>
            Days Ago
          </label>

          <select
            value={daysAgo}
            onChange={(event) =>
              setDaysAgo(event.target.value)
            }
          >
            <option value="1 day ago">
              1 day ago
            </option>

            <option value="2 days ago">
              2 days ago
            </option>

            <option value="3 days ago">
              3 days ago
            </option>

            <option value="4 days ago">
              4 days ago
            </option>

            <option value="5 days ago">
              5 days ago
            </option>

            <option value="6 days ago">
              6 days ago
            </option>

            <option value="7 days ago">
              7 days ago
            </option>
          </select>

          <label>
            Dream Type
          </label>

          <select
            value={dreamType}
            onChange={(event) =>
              setDreamType(event.target.value)
            }
          >
            <option value="Good">
              Good
            </option>

            <option value="Bad">
              Bad
            </option>
          </select>

          <button
            className="ok-button"
            type="submit"
          >
            {isEditing ? "Update Dream" : "OK"}
          </button>

        </form>

      </div>

    </div>
  );
}

export default AddDream;