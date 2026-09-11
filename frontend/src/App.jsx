import { useEffect, useState } from "react";
import API_URL from "./services/api";

import Home from "./pages/Home";
import DreamsDiary from "./pages/DreamsDiary";
import DreamsTotal from "./pages/DreamsTotal";
import AddDream from "./pages/AddDream";

function App() {
  const [page, setPage] = useState("home");

  const [dreams, setDreams] = useState([]);

  // Dream currently selected for editing
  const [editingDream, setEditingDream] = useState(null);

  // Read API response safely
  const getResponseData = async (response) => {
    const text = await response.text();

    if (!text) {
      return {};
    }

    try {
      return JSON.parse(text);
    } catch {
      return {
        message: text
      };
    }
  };

  // Get a useful error message
  const getErrorMessage = (error, defaultMessage) => {
    if (
      error instanceof TypeError &&
      error.message === "Failed to fetch"
    ) {
      return "Unable to connect to the server.";
    }

    return error.message || defaultMessage;
  };

  // Load dreams from backend when application starts
  useEffect(() => {
    const loadDreams = async () => {
      try {
        const response = await fetch(
          `${API_URL}/dreams`
        );

        const result = await getResponseData(response);

        if (!response.ok) {
          throw new Error(
            result.message || "Failed to load dreams."
          );
        }

        if (!Array.isArray(result.data)) {
          throw new Error(
            "Invalid response received from server."
          );
        }

        const formattedDreams = result.data.map(
          (dream) => ({
            id: dream.id,
            name: dream.name,
            daysAgo: `${dream.days_ago} ${
              dream.days_ago === 1
                ? "day"
                : "days"
            } ago`,
            type: dream.type
          })
        );

        setDreams(formattedDreams);
      } catch (error) {
        console.error(
          "Load dreams error:",
          error
        );

        alert(
          getErrorMessage(
            error,
            "Failed to load dreams."
          )
        );
      }
    };

    loadDreams();
  }, []);

  const navigate = (newPage) => {
    setPage(newPage);
  };

  // Remove dream from backend and database
  const removeDream = async (id) => {
    try {
      const response = await fetch(
        `${API_URL}/dreams/${id}`,
        {
          method: "DELETE"
        }
      );

      const result = await getResponseData(response);

      if (!response.ok) {
        throw new Error(
          result.message || "Failed to delete dream."
        );
      }

      setDreams((currentDreams) =>
        currentDreams.filter(
          (dream) => dream.id !== id
        )
      );
    } catch (error) {
      console.error(
        "Delete dream error:",
        error
      );

      alert(
        getErrorMessage(
          error,
          "Failed to remove dream."
        )
      );
    }
  };

  // Add dream to backend and database
  const addDream = async (newDream) => {
    try {
      if (dreams.length >= 10) {
        alert(
          "You can have a maximum of 10 dreams."
        );
        return;
      }

      const daysNumber = Number(
        newDream.daysAgo
          .toString()
          .match(/\d+/)?.[0]
      );

      const response = await fetch(
        `${API_URL}/dreams`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            name: newDream.name,
            daysAgo: daysNumber,
            type: newDream.type
          })
        }
      );

      const result = await getResponseData(response);

      if (!response.ok) {
        throw new Error(
          result.message || "Failed to add dream."
        );
      }

      if (!result.data) {
        throw new Error(
          "Invalid response received from server."
        );
      }

      const savedDream = result.data;

      const formattedDream = {
        id: savedDream.id,
        name: savedDream.name,
        daysAgo: `${savedDream.days_ago} ${
          savedDream.days_ago === 1
            ? "day"
            : "days"
        } ago`,
        type: savedDream.type
      };

      setDreams((currentDreams) => [
        ...currentDreams,
        formattedDream
      ]);

      setPage("diary");

    } catch (error) {
      console.error(
        "Add dream error:",
        error
      );

      alert(
        getErrorMessage(
          error,
          "Failed to add dream."
        )
      );
    }
  };

  // Open a dream for editing
  const editDream = (dream) => {
    setEditingDream(dream);
    setPage("edit");
  };

  // Update dream in backend and database
  const updateDream = async (updatedDream) => {
    try {
      const daysNumber = Number(
        updatedDream.daysAgo
          .toString()
          .match(/\d+/)?.[0]
      );

      const response = await fetch(
        `${API_URL}/dreams/${updatedDream.id}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            name: updatedDream.name,
            daysAgo: daysNumber,
            type: updatedDream.type
          })
        }
      );

      const result = await getResponseData(response);

      if (!response.ok) {
        throw new Error(
          result.message || "Failed to update dream."
        );
      }

      if (!result.data) {
        throw new Error(
          "Invalid response received from server."
        );
      }

      const updatedFromBackend = result.data;

      setDreams((currentDreams) =>
        currentDreams.map((dream) =>
          dream.id === updatedFromBackend.id
            ? {
                id: updatedFromBackend.id,
                name: updatedFromBackend.name,
                daysAgo: `${updatedFromBackend.days_ago} ${
                  updatedFromBackend.days_ago === 1
                    ? "day"
                    : "days"
                } ago`,
                type: updatedFromBackend.type
              }
            : dream
        )
      );

      setEditingDream(null);
      setPage("diary");

    } catch (error) {
      console.error(
        "Update dream error:",
        error
      );

      alert(
        getErrorMessage(
          error,
          "Failed to update dream."
        )
      );
    }
  };

  if (page === "diary") {
    return (
      <DreamsDiary
        navigate={navigate}
        dreams={dreams}
        removeDream={removeDream}
        onEdit={editDream}
      />
    );
  }

  if (page === "add") {
    return (
      <AddDream
        navigate={navigate}
        addDream={addDream}
      />
    );
  }

  if (page === "edit") {
    return (
      <AddDream
        navigate={navigate}
        editingDream={editingDream}
        updateDream={updateDream}
      />
    );
  }

  if (page === "total") {
    return (
      <DreamsTotal
        navigate={navigate}
        dreams={dreams}
      />
    );
  }

  return (
    <Home
      navigate={navigate}
      dreams={dreams}
    />
  );
}

export default App;