import React, { Suspense, useState } from "react";

const UserMFLazy = React.lazy(() =>
  import("userMF/App").catch(() => ({ default: () => null }))
);

const UserMF = () => {
  const [failed] = useState(false);

  return (
    <Suspense fallback={<div>Загрузка…</div>}>
      {!failed ? <UserMFLazy /> : <div>Микросервис не загрузился</div>}
    </Suspense>
  );
};

export default UserMF;
