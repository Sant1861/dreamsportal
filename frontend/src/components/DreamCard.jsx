function DreamCard({ dream, onEdit }) {
  return (
    <div className="dream-card">
      <h3>{dream.name}</h3>

      <p>{dream.daysAgo}</p>

      <p>{dream.type}</p>

      <button onClick={() => onEdit(dream)}>
        Edit
      </button>
    </div>
  );
}

export default DreamCard;