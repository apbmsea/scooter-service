import React, { Suspense } from "react";
import LoadingWidget from "../widgets/LoadingWidget/ui/LoadingWidget";

const HeaderMFLazy = React.lazy(() =>
  import("headerMF/App").catch(() => ({ default: () => null }))
);

const HeaderMF = () => {
  return (
    <Suspense fallback={<LoadingWidget active={true} />}>
      <HeaderMFLazy />
    </Suspense>
  );
};

export default HeaderMF;
