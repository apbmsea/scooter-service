import qr from "qr-image";
import fs from "fs";
import { Request, Response } from "express";
import {
  codeRequestDataType,
  codeResponseDataType,
  errorResponseType,
} from "../types/generate-code";
import path from "path";
import { htmlDir } from "../app";
import { scooterInfoTemplate } from "../templates/scooter-info";

export const generateCode = (
  req: Request<codeRequestDataType>,
  res: Response<codeResponseDataType | errorResponseType>
): void => {
  const data = req.body || null;
  const filePath = path.join(htmlDir, "scooter-info.html");

  if (!data) {
    res.status(400).json({
      error: "[Error]: No Parameters included",
    });
    return;
  }

  fs.writeFileSync(filePath, scooterInfoTemplate(data));

  const code = qr.image(`http://${req.get("host")}/scooter-info`, {
    type: "png",
  });
  const chunks: any[] = [];

  code.on("data", (chunk) => {
    chunks.push(chunk);
  });

  code.on("end", () => {
    const buffer = Buffer.concat(chunks);
    const image = buffer.toString("base64");
    res.status(200).json({ code: image });
  });
};
