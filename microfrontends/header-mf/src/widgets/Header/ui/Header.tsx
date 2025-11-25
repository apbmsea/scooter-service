import { useNavigate } from "react-router-dom";
import { logout } from "authMF/logout";
import "./Header.scss";
import { useEffect, useState } from "react";
import { $api } from "sharedMF/api";
import { event } from "sharedMF/event";
import { refresh } from "authMF/refresh";

interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  phone: string;
  role: "USER" | "OPERATOR" | "ADMIN";
  createdAt: string;
  updatedAt: string;
}

const Header = () => {
  const navigate = useNavigate();
  const [user, setUser] = useState<User | null>(null);

  const handleNavigate = (path: string) => {
    return () => navigate(path);
  };

  useEffect(() => {
    const fetchUserEffect = async () => {
      try {
        const res = await $api.get<User>("/users/me");
        setUser(res.data);
      } catch (err: unknown) {
        if (err instanceof Error) console.error(err.message);
        else console.error(err);
      }
    };

    fetchUserEffect();

    const unsubscribe = event.on("user_update", () => {
      fetchUserEffect();
    });

    return unsubscribe;
  }, []);

  useEffect(() => {
    const fetchRefreshEffect = async () => {
      try {
        refresh()
      } catch (err: unknown) {
        if (err instanceof Error) console.error(err.message);
        else console.error(err);
      }
    };

    const unsubscribe = event.on("refresh", () => {
      fetchRefreshEffect();
    });

    return unsubscribe;
  }, []);

  return (
    <header className="header">
      <section className="container">
        <nav className="header__navbar">
          <h1 className="header__navbar-logo" onClick={() => navigate("/home")}>
            Самокат
          </h1>
        </nav>
        <div className="header__buttons">
          {user ? (
            <>
              <button
                onClick={handleNavigate("/users/me")}
                className="header__buttons-default"
              >
                Настройки
              </button>
              <button
                onClick={() => logout()}
                className="header__buttons-logout"
              >
                Выход
              </button>
            </>
          ) : (
            <>
              <button
                onClick={handleNavigate("/auth/login")}
                className="header__buttons-default"
              >
                Вход
              </button>
              <button
                onClick={handleNavigate("/auth/register")}
                className="header__buttons-secondary"
              >
                Регистрация
              </button>
            </>
          )}
        </div>
      </section>
    </header>
  );
};

export default Header;
