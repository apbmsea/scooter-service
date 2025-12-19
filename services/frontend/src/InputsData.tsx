import { useState } from "react";
import { sendQRCodeData, type QRCodeData } from "./api/qrApi";
import "./InputsData.css";

export function InputData() {
  // Состояние для серийного номера самоката
  const [serialNumber, setSerialNumber] = useState("");

  // Состояние для модели самоката
  const [scooterModel, setScooterModel] = useState("");

  // Состояние для заряда батареи (в процентах)
  const [batteryCharge, setBatteryCharge] = useState("");

  // Состояние для base64 изображения QR кода с сервера
  const [qrCodeBase64Image, setQrCodeBase64Image] = useState("");

  // Состояние загрузки
  const [isLoading, setIsLoading] = useState(false);

  // Состояние ошибки
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  // Проверка валидности формы - все поля должны быть заполнены
  const isFormValid =
    serialNumber.trim() !== "" &&
    scooterModel.trim() !== "" &&
    batteryCharge.trim() !== "";

  // Обработчик генерации QR кода
  const handleGenerateQRCode = async () => {
    if (isFormValid) {
      setIsLoading(true);
      setErrorMessage(null);

      const qrData: QRCodeData = {
        serial_number: serialNumber,
        scooter_model: scooterModel,
        battery_charge: Number(batteryCharge),
      };

      try {
        // Отправляем данные на сервер и получаем base64 изображение
        const serverResponse = await sendQRCodeData(qrData);

        // Сохраняем base64 изображение QR кода из ответа сервера
        if (serverResponse.code) {
          setQrCodeBase64Image(serverResponse.code);
        } else {
          setErrorMessage("Сервер не вернул QR код");
        }
      } catch (err) {
        setErrorMessage("Ошибка при отправке данных на сервер");
        console.error(err);
      } finally {
        setIsLoading(false);
      }
    }
  };

  return (
    <div className="container">
      <h1 className="title">QR Code Generator</h1>

      {/* Область отображения QR кода */}
      <div className="qr-container">
        {qrCodeBase64Image ? (
          <img
            src={`data:image/png;base64,${qrCodeBase64Image}`}
            alt="QR Code"
            style={{
              maxWidth: "100%",
              maxHeight: "100%",
              objectFit: "contain",
            }}
          />
        ) : (
          <div className="empty-state">Empty</div>
        )}
      </div>

      {/* Поля ввода данных */}
      <div className="inputs-container">
        <div className="input-group">
          {/* <label htmlFor="serialNumber">Serial Number</label> */}
          <input
            id="serialNumber"
            type="text"
            value={serialNumber}
            onChange={(e) => setSerialNumber(e.target.value)}
            placeholder="Enter serial number"
          />
        </div>

        <div className="input-group">
          {/* <label htmlFor="scooterModel">Scooter Model</label> */}
          <input
            id="scooterModel"
            type="text"
            value={scooterModel}
            onChange={(e) => setScooterModel(e.target.value)}
            placeholder="Enter scooter model"
          />
        </div>

        <div className="input-group">
          {/* <label htmlFor="batteryCharge">Battery Charge %</label> */}
          <input
            id="batteryCharge"
            type="number"
            min="0"
            max="100"
            value={batteryCharge}
            onChange={(e) => setBatteryCharge(e.target.value)}
            placeholder="Enter battery charge %"
          />
        </div>
      </div>

      {/* Сообщение об ошибке */}
      {errorMessage && <div className="error-message">{errorMessage}</div>}

      {/* Кнопка генерации QR кода */}
      <button
        className="generate-button"
        onClick={handleGenerateQRCode}
        disabled={!isFormValid || isLoading}
      >
        {isLoading ? "GENERATING..." : "Generate"}
      </button>
    </div>
  );
}
