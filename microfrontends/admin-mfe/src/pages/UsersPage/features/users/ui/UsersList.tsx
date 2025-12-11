import { useEffect } from "react";
import {
  useAppDispatch,
  useAppSelector,
} from "../../../../../shared/hooks/store.hooks";
import { getUsersRequest } from "../model/usersSlice";
import UserCard from "./UserCard";
import "./UsersList.scss";

const UsersList = () => {
  const dispatch = useAppDispatch();
  const users = useAppSelector((state) => state.users);

  useEffect(() => {
    dispatch(getUsersRequest());
  }, [dispatch]);

  return (
    <div className="users-list">
      <div className="users-list__table">
        <div className="users-list__table-header">
          <div>Email</div>
          <div>Имя</div>
          <div>Фамилия</div>
          <div>Телефон</div>
          <div>Роль</div>
          <div>Создан</div>
          <div>Обновлён</div>
          <div>
            <button
              className="update-button"
              onClick={() => dispatch(getUsersRequest())}
            >
              Обновить
            </button>
          </div>
        </div>

        <div className="users-list__body">
          {users.map((user) => (
            <UserCard user={user} key={user.id} />
          ))}
        </div>
      </div>
    </div>
  );
};

export default UsersList;
