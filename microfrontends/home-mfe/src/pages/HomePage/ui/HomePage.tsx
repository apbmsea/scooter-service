import "./HomePage.scss";
import tariff from "../../../app/assets/images/tariff.jpg";
import zone from "../../../app/assets/images/zone.jpg";
import faq from "../../../app/assets/images/faq.jpg";

const Home = () => {
  return (
    <div className="home">
      <section className="home__tiles">
        <div className="tile tile--lg tile--white tile--no-border">
          <span className="tile__welcome-text">Добро пожаловать!</span>
          <div className="tile__content"></div>
        </div>

        <div className="tile tile--md tile--white">
          <img src={tariff} alt="" className="tile__tariff-img" />
          <div className="tile__content">
            <h3>Тарифы</h3>
            <p>Простая система оплаты</p>
          </div>
        </div>

        <div className="tile tile--md tile--blue">
          <span className="tile__bonus-text">1000 бонусов</span>
          <div className="tile__content">
            <h3>Бонусы</h3>
            <p>Получайте вознаграждения за поездки</p>
          </div>
        </div>

        <div className="tile tile--lg tile--white">
          <img src={zone} alt="" className="tile__zone-img" />
          <div className="tile__content">
            <h3>Зоны катания</h3>
            <p>Посмотрите, где можно ездить безопасно</p>
          </div>
        </div>

        <div className="tile tile--sm tile--white">
          <img src={faq} alt="" className="tile__faq-img" />
          <div className="tile__content">
            <h3
            >
              FAQ
            </h3>
          </div>
        </div>

        <div className="tile tile--sm tile--blue tile--cta">
          <button className="tile__cta-button">Начать поездку</button>
        </div>
      </section>

      <footer className="hp__footer">
        <div className="hp__container footer__inner">
          <div className="footer__brand">
            <a className="hp__logo" href="/home">
              Самокат
            </a>
            <p className="footer__copy">© 2025 Самокат. Все права защищены.</p>
          </div>
          <div className="footer__links">
            <a href="/support">Поддержка</a>
            <a href="/terms">Условия</a>
            <a href="/privacy">Политика конфиденциальности</a>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default Home;
