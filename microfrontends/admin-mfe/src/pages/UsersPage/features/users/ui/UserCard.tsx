import { type User } from "scooter-shared";
import "./UserCard.scss";
import { useAppDispatch } from "../../../../../shared/hooks/store.hooks";
import { deleteUserRequest } from "../model/usersSlice";

type UserCardProps = {
  user: User;
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

const UserCard = ({ user }: UserCardProps) => {
  const dispatch = useAppDispatch();
  const deleteUser = (id: string) => () => {
    dispatch(deleteUserRequest({ id }));
  };

  return (
    <div className="user-card" key={user.id}>
      <div className="user-cell">{user.email}</div>
      <div className="user-cell">{user.firstName}</div>
      <div className="user-cell">{user.lastName}</div>
      <div className="user-cell">{user.phone}</div>
      <div className="user-cell">{user.role}</div>
      <div className="user-cell">{normalizeDate(user.createdAt)}</div>
      <div className="user-cell">{normalizeDate(user.updatedAt)}</div>
      <button onClick={deleteUser(user.id)} className="user-delete-btn">
        Удалить
      </button>
    </div>
  );
};

export default UserCard;
