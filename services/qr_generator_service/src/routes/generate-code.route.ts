import express from "express"
import { generateCode } from "../controllers/generate-code.controller";

const router = express.Router();

router.post('/', generateCode);

export default router;