// Интерфейс данных для QR кода
export interface QRCodeData {
  serial_number: string; // Серийный номер самоката
  scooter_model: string; // Модель самоката
  battery_charge: number; // Заряд батареи в процентах
}

// Интерфейс ответа от сервера
export interface QRCodeResponse {
  //   success: boolean; // Статус успешности операции
  //   message?: string; // Дополнительное сообщение
  //   data?: QRCodeData; // Отправленные данные
  code: string;
}

/**
 * Отправляет данные QR кода на сервер
 * @param qrData - Данные для отправки (серийный номер, модель, заряд батареи)
 * @returns Promise с ответом от сервера
 */
export async function sendQRCodeData(
  qrData: QRCodeData
): Promise<QRCodeResponse> {
  try {
    const form = new URLSearchParams({
      serial_number: qrData.serial_number,
      scooter_model: qrData.scooter_model,
      battery_charge: String(qrData.battery_charge),
    });

    // Выполняем POST запрос на сервер
    const response = await fetch("http://localhost:3000/generate-code", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
      },
      body: form,
    });

    // Проверяем успешность запроса
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    // Парсим и возвращаем результат
    const serverResponse: QRCodeResponse = await response.json();
    return serverResponse;
  } catch (error) {
    console.error("Error sending QR code data:", error);
    throw error;
  }
}
