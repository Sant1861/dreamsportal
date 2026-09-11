import "../styles/Home.css";

function Home({ navigate, dreams }) {

  const dreamCount = dreams.length;

  const dayCount =
    dreams.length > 0
      ? new Set(
          dreams.map((dream) => dream.daysAgo)
        ).size
      : 0;

  return (
    <div className="home-page">
      <div className="home-card">

        <div className="dream-icon">✨</div>

        <h1>Dream Portal</h1>

        <p className="subtitle">
          Explore your dream journey
        </p>

        <div className="stats">

          <div className="stat-item">
            <div className="number">
              {dreamCount}
            </div>

            <div className="label">
              Dreams
            </div>
          </div>

          <div className="stat-item">
            <div className="number">
              {dayCount}
            </div>

            <div className="label">
              Days
            </div>
          </div>

        </div>

        <button
          className="home-button"
          onClick={() => navigate("diary")}
        >
          MY DREAMS
        </button>

        <button
          className="home-button"
          onClick={() => navigate("total")}
        >
          VIEW SUMMARY
        </button>

      </div>
    </div>
  );
}

export default Home;