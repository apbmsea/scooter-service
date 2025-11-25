import React, { Suspense, useState } from "react";

const HeaderMFLazy = React.lazy(() =>
  import("headerMF/App").catch(() => ({ default: () => null }))
);

const HeaderMF = () => {
  const [failed] = useState(false);

  return (
    <Suspense fallback={<div>Загрузка…</div>}>
      {!failed ? <HeaderMFLazy /> : <div>Микросервис не загрузился</div>}
    </Suspense>
  );
};

export default HeaderMF;
