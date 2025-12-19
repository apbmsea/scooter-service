import fs from "fs";
import cors from "cors";
import helmet from "helmet";
import dotenv from "dotenv";
import express from "express";
import { errorResponseType } from "./types/generate-code";
import scooterInfoRoute from "./routes/scooter-info.route";
import generateCodeRoute from "./routes/generate-code.route";
import path = require("path");

dotenv.config();

const app = express();

// Middleware
app.use(helmet());
app.use(cors({ origin: "*" }));
app.use(express.json({ limit: "5mb" }));
app.use(express.urlencoded({ extended: true }));

// HTML keeping dir
export const htmlDir = path.join(__dirname, "generated");
if (!fs.existsSync(htmlDir)) fs.mkdirSync(htmlDir);

// Routes
app.use("/generate-code", generateCodeRoute);
app.use("/scooter-info", scooterInfoRoute);

// 404 handler
app.use((req, res: express.Response<errorResponseType>) => {
  res.status(404).json({ error: "[Error]: Not found" });
});

export default app;
