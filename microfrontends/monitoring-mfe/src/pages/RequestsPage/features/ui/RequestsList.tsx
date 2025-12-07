import { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "@shared/hooks/store.hooks";
import "./RequestsList.scss";
import RequestCard from "./RequestCard";
import { getRequestsRequest } from "../model/requestsSlice";

const RequestsList = () => {
  const dispatch = useAppDispatch();
  const requests = useAppSelector((state) => state.requests);

  useEffect(() => {
    dispatch(getRequestsRequest());

    const interval = setInterval(() => {
      dispatch(getRequestsRequest());
    }, 5000);

    return () => clearInterval(interval);
  }, [dispatch]);

  return (
    <div className="requests-list">
      <div className="requests-list__table">
        <div className="requests-list__table-header">
          <div>Метод</div>
          <div>Статус</div>
          <div>Сервис</div>
          <div>Сообщение</div>
          <div>Время</div>
          <div>Тип</div>
        </div>

        <div className="requests-list__body">
          {requests.map((request) => (
            <RequestCard request={request} />
          ))}
        </div>
      </div>
    </div>
  );
};

export default RequestsList;
