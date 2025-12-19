export const scooterInfoTemplate = (content: any) => `
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>
  <style>
    html, body {
      height: 100vh;
      margin: 0;
      padding: 0;
      font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;
    }

    * {
      box-sizing: border-box;
    }

    h3 {
      margin: 0;
      font-size: 18px;
      font-weight: bold;
    }
    
    p {
      margin: 0;
      font-size: 18px;
      font-weight: 400;
    }

    .wrapper {
      width: 100%;
      height: 100%;
      max-width: 390px;
      margin-inline: auto;
      padding: 24px;
      border-left: 1px solid #EEEEEE;
      border-right: 1px solid #EEEEEE;
    }

    .info {
      width: 100%;
      margin-top: 2.5rem;
      display: flex;
      flex-direction: column;
      gap: 1.5rem;
    }

    .stat {
      width: 100%;
      display: flex;
      justify-content: space-between;
    }
  </style>
</head>
<body>
  <div class="wrapper">
    <h3>Scooter info</h3>
    <div class="info">
      ${Object.entries(content)
        .map(
          ([key, value]) => `
        <div class="stat">
          <p>${key.replace(/_/g, " ")}:</p>
          <p style="color: #AAA;">${value}</p>
        </div>
      `
        )
        .toString()
        .replace(/,/g, "")}
    </div>
  </div>
</body>
</html>
`;
