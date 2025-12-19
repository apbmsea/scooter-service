import fs from "fs";
import path from "node:path";
import express from "express";
import { htmlDir } from "../app";

const router = express.Router();

router.get('/', (req, res) => {
  const filePath = path.join(htmlDir, 'scooter-info.html');
  if (fs.existsSync(filePath)) {
    res.sendFile(filePath);
  } else {
    res.status(404).send('Not found');
  }
});

export default router;