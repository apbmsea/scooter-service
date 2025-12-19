export type codeRequestDataType = {
  query: {
    serial_number: string;
    scooter_model: string; 
    battery_charge: number;
  }
}

export type codeResponseDataType = {
  code: string
}

export type errorResponseType = {
  error: string;
}