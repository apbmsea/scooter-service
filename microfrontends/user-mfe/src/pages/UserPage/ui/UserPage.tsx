import { useAppDispatch } from "@shared/hooks/store.hooks";
import { UserForm } from "../features/user";
import "./UserPage.scss";
import { event } from "scooter-shared";
import { useEffect } from "react";
import { updateIUserFailure } from "../features/user/model/userSlice";


const UserPage = () => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(updateIUserFailure({}));
  }, [dispatch]);
  return (
    <main className="user-page">
      <div className="settings">
        <header className="settings__header">
          <h1 className="settings__title">Настройки</h1>
        </header>

        <div className="settings__content">
          <nav className="settings__navbar">
            <div className="settings__navbar-section">
              <h2 className="settings__navbar-title">Основные</h2>
              <ul className="settings__navbar-list">
                <li className="settings__navbar-item settings__navbar-item_active">
                  Аккаунт
                </li>
              </ul>
            </div>
            <div className="settings__navbar-section">
              <button
                onClick={() => event.emit("logout")}
                className="settings__navbar-logout"
              >
                Выход
              </button>
            </div>
          </nav>

          <div className="settings__form-wrapper">
            <UserForm />
          </div>
        </div>
      </div>
    </main>
  );
};

export default UserPage;
