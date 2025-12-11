import "./RequestCard.scss";
import { type Request } from "@pages/RequestsPage/entities/requests.types";

type RequestCardProps = {
  request: Request;
};

const normalizeDate = (s: string) => {
  const d = new Date(s.replace(/(\.\d{3})\d+/, "$1"));
  return d.toLocaleString("ru-RU", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const RequestCard = ({ request }: RequestCardProps) => {
  return (
    <div className={`request-card ${request.type.toLocaleLowerCase()}`}>
      <div className="request-cell">{request.method}</div>
      <div className="request-cell">{request.status}</div>
      <div className="request-cell">{request.service}</div>
      <div className="request-cell">{request.message}</div>
      <div className="request-cell">{normalizeDate(request.time)}</div>
      <div className="request-cell">{request.type}</div>
    </div>
  );
};

export default RequestCard;
