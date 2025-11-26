import React, { Suspense } from "react";
import LoadingWidget from "../widgets/LoadingWidget/ui/LoadingWidget";

const AuthMFLazy = React.lazy(() =>
  import("authMF/App").catch(() => ({ default: () => null }))
);

const AuthMF = () => {
  return (
    <Suspense fallback={<LoadingWidget active={true} />}>
      <AuthMFLazy />
    </Suspense>
  );
};

export default AuthMF;
