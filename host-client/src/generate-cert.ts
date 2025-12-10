import fs from 'fs';
import path from 'path';
import selfsigned from 'selfsigned';
import { envConfig } from './configs/env.config';

async function generate(): Promise<void> {
    const certsDir = path.join(__dirname, envConfig.ssl.certDir);
    const keyPath = path.join(certsDir, envConfig.ssl.keyFile);
    const certPath = path.join(certsDir, envConfig.ssl.certFile);

    if (!fs.existsSync(certsDir)) {
        fs.mkdirSync(certsDir, { recursive: true });
    }

    const attrs = [{ name: 'commonName', value: envConfig.server.hostIp }];

    let pems;
    try {
        pems = await selfsigned.generate(attrs, {
            keySize: 2048,
            algorithm: 'sha256',
            extensions: [
                { name: 'basicConstraints', cA: false },
                {
                    name: 'keyUsage',
                    keyEncipherment: true,
                    digitalSignature: true,
                },
                {
                    name: 'subjectAltName',
                    altNames: [
                        { type: 2, value: envConfig.server.hostIp },
                        { type: 2, value: 'localhost' },
                        { type: 7, ip: '127.0.0.1' },
                        { type: 7, ip: envConfig.server.hostIp }
                    ]
                },
            ],
        });
    } catch (err) {
        console.log(err)
        process.exit(1);
    }

    if (!pems || !pems.private || !pems.cert) {
        console.error(pems);
        process.exit(1);
    }

    fs.writeFileSync(keyPath, pems.private);
    fs.writeFileSync(certPath, pems.cert);
}

export default generate();