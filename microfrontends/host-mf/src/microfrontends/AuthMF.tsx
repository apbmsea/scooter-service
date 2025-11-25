import React, { Suspense, useState } from "react";

const AuthMFLazy = React.lazy(() =>
  import("authMF/App").catch(() => ({ default: () => null }))
);

const AuthMF = () => {
  const [failed] = useState(false);

  return (
    <Suspense fallback={<div>Загрузка…</div>}>
      {!failed ? <AuthMFLazy /> : <div>Микросервис не загрузился</div>}
    </Suspense>
  );
};

export default AuthMF;
